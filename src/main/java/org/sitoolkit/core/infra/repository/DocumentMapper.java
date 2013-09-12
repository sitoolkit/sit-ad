/*
 * Copyright 2013 Monocrea Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.sitoolkit.core.infra.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import org.sitoolkit.core.infra.repository.schema.Column;
import org.sitoolkit.core.infra.repository.schema.Document;
import org.sitoolkit.core.infra.repository.schema.Table;
import org.sitoolkit.core.infra.util.SitException;
import org.sitoolkit.core.infra.util.SitJaxbUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

/**
 *
 * @author yuichi.kuwahara
 */
public class DocumentMapper {

	protected final Logger log = LoggerFactory.getLogger(getClass());

	@Resource
	ApplicationContext appCtx;
	/**
	 *
	 */
	private Map<String, Table> tableMap = new HashMap<String, Table>();
	/**
	 * BeanUtilsで使用するConverter
	 */
	private Map<Class<?>, ? extends Converter> converterMap = new HashMap<Class<?>, Converter>();

	/**
	 * 設定ファイルのパス
	 */
	private String configFilePath = "/documentMapping.xml";

	public RowData map(String beanId, Object bean) {
		RowData rowData = new RowData();
		Table table = tableMap.get(beanId);

		for (Column column : table.getColumn()) {
			if (Boolean.TRUE.equals(column.isReadOnly())) {
				continue;
			}
			try {
				Object value = BeanUtils.getProperty(bean, column.getProperty());
				Class<?> propertyType = PropertyUtils.getPropertyType(bean, column.getProperty());
				if (ClassUtils.isAssignable(propertyType, Integer.class)) {
					rowData.setInt(column.getName(), Integer.parseInt(value.toString()), column.getMin());
				} else if (ClassUtils.isAssignable(propertyType, Boolean.class)) {
					boolean bool = value == null ? false : Boolean.parseBoolean(value.toString());
					String cellValue = bool ? column.getTrueStr() : column.getFalseStr();
					rowData.setCellValue(column.getName(), cellValue);
				} else {
					rowData.setCellValue(column.getName(), value);
				}
			} catch (Exception e) {
				throw new SitException(e);
			}
		}
		return rowData;
	}

	/**
	 * 1行分のデータをSpring Beanの各プロパティに設定します。
	 * 行内の列とプロパティとの対応は、designDoc.xmlの定義に従います。
	 * designDoc.xmlは、{@link #init() }で予め読み込まれている事が前提です。
	 * @param <T> Spring Beanの型
	 * @param beanId Spring Beanを指定するID
	 * @param rowData 1行分のデータ
	 * @param type Spring Beanの型
	 * @return 1行分のデータを設定されたSpring Bean
	 */
	public <T> T map(String beanId, RowData rowData, Class<T> type) {
		T bean = appCtx.getBean(beanId, type);
		Table table = tableMap.get(beanId);

		for (Column column : table.getColumn()) {
			map(bean, column, rowData);
		}
		return bean;
	}

	protected void map(Object bean, Column column, RowData rowData) {
		try {
			String property = column.getProperty();
			Object value = retriveValue(
					 PropertyUtils.getPropertyType(bean, column.getProperty()),
					 rowData,
					 column);

			if (log.isTraceEnabled()) {
				log.trace("{}.{}に[{}]を設定します",
						new Object[]{bean.getClass().getSimpleName(), property, value});
			}

			BeanUtils.setProperty(bean, property, value);
		} catch (Exception e) {
			log.error("ID:{}", column.getProperty(), e);
			throw new SitException(e);
		}
	}

	/**
	 * 行データの指定された列の値を取得します。
     * 値は、beanの対応するプロパティの型よって適宜変換されます。
	 * @param type プロパティをの型
	 * @param row プロパティに設定する値を取得する行
	 * @param col 設定するプロパティを特定する列
	 * @return beanに設定する値
	 */
	protected Object retriveValue(Class<?> type, RowData row, Column col) {
		if (StringUtils.isNotEmpty(col.getPattern())) {
			if (ClassUtils.isAssignable(type, Map.class)) {
				return row.getCellValuesAsMap(
						col.getPattern(),
						1,
						col.getReplace());
			} else {
				return row.getCellValues(
						col.getPattern(),
						col.getReplace(),
						col.isExcludeEmptyValue());
			}
		} else {
			if (ClassUtils.isAssignable(type, Integer.class)) {
				return row.getInt(col.getName(), col.getReplace());
			} else if (ClassUtils.isAssignable(type, Boolean.class)) {
				return row.getBoolean(col.getName(), col.getTrueStr(), col.getReplace());
			} else {
				return row.getCellValue(col.getName(), col.getReplace());
			}
		}
	}

	@PostConstruct
	public void init() {
		log.info("設計書定義を読み込みます。");
		Document doc = SitJaxbUtils.res2obj(Document.class, getConfigFilePath());

		for (Table table : doc.getTable()) {
			for (String id : table.getBeanId().split(",")) {
				tableMap.put(id, table);
			}
		}
		log.info("設計書定義を読み込みました。{}", tableMap);
	}

	@PostConstruct
	public void initConverters() {
		if (log.isDebugEnabled()) {
			log.debug("Commons BeanUtilsのコンバータを登録します。{}", getConverterMap());
		}
		for (Entry<Class<?>, ? extends Converter> entry : getConverterMap().entrySet()) {
			ConvertUtils.register(entry.getValue(), entry.getKey());
		}

	}

	public Map<Class<?>, ? extends Converter> getConverterMap() {
		return converterMap;
	}

	public void setConverterMap(Map<Class<?>, ? extends Converter> converterMap) {
		this.converterMap = converterMap;
	}

	public String getConfigFilePath() {
		return configFilePath;
	}

	public void setConfigFilePath(String configFilePath) {
		this.configFilePath = configFilePath;
	}
}
