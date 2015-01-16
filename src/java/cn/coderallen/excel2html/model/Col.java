package cn.coderallen.excel2html.model;

/**
 * 表格的列。
 * 
 */
public class Col {
	/**
	 * 列的值
	 */
	public String value;
	/**
	 * 几行合并为1行
	 */
	public int rowspan;
	/**
	 * 几列合并为1列
	 */
	public int colspan;

}
