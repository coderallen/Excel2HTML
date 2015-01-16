package cn.coderallen.excel2html.model;

import java.util.List;

/**
 * 展示界面用到的数据模型。
 * 
 */
public class DisplayDataModel {

	/**
	 * 多个表格
	 */
	private List<TableModel> tableModelList;

	public List<TableModel> getTableModelList() {
		return tableModelList;
	}

	public void setTableModelList(List<TableModel> tableModelList) {
		this.tableModelList = tableModelList;
	}

}
