//列表
function userList(){
   	$('#dg').datagrid({
		url:'../operator/userList',
		toolbar:"#tb",
        iconCls:'icon-edit',
        width: 'auto',
        height: 'auto',
        nowrap: false,  
        striped: true,
        border: true,
   		pageSize:20,
   		showFooter: true,
   		pageList:[20,25,30],
        collapsible:false,
        remoteSort:false,   
        idField:'id',
		singleSelect:true,
		selectOnCheck: true,
		checkOnSelect: true,
		autoRowHeight:false,
        pagination:true,
        rownumbers:true,
		fitColumns:true,
   		loadMsg:'正在加载....',
           columns:[[
            {align:'center',field:'nickname',width:'150',title: '用户昵称' },
            {align:'center',field:'email',width:'150',title: '邮箱' },
            {align:'center',field:'gender',formatter:gender,width:'50',title: '性别' },
            {align:'center',field:'identityType',formatter:regWay,width:'80',title: '注册方式' },
			{align:'center',field:'createdTime',formatter:regTime,width:'140',title: '注册时间' }
            //{align:'center',field:'operate',width:'80',formatter:operate,title: '操作' }
           ]]
       });
}

//性别
function gender(value,row,index){
	if(value=="1"){
		return '男';
	}else if(value=="2"){
		return '女';
	}else{
		return '未知';
	}
}

function regWay(value,row,index){
	if(value=="wechat"){
		return '微信';
	}else if(value=="qq"){
		return 'QQ';
	}else{
		return '邮箱';
	}
}

function regTime(value,row,index){
	var date = new Date(value);
	var y = date.getFullYear();
	var M = date.getMonth()+1;
	var d = date.getDate();
	var h = date.getHours();
	var m = date.getMinutes();
	var s = date.getSeconds();
	return y+'-'+(M<10?('0'+M):M)+'-'+(d<10?('0'+d):d)+' '+(h<10?'0'+h:h)+":"+(m<10?'0'+m:m)+":"+(s<10?'0'+s:s);
}

function operate(value,row,index){
   	var c1='<a href="javascript:doEdit('+"'"+row.id+"'"+')"><img title="编辑" src="../images/icons/edit.png" style="vertical-align:middle;"/></a>';
   	var c2='<a href="javascript:doDel('+"'"+row.id+"'"+')"><img title="删除" src="../images/icons/delete.png" style="vertical-align:middle;"/></a>';
	return c1+'&nbsp;&nbsp;'+c2+'&nbsp;&nbsp;';
}

function doSearch(){
	var name = $("#nickname").val();
	var regway = $('#regway').combobox('getValue');
	$('#dg').datagrid('load',{
		name:name,
		identityType:regway
	});
	$('#dg').datagrid('clearSelections');
}


/*******************公共*********************/
function doLoading(){
    $("<div class=\"datagrid-mask\"></div>").css({display:"block",width:"100%",height:$(window).height()}).appendTo("body");
    $("<div class=\"datagrid-mask-msg\"></div>").html("正在处理，请稍候...").appendTo("body").css({display:"block",left:($(document.body).outerWidth(true) - 190) / 2,top:($(window).height() - 45) / 2});
}

function doLoadEnd(){
    $(".datagrid-mask").remove();
    $(".datagrid-mask-msg").remove();       
}

$(document).ready(function() {
	userList();	
});
