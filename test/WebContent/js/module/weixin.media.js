var editor_items=['fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold', 'italic', 'underline','removeformat', '|', 
				  'justifyleft', 'justifycenter', 'justifyright', 'insertorderedlist','insertunorderedlist', '|', 'emoticons', 'image', 'link'];

//************************************************ 公共组件 start
//显示媒体上传
function showUploadImg(){
	$('#img_form').form('clear');
	$('#img_type').val("image");
	$("#isCover").val("1");
	$('#img_info').show();
    $('#img_info').dialog({modal:true});
}

//提交媒体上传
function doUploadImg(){
	$('#img_form').form('submit', {
		onSubmit: function(param){
			var file=$("#media_img").val();
			if(file===""){
				$.messager.alert('消息提示','请选择文件!');
				return false;
			}
			/*file=file.toUpperCase();
            if(!(file.indexOf(".JPG")!=-1)){
				$.messager.alert('消息提示','文件需为JPG格式!');
				return false;
			}*/
		},
		success: function(json){
			var data = eval('(' + json + ')');
			if (data.success){
				$("input[name='thumb_media_id']").val(data.media_id);
				$("input[name='cover']").val(data.fileName);
				$("#tw_fm").show().html("<img src='../"+data.fileName+"' style='height:100px;margin-bottom:5px;'>");
				$("#img_info").dialog('close');
				$.messager.show({title:'消息提示',msg:'上传成功！',showType:'show'});
			}else{
				$.messager.alert('消息提示','上传未成功!');
			}
		}
	});
}

//************************************************ 公共组件 end

//************************************************ 多图文消息 start
//多图文列表
function moreList(id){
   	$('#more_data').datagrid({
		url:'../weixin/graphicMsgListById?id='+id,
		toolbar:"#more_tb",
        iconCls:'icon-edit',
        width: 'auto',
        height: 'auto',
        nowrap: false,  
        striped: true,
        border: false,
   		showFooter: true,
        collapsible:false,
        remoteSort:false,   
        idField:'id',
		singleSelect:true,
		selectOnCheck: true,
		checkOnSelect: true,
		autoRowHeight:false,
        pagination:false,
        rownumbers:true,
		fitColumns:true,
   		loadMsg:'正在加载....',
        columns:[[{align:'left',field:'title',width:'100',title:'标题'}]],
		onClickRow:function(index,row){
			var tmpMediaId = $("#more_media_id").val();
			$('#more_form').form('clear');
			$("#more_fm").html("");
			$.ajax({
				url:'../weixin/graphicMsgLoad?id='+row.id,
				type: "post",
		        dataType: "json",
				success:function(row){
					$("#temp_id").val(row.weixinGraphicId);
					//设置封面
					if(typeof(row.cover)!=='undefined'&&row.cover!==''){
						$("#tw_fm").html("<img src='../"+row.cover+"' style='height:100px;'>");
					}
					//表单加载
					$('#more_form').form('load',{
					   	id:row.weixinGraphicId,
					   	media_id:tmpMediaId,
					   	msgid:row.id,
					   	type:'2',
					   	title:row.title,
					   	cover:row.cover,
					   	thumb_media_id:row.thumb_media_id,
					   	author:row.author,
					   	title:row.title,
					   	content_source_url:row.content_source_url,
					   	content:row.content,
					   	digest:row.digest,
					   	show_cover_pic:row.show_cover_pic,
					   	index:index
					});
					//表单编辑器
//					var editor=	KindEditor.create('#more_content', {
//						resizeType : 1,
//						allowPreviewEmoticons : false,
//						allowImageUpload : true,
//						uploadJson : '../js/kindeditor-4.1.10/jsp/upload_json.jsp',
//				        fileManagerJson : '../js/kindeditor-4.1.10/jsp/file_manager_json.jsp',
//				        items : editor_items,
//						afterBlur: function(){this.sync();}
//					});
//					KindEditor.html('#more_content',typeof(row.content)=='undefined'?'':row.content);
					wwei_editor.setContent(row.content);
				}
			});
		}
   	});
}

//多图文窗口显示
/*function showMore(type){
	$('#more_form').form('clear');
	$("#more_fm").html("");
	$("#more_type").val(type);
	$('#more_edit').show();
    $('#more_edit').dialog({modal:true});
	$("#temp_id").val("");
	moreList("");//加载列表
	$("#more_show_cover_pic").val($('#more_data').datagrid('getData').total===0?'1':'0');//设置是否为第一行，第一行默认为封面
	KindEditor.html('#more_content','');//设置文本内容
}*/

//多图文新增
function addMore(){
	$('#more_form').form('clear');
	$("#tw_fm").html("").hide();
	$("#more_id").val($("#temp_id").val());
	//$("#more_show_cover_pic").val($('#more_data').datagrid('getData').total===0?'1':'0');//设置是否为第一行，第一行默认为封面
	$("#more_show_cover_pic").val("0");
	$("#more_index").val($('#more_data').datagrid('getData').total);
	//KindEditor.html('#more_content','');//设置文本内容
	wwei_editor.setContent("");
}

//多图文消息编辑
/*function doEditMore(id){
	$('#more_form').form('clear');
	//KindEditor.html('#more_content','');
	$.ajax({
		url:'../weixin/graphicLoad?id='+id,
		type: "post",
        dataType: "json",
		success:function(data){
			$("#temp_id").val(data.info.id);
			//设置封面
			if(typeof(data.info.cover)!=='undefined'&&data.info.cover!==''){
				$("#more_fm").html("<img src='../upload/"+data.info.cover+"' style='width:230px;height:98px;'>");
			}
			//表单加载
			$('#more_form').form('load',{
			   	id:data.info.id,
			   	msgid:data.msg.id,
			   	type:data.info.type,
			   	title:data.info.title,
			   	cover:data.info.cover,
			   	thumb_media_id:data.msg.thumb_media_id,
			   	author:data.msg.author,
			   	title:data.msg.title,
			   	content_source_url:data.msg.content_source_url,
			   	content:data.msg.content,
			   	digest:data.msg.digest,
			   	show_cover_pic:data.msg.show_cover_pic
			});
			//表单编辑器
			var editor=	KindEditor.create('#more_content', {
				resizeType : 1,
				allowPreviewEmoticons : false,
				allowImageUpload : true,
				uploadJson : '../js/kindeditor-4.1.10/jsp/upload_json.jsp',
		        fileManagerJson : '../js/kindeditor-4.1.10/jsp/file_manager_json.jsp',
		        items : editor_items,
				afterBlur: function(){this.sync();}
			});
			KindEditor.html('#more_content',typeof(data.msg.content)=='undefined'?'':data.msg.content);
			//设置列表
			moreList(id);
		}
	});
	$('#more_edit').show();
    $('#more_edit').dialog({modal:true});
}*/

function doEditMore(id){
	window.location.href = "../weixin/morewxeditor?id="+id;
}

//多图文提交
function doSubmitMore(refer){
	$("#more_refer").val(refer);
	$("#more_type").val("2");
	var thumb = $("#more_thumb_media_id").val();
	if(thumb==''){
		alert("请上传封面！");
		return false;
	}
    $('#more_form').form('submit',{
		onSubmit: function(param){
			return $(this).form('enableValidation').form('validate');
	    },
        success: function(json){        	
	        var data = eval('(' + json + ')');
	        if (refer==='save'){
		        if (data.success===false){
		        	$("#temp_id").val(data.graphic.id);
		        	$("#more_id").val(data.graphic.id);
		        	$("#more_msgid").val(data.graphicMsg.id);
		        	moreList(data.graphic.id);
		        	//$('#tw_data').datagrid('reload');
			 	 	$.messager.show({title:'消息提示',msg:'保存成功！',showType:'show'});
		        }else{
				 	$.messager.alert('消息提示','提交失败!');
				}
	        }else{
		        if(data.success===true){
					//$('#tw_data').datagrid('reload');
				 	//$("#more_edit").dialog('close');
		        	$("#more_media_id").val(data.graphic.media_id);
			 	 	$.messager.show({title:'消息提示',msg:'提交成功！',showType:'show'});
			 	 	window.location.href='../weixin/mediaView';
		        }else{
		        	if(typeof(data.errmsg)!='undefined'){
		        		$.messager.alert('消息提示',data.errmsg);
		        	}else{
		        		$.messager.alert('消息提示','提交失败!');
		        	}
				}
	        }
        }
    });
}

//************************************************ 多图文消息 start

//************************************************ 单图文消息 start
//显示图文窗口
//function showTW(type){
//	$('#tw_form').form('clear');
//	$("#tw_fm").html("");
//	$("#tw_type").val(type);
//	$('#tw_edit').show();
//    $('#tw_edit').dialog({modal:true});
//	
//	//编辑器
//	var editor=	KindEditor.create('#content', {
//		resizeType : 1,
//		allowPreviewEmoticons : false,
//		allowImageUpload : true,
//		uploadJson : '../js/kindeditor-4.1.10/jsp/upload_json.jsp',
//        fileManagerJson : '../js/kindeditor-4.1.10/jsp/file_manager_json.jsp',
//        items : editor_items,
//		afterBlur: function(){this.sync();}
//	});
//	KindEditor.html('#content','');
//}
function showTW(){
	window.location.href="../weixin/wxeditor";
}
function showMore(){
	window.location.href = "../weixin/morewxeditor";
}

//显示图文类型
function twTypeView(value,row,index){
	var label='';
   	if(value=='1'){
   		label="单图文";
   	}else{
   		label="多图文";
   	}
	return label;
}

//显示图文封面
function twCoverView(value){
   	if(value!==''&&typeof(value)!=='undefined'){
   		return "<img src='../"+value+"'/>";
   	}
	return value;
}

//显示操作按钮
function twCZButton(value,row,index){
	if(row.type==1){
		return "<a href='javascript:doEditTW("+'"'+row.id+'"'+")'>编辑</a>";
	}else{
		return "<a href='javascript:doEditMore("+'"'+row.id+'"'+")'>编辑</a>";
	}
}

//编辑图文消息
/*function doEditTW(id){
	$('#tw_form').form('clear');
	$.ajax({
		url:'../weixin/graphicLoad?id='+id,
		type: "post",
        dataType: "json",
		success:function(data){
			//设置封面
			if(typeof(data.info.cover)!='undefined'){
				$("#tw_fm").html("<img src='../upload/"+data.info.cover+"' style='width:230px;height:98px;'>");
			}
		
			//表单加载
			$('#tw_form').form('load',{
			   	id:data.info.id,
			   	type:data.info.type,
			   	title:data.info.title,
			   	cover:data.info.cover,
			   	thumb_media_id:data.msg.thumb_media_id,
			   	author:data.msg.author,
			   	title:data.msg.title,
			   	content_source_url:data.msg.content_source_url,
			   	content:data.msg.content,
			   	digest:data.msg.digest,
			   	show_cover_pic:data.msg.show_cover_pic
			});
			
			//编辑器
			var editor=	KindEditor.create('#content', {
				resizeType : 1,
				allowPreviewEmoticons : false,
				allowImageUpload : true,
				uploadJson : '../js/kindeditor-4.1.10/jsp/upload_json.jsp',
		        fileManagerJson : '../js/kindeditor-4.1.10/jsp/file_manager_json.jsp',
		        items : editor_items,
				afterBlur: function(){this.sync();}
			});
			KindEditor.html('#content',typeof(data.msg.content)=='undefined'?'':data.msg.content);
		}
	});
	$('#tw_edit').show();
    $('#tw_edit').dialog({modal:true});
}*/

function doEditTW(id){
	window.location.href = "../weixin/wxeditor?id="+id;
}

function doSubmitPush(){
	$('#tw_form').form('submit',{
		onSubmit: function(param){
			return $(this).form('enableValidation').form('validate');
	    },
        success: function(json){
	        var data = eval('(' + json + ')');
	        if(data.success=="true"){
        		$.messager.show({title:'消息提示',msg:'保存成功！',showType:'show'});
		 	 	window.location.href='../weixin/graphicMsgView';
        	}
        }
    });
}

//提交单图文消息
function doSubmitTW(refer){
	$("#refer").val(refer);
	$("#tw_type").val("1");
	$("#wwei_editor").val(wwei_editor.getContent());
	var thumb = $("#tw_thumb_media_id").val();
	if(thumb==''){
		alert("请上传封面！");
		return false;
	}
	/*if(refer=="submit"){
		var content = $("#wwei_editor").val();
		var images = $(content).find("img");
		images.each(function(){
			if($(this).attr("src").indexOf("http://mmbiz.qpic.cn")<0){
				var img = $(this);
				var imgSrc = $(this).attr("src");
				$.ajax({
					url:"../weixin/uploadImg",
					data:"imgSrc="+imgSrc,
					type:"post",
					async:false,
					success:function(result){
						var result = eval("("+result+")");
						if(result.url!=null&&result.url!=''){
							alert(result.url);
							img.attr("src",result.url);
						}else{
							alert("errorCode:" + result.errcode);
						}
					}
				});
			}
		});
		return false;
	}*/
    $('#tw_form').form('submit',{
		onSubmit: function(param){
			return $(this).form('enableValidation').form('validate');
	    },
        success: function(json){
	        var data = eval('(' + json + ')');
	        if(refer=="save"){
	        	if(data.success=="true"){
	        		$.messager.show({title:'消息提示',msg:'保存成功！',showType:'show'});
			 	 	window.location.href='../weixin/mediaView';
	        	}
	        }else if(refer=="submit"){
	        	if(data.media_id!=null&&data.media_id!=''&&typeof(data.media_id)!='undefined'){
	        		$.messager.show({title:'消息提示',msg:'提交成功！',showType:'show'});
	        		window.location.href='../weixin/mediaView';
	        	}else if(data.errcode!=null&&data.errcode=='0'){
	        		$.messager.show({title:'消息提示',msg:'修改成功！',showType:'show'});
	        		window.location.href='../weixin/mediaView';
	        	}else{
	        		if(typeof(data.errcode)!='undefined'){
		        		$.messager.alert('消息提示',data.errmsg);
		        	}else{
		        		$.messager.alert('消息提示','提交失败!');
		        	}
		        }
	        }
        }
    });
}


//图文视图
var twcardview = $.extend({}, $.fn.datagrid.defaults.view, {
    renderRow: function(target, fields, frozen, rowIndex, rowData){
        var cc = [];
        cc.push('<td colspan=' + fields.length + ' style="padding:5px 5px;border:0;">');
        if (!frozen){
            cc.push('<div style="width:30px;float:left;text-align:center;padding:73px 0 59px 0;border:1px dashed #B8B8B8;">'+(rowIndex+1)+'</div>');
            cc.push('<img src="../' + rowData.cover + '" style="width:250px;height:150px;float:left;border:1px dashed #B8B8B8;"><div style="position:absolute;"><div style="left:33px;position:relative;float:left;font-size:13px;">'+(rowData.type=='1'?'<span class="c1">单</span>':'<span class="c6">多</span>')+'</div></div>');
            cc.push('<div style="float:left;width:350px;height:150px;padding:0 50px 0 50px;border:1px dashed #B8B8B8;">');
            for(var i=0; i<fields.length; i=i+1){
                var copts = $(target).datagrid('getColumnOption', fields[i]);
                if(copts.title=='消息类型'){
                	var media_id=rowData['media_id'];
                	var bjzt=(typeof(media_id)!=='undefined'?"<sup style='color:green;'>已提交</sup>":"<sup style='color:red;'>编辑</sup>");
                	if(rowData[fields[i]]=="1"){
                		rowData[fields[i]]="<span>单图文消息</span>&nbsp;&nbsp;"+bjzt;
                	}else{
                		rowData[fields[i]]="<span>多图文消息</span>&nbsp;&nbsp;"+bjzt;
                	}
                }
                else if(copts.title=='操　　作'){
					if(rowData['type']==1){
						rowData[fields[i]]="<a href='javascript:doEditTW("+'"'+rowData['id']+'"'+")'>编辑</a>";
					}else{
						rowData[fields[i]]="<a href='javascript:doEditMore("+'"'+rowData['id']+'"'+")'>编辑</a>";
					}
                }
                //编辑状态
               	cc.push('<p style="border-bottom:1px dashed #B8B8B8;white-space:nowrap;text-overflow:ellipsis;overflow:hidden;"><span class="c-label" style="">'+copts.title+':</span> '+rowData[fields[i]]+'</p>');
            }
            cc.push('</div>');
        }
        cc.push('</td>');
        return cc.join('');
    }
});

$(document).ready(function() {
	//页签
	$('#tt').tabs({ 
		onSelect:function(title){ 
			if(title=='素材管理'){
		    	midiaList();
			}  
		} 
	});
	//列表
   	$('#tw_data').datagrid({
//   		view: twcardview,
		url:'../weixin/graphicList',
		toolbar:"#tw_tb",
        iconCls:'icon-edit',
        width: 'auto',
        height: 'auto',
        nowrap: false,
        striped: true,
        border: true,
   		pageSize:10,
   		showFooter: true,
   		pageList:[5,10,15,20],
   		fit:true,
        collapsible:false,
        remoteSort:false,   
        idField:'id',
		singleSelect:true,
		selectOnCheck: true,
		checkOnSelect: true,
		autoRowHeight:false,
        pagination:true,
        rownumbers:false,
		fitColumns:true,
   		loadMsg:'正在加载....',
        columns:[[
			{align:'center',field:'type',width:'250',formatter:twTypeView,title: '消息类型' },
			{align:'center',field:'title',width:'300',title: '消息标题' },
            {align:'center',field:'createdTime_',width:'150',title: '上传时间' },
            {align:'center',field:'twcz',width:'150',formatter:twCZButton,title: '操　　作' }
        ]]
   	});
});
//************************************************ 单图文消息 end

//************************************************ 素材管理 start
//显示媒体上传:图片（image）、语音（voice）、视频（video）和缩略图（thumb）
function doSearch(type){
	$('#media_data').datagrid('load',{ type:type });
	$('#media_data').datagrid('clearSelections');
}

//显示媒体上传
function showUpload(){
	$('#sc_form').form('clear');
	$("input[type=radio][name=type][value='image']").prop("checked",true);
	$('#media_info').show();
    $('#media_info').dialog({modal:true});
}

//提交媒体上传
function doUpload(){
	$('#sc_form_button').linkbutton({ text: '上传中..' });
	$('#sc_form_button').linkbutton('disable');
    $('#sc_form').form('submit', {
	    onSubmit: function(param){
			var file=$("#media").val();
			var type=$('input[name="type"]:checked').val();
                if(type===""||type===null){
					$.messager.alert('消息提示','请选择类型!');
					$('#sc_form_button').linkbutton('enable');
					return false;
				}
                if(file===""){
					$.messager.alert('消息提示','请选择文件!');
					$('#sc_form_button').linkbutton('enable');
					return false;
				}
				file=file.toUpperCase();
				if(type=="image"){
					if(!(file.indexOf(".JPG")!=-1)&&!(file.indexOf(".BMP")!=-1)&&!(file.indexOf(".PNG")!=-1)&&!(file.indexOf(".JPEG")!=-1)&&!(file.indexOf(".GIF")!=-1)){
						$.messager.alert('消息提示','文件格式错误!');
						$('#sc_form_button').linkbutton('enable');
						return false;
					}
				}
                /*if(!(file.indexOf(".JPG")!=-1||file.indexOf(".AMR")!=-1||file.indexOf(".MP3")!=-1||file.indexOf(".MP4")!=-1)){
					$.messager.alert('消息提示','文件需为JPG、AMR、MP3、MP4格式!');
					$('#sc_form_button').linkbutton('enable');
					return false;
				}*/
           },
           success: function(json){
           		var data = eval('(' + json + ')');
               	if (data.success){
   					$('#media_info').dialog('close');
   					$('#media_data').datagrid('reload',{});
                	$.messager.show({title:'消息提示',msg:'上传成功！',showType:'show'});
	           	}else{
			   		$.messager.alert('消息提示','上传未成功!');
			   	}
		   		$('#sc_form_button').linkbutton({ text: '上传' });
				$('#sc_form_button').linkbutton('enable');
           }
    });
}

//显示.媒体类型
function mediaTypeView(value,row,index){
	var label='';
   	if(value=='image'){label="图片";}
   	else if(value=='voice'){label="语音";}
   	else if(value=='video'){label="视频";}
   	else if(value=='thumb'){label="缩略图";}
	return label;
}

//显示.名称
function mediaNameView(value,row,index){
   	if(row.type=='image'||row.type=='thumb'){
		if(typeof(row.fileName)!="undefined"){
			value="<img src='../"+row.fileName+"' style='height:80px;'/>";
		}
	}
	return value;
}

//媒体上传.列表
function midiaList(){
   	$('#media_data').datagrid({
		url:'../weixin/mediaList',
		//toolbar:"#media_tools",
        iconCls:'icon-edit',
        toolbar:"#tb",
        width: 'auto',
        height: 'auto',
        nowrap: false,  
        striped: true,
        border: true,
   		pageSize:5,
   		showFooter: true,
   		pageList:[5,10,15,20],
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
			{align:'center',field:'type',width:'100',formatter:mediaTypeView,title:'素材类型'},
			{align:'center',field:'name',width:'100',title:'素材名称'},
			{align:'center',field:'fileName',width:'200',formatter:mediaNameView,title:'素材内容'},
	        {align:'center',field:'createdTime_',width:'120',title:'上传时间'}
        ]]
   	});
}

//************************************************ 素材管理 end


function initTwForm(id){
	$('#tw_form').form('clear');	
	$.ajax({
		url:"../weixin/graphicLoad?id="+id,
		type: "post",
        dataType: "json",
		success:function(data){
			//设置封面
			if(typeof(data.info.cover)!='undefined'){
				$("#tw_fm").html("<img src='../"+data.info.cover+"' style='height:100px;'>").show();
			}
			
			//表单加载
			$('#tw_form').form('load',{
			   	id:data.info.id,
			   	type:data.info.type,
			   	title:data.info.title,
			   	cover:data.info.cover,
			   	thumb_media_id:data.msg.thumb_media_id,
			   	author:data.msg.author,
			   	title:data.msg.title,
			   	content_source_url:data.msg.content_source_url,
			   	content:data.msg.content,
			   	digest:data.msg.digest,
			   	show_cover_pic:data.msg.show_cover_pic
			});
			
			wwei_editor.addListener("ready", function () {
		        // editor准备好之后才可以使用
		        wwei_editor.setContent(data.msg.content);
		    });
		}
	});
}

function initMoreForm(id){
	$('#more_form').form('clear');
	$.ajax({
		url:'../weixin/graphicLoad?id='+id,
		type: "post",
        dataType: "json",
		success:function(data){
			$("#temp_id").val(data.info.id);
			//设置封面
			if(typeof(data.info.cover)!=='undefined'&&data.info.cover!==''){
				$("#tw_fm").html("<img src='../"+data.info.cover+"' style='height:98px;'>").show();
			}
			//表单加载
			$('#more_form').form('load',{
			   	id:data.info.id,
			   	media_id:data.info.media_id,
			   	msgid:data.msg.id,
			   	type:data.info.type,
			   	title:data.info.title,
			   	cover:data.info.cover,
			   	thumb_media_id:data.msg.thumb_media_id,
			   	author:data.msg.author,
			   	title:data.msg.title,
			   	content_source_url:data.msg.content_source_url,
			   	content:data.msg.content,
			   	digest:data.msg.digest,
			   	show_cover_pic:data.msg.show_cover_pic,
			   	index:0
			});
			//表单编辑器
			wwei_editor.addListener("ready", function () {
		        // editor准备好之后才可以使用
		        wwei_editor.setContent(data.msg.content);
		    });
			//设置列表
			moreList(id);
		}
	});	
}

function initForm(id){
	$('#tw_form').form('clear');	
	$.ajax({
		url:"../weixin/graphicMsgLoad?id="+id,
		type: "post",
        dataType: "json",
		success:function(data){			
			//表单加载
			$('#tw_form').form('load',{
				title:data.title,
				coverLink:data.cover,
				digest:data.digest,
				source:data.author,
				graphicMsgId:data.id
			});
			
			wwei_editor.addListener("ready", function () {
		        // editor准备好之后才可以使用
		        wwei_editor.setContent(data.content);
		    });
		}
	});
}
