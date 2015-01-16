package cn.coderallen.excel2html.util;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import cn.coderallen.excel2html.ExcelToHtmlTable;

/**
 * Excel模版加载器。
 * 
 */
public class TemplateLoader {

	// 为了跨平台
	private static final String SEPARATOR = File.separator;
	private static final String SIMPLE_REPORT_TEMPLATE_XLSX = "FansUnionReport.xlsx";
	static String simplePath;

	private static Logger log = Logger.getLogger(TemplateLoader.class);

	static {
		URL resource = ExcelToHtmlTable.class.getClassLoader().getResource("");

		if (resource != null) {
			log.info(resource.getPath());
			File file = new File(resource.getPath());
			String parentPath = file.getParent();
			File file2 = new File(parentPath);

			String newPath = file2.getParent() + SEPARATOR + "template"
					+ SEPARATOR;

			simplePath = newPath + SIMPLE_REPORT_TEMPLATE_XLSX;

		}

	}

	public static XSSFWorkbook buildSimpleWorkbook() {
		XSSFWorkbook wb = null;
		try {
			OPCPackage simpleOpcPackage = OPCPackage.open(simplePath);
			wb = new XSSFWorkbook(simpleOpcPackage);

		} catch (InvalidFormatException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return wb;
	}

}
