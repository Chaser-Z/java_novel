//搜索
function doSearch(value){
	$('#list_data').datagrid('load',{
		content:value
	});
	$('#list_data').datagrid('clearSelections');
}
	
//新建
var editor;
function doAdd(type){
	/*$("#wxeditorDetailSrc").attr("src","../weixin/wxeditor"); //设置IFRAME的SRC;
	$("#dialog_wxeditor").show();
    $("#dialog_wxeditor").dialog({
        bgiframe: true,
        resizable: true, //是否可以重置大小
        height: 683, //高度
        width: 920, //宽度
        draggable: false, //是否可以拖动。
        title: "微信编辑器",
        modal: true
    });*/
	//设置表单
	$('#m_form').form('clear');
	$('#msgtype').val("text");
	$('#type').combobox('select', 'all');
	
	//设置窗口
	$('#dialog_edit').show();
    $('#dialog_edit').dialog({modal:true});
	
	//编辑器插件.文字
	KindEditor.lang({wzcj : '文字'});
	KindEditor.plugin('wzcj', function(K) {
		var self = this, name = 'wzcj';
		self.clickToolbar(name, function() {
			//清空内容
			self.html('');
			
		    //设置消息类型
		    $('#msgtype').val("text");
		});
	});
	//编辑器插件.图片
	KindEditor.lang({tpcj : '图片'});
	KindEditor.plugin('tpcj', function(K) {
		var self = this, name = 'tpcj';
		self.clickToolbar(name, function() {
			//清空内容
			self.html('');
			
		    //设置消息类型
		    $('#msgtype').val("image");
		    
			//打开窗体
			$('#dialog_mt').show();
		    $('#dialog_mt').dialog({modal:true});
		    
		    //设置列表
		    mediaList();
		});
	});
	//编辑器插件.语音
	KindEditor.lang({yycj : '语音'});
	KindEditor.plugin('yycj', function(K) {
		var self = this, name = 'yycj';
		self.clickToolbar(name, function() {
			//清空内容
			self.html('');
			
		    //设置消息类型
		    $('#msgtype').val("voice");
		    
			//打开窗体
			$('#dialog_mt').show();
		    $('#dialog_mt').dialog({modal:true});
		    
		    //设置列表
		    mediaList();
		});
	});
	//编辑器插件.视频
	KindEditor.lang({spcj : '视频'});
	KindEditor.plugin('spcj', function(K) {
		var self = this, name = 'spcj';
		self.clickToolbar(name, function() {
			//清空内容
			self.html('');
			
		    //设置消息类型
		    $('#msgtype').val("video");
		    
			//打开窗体
			$('#dialog_mt').show();
		    $('#dialog_mt').dialog({modal:true});
		    
		    //设置列表
		    mediaList();
		});
	});
	//编辑器插件.图文
	KindEditor.lang({twxx : '图文消息'});
	KindEditor.plugin('twxx', function(K) {
		var self = this, name = 'twxx';
		self.clickToolbar(name, function() {
		    //清空内容
			self.html('');
			
		    //设置消息类型
		    $('#msgtype').val("mpnews");
			
			//打开窗体
			$('#dialog_tw').show();
		    $('#dialog_tw').dialog({modal:true});
		    
		    //设置列表
		    graphicList();
		});
	});
	//编辑器插件.插件应用
	editor=	KindEditor.create('textarea[name="content"]', {
		resizeType : 1,
		allowImageUpload : false,
		allowPreviewEmoticons : false,
		items : ['wzcj','tpcj','yycj','spcj','twxx'],
		//items : ['wzcj','tpcj','yycj','twxx'],//TODO 视频发送需再调试
		afterBlur: function(){this.sync();}
	});
	KindEditor.html('textarea[name="content"]','');
}

//媒体列表
function mediaList(){
	//媒体类型
	function mediaTypeView(value,row,index){
		var label='';
	   	if(value=='image'){label="图片";}
	   	else if(value=='voice'){label="语音";}
	   	else if(value=='video'){label="视频";}
	   	else if(value=='thumb'){label="缩略图";}
		return label;
	}
	
	//媒体名称
	function mediaNameView(value,row,index){
	   	if(row.type=='image'||row.type=='thumb'){
			if(typeof(row.fileName)!="undefined"){
				value="<img src='../upload/"+row.fileName+"' style='width:250px;height:150px;'/>";
			}
		}
		return value;
	}
	
	//媒体列表
   	$('#mt_data').datagrid({
		url:'../weixin/mediaList?type='+$('#msgtype').val(),
        iconCls:'icon-edit',
        width: '620px',
        height: 'auto',
        nowrap: false,  
        striped: true,
        border: true,
   		pageSize:15,
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
			{align:'center',field:'type',width:'100',formatter:mediaTypeView,title:'消息类型'},
			{align:'center',field:'name',width:'350',formatter:mediaNameView,title:'名称'},
	        {align:'center',field:'createdTime_',width:'120',title:'上传时间'}
        ]],
		onClickRow:function(rowIndex, rowData){
            $('#id').val(rowData.id);
            $('#media_id').val(rowData.media_id);
            $('#dialog_mt').dialog('close');
            
            if(rowData.type=='image'){
            	KindEditor.html('textarea[name="content"]','<img style="width:250px;height:150px;" src="../upload/'+rowData.fileName+'"/>&nbsp;[图片]');
            	KindEditor.html('disabled');
            }
            else if(rowData.type=='voice'){
            	KindEditor.html('textarea[name="content"]',rowData.name+'&nbsp;[语音]');
            }
            else if(rowData.type=='video'){
            	KindEditor.html('textarea[name="content"]',rowData.name+'&nbsp;[视频]');
            }
        }
   	});
}

//图文列表
function graphicList(){
	//图文类型
	function twTypeView(value,row,index){
		var label='';
	   	if(value=='1'){
	   		label="单图文";
	   	}
	   	else if(value=='2'){
	   		label="多图文";
	   	}
		return label;
	}
	//图文视图
	var twcardview = $.extend({}, $.fn.datagrid.defaults.view, {
	    renderRow: function(target, fields, frozen, rowIndex, rowData){
	        var cc = [];
	        cc.push('<td colspan=' + fields.length + ' style="padding:5px 5px;border:0;">');
	        if (!frozen){
	            cc.push('<div style="width:30px;float:left;text-align:center;padding:73px 0 59px 0;border:1px dashed #B8B8B8;">'+(rowIndex+1)+'</div>');
	            cc.push('<img src="../upload/' + rowData.cover + '" style="width:250px;height:150px;float:left;border:1px dashed #B8B8B8;"><div style="position:absolute;"><div style="left:33px;position:relative;float:left;font-size:13px;">'+(rowData.type=='1'?'<span class="c1">单</span>':'<span class="c6">多</span>')+'</div></div>');
	            cc.push('<div style="float:left;width:250px;height:150px;padding:0 50px 0 50px;border:1px dashed #B8B8B8;">');
	            for(var i=0; i<fields.length; i=i+1){
	                var copts = $(target).datagrid('getColumnOption', fields[i]);
	                if(copts.title=='消息类型'){
	                	if(rowData[fields[i]]=="1"){
	                		rowData[fields[i]]="<span>单图文消息</span>";
	                	}else{
	                		rowData[fields[i]]="<span>多图文消息</span>";
	                	}
	                }
	               	cc.push('<p style="border-bottom:1px dashed #B8B8B8;"><span class="c-label">' + copts.title + ':</span> ' + rowData[fields[i]] + '</p>');
	            }
	            cc.push('</div>');
	        }
	        cc.push('</td>');
	        return cc.join('');
	    }
	});
	//图文列表
   	$('#tw_data').datagrid({
   		view: twcardview,
		url:'../weixin/graphicList?publish=true',
        iconCls:'icon-edit',
        width: '650px',
        height: 'auto',
        nowrap: false,
        striped: true,
        border: true,
   		pageSize:10,
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
        rownumbers:false,
		fitColumns:true,
   		loadMsg:'正在加载....',
        columns:[[
			{align:'center',field:'type',width:'200',formatter:twTypeView,title: '消息类型' },
			{align:'center',field:'title',width:'150',title: '消息标题' },
            {align:'center',field:'createdTime_',width:'120',title: '上传时间' }
        ]],
		onClickRow:function(rowIndex, rowData){
            $('#id').val(rowData.id);
            $('#media_id').val(rowData.media_id);
            $('#dialog_tw').dialog('close');
            KindEditor.html('textarea[name="content"]','<img style="width:250px;height:150px;" src="../upload/'+rowData.cover+'"/>');
        }
   	});
}

//图文消息预览
function doPreview(){
	$('#m_form_preview').linkbutton({text:'发送中..'});
	$('#m_form_preview').linkbutton('disable');
	$('#isPreview').val('true');
    $('#m_form').form('submit',{
		onSubmit: function(param){
			return $(this).form('enableValidation').form('validate');
	    },
        success: function(json){
	        var data = eval('(' + json + ')');
	        if (data.errcode===0){
			 	$("#dialog_edit").dialog('close');
		 	 	$.messager.show({title:'消息提示',msg:'发送成功！',showType:'show'});
	        }else{
	        	if(typeof(data.errmsg)!='undefined'){
	        		$.messager.alert('消息提示',data.errcode+":"+data.errmsg);
	        	}else{
	        		$.messager.alert('消息提示','发送失败!');
	        	}
			}
	   		$('#m_form_preview').linkbutton({text:'预览'});
			$('#m_form_preview').linkbutton('enable');
        }
    });
}

//图文消息提交
function doSubmit(){
	$('#m_form_submit').linkbutton({text:'发送中..'});
	$('#m_form_submit').linkbutton('disable');
	$('#isPreview').val('false');
    $('#m_form').form('submit',{
		onSubmit: function(param){
			return $(this).form('enableValidation').form('validate');
	    },
        success: function(json){
	        var data = eval('(' + json + ')');
	        if (data.errcode===0){
				$('#list_data').datagrid('reload');
			 	$("#dialog_edit").dialog('close');
		 	 	$.messager.show({title:'消息提示',msg:'发送成功！',showType:'show'});
	        }else{
	        	if(typeof(data.errmsg)!='undefined'){
	        		$.messager.alert('消息提示',data.errcode+":"+data.errmsg);
	        	}else{
	        		$.messager.alert('消息提示','发送失败!');
	        	}
			}
	   		$('#m_form_submit').linkbutton({text:'发送'});
			$('#m_form_submit').linkbutton('enable');
        }
    });
}

//显示.群发对象
function typeView(value,row,index){
	var label=value;
   	if(value=='all'){
   		label="全部用户";
   	}
   	else if(value=='group'){
   		label="群组:"+(typeof(row.groupName)!='undefined'?row.groupName:'');
   	}
	return label;
}

//显示.消息类型
function msgTypeView(value,row,index){
	var label='';
   	if(value=='text'){label="文本";}
   	else if(value=='image'){label="图片";}
   	else if(value=='voice'){label="语音";}
   	else if(value=='video'){label="视频";}
   	else if(value=='mpnews'){label="图文消息";}
	return label;
}
	
$(document).ready(function() {
	$('#type').combobox({
		onChange : function(n,o){
      		if(n=='all'){
      			$("#group_span").hide();
      		}else{
      			$('#group_span').show();
      		}
      	}
	});
   
   	$('#list_data').datagrid({
		url:'../weixin/crowdList',
		toolbar:"#tb",
        iconCls:'icon-edit',
        width: 'auto',
        height: 'auto',
        nowrap: false,
        striped: true,
        border: true,
   		pageSize:15,
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
			{align:'center',field:'type',width:'100',formatter:typeView,title: '群发对象' },
			{align:'center',field:'msgtype',width:'100',formatter:msgTypeView,title: '消息类型' },
			{align:'left',halign:'center',field:'content',width:'350',title: '消息内容' },
        	{align:'center',field:'createdTime_',width:'120',title: '发送时间' }
        ]]
   	});
});
