<%@ page contentType="text/html;charset=UTF-8" %>

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
    </style>
</head>

<body>
<div class="error">内部错误!</div>
</body>
</html>