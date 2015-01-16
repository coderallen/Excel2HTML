<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>报表展示工具</title>

<!-- 核心 css -->
<link rel="stylesheet" type="text/css" href="css/jquery.ui.core.css" />

<!-- 选项卡css -->
<link rel="stylesheet" type="text/css" href="css/jquery.ui.tabs.css" />

<!-- 主题css -->
<link rel="stylesheet" type="text/css" href="css/jquery.ui.theme.css" />

<!-- jquery  -->
<script type="text/javascript" src="js/jquery.js"></script>

<!-- 核心js -->
<script type="text/javascript" src="js/jquery.ui.core.js"></script>

<!-- 微件js -->
<script type="text/javascript" src="js/jquery.ui.widget.js"></script>

<!-- 选项卡插件js -->
<script type="text/javascript" src="js/jquery.ui.tabs.js"></script>


<script type="text/javascript">
	var costTime = 0.0;

	var timer = setInterval(function() {
		updateCostTime();
	}, 100);

	function updateCostTime() {
		costTime += 0.1;
		$("#costTimIng").text(format(costTime));
	}

	function sleep(numberMillis) {
		var now = new Date();
		var exitTime = now.getTime() + numberMillis;
		while (true) {
			now = new Date();
			if (now.getTime() > exitTime)
				return;
		}
	}

	$(document).ready(

	function() {

		$.post("QueryReport", {
			date : "${date}",
			reportType : "${reportType}"
		}, function(dataModel) {
			//取消计时器
			clearInterval(timer);
		
			$("#tipsContent").text("查询结束，1 秒后，展示查询结果!");
			setTimeout(displayTable2(dataModel),1000);
		});
	});

	//setTimeout不能带参数
	function displayTable2(dataModel){
		//返回一个不带参数的函数
	       return function(){
	    	   displayTable(dataModel);
	       }
	}

	function displayTable(dataModel) {
		$("#dialog").css("display", "none");

		var ul = $("#ul");
		for ( var index in dataModel.tableModelList) {
			var tableModel = dataModel.tableModelList[index];
			ul.append("<li><a href='#" + tableModel.tableId
+ "'>"
					+ tableModel.tableName + "</a></li>");
			//alert("ul");
		}

		var tabs = $("#tabs");
		for ( var index in dataModel.tableModelList) {
			var tableModel = dataModel.tableModelList[index];

			tabs.append("<div id="+tableModel.tableId+"><p>"
					+ tableModel.tableHtml + "</p></div>");

		}

		$("#costTime").text(dataModel.costTime);
		$("#dataCount").text(dataModel.dataCount);
		$("#tabs").tabs({

			//设置各选项卡在切换时的动画效果
			fx : {
				opacity : "toggle",
				height : "toggle"
			},
			//通过鼠标单击事件切换选项卡
			event : "click"
		});

	}

	function format(x) {

		var f_x = Math.round(x * 100) / 100;
		var s_x = f_x.toString();
		var pos_decimal = s_x.indexOf('.');
		if (pos_decimal < 0) {
			pos_decimal = s_x.length;
			s_x += '.';
		}
		while (s_x.length <= pos_decimal + 1) {
			s_x += '0';
		}
		return s_x;
	}
</script>

<style type="text/css">
body {
	width: 100%;
	text-align: center;
	font-size:30px;
}
#dialog {
	margin: 0 auto;
	width: 50%;
	height: 100%;
	border: solid 1px black;
	font-weight: bold;
	font-size: 30px;
	padding-top: 500px;
	padding-bottom: 500px;
}


#tips {
	margin: 0 auto;
}
</style>
</head>
<body>
	
	<div id="tabs" style="width: 100%">
		<ul id="ul">
		</ul>
	</div>

</body>
</html>