<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<html>
	<head>
		<title>修改标签</title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
		<meta name="layout" content="adminmain"/>
	</head>
<body>
		 	<div class="row">
		 			<div class="span12">
		 				<form action="${base }/admin/tag/update" method="post">
		 					<input type="hidden" name="id" value="${obj.id }"/>
		 					<table>
		 						<tbody>
		 							<tr>
		 								<td>名称:</td>
		 								<td><input  name="name" value="${obj.name }"/></td>
		 							</tr>
		 							<tr>
		 								<td colspan="2"><input type="submit"  value="提交"/></td>
		 							</tr>
		 						</tbody>
		 					</table>
		 					 </form>
		 			</div>
		 	</div>
</body>
</html>