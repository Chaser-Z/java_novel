//1.加载内容
function menu(url) {
	if (url != "") {
		$("#center_iframe").attr("src", url);
	}
}
//2.菜单选择
function menuSelect(role, index) {
	//取消选中菜单
	$("div[id^='_easyui_tree_']").attr("class", "tree-node");
	//选中目录&菜单
	$("div[id^='r00']").each(function (i, o) {
		if (o.id == role) {
			$("#accordion").accordion({border:false, fit:true}).accordion("select", i);//选中目录
			$("div[id='_easyui_tree_" + (i + index) + "']").attr("class", "tree-node tree-node-selected");//选中菜单
		}
	});
}

//3.登录检测
var ls;
function loginStatus(){
   jQuery.ajax({
        type: 'POST',
        url: '../manage/loginStatus',
        dataType: 'json',
        error: function(date){
        	$.messager.alert('消息提示','服务已经停止，请联系网站管理员!','warning');
        },
        success: function(date){
	 	 if(date.success==false){
	 	 	clearInterval(ls);
			window.location.href=window.location.href;
	 	 }
	    }
   });
}
//1000为1秒钟，5分钟检查一次
ls=setInterval("loginStatus()",5*60*1000);

//5.设置选中菜单样式
$(function(){
	//设置选中菜单样式
	$("div ul li ul li a").click(function(){
		$("div ul li ul li a").attr("style","");
		$(this).attr("style","color:#003D79;");
		
		$("div ul li ul li a").find("font").html("");
		$(this).find("font").html("&nbsp;<img class='img_hand' style='vertical-align:middle;' src='../images/icons/hand.png'/>");
  	});
});
