package cn.coderallen.excel2html.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 属性读取器，读取配置文件属性。
 * 
 */
public class TemplatePropertyReader {

	private static final String TEMPLATE_PROPERTIES = "template.properties";

	private static Properties props;

	static {
		props = new Properties();
		InputStream in = null;
		try {
			// TemplatePropertyReader.class所在目录
			in = TemplatePropertyReader.class
					.getResourceAsStream(TEMPLATE_PROPERTIES);
			if (in == null) {
				// classpath路径下
				in = TemplatePropertyReader.class.getClassLoader()
						.getResourceAsStream(TEMPLATE_PROPERTIES);
			}
			props.load(in);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static final int SIMPLE_START_ROW;
	public static final int SIMPLE_END_ROW;
	public static final int SIMPLE_START_COL;
	public static final int SIMPLE_END_COL;

	static {
		SIMPLE_START_ROW = TemplatePropertyReader.getInt("simple.startRow");
		SIMPLE_END_ROW = TemplatePropertyReader.getInt("simple.endRow");
		SIMPLE_START_COL = TemplatePropertyReader.getInt("simple.startCol");
		SIMPLE_END_COL = TemplatePropertyReader.getInt("simple.endCol");
	}
	
	public static String get(String key) {
		return (String) props.get(key);
	}

	public static int getInt(String key) {
		String str = (String) props.get(key);
		return strToInt(str);
	}

	

	private static int strToInt(String str) {
		return Integer.parseInt(str);
	}

}
