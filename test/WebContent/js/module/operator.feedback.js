/*******************1平台*********************/
//列表
function feedbackList(){
   	$('#dg').datagrid({
		url:'../operator/feedbackList',
		toolbar:"#tb",
        //iconCls:'icon-edit',
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
            {align:'center',field:'name',width:'100',title: '姓名' },
            {align:'center',field:'email',width:'180',title: '邮箱' },
			{align:'left',field:'content',width:'660',title: '反馈内容' },
			{align:'center',field:'createdTime',formatter:createTime,width:'80',title: '反馈时间' }
            //{align:'center',field:'cz',width:'80',formatter:qxButtonJcznpt,title: '操作' }
           ]]
       });
}

function createTime(value,row,index){
	var date = new Date(value);
	var y = date.getFullYear();
	var M = date.getMonth()+1;
	var d = date.getDate();
	var h = date.getHours();
	var m = date.getMinutes();
	var s = date.getSeconds();
	return y+'-'+(M<10?('0'+M):M)+'-'+(d<10?('0'+d):d)+' '+(h<10?'0'+h:h)+":"+(m<10?'0'+m:m)+":"+(s<10?'0'+s:s);
}

function doSearch(value){
	$('#dg').datagrid('load',{
		content:value
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
	feedbackList();
});
