//新建
var editor;
function doAdd(type){
	//设置表单
	$('#m_form').form('clear');
	$('#type').val(type);
	//$('#type').combobox('select', '');
	
	//设置窗口
	$('#dialog_edit').show();
    $('#dialog_edit').dialog({modal:true});
	
	//编辑器插件.文字
	KindEditor.lang({ wzcj : '文字' });
	KindEditor.plugin('wzcj', function(K) {
		var self = this, name = 'wzcj';
		self.clickToolbar(name, function() {
			//清空内容
			self.html('');
			
		    //设置消息类型
		    $('#type').val("text");
		});
	});
	//编辑器插件.图片
	KindEditor.lang({ tpcj : '图片' });
	KindEditor.plugin('tpcj', function(K) {
		var self = this, name = 'tpcj';
		self.clickToolbar(name, function() {
			//清空内容
			self.html('');
			
		    //设置消息类型
		    $('#type').val("image");
		    
			//打开窗体
			$('#dialog_mt').show();
		    $('#dialog_mt').dialog({modal:true});
		    
		    //设置列表
		    mediaList();
		});
	});
	//编辑器插件.语音
	KindEditor.lang({
		yycj : '语音'
	});
	KindEditor.plugin('yycj', function(K) {
		var self = this, name = 'yycj';
		self.clickToolbar(name, function() {
			//清空内容
			self.html('');
			
		    //设置消息类型
		    $('#type').val("voice");
		    
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
		    $('#type').val("video");
		    
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
		    $('#type').val("mpnews");
			
			//打开窗体
			$('#dialog_tw').show();
		    $('#dialog_tw').dialog({modal:true});
		    
		    //设置列表
		    graphicList();
		});
	});
	//编辑器插件.插件应用
	editor=	KindEditor.create('textarea[name="answer"]', {
		resizeType : 1,
		allowPreviewEmoticons : false,
		allowImageUpload : false,
		items : ['wzcj','tpcj','yycj'],
		//items : ['wzcj','tpcj','yycj','spcj','twxx'],
		afterBlur: function(){this.sync();}
	});
	KindEditor.html('textarea[name="answer"]','');
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
		url:'../weixin/mediaList?type='+$('#type').val(),
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
            $('#media_id').val(rowData.media_id);
            $('#dialog_mt').dialog('close');
            if(rowData.type=='image'){
            	//KindEditor.html('textarea[name="answer"]','<img style="width:250px;height:150px;" src="../upload/'+rowData.fileName+'"/>');
            	editor.html('<img style="width:250px;height:150px;" src="../upload/'+rowData.fileName+'"/>');
            	editor.sync();
            }
            else if(rowData.type=='voice'){
            	//KindEditor.html('textarea[name="answer"]',rowData.name);
            	editor.html(rowData.name);
            	editor.sync();
            }
            else if(rowData.type=='video'){
            	//KindEditor.html('textarea[name="answer"]',rowData.name);
            	editor.html(rowData.name);
            	editor.sync();
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
            KindEditor.html('textarea[name="answer"]','<img style="width:250px;height:150px;" src="../upload/'+rowData.cover+'"/>');
        }
   	});
}

//搜索
function doSearch(value){
	$('#list_data').datagrid('load',{ content:value });
	$('#list_data').datagrid('clearSelections');
}

//编辑
function doEdit(id){
	$('#m_form').form('clear');
	//数据加载
	$.ajax({
		url:'../weixin/answerLoad?id='+id,
		type: "post",
        dataType: "json",
		success:function(data){
			//加载富文本
			doAdd(data.type);
			//设置富文本
			KindEditor.html('textarea[name="answer"]',data.answer);
			editor.sync();
			//表单加载
			$('#m_form').form('load',{
			   	id:data.id,
			   	type:data.type,
			   	answer:data.answer,
			   	media_id:data.media_id,
			   	frequency:data.frequency,
			   	question:data.question
			});
		}
	});
}

//提交
function doSubmit(){
	$('#m_form').form('submit', {
		url:'../weixin/answerSave',
		onSubmit: function(param){
     		return $(this).form('enableValidation').form('validate');
		},
        success:function(data){
			var data = eval('(' + data + ')');
            if(data.success==true){
            	$('#m_form').form('clear');
            	KindEditor.html('textarea[name="answer"]','');
				$('#dialog_edit').dialog('close');
				$('#list_data').datagrid('reload');
			}else{
				alert("error!");
			}
         }
	});
}
	
//删除
function doDel(id){
	$.messager.confirm('消息提示', '确定删除?', function(r){
		if(r){
       		jQuery.ajax({
       	    	type:　"post",
       	    	dataType: 'json',
       	    	url:　'../weixin/answerDel', 
       	    	data: {
       				id:id
       			},
       			error:　function(data){
       				alert('error');
       			},
       	    	success:　function(data){
                    $('#list_data').datagrid('reload');
       			}
        	});
		}
	});
}
	
//类型
function typeView(value,row,index){
	if(value=="text"){
		return '文本';
	}
	else if(value=="image"){
		return '图片';
	}
	else if(value=="voice"){
		return '语音';
	}
	else if(value=="video"){
		return '视频';
	}
	else if(value=="music"){
		return '音乐';
	}
	else if(value=="news"){
		return '图文';
	}
	return value;
}
	
//按钮
function qxButton(value,row,index){
   	var c1='<a href="javascript:doEdit('+"'"+row.id+"'"+')"><img title="编辑" src="../images/icons/edit.png" style="vertical-align:middle;"/></a>';
   	var c2='<a href="javascript:doDel('+"'"+row.id+"'"+')"><img title="删除" src="../images/icons/delete.png" style="vertical-align:middle;"/></a>';
	return c1+'&nbsp;&nbsp;'+c2+'&nbsp;&nbsp;';
}
	
//问答列表
$(document).ready(function() {
  	$('#list_data').datagrid({
		url:'../weixin/answerList',
		toolbar:"#tb",
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
			{align:'center',field:'type',width:'80',formatter:typeView,title: '类型' },
			{halign:'center',align:'left',field:'question',width:'150',title: '问题' },
            {halign:'center',align:'left',field:'answer',width:'300',title: '答案' },
            {align:'center',field:'createdTime_',width:'120',title: '创建时间' },
            {align:'center',field:'operate',width:"120",formatter:qxButton,title: '操作' }
        ]]
      });
});
 