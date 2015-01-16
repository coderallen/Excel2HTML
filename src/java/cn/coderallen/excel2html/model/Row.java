package cn.coderallen.excel2html.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 表格的行。
 * 
 */
public class Row {

	/**
	 * 一行由多干列组成
	 */
	public List<Col> cellList = new ArrayList<Col>();
}
