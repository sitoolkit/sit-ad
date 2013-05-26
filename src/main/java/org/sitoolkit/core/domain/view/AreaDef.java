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
package org.sitoolkit.core.domain.view;

import java.util.*;
import org.sitoolkit.core.domain.view.itemcmp.DesignInfoType;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * このクラスは、領域を表すエンティティです。
 * 
 * @author Yuichi Kuwahara
 *
 */
public class AreaDef {

	private static final Logger LOG = LoggerFactory.getLogger(AreaDef.class);
	/**
	 * 領域名
	 */
	private String name;
	/**
	 * 項目物理名
	 */
	private String pname;
	/**
	 * 当該領域内に直接含まれる画面項目のマップ
	 * キー:項目名、値:項目
	 */
	private Map<String, ItemDef> itemMap = new LinkedHashMap<String, ItemDef>();

	/**
	 * 領域の部分ごとに画面項目を保持するマップ
	 * キー：領域部名、値：項目のリスト
	 */
	private Map<String, AreaDef> subAreaMap;
	
	/**
	 * 当該領域内に含む全画面項目のリスト
	 */
	private List<ItemDef> allItems = new ArrayList<ItemDef>();
	
	/**
	 * 当該領域内に含む子領域のリスト
	 */
	private List<AreaDef> subAreas;
	/**
	 * 領域のタイプ
	 */
	private AreaType type = AreaType.シンプル;
	/**
	 * ドメイン
	 */
	private String domain;
	
	/**
	 * フォーム領域の列数
	 */
	private int formLane = 1;

	/**
	 * 領域を初期化します。初期化のための情報は、指定された項目定義から参照します。
	 * 
	 * <pre>
	 * 領域　→　領域名
	 * その他設計情報．領域物理名　→　領域物理名
	 * その他設計情報．領域タイプ　→　領域タイプ
	 * その他設計情報．フォームレーン数　→　フォームレーン数
	 * </pre>
	 * 
	 * @param domain 領域が所属するドメイン
	 * @param item 領域内の最初の項目
	 */
	public void init(String domain, ItemDef item) {
		setDomain(domain);
		setName(item.getAreaName());
		setPname(item.getDesignInfo(DesignInfoType.AreaPName));
		String areaType = item.getDesignInfo(DesignInfoType.AreaType);
		if(StringUtils.isNotEmpty(areaType)) {
			setType(areaType);
			if(AreaType.フォーム.equals(getType())) {
				setFormLane(NumberUtils.toInt(item.getDesignInfo(DesignInfoType.FormLaneCount), 1));
			}
		}
	}

	public List<AreaDef> getAreas() {
		if (subAreas == null) {
			subAreas = new ArrayList<AreaDef>();
		}
		return subAreas;
	}

	/**
	 * @return 当該領域内に直接含まれる画面項目のコレクション
	 */
	public Collection<ItemDef> getItems() {
		return getItemMap().values();
	}
	
	public boolean isLeaf() {
		return getAreas().isEmpty();
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * 領域の種類を表す列挙型
	 */
	public static enum AreaType {
		フォーム("form"),
		テーブル("table"),
		ボックス("box"),
		メタ情報("metadata"),
		シンプル("")
		;
		
		private String pname;

		private AreaType(String pname) {
			this.pname = pname;
		}
		
		public String getStyleClass() {
			return pname + "-area";
		}
	
		public static AreaType decode(String str) {
			for(AreaType t : values()) {
				if(t.name().equals(str)) {
					return t;
				}
			}
			LOG.warn("領域タイプに指定された文字列[{}]は不正です。", str);
			return シンプル;
		}
	}

	public AreaType getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = AreaType.decode(type);
	}

	/**
	 * @return 当該領域のフッター部に配置される項目
	 */
	public Collection<ItemDef> getFooterItems() {
		AreaDef footerArea = getSubAreaMap().get("フッター");
		if(footerArea == null) {
			return Collections.emptyList();
		} else {
			return footerArea.getItems();
		}
	}

	/**
	 * 項目を当該領域、当該領域のサブ領域、親項目のいずれかに追加します。
	 * @param item 追加対象項目
	 */
	public void addItem(ItemDef item) {
		getAllItems().add(item);
		item.setDomain(getDomain());

		if(item.isLocatedMainArea()) {
			addItemToSelfOrParemtItem(item);
		} else {
			final String subAreaName = item.getAreaName();
			AreaDef subArea = getSubAreaMap().get(subAreaName);
			if(subArea == null) {
				subArea = new AreaDef();
				subArea.setName(subAreaName);
				subArea.setPname(item.getDesignInfo(DesignInfoType.SubAreaPName));
				getSubAreaMap().put(subAreaName, subArea);
				if (LOG.isDebugEnabled()) {
					LOG.debug("サブ領域[{}]を領域[{}]に追加します。", 
							subAreaName, getName());
				}
			}
			if (LOG.isDebugEnabled()) {
				LOG.debug("項目[{}]を領域[{}]のサブ領域[{}]に追加します。", 
						new Object[]{item.getName(), getName(), subAreaName});
			}
			subArea.addItemToSelfOrParemtItem(item);
		}
		
	}
	
	/**
	 * 項目を当該領域、または項目の親項目に追加します。
	 * @param item 追加対象項目
	 */
	private void addItemToSelfOrParemtItem(ItemDef item) {
		if(item.isSubItem()) {
			ItemDef parent = getItemMap().get(item.getParentName());
			if(parent == null) {
				LOG.warn("サブ項目[{}]の親項目[{}]が見つかりません。このサブ項目は出力結果に含まれません。",
						new String[]{item.getName(), item.getParentName()});
			} else {
				if (LOG.isDebugEnabled()) {
					LOG.debug("サブ項目[{}]を項目[{}]に追加します。", item.getName(), parent.getName());
				}
				parent.addSubItem(item);
			}
		} else {
			if (LOG.isDebugEnabled()) {
				LOG.debug("項目[{}]を領域[{}]に追加します。", item.getName(), getName());
			}
			getItemMap().put(item.getName(), item);
		}
	}

	/**
	 * 
	 * @return 当該領域に直接含まれる画面項目のマップ
	 */
	public Map<String, ItemDef> getItemMap() {
		return itemMap;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}
	
	public String getPname() {
		if(pname == null) {
			pname = "";
		}
		return pname;
	}

	public void setPname(String pname) {
		this.pname = pname;
	}
	
	public String getId() {
		return StringUtils.isEmpty(getPname()) ? getName() : getPname();
	}

	public Map<String, AreaDef> getSubAreaMap() {
		if(subAreaMap == null) {
			subAreaMap = new LinkedHashMap<String, AreaDef>();
		}
		return subAreaMap;
	}

	/**
	 * 当該領域に含まれる全画面項目のリストを取得する。
	 * @return 当該領域に含まれる全画面項目のリスト
	 */
	public List<ItemDef> getAllItems() {
		return allItems;
	}

	public int getFormLane() {
		return formLane;
	}

	public void setFormLane(int formLane) {
		this.formLane = formLane;
	}
	
	public int getItemCount() {
		return getAllItems().size();
	}

}
