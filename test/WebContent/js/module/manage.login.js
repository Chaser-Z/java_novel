function login(){
   $('#login_form').form('submit',{
      onSubmit: function(param){
   		return $(this).form('enableValidation').form('validate');
      },
      success:function(data){
 		var data = eval('(' + data + ')');
        if(data.success==true){
			window.location.href='../manage/index.html';
		}else{
			$.messager.alert('消息提示','用户名或密码不正确！','warning');
		}
      }
   });
}

function getQueryString(name){
  var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
  var r = window.location.search.substr(1).match(reg);
  if (r != null) return unescape(r[2]); return null;
}
 
function loginValid(){
  var username=$('#username').textbox("getValue");
  var password=$('#password').textbox("getText");
  var gotopage=getQueryString("goto_page");
  if($('#login_form').form('validate'))
     jQuery.ajax({
        type: 'POST',
        url: '../manage/loginValid',
        data: {
        	username:username,
        	password:password,
        	goto_page:gotopage
        },
        dataType: 'json',
        success: function(date){
	 	 if(date.success){
		 	window.location.href='../manage/index.html';
		 }else{
		 	$.messager.alert('消息提示','用户名或密码不正确！','warning');
		 }
	    }
   });
}

jQuery(document).ready(function(){
	//valid login hash top
	if (self != top) {  
		top.location.href=window.location.href;
	}
	
	//use key
	document.onkeydown = function(e){ 
        var ev = document.all ? window.event : e; 
        if(ev.keyCode==13) { 
        	loginValid();
        } 
    }
});
