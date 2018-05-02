<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    
    <title>注册</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<meta http-equiv="content-type" content="text/html;charset=utf-8">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<link rel="stylesheet" type="text/css" href="<c:url value='/css/css.css'/>">
	<link rel="stylesheet" type="text/css" href="<c:url value='/jsps/css/user/regist.css'/>">
	<script type="text/javascript" src="<c:url value='/jquery/jquery-1.5.1.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/js/common.js'/>"></script>
	<script type="text/javascript" src="<c:url value='/jsps/js/user/regist.js'/>"></script>
  </head>
  
  <body>
	<div id="divMain">
		<div id="divTitle">
			<span id="spanTitle">新用户注册</span>
		</div>
		<div id="divBody">
		<form action="<c:url value='/UserServlet'/>" method="post" id="registForm">
			<input type="hidden" name="method" value="regist"/>
			<table id="tableForm">
				<tr>
					<td class="tdtext">用户名：</td>
					<td class="tdinput">
						<input class="inputclass" type="text" name="loginname" id="loginname" value="${form.loginname }">
					</td>
					<td class="tderror">
						<label class="errorclass" id="loginnameError">${errors.loginname }</label>
					</td>
				</tr>
				<tr>
					<td class="tdtext">登陆密码：</td>
					<td><input class="inputclass" type="password" name="loginpass" id="loginpass" value="${form.loginpass }"></td>
					<td>
						<label class="errorclass" id="loginpassError">${errors.loginpass}</label>
					</td>
				</tr>
				<tr>
					<td class="tdtext">确认密码：</td>
					<td><input class="inputclass" type="password" name="reloginpass" id="reloginpass"  value="${form.reloginpass }"></td>
					<td>
						<label class="errorclass" id="reloginpassError">${errors.reloginpass}</label>
					</td>
				</tr>
				<tr>
					<td class="tdtext">Email：</td>
					<td><input class="inputclass" type="text" name="email" id="email" value="${form.email }"></td>
					<td>
						<label class="errorclass" id="emailError">${errors.email}</label>
					</td>
				</tr>
				<tr>
					<td class="tdtext">图形验证码：</td>
					<td><input class="inputclass" type="text" name="verifyCode" id="verifyCode"  value="${form.verifyCode }"></td>
					<td>
						<label class="errorclass" id="verifyCodeError">${errors.verifyCode }</label>
					</td>
				</tr>
				<tr>
					<td></td>
					<td>
						<div id="divVerfiyCode">
							<img id="imgVerifyCode" src="<c:url value='/VerifyCodeServlet'/>">
						</div>
					</td>
					<td><a href="javascript:_hyz();">换一张</a></td>
				</tr>
				<tr>
					<td></td>
					<td>
						<input type="image" id="submitBtn" src="<c:url value='/images/regist1.jpg'/>">
					</td>
					<td></td>
				</tr>
			</table>
		</form>
		</div>
    </div>
  </body>
</html>
	