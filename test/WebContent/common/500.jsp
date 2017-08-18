<%@ page contentType="text/html;charset=UTF-8" isErrorPage="true" %>
<%@ page import="org.slf4j.Logger,org.slf4j.LoggerFactory" %>

<%
	Throwable ex = null;
	if (exception != null)
		ex = exception;
	if (request.getAttribute("javax.servlet.error.exception") != null)
		ex = (Throwable) request.getAttribute("javax.servlet.error.exception");

	//记录日志
	Logger logger = LoggerFactory.getLogger("500.jsp");
	logger.error(ex.getMessage(), ex);
%>

<!DOCTYPE html>
<html>
<head>
	<title>500 - 系统内部错误</title>
	<meta name="viewport" content="width=device-width,initial-scale=1.0,user-scalable=no" />
	<script src="../js/jquery-1.11.1.min.js"></script>
    <script type="text/javascript">
    	window.onload=function(){
    		var winH =  $(window).height();
    		var certH = $(".error").height();
    		$(".error").css("margin-top",winH/2-certH/2-30);
    	}
    </script>
    <style>
    	.error{ background:#c12e2a; text-align:center; padding:30px; margin-left:30px; margin-right:30px; color:#fff; border-radius:5px;}
    	@media screen and (min-width:1024px){
    		.error{ width:700px; margin-left:auto; margin-right:auto;}
    	}
    </style>
</head>

<body>
<div class="error">内部错误!</div>
</body>
</html>
