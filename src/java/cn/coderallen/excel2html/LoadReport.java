package cn.coderallen.excel2html;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/LoadReport")
public class LoadReport extends HttpServlet {

	private static final long serialVersionUID = 1L;

	// 简单报表
	public static final String SIMPLE_REPORT_TYPE = "0";

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		// 根据报表的类型，跳转到不同的页面
		RequestDispatcher requestDispatcher = request
				.getRequestDispatcher(QueryReport.SIMPLE_REPORT_PAGE);

		requestDispatcher.forward(request, response);

	}

}
