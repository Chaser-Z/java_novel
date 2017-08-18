var editor_items=['fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold', 'italic', 'underline','removeformat','clearhtml', '|', 
		          'justifyleft', 'justifycenter', 'justifyright','formatblock','insertorderedlist','insertunorderedlist', '|', 'emoticons', 'image', 'link','unlink','|','source','fullscreen'];

//新增
function doAdd(){
	$('#teacher_form').form('clear');
	$("#teacher_cover_").html("");
	$("input:radio[name=status]").removeAttr("checked");
	$("input:radio[name='status']").eq(0).attr("checked",'checked');
	$('#teacher_edit').show();
    $('#teacher_edit').dialog({modal:true});
	
	//编辑器
	var editor=	KindEditor.create('#teacher_content', {
		resizeType : 1,
		allowPreviewEmoticons : false,
		allowImageUpload : true,
		uploadJson : '../js/kindeditor-4.1.10/jsp/upload_json.jsp',
        fileManagerJson : '../js/kindeditor-4.1.10/jsp/file_manager_json.jsp',
		items : editor_items,
		afterBlur: function(){this.sync();}
	});
	KindEditor.html('#teacher_content','');
}

//查询
function doSearch(title,type){
	$('#teacher_data').datagrid('load',{
		type:type,
		title:title
	});
}

//显示封面上传
function showUploadImg(){
	$('#img_form').form('clear');
	$('#img_info').show();
    $('#img_info').dialog({modal:true});
}

//封面上传
function doUploadImg(){
	$('#img_form').form('submit', {
		onSubmit: function(param){
			var file=$("#media_img").val();
			if(file===""){
				$.messager.alert('消息提示','请选择文件!');
				return false;
			}
			//file=file.toUpperCase();
            //if(!(file.indexOf(".JPG")!=-1)){
			//	$.messager.alert('消息提示','文件需为JPG格式!');
			//	return false;
			//}
		},
		success: function(json){
			var data = eval('(' + json + ')');
			if (data.success){
				$("#teacher_cover").val(data.fileName);
				$("#teacher_cover_").html("<img src='../upload/"+data.fileName+"' style='width:320px;height:160px;'>");
				$("#img_info").dialog('close');
				$.messager.show({title:'消息提示',msg:'上传成功！',showType:'show'});
			}else{
				$.messager.alert('消息提示','上传未成功!');
			}
		}
	});
}

//提交
function doSubmit(){
    $('#teacher_form').form('submit',{
		onSubmit: function(param){
			return $(this).form('enableValidation').form('validate');
	    },
        success: function(data_json){
	        var data = eval('(' + data_json + ')');
	        if (data.success===true){
				$('#teacher_data').datagrid('reload');
			 	$("#teacher_edit").dialog('close');
		 	 	$.messager.show({title:'消息提示',msg:'提交成功！',showType:'show'});
	        }else{
			 	$.messager.alert('消息提示','提交失败!');
			}
        }
    });
}

//编辑
function doEdit(id){
	$('#teacher_form').form('clear');
	$("#teacher_cover_").html("");
	$('#teacher_edit').show();
    $('#teacher_edit').dialog({modal:true});
	$.ajax({
		url:'../wechat/lookLoad?id='+id,
		type: "post",
        dataType: "json",
		success:function(data){
			//表单加载
			$('#teacher_form').form('load',{
			   	id:data.info.id,
			   	type:data.info.type,
			   	status:data.info.status,
			   	title:data.info.title,
			   	cover:data.info.cover,
			   	sequence:data.info.sequence,
			   	content:data.info.content
			});
			//编辑器
			KindEditor.create('#teacher_content', {
				resizeType : 1,
				allowPreviewEmoticons : false,
				allowImageUpload : true,
				uploadJson : '../js/kindeditor-4.1.10/jsp/upload_json.jsp',
		        fileManagerJson : '../js/kindeditor-4.1.10/jsp/file_manager_json.jsp',
				items :editor_items,
				afterBlur: function(){this.sync();}
			});
			KindEditor.html('#teacher_content',typeof(data.info.content)=='undefined'?'':data.info.content);
			//图片
			if(typeof(data.info.cover)!='undefined'){
				$("#teacher_cover_").html("<img src='../upload/"+data.info.cover+"' style='width:320px;height:160px;'>");
			}
		}
	});
}

//编辑
function doDel(id){
    $.messager.confirm("操作提示", "确定要删除操作？", function (data) {
        if (data) {
			$.ajax({
				url:'../wechat/lookDel?id='+id,
				type: "post",
		        dataType: "json",
				success:function(data){
					$('#teacher_data').datagrid('reload');
			 	 	$.messager.show({title:'消息提示',msg:'提交成功！',showType:'show'});
				}
			});
        }
    });
}

//封面
function coverView(value,row,index){
	return "<img style='width:200px;height:100px;padding:0;' src='../upload/"+value+"'/>";
}

//分类
function typeView(value,row,index){
	if(value=="1"){
		return '领域前沿';
	}else if(value=="2"){
		return '指南动态';
	}else if(value=="3"){
		return '教学技巧';
	}else if(value=="4"){
		return '实用资源';
	}
}

//状态
function statusView(value,row,index){
	if(value=="1"){
		return "<img src='../images/icons/editor.png' style='vertical-align:middle;'/>";
	}else if(value=="2"){
		return "<img src='../images/icons/issue.png' style='vertical-align:middle;'/>";
	}
}

//按钮
function czButton(value,row,index){
   	var c1='<a href="javascript:doEdit('+"'"+row.id+"'"+')"><img title="编辑" src="../images/icons/edit.png" style="vertical-align:middle;"/></a>';
   	var c2='<a href="javascript:doDel('+"'"+row.id+"'"+')"><img title="删除" src="../images/icons/delete.png" style="vertical-align:middle;"/></a>';
	return c1+'&nbsp;&nbsp;'+c2+'&nbsp;&nbsp;';
}

//列表
$(document).ready(function() {
 	$('#teacher_data').datagrid({
		url:'../wechat/lookList',
		toolbar:"#teacher_tb",
        iconCls:'icon-edit',
        idField:'id',
        pageSize:5,
        width: 'auto',
        height: 'auto',
        border: true,
        nowrap: false,
        striped: true,
 		showFooter: true,
 		remoteSort:false,
        collapsible:false,
		singleSelect:true,
		selectOnCheck:true,
		checkOnSelect:true,
		autoRowHeight:false,
        pagination:true,
        rownumbers:true,
		fitColumns:true,
		pageList:[5,10,15,20],
 		loadMsg:'正在加载....',
        columns:[[
			{align:'center',field:'cover',width:'200',formatter:coverView,title:'封面'},
			{align:'center',field:'type',width:'60',formatter:typeView,title:'分类'},
	        {halign:'center',align:'left',field:'title',width:'160',title:'标题'},
	        {align:'center',field:'sequence',width:'50',title:'排序'},
	        {align:'center',field:'readCount',width:'50',title:'阅读'},
	        {align:'center',field:'status',width:'50',formatter:statusView,title:'状态'},
	        {align:'center',field:'createdTime_',width:'120',title:'创建时间'},
	        {align:'center',field:'operate',width:"100",formatter:czButton,title:'操作'}
        ]]
 	});
 });
