//搜索
function doSearch(value){
	$('#list_data').datagrid('load',{
		content:value
	});
	$('#list_data').datagrid('clearSelections');
}

//开始刷新
function doRefresh(){
	var sx=$("#sx").val();
	var js=$("#js").val();
	var ti=setTimeout(doRefresh,1000);
	if(sx=="1"){
		clearTimeout(ti);
		$("#sx").val("");
	}else{
		if(js==''||js=='1'){ js=6; }
		js--;
		$("#js").val(js);
		$("#wz").html(js+"秒倒计时..");
		if(js==5){ doSearch(); }
		$("#tb").find("a:eq(2)").hide();
		$("#tb").find("a:eq(3)").show();
		$("#wz").show();
	}
}

//停止刷新
function doStop(){
	$("#tb").find("a:eq(2)").show();
	$("#tb").find("a:eq(3)").hide();
	$("#sx").val("1");
	$("#js").val('');
	$("#wz").hide();
	
}

//下载语音
function xzyy(media_id){
	$.ajax({
		url:'../weixin/mediaDonwload?media_id='+media_id,
		type: "post",
        dataType: "json",
		success:function(data){
			if(data.success === true){
				window.open(data.url,'_self');
			}else{
				$.messager.alert('消息提示','媒体文件保存时间为3天，该文件已经过期!','warning');
			}
		}
	});
}
	
//详情
function doInfo(id){
      jQuery.ajax({
        type: 'POST',
        url: '../weixin/msgDetail',
        data: {id:id},
  		dataType: 'json',
        success: function(json){
		  	$("#dialog_info_detail").html("");
		  	$("#dialog_info_reply").html("");
		  	$('#dg').datagrid('loadData',{total:0,rows:[]});
		  	
		  	//设置回复
		  	if(json.weixinMsg.hasReply=='1'){
				$(json.replyList).each(function(i,o){
				    $('#dg').datagrid('appendRow',{
						dg_time: o.createdTime_,
						dg_text: o.replyText
					});
				});
		  	}
		  	
		  	if(json.weixinMsg.msgType=='text'){//文本
				$("#dialog_info_detail").html(json.weixinMsg.content);
			}
	       	else if(json.weixinMsg.msgType=='image'){//图片
	       		var img=new Image();
				img.src=json.weixinMsg.picUrl;
				var width=img.width;
				if(width>440||width===0){
					width=440;
				}
				$("#dialog_info_detail").html("<img src="+json.weixinMsg.picUrl+" alt='图标' style='width:"+width+"px;'/>");
			}
	       	else if(json.weixinMsg.msgType=='voice'){//语音
				//语音 API接口拉取
				$("#dialog_info_detail").html("语音文件<a href='javascript:xzyy("+'"'+json.weixinMsg.mediaId+'"'+")'>下载</a>");
			}
	       	else if(json.weixinMsg.msgType=='video'){//视频
				//视频 API接口拉取
				$("#dialog_info_detail").html("暂不支撑视频下载！");
			}
	       	else if(json.weixinMsg.msgType=='location'){//地理位置
			    $("#dialog_info_detail").html("<iframe src='../weixin/sosoMap?location_X="+json.weixinMsg.location_X+"&location_Y="+json.weixinMsg.location_Y+"' frameborder='0' scrolling='auto' width='100%' height='100%'>");
			}
	       	else if(json.weixinMsg.msgType=='link'){//链接
				$("#dialog_info_detail").html("<iframe src='"+json.weixinMsg.url+"' frameborder='0' scrolling='auto' width='100%' height='100%'>");
			}
   			$('#dialog_info').show();
            $('#dialog_info').dialog({modal:true});
	  	}
      });
}
	
//显示回复
function showReply(id,openid){
	$("#m_form").form('clear');
    jQuery.ajax({
        type:'post',
        url:'../weixin/getAccessToken',
        dataType:'json',
        success:function(data){
			$('#id').val(id);
			$('#token').val(data.token);
			$('#openid').val(openid);
			$('#dialog_edit').show();
	      	$('#dialog_edit').dialog({modal:true});
        },
        error:function(){
			alert("请求错误！");
        }
    });
}
	
//提交回复
function doReply(){
	if(id==''||openid==''){
		alert("回复失败！");
		return;
	}
    $('#m_form').form('submit', {
          url:'../weixin/msgReply',
          onSubmit: function(param){
      		return $(this).form('enableValidation').form('validate');
          },
          success:function(data){
		    var data = eval('(' + data + ')');
            if(data.errmsg=='ok'){
				$('#dialog_edit').dialog('close');
				$('#list_data').datagrid('reload');
			}else{
				$.messager.alert('消息提示','消息超过48小时不能进行回复!','warning');
			}
          }
    });
}
	
//显示.消息类型
function typeView(value,row,index){
	var label='';
   	if(value=='text')label="文本";
   	else if(value=='image')label="图片";
   	else if(value=='voice')label="语音";
   	else if(value=='video')label="视频";
   	else if(value=='location')label="位置";
   	else if(value=='link')label="链接";
	return '<img src="../images/icons/'+value+'.png" style="vertical-align:middle;"/>'+label;
}

//显示.人工回复
function hfView(value,row,index){
	var label='';
   	if(value=='1'){label="<img src='../images/icons/check.png' style='vertical-align:middle;'/>"};
	return label;
}

//按钮.操作
function qxButton(value,row,index){
   	var c1='<a href="javascript:doInfo('+"'"+row.id+"'"+')"><img title="详情" src="../images/icons/detail.png" style="vertical-align:middle;"/></a>';
   	var c2='<a href="javascript:showReply('+"'"+row.id+"','"+row.fromUserName+"'"+')"><img title="回复" src="../images/icons/wechar.png" style="vertical-align:middle;"/></a>';
	return c1+'&nbsp;&nbsp;'+c2;
}

//显示.统计
function showStat(){
    jQuery.ajax({
        type:'post',
        url:'../weixin/msgStatistics',
        dataType:'json',
        success:function(data){
			//关键字排行榜.数据
			var gjzn=new Array();
			var gjzs=new Array();
            if(data.gjz!=''){
				$(data.gjz).each(function(i,o){gjzn.push(o.Content);gjzs.push(parseFloat(o.sl));});
			}
			//关键字排行榜.图形
            $('#gjz_data').highcharts({
		     	chart: {type: 'column'},
                title: {text: '热门词语搜索排行'},
                subtitle:{text: ''},
                xAxis:  {categories: gjzn},
                yAxis:  {title: {text:'关键字数量'}},
			    tooltip: {
		            formatter: function() {
		                return '<b>'+this.point.category +'</b>:<span style="color:green;">'+ this.point.y +'</span>次搜索';
		            }
		        },
                series: [{name: '搜索关键字',data: gjzs}]
            });
            
			//活跃用户排行.数据
			var hyhn=new Array();
			var hyhs=new Array();
            if(data.hyh!=''){
				$(data.hyh).each(function(i,o){hyhn.push(o.nickname);hyhs.push(parseFloat(o.sl));});
			}
			//活跃用户排行.图形
            $('#hyh_data').highcharts({
		     	chart: {type: 'column'},
                title: {text: '活跃用户消息排行'},
                subtitle:{text: ''},
                xAxis: {categories: hyhn},
                yAxis:  {title: {text:'消息数量'}},
			    tooltip: {
		            formatter: function() {
		                return '<b>'+this.point.category +'</b>:<span style="color:green;">'+ this.point.y +'</span>条消息';
		            }
		        },
                series: [{name: '用户昵称',data: hyhs}]
            });
            
        	//窗口展示
			$('#dialog_stat').show();
			$('#dialog_stat').dialog({modal:true});
        },
        error:function(){
			alert("请求错误！");
        }
    });
}

$(document).ready(function() {
   	$('#list_data').datagrid({
		url:'../weixin/msgList',
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
			{align:'center',field:'nickname',width:'100',title: '微信昵称' },
			{align:'center',field:'msgType',width:'80',formatter:typeView,title: '消息类型' },
			{halign:'center',align:'left',field:'content',width:'250',title: '消息内容' },
	        {align:'center',field:'createdTime_',width:'120',title: '接收时间' },
	        {align:'center',field:'hasReply',width:'80',formatter:hfView,title: '消息回复' },
	        {align:'center',field:'operate',width:"160",formatter:qxButton,title: '操作' }
       	]]
   	});
});
