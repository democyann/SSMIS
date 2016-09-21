<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<%@include file="/pages/common/taglibs.jsp"%>
<%@include file="/pages/common/meta.jsp"%>
<%@include file="/common/validator.jsp"%>
<style type="text/css">
.title {
	font-family: 楷体;
	font-weight: bold;
}
.title1 {
	font-family: 楷体;
}
</style>
<title>帮助文档</title>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
	String filePath1 = (String) request.getAttribute("wordPath");
	String filePath = request.getSession().getServletContext().getRealPath("/files/mould/test.doc");
%>
</head>
<body>
	<div id="r_main">
		<div class="x-toolbar">
			<table width="100%" cellpadding="0" cellspacing="0" style="margin-left: 0px; margin-top: 0px">
				<tr>
					<td width="75%"></td>
					<td width="25%" align="right"></td>
				</tr>
			</table>
		</div>
		<div class="x-panel-body">


			<table id="fineTable" width="800" style="margin: auto; margin-top: 18px">
				<tr>
					<td style="text-align: center"><font size="5" class="title" style="color: red;">使用说明</font><br></td>
				</tr>
				<!-- 	<tr>
						<td>
							<font size="3" class="title">行政办公模块:</font><br>
							<span style="margin-left: 20px;">1.行政办公模块暂时未开放；</span>
						
						</td>
					</tr> -->
				<tr>
					<td><!-- <font size="3" class="title">使用说明</font><br> --> <span  style="margin-left: 20px;"><font size="3" class="title1">查看系统使用手册请点击：<a href="${ctx }/files/helpDoc/czsc.doc" target="_blank" style="color: blue;">河北出入境检疫处理综合信息管理系统使用手册</a></font></span><br></td>
				</tr>
				<!-- <tr>
						<td>
							<font size="3" class="title">工作周报模块:</font><br>
							<span style="margin-left: 20px;">1.员工周报：有权限人员对公司所有员工的周报查询、查看；有权限的人可以查看（系统管理员可以根据工作的具体需求，分配权限）</span><br>
							<span style="margin-left: 20px;">2.我的周报：有权限人员对自己个人周报的添加、查看、修改；所有员工都具有该项功能权限；</span><br>
							<span style="margin-left: 20px;">3.部门周报：有权限人员对公司内所有部门提交的周报进行查询、查看；有权限人员查看（系统管理员可以根据工作的具体需求，分配权限）</span><br>
							<span style="margin-left: 20px;">4.部门周报汇总：有权限人员对所在部门周报的添加、查看、修改；有权限人员查看（系统管理员可以根据工作的具体需求，分配权限）</span><br>
							<span style="margin-left: 20px;">5.周报汇总：有权限人员对公司汇总的周报进行查询，查看；有权限人员查看（系统管理员可以根据工作的具体需求，分配权限）</span><br>
							<span style="margin-left: 20px;">6.新增周报汇总：有权限人员对本公司周报的添加，查看，修改；有权限人员查看（系统管理员可以根据工作的具体需求，分配权限）</span><br>
							
						</td>
					</tr> -->
				
			</table>
			<table id="fineTable" width="800" style="margin: auto; margin-top: 18px">
				<tr>
					<td style="text-align: center" colspan="3"><font size="5" class="title" style="color: red;">系统版本</font><br></td>
				</tr>
				<tr>
					<td><font size="3" style="margin-left: 20px;" class="title">版本号</font></td>
					<td><font size="3" style="margin-left: 20px;" class="title">更新时间</font></td>
					<td><font size="3" style="margin-left: 20px;"  class="title">查看详情</font></td>
				</tr>
				<tr>
					<td><span style="margin-left: 20px;">1.1</span></td>
					<td><span style="margin-left: 20px;">2014-09-15</span></td>
					<td><span style="margin-left: 20px;"><a href="${ctx }/files/helpDoc/1.1.doc" target="_blank">查看</a></span></td>
				</tr>
				<tr>
					<td><span style="margin-left: 20px;">1.2</span></td>
					<td><span style="margin-left: 20px;">2014-09-17</span></td>
					<td><span style="margin-left: 20px;"><a href="${ctx }/files/helpDoc/1.2.doc" target="_blank">查看</a></span></td>
				</tr>
				<tr>
					<td><span style="margin-left: 20px;">1.3</span></td>
					<td><span style="margin-left: 20px;">2014-09-22</span></td>
					<td><span style="margin-left: 20px;"><a href="${ctx }/files/helpDoc/1.3.doc" target="_blank">查看</a></span></td>
				</tr>
				<tr>
					<td><span style="margin-left: 20px;">1.4</span></td>
					<td><span style="margin-left: 20px;">2014-09-25</span></td>
					<td><span style="margin-left: 20px;"><a href="${ctx }/files/helpDoc/1.4.doc" target="_blank">查看</a></span></td>
				</tr>
				<tr>
					<td><span style="margin-left: 20px;">1.5</span></td>
					<td><span style="margin-left: 20px;">2014-09-28</span></td>
					<td><span style="margin-left: 20px;"><a href="${ctx }/files/helpDoc/1.5.doc" target="_blank">查看</a></span></td>
				</tr>
				<tr>
					<td><span style="margin-left: 20px;">1.7</span></td>
					<td><span style="margin-left: 20px;">2014-10-10</span></td>
					<td><span style="margin-left: 20px;"><a href="${ctx }/files/helpDoc/1.7.doc" target="_blank">查看</a></span></td>
				</tr>
					<tr>
					<td><span style="margin-left: 20px;">1.8</span></td>
					<td><span style="margin-left: 20px;">2014-10-14</span></td>
					<td><span style="margin-left: 20px;"><a href="${ctx }/files/helpDoc/1.8.doc" target="_blank">查看</a></span></td>
				</tr>
				</tr>
				<tr>
					<td><span style="margin-left: 20px;">1.9</span></td>
					<td><span style="margin-left: 20px;">2014-10-23</span></td>
					<td><span style="margin-left: 20px;"><a href="${ctx }/files/helpDoc/1.9.doc" target="_blank">查看</a></span></td>
				</tr>
				<tr>
					<td><span style="margin-left: 20px;">2.0</span></td>
					<td><span style="margin-left: 20px;">2014-12-11</span></td>
					<td><span style="margin-left: 20px;"><a href="${ctx }/files/helpDoc/2.0.doc" target="_blank">查看</a></span></td>
				</tr>
				<tr>
					<td><span style="margin-left: 20px;">2.1</span></td>
					<td><span style="margin-left: 20px;">2014-12-23</span></td>
					<td><span style="margin-left: 20px;"><a href="${ctx }/files/helpDoc/2.1.doc" target="_blank">查看</a></span></td>
				</tr>
				<tr>
					<td><span style="margin-left: 20px;">2.2</span></td>
					<td><span style="margin-left: 20px;">2015-01-08</span></td>
					<td><span style="margin-left: 20px;"><a href="${ctx }/files/helpDoc/2.2.doc" target="_blank">查看</a></span></td>
				</tr>
				<tr>
					<td><span style="margin-left: 20px;">2.3</span></td>
					<td><span style="margin-left: 20px;">2015-02-10</span></td>
					<td><span style="margin-left: 20px;"><a href="${ctx }/files/helpDoc/2.3.doc" target="_blank">查看</a></span></td>
				</tr>
				<tr>
					<td><span style="margin-left: 20px;">2.4</span></td>
					<td><span style="margin-left: 20px;">2015-03-17</span></td>
					<td><span style="margin-left: 20px;"><a href="${ctx }/files/helpDoc/2.4.doc" target="_blank">查看</a></span></td>
				</tr>
			</table>
		</div>
		<div align="center" style="margin-top: 5px;">
			<input type="button" value="后退" class="button" onclick="javascript:history.back(-1);">
		</div>
	</div>

</body>
</html>