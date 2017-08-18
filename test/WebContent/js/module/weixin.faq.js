//新增
function doAdd(){
	$('#faq_form').form('clear');
	$('#faq_edit').show();
    $('#faq_edit').dialog({modal:true});
}

//查询
function doSearch(value){
	$('#faq_data').datagrid('load',{
		question:value
	});
}

//提交
function doSubmit(){
    $('#faq_form').form('submit',{
		onSubmit: function(param){
			return $(this).form('enableValidation').form('validate');
	    },
        success: function(data_json){
	        var data = eval('(' + data_json + ')');
	        if (data.success===true){
				$('#faq_data').datagrid('reload');
			 	$("#faq_edit").dialog('close');
		 	 	$.messager.show({title:'消息提示',msg:'提交成功！',showType:'show'});
	        }else{
			 	$.messager.alert('消息提示','提交失败!');
			}
        }
    });
}

//编辑
function doEdit(id){
	$('#faq_form').form('clear');
	$('#faq_form').form('load','../wechat/faqLoad?id='+id);
	$('#faq_edit').show();
    $('#faq_edit').dialog({modal:true});
}

//删除
function doDel(id){
    $.messager.confirm("操作提示", "确定要删除操作？", function (data) {
        if (data) {
			$.ajax({
				url:'../wechat/faqDel?id='+id,
				type: "post",
		        dataType: "json",
				success:function(data){
					$('#faq_data').datagrid('reload');
			 	 	$.messager.show({title:'消息提示',msg:'提交成功！',showType:'show'});
				}
			});
        }
    });
}

//按钮
function czButton(value,row,index){
   	var c1='<a href="javascript:doEdit('+"'"+row.id+"'"+')"><img title="编辑" src="../images/icons/edit.png" style="vertical-align:middle;"/></a>';
   	var c2='<a href="javascript:doDel('+"'"+row.id+"'"+')"><img title="删除" src="../images/icons/delete.png" style="vertical-align:middle;"/></a>';
	return c1+'&nbsp;&nbsp;'+c2+'&nbsp;&nbsp;';
}

$(document).ready(function() {
	//常见问题列表
 	$('#faq_data').datagrid({
		url:'../wechat/faqList',
		toolbar:"#faq_tb",
        iconCls:'icon-edit',
        width: 'auto',
        height: 'auto',
        nowrap: false,  
        striped: true,
        border: true,
 		pageSize:20,
 		showFooter: true,
 		pageList:[10,15,20],
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
			{halign:'center',align:'left',field:'question',width:'150',title:'问题'},
	        {halign:'center',align:'left',field:'answer',width:'350',title:'答案'},
	        {align:'center',field:'createdTime_',width:'120',title:'创建时间'},
	        {align:'center',field:'operate',width:"80",formatter:czButton,title:'操作'}
        ]]
 	});
 });