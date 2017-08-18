<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
	<title>404 - 页面不存在</title>
	<meta name="viewport" content="width=device-width,initial-scale=1.0,user-scalable=no" />
	<script src="../js/jquery-1.11.1.min.js"></script>
    <script type="text/javascript">
    	window.onload=function(){
    		var winH =  $(window).height();
    		var certH = $(".error").height();
    		$(".error").css("margin-top",winH/2-certH/2-30);
    	}
    </script>    
	<style type="text/css">
		.error{ padding-bottom:30px; /* border:solid 1px #e3e5e8; box-shadow:0 0 3px #edf1f0; */}
		@media screen and (min-width:1024px){
    		.error{ width:700px; margin-left:auto; margin-right:auto;}
    	}
		.img404{text-align:center; width:80%; margin:0 auto;}
		.text404{ text-align:center; font-size:15px; color:#8d8e90;}
	</style>   
</head>

<body>
<div>
	<div class="error">
		<div class="img404"><img src="${ctx }/images/404.png" width="100%" /></div>
		<div class="text404">哦哦！您访问的资源不存在或已删除！</div>
	</div>
</div>
</body>
</html>