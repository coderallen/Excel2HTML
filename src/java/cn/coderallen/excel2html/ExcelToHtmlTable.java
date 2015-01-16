package cn.coderallen.excel2html;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import cn.coderallen.excel2html.model.Col;
import cn.coderallen.excel2html.model.Row;
import cn.coderallen.excel2html.model.Table;
import cn.coderallen.excel2html.model.TableModel;
import cn.coderallen.excel2html.util.TemplatePropertyReader;


public class ExcelToHtmlTable {

	private List<TableModel> tableModelList = new ArrayList<TableModel>();

	private XSSFWorkbook templateWorkbook;

	// 公式执行器
	private XSSFFormulaEvaluator formulaEvaluator;

	public ExcelToHtmlTable(XSSFWorkbook templateWorkbook) {
		this.templateWorkbook = templateWorkbook;
		this.formulaEvaluator = new XSSFFormulaEvaluator(templateWorkbook);

	}

	public void init() {

		int sheetNum = templateWorkbook.getNumberOfSheets();
		//sheetNum = 1;发现问题，调用用
		// 遍历每一个sheet
		for (int sheetIndex = 0; sheetIndex < sheetNum; sheetIndex++) {

			XSSFSheet currentSheet = templateWorkbook.getSheetAt(sheetIndex);

			Map<String, String> mergeMap = getMergeMap(currentSheet);

			String sheetName = currentSheet.getSheetName();

			int firstRowIndex = TemplatePropertyReader.SIMPLE_START_ROW;
			int lastRowIndex = TemplatePropertyReader.SIMPLE_END_ROW;
			int firstColIndex = TemplatePropertyReader.SIMPLE_START_COL;
			int lastColIndex = TemplatePropertyReader.SIMPLE_END_COL;

			Table table = new Table();
			// 遍历每一行
			List<Row> rowList = table.rowList;
			for (int currentRowIndex = firstRowIndex; currentRowIndex <= lastRowIndex; currentRowIndex++) {

				XSSFRow currentRow = currentSheet.getRow(currentRowIndex);

				// 这个地方加入一个空的行，有N个单元格，每个单元格的值为" "
				if (currentRow == null) {
					// TODO 如果为null，直接跳过去么？
					System.out.println("currentRow is null:" + currentRowIndex);
					Row row = new Row();
					rowList.add(row);
					// 构造N个空的单元格 TODO
					continue;
				}

				if (firstColIndex < 0 || lastColIndex < 0) {
					continue;
				}

				Row row = new Row();
				rowList.add(row);

				// 遍历每一列
				for (int currentColIndex = firstColIndex; currentColIndex <= lastColIndex; currentColIndex++) {
					XSSFCell currentCell = currentRow.getCell(currentColIndex);
					// 这个地方加入一个空的单元格，值为" "
					if (currentCell == null) {
						// TODO 2013年10月2日发现的bug，单元格为空的时候，怎么会得到null呢？
						System.out
								.println("currentCell is null:currentRowIndex="
										+ currentRowIndex + "currentColIndex="
										+ currentColIndex);
						// 当作1个空的单元格处理
						Col col = new Col();
						col.value = " ";
						row.cellList.add(col);

						continue;
					}

					Col col = new Col();

					String cellValue = null;

					int cellType = currentCell.getCellType();

					switch (cellType) {

					case Cell.CELL_TYPE_STRING:
						MergeModel mergeModel2 = getMergeModel(currentSheet,
								currentRowIndex, currentColIndex);
						boolean inMergeMap2 = mergeModel2.isMergeMap;
						CellRangeAddress mergeMapCellRangeAddress2 = mergeModel2.mergeMapCellRangeAddress;

						// 不属于“合并单元格”的一部分
						if (!inMergeMap2) {
							cellValue = currentCell.getStringCellValue();
						}
						// 合并单元格的一部分，如果该区域（合并单元格）都是空的
						else if (inMergeMap2) {

							int firstCol = mergeMapCellRangeAddress2
									.getFirstColumn();

							int firstRow = mergeMapCellRangeAddress2
									.getFirstRow();

							// 只需要设置一次
							if (firstCol == currentColIndex
									&& firstRow == currentRowIndex) {
								cellValue = currentCell.getStringCellValue();
							}
						}

						break;

					case Cell.CELL_TYPE_FORMULA:
						// 获得公式单元格计算后的值
						cellValue = getForumaCellValue(currentCell);
						break;

					case Cell.CELL_TYPE_NUMERIC:
						cellValue = currentCell.getNumericCellValue() + "";
						break;
					case Cell.CELL_TYPE_BOOLEAN:
						break;

					// 处理“空单元格”
					case Cell.CELL_TYPE_BLANK:

						MergeModel mergeModel = getMergeModel(currentSheet,
								currentRowIndex, currentColIndex);
						boolean inMergeMap = mergeModel.isMergeMap;
						CellRangeAddress mergeMapCellRangeAddress = mergeModel.mergeMapCellRangeAddress;

						// 不属于“合并单元格”的一部分
						if (!inMergeMap) {
							cellValue = " ";
						}
						// 合并单元格的一部分，如果该区域（合并单元格）都是空的
						else if (inMergeMap) {
							boolean allEmpty = true;

							int firstCol = mergeMapCellRangeAddress
									.getFirstColumn();
							int lastCol = mergeMapCellRangeAddress
									.getLastColumn();
							int firstRow = mergeMapCellRangeAddress
									.getFirstRow();
							int lastRow = mergeMapCellRangeAddress.getLastRow();

							for (int rowIndex = firstRow; rowIndex <= lastRow; rowIndex++) {
								XSSFRow currentRow2 = currentSheet
										.getRow(rowIndex);
								for (int colIndex = firstCol; colIndex <= lastCol; colIndex++) {

									XSSFCell currentCell2 = currentRow2
											.getCell(colIndex);
									// 可以为空格" "
									String cellValue2 = getCellValue(currentCell2);
									if (cellValue2 != null
											&& !cellValue2.equals("")) {
										allEmpty = false;
									}
								}
							}
							// 合并单元格每一个都是空的，需要设置第1个子单元格为空
							if (allEmpty) {
								cellValue = " ";
								currentSheet.getRow(firstRow).getCell(firstCol)
										.setCellValue(cellValue);
							}
						}

						break;

					default:
						break;
					}
					if (cellValue != null) {
						col.value = cellValue;

						row.cellList.add(col);
					} else {
						// 非常必要
						row.cellList.add(null);
					}
				}
			}

			StringBuffer tableHtml = buildTableHtml(mergeMap, rowList);

			tableModelList.add(new TableModel(sheetName, sheetName, tableHtml
					.toString()));

		}

	}

	private StringBuffer buildTableHtml(Map<String, String> mergeMap,
			List<Row> rowList) {
		// 将table转换为html
		StringBuffer tableHtml = new StringBuffer();
		tableHtml.append("<html>");
		tableHtml
				.append("<head><meta http-equiv=Content-Type content=\"text/html; charset=UTF-8\">");
		String style = "<style type=\"text/css\">       table{ width:100%; height:300px;border:1px;padding:0px;margin:0px;border-collapse:collapse;}       td{ border:solid 1px black;height:30px;}    </style>";
		tableHtml.append(style);

		tableHtml.append("</head>");
		tableHtml.append("<body>");
		tableHtml.append("<table >");

		int rowSize = rowList.size();
		for (int rowIndex = 0; rowIndex < rowSize; rowIndex++) {
			tableHtml.append("<tr>");
			List<Col> cellList = rowList.get(rowIndex).cellList;
			int colSize = cellList.size();
			for (int colIndex = 0; colIndex < colSize; colIndex++) {
				String colSpanInsert = "";

				String string = rowIndex + "," + colIndex;
				String colSpanValue = mergeMap.get(string);

				if (colSpanValue != null) {
					String[] ss = colSpanValue.split(",");
					String colspan = ss[0];
					String rowspan = ss[1];
					// 如果只占居1行或1列，没有必要设置colspan=1或rowspan=1
					if (!colspan.equals("1")) {
						colSpanInsert += " colspan=" + colspan;
					}

					if (!rowspan.equals("1")) {
						colSpanInsert += " rowspan=" + rowspan;
					}

				}

				Col col = cellList.get(colIndex);
				if (col != null) {
					tableHtml.append("<td  " + colSpanInsert + ">");

					tableHtml.append(col.value);
					tableHtml.append("</td>");

				}

			}
			tableHtml.append("</tr>");
		}
		tableHtml.append("</table>");
		tableHtml.append("</body></html>");
		return tableHtml;
	}

	// 返回一个合并模型：是否在单元格的合并区域中，合并区域对象
	private MergeModel getMergeModel(XSSFSheet currentSheet,
			int currentRowIndex, int currentColIndex) {
		MergeModel mergeModel = new MergeModel();
		int num = currentSheet.getNumMergedRegions();

		for (int index = 0; index < num; index++) {
			CellRangeAddress cra = currentSheet.getMergedRegion(index);
			int firstColumn = cra.getFirstColumn();
			int lastColumn = cra.getLastColumn();
			int firstRow = cra.getFirstRow();
			int lastRow = cra.getLastRow();

			if ((currentRowIndex >= firstRow && currentRowIndex <= lastRow)
					&& (currentColIndex >= firstColumn && currentColIndex <= lastColumn)) {
				mergeModel.isMergeMap = true;

				mergeModel.mergeMapCellRangeAddress = cra;
				// String string = "currentValue=" + currentRowIndex + "," +
				// currentColIndex
				// + "....mergeValue=" + firstRow + "," + lastRow + ",," +
				// firstColumn + ","
				// + lastColumn;
				// print(string);
				break;
			}
		}
		return mergeModel;
	}

	class MergeModel {
		boolean isMergeMap = false;
		CellRangeAddress mergeMapCellRangeAddress = null;
	}

	// 获得公式单元格的值
	private String getForumaCellValue(XSSFCell currentCell) {

		CellValue evaluate = formulaEvaluator.evaluate(currentCell);
		String cellValue = (int) evaluate.getNumberValue() + "";

		return cellValue;
	}

	// 获得合并单元格的区域
	public Map<String, String> getMergeMap(XSSFSheet currentSheet) {
		Map<String, String> mergeMap = new HashMap<String, String>();
		int num = currentSheet.getNumMergedRegions();
		for (int index = 0; index < num; index++) {
			CellRangeAddress cra = currentSheet.getMergedRegion(index);
			int firstColumn = cra.getFirstColumn();
			int lastColumn = cra.getLastColumn();
			int firstRow = cra.getFirstRow();
			int lastRow = cra.getLastRow();
			mergeMap.put(firstRow + "," + firstColumn, (lastColumn
					- firstColumn + 1)
					+ "," + (lastRow - firstRow + 1));
		}
		return mergeMap;
	}

	private String getCellValue(XSSFCell currentCell) {
		String cellValue = "";

		int cellType = currentCell.getCellType();

		switch (cellType) {

		case Cell.CELL_TYPE_STRING:
			cellValue = currentCell.getStringCellValue();

			break;

		// 公式不参与，用“foruma”表示该公式单元格不为空
		case Cell.CELL_TYPE_FORMULA:
			cellValue = "foruma";

			break;

		case Cell.CELL_TYPE_NUMERIC:
			cellValue = currentCell.getNumericCellValue() + "";
			break;
		case Cell.CELL_TYPE_BOOLEAN:
			break;

		case Cell.CELL_TYPE_BLANK:
			break;

		default:
			break;
		}
		return cellValue;
	}

	public List<TableModel> getTableModelList() {
		return tableModelList;
	}

}
