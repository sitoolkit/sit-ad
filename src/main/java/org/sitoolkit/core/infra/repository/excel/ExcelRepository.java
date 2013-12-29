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
package org.sitoolkit.core.infra.repository.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.annotation.Resource;
import org.sitoolkit.core.infra.repository.DocumentRepository;
import org.sitoolkit.core.infra.repository.RowData;
import org.sitoolkit.core.infra.repository.TableData;
import org.sitoolkit.core.infra.repository.TableDataCatalog;
import org.sitoolkit.core.infra.util.SitException;
import org.sitoolkit.core.infra.util.SitStringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.sitoolkit.core.infra.repository.FileInputSourceWatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Excelファイルを読み書きするためのリポジトリ実装です。
 * @author yuichi.kuwahara
 */
public class ExcelRepository implements DocumentRepository {

	private static final Logger LOG = LoggerFactory.getLogger(ExcelRepository.class);

	/**
	 * コーナーセルのパターン(正規表現)
	 */
	private String cornerCellPattern = "項番|No\\.|#";

	/**
	 * 読み取り対象から除外するシート名
	 */
	private Set<String> excludingSheetNames = new HashSet<>();

	@Resource
	protected FileInputSourceWatcher watcher;

	@Override
	public TableDataCatalog readAll(String filePath) {
		return read(filePath, new String[0]);
	}

	@Override
	public TableData read(String filePath, String sheetName) {
		TableDataCatalog tableDataCatalog = read(filePath, new String[]{sheetName});
		return tableDataCatalog.get(sheetName);

	}

	public TableDataCatalog read(String filePath, String[] sheetNames) {
		File file = new File(filePath);
		Workbook workbook = load(file);
		TableDataCatalog catalog = ArrayUtils.isEmpty(sheetNames)
				? readWorkbookByExceptedSheetNames(workbook)
				: readWorkbookBySheetNames(workbook, sheetNames);

		for (TableData table : catalog.tables()) {
			table.setInputSource(file.getAbsolutePath());
		}
		watcher.watch(filePath);

		return catalog;
	}

	private Workbook load(File file) {
		LOG.info("Excelファイルを読み込みます。{}", file.getAbsolutePath());
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			if (file.getName().endsWith(".xlsx")) {
				return new XSSFWorkbook(fis);
			} else {
				return new HSSFWorkbook(fis);
			}
		} catch (IOException e) {
			throw new SitException(e);
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					LOG.warn("ストリームのクローズで例外が発生しました。", e);
				}
			}
		}
	}

	@Override
	public void write(String templateFilePath, String targetFilePath, TableDataCatalog catalog) {
		write(templateFilePath, new File(targetFilePath), catalog);
	}

	public void write(String templateFile, File targetFile, TableDataCatalog catalog) {
		LOG.info("Excelファイルに書き込みます。{}", targetFile.getAbsolutePath());
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(targetFile);
			writeWorkbook(load(new File(templateFile)), catalog).write(fos);
			LOG.debug("Excelファイルに書き込みました。");
		} catch (IOException e) {
			throw new SitException(e);
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					LOG.warn("ストリームのクローズで例外が発生しました。", e);
				}
			}
		}
	}

	private Workbook writeWorkbook(Workbook workbook, TableDataCatalog catalog) {
		for(TableData tableData : catalog.tables()) {
			Sheet sheet = workbook.getSheet(tableData.getName());
			if(sheet == null) {
				sheet = workbook.createSheet(tableData.getName());
			}
			writeSheet(sheet, tableData);
		}
		return workbook;
	}

	private void writeSheet(Sheet sheet, TableData tableData) {
		LOG.debug("シート[{}]に書き込みます。", sheet.getSheetName());
		Row headerRow = findHeaderRow(sheet);
		Map<String, Integer> schema = retriveSchema(headerRow);

		Row row = headerRow;
		for(RowData rowData : tableData.getRows()) {
			row = getNextRow(sheet, row);
			writeRow(rowData, schema, row);
		}

	}

	private void writeRow(RowData rowData, Map<String, Integer> schema, Row row) {
		LOG.debug("{}行目に書き込みます。{}", row.getRowNum(), SitStringUtils.escapeReturn(rowData));
		for(String columnName : schema.keySet()) {
			writeCellValue(rowData.getCellValue(columnName), schema, columnName, row);
		}
	}

	private Row getNextRow(Sheet sheet, Row lastRow) {
		if(lastRow == null) {
			return sheet.getRow(sheet.getLastRowNum() + 1);
		} else {
			int newRowNum = lastRow.getRowNum() + 1;
			Row newRow = sheet.getRow(newRowNum);
			if(newRow == null) {
				newRow = sheet.createRow(newRowNum);
				copyStyle(lastRow, newRow);
			}
			return newRow;
		}
	}

	/**
	 * コピー元行の全セルのスタイルを、コピー先行の同じ列番号のセルにコピーする。
	 *
	 * @param src
	 *            コピー元行
	 * @param dst
	 *            コピー先行
	 */
	private void copyStyle(Row src, Row dst) {
		for(int i = src.getFirstCellNum(); i < src.getLastCellNum(); i++) {
			Cell srcCell = src.getCell(i);
			Cell dstCell = dst.getCell(i, Row.CREATE_NULL_AS_BLANK);
			dstCell.setCellStyle(srcCell.getCellStyle());
		}
	}

	/**
	 * 処理行の列名に対応するセルの値を設定する。
	 * @param column
	 * @param value
	 * @return
	 */
	private void writeCellValue(Object value, Map<String, Integer> schema, String column, Row row) {
		if(column == null || value == null) {
			return ;
		}
		Cell cell = row.getCell(schema.get(column), Row.CREATE_NULL_AS_BLANK);
		if(NumberUtils.isNumber(value.toString())) {
			cell.setCellValue(Double.parseDouble(value.toString()));
		} else {
			if(value.toString().contains("\n")) {
				cell.getCellStyle().setWrapText(true);
			}
			cell.setCellValue(value.toString());
		}
	}

	private TableDataCatalog readWorkbookByExceptedSheetNames(Workbook workbook) {
		TableDataCatalog catalog = new TableDataCatalog();

		final int numberOfSheets = workbook.getNumberOfSheets();
		for (int i = 0; i < numberOfSheets; i++) {
			Sheet sheet = workbook.getSheetAt(i);
			if(excludingSheetNames.contains(sheet.getSheetName())) {
				continue;
			}
			TableData tableData = readSheet(sheet);
			if(tableData == null) {
				LOG.warn("シート[{}]は読み取り可能なテーブルデータがありません。", sheet.getSheetName());
				continue;
			}
			catalog.add(tableData);
		}

		LOG.info("Excelファイルを読み込みました。");
		return catalog;
	}

	private TableDataCatalog readWorkbookBySheetNames(Workbook workbook, String[] sheetNames) {
		TableDataCatalog catalog = new TableDataCatalog();

		for(String sheetName : sheetNames) {
			Sheet sheet = workbook.getSheet(sheetName);
			TableData tableData = readSheet(sheet);
			if(tableData == null) {
				LOG.warn("シート[{}]は読み取り可能なテーブルデータがありません。", sheet.getSheetName());
				continue;
			}
			catalog.add(tableData);
		}
		return catalog;
	}



	private TableData readSheet(Sheet sheet) {
		if (LOG.isDebugEnabled()) {
			LOG.debug("シート[{}]を読み込みます。", sheet.getSheetName());
		}
		Row headerRow = findHeaderRow(sheet);
		if (headerRow == null) {
			return null;
		}
		TableData tableData = new TableData();
		tableData.setName(sheet.getSheetName());

		Map<String, Integer> schema = retriveSchema(headerRow);
		final int rowCnt = sheet.getLastRowNum();
		for (int i = headerRow.getRowNum() + 1; i <= rowCnt; i++) {
			Row row = sheet.getRow(i);
			tableData.add(readRow(schema, row));
		}
		LOG.info("シート[{}]内の表データを{}行読み込みました。",  sheet.getSheetName(), tableData.getRowCount());

		return tableData;
	}

	private Map<String, Integer> retriveSchema(Row headerRow) {
		Map<String, Integer> schema = new LinkedHashMap<String, Integer>();

		final int lastCellNum = headerRow.getLastCellNum();
		for (int i = 0; i < lastCellNum; i++) {
			schema.put(retriveCellValue(headerRow.getCell(i)), i);
		}

		if (LOG.isDebugEnabled()) {
			LOG.debug("ヘッダー行{}", schema.toString().replace("\n", ""));
		}
		return schema;
	}

	/**
	 *
	 * @param sheet
	 * @return
	 */
	private Row findHeaderRow(Sheet sheet) {
		LOG.debug("シート[{}]のヘッダー行を特定します。", sheet.getSheetName(), getCornerCellPattern());
		Row headerRow = null;
		final int rowCnt = sheet.getPhysicalNumberOfRows();
		for (int i = 0; i < rowCnt; i++) {
			Row row = sheet.getRow(i);
			if (row == null) {
				continue;
			}

			Cell cell = row.getCell(0);
			if (cell == null) {
				continue;
			}
			String cellValue = retriveCellValue(cell);
			if (cellValue.matches(getCornerCellPattern())) {
				headerRow = row;
				LOG.debug("セル({}, {})に値[{}]が見つかりました。ヘッダー行は{}行目です。",
						new Object[]{cell.getRowIndex(), cell.getColumnIndex(), cellValue, row.getRowNum()});
				break;
			}
		}
		if (headerRow == null) {
			LOG.warn("シート[{}]には、パターン[{}]に一致するセルが見つかりません。",
					sheet.getSheetName(), getCornerCellPattern());
		}
		return headerRow;
	}

	/**
	 *
	 * @param schema スキーマ
	 * @param row 行オブジェクト
	 * @return 行オブジェクトの情報を読み込んだRowDataオブジェクト
	 */
	private RowData readRow(Map<String, Integer> schema, Row row) {
		RowData rowData = new RowData();

		for (Entry<String, Integer> entry : schema.entrySet()) {
			Cell cell = row.getCell(entry.getValue());
			if (cell == null) {
				LOG.warn("存在しない列名が指定されました。{}", entry.getValue());
			}
			rowData.setCellValue(entry.getKey(), retriveCellValue(cell));
		}

		if (LOG.isDebugEnabled()) {
			LOG.debug("{}行目を読み込みました。{}", row.getRowNum(), SitStringUtils.escapeReturn(rowData));
		}

		return rowData;
	}

	/**
	 * セルの値をStringとして取得します。
	 * @param cell 値を取得するセル
	 * @return セルの値　セルがnullまたは値の取得に失敗した場合は空文字を返します。
	 */
	private String retriveCellValue(Cell cell) {
		String cellValue = "";
		if (cell == null) {
			return cellValue;
		}
		try {
			switch (cell.getCellType()) {
			case Cell.CELL_TYPE_NUMERIC:
				cellValue = roundValue(cell.getNumericCellValue());
				break;
			case Cell.CELL_TYPE_FORMULA:
				cellValue = roundValue(
						cell.getSheet().getWorkbook().getCreationHelper()
                        .createFormulaEvaluator().evaluate(cell).formatAsString()
					);
				break;
			default:
				cellValue = cell.getStringCellValue();
			}
		} catch (Exception e) {
			LOG.warn("セル({}, {})の値の取得が失敗しました。{}",
					cell.getRowIndex(), cell.getColumnIndex(), e.getMessage());
		}

		return cellValue;
	}

	String roundValue(String str) {
        if (StringUtils.isEmpty(str)) {
            return StringUtils.EMPTY;
        } else if (NumberUtils.isNumber(str)) {
			return roundValue(NumberUtils.toDouble(str));
		} else {
            if (str.length() > 1
                    && str.startsWith("\"")
                    && str.endsWith("\"")) {
                return str.substring(1, str.length() - 1);
            } else {
    			return str;
            }
		}
	}

	String roundValue(double value) {
		if (value == Math.round(value)) {
			return Integer.toString((int)value);
		} else {
			return Double.toString(value);
		}
	}

	/**
	 * 読み取り対象から除外するシート名を取得します。
	 * @return 読み取り対象から除外するシート名
	 */
	public Set<String> getExcludingSheetNames() {
		return excludingSheetNames;
	}

	/**
	 * 読み取り対象から除外するシート名を設定します。
	 * @param excludingSheetNames 読み取り対象から除外するシート名
	 */
	public void setExcludingSheetNames(Set<String> excludingSheetNames) {
		this.excludingSheetNames = excludingSheetNames;
	}

	/**
	 * コーナーセルのパターン(正規表現)を取得します。
	 * @return コーナーセルのパターン(正規表現)
	 */
	public String getCornerCellPattern() {
		return cornerCellPattern;
	}

	/**
	 * コーナーセルのパターン(正規表現)を設定します。
	 * @param cornerCellPattern コーナーセルのパターン(正規表現)
	 */
	public void setCornerCellPattern(String cornerCellPattern) {
		this.cornerCellPattern = cornerCellPattern;
	}

}
