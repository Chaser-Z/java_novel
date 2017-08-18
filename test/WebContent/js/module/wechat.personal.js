//1.隐藏微信中网页底部导航栏
function onBridgeReady(){
   WeixinJSBridge.call('hideToolbar');
}
if (typeof WeixinJSBridge == "undefined"){
    if( document.addEventListener ){
        document.addEventListener('WeixinJSBridgeReady', onBridgeReady, false);
    }else if (document.attachEvent){
        document.attachEvent('WeixinJSBridgeReady', onBridgeReady); 
        document.attachEvent('onWeixinJSBridgeReady', onBridgeReady);
    }
}else{
    onBridgeReady();
}

//2.验证用户
function validUser(username,password){
	$.ajax({
		url:'../wechat/validUser',
	    data:{    
	           username:username,
	           password:password
	    }, 
		type: "post",
	       dataType: "json",
		success:function(data){
			if(data.success==true){
				bindingUser(username);
			}else{
				$("#msg_bind").html(data.msg);
			}
		}
	});
}

//3.绑定用户
function bindingUser(username){
	var openid=$("#openid").val();
	var jczn_url=$("#jczn_url").val();
	$.ajax({
		url:'../wechat/bindingUser',
	    data:{    
            username:username,
            openid:openid
	    }, 
		type: "post",
        dataType: "json",
		success:function(data){
			if(data.success==true){
				//绑定成功，跳转页面
				window.location.href="../wechat/personal?username="+username+"&openid="+openid;
			}else{
				$("#msg_bind").html(data.msg);
			}
		}
	});
}

$(document).ready(function() {
	//提交用户验证
	$("#validFormSave").click( function() {
		//参数
		var username=$("#username").val();
  		var password=$("#password").val();
  		if(username==''){
  			$("#username").focus();
  			$("#msg_bind").html('请填写用户名！');
  			return;
  		}
  		if(password==''){
  			$("#password").focus();
  			$("#msg_bind").html('请填写密码！');
  			return;
  		}
		//提交
		validUser(username,password);
	});
});