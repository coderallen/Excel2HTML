package cn.coderallen.excel2html.model;

/**
 * Html表格模型，封装了一个Html表格的一些属性。
 * 
 */
public class TableModel {
	/**
	 * 表名（可以不唯一）
	 */
	private String tableName;
	/**
	 * 这个表格对应的Html源码
	 */
	private String tableHtml;
	/**
	 * 表格的id，需要唯一，前端js需要用到
	 */
	private String tableId;

	public TableModel(String tableId, String tableName, String tableHtml) {
		this.tableId = tableId;
		this.tableName = tableName;
		this.tableHtml = tableHtml;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getTableHtml() {
		return tableHtml;
	}

	public void setTableHtml(String tableHtml) {
		this.tableHtml = tableHtml;
	}

	public String getTableId() {
		return tableId;
	}

	public void setTableId(String tableId) {
		this.tableId = tableId;
	}

}
