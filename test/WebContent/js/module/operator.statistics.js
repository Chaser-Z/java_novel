//config
require.config({
    paths: {
        echarts: '../js/echarts-2.1.10/build/dist'
    }
});

//require
require(
    [
        'echarts',
        'echarts/chart/bar',
        'echarts/chart/pie',
        'echarts/chart/line',
        'echarts/chart/map',
        'echarts/chart/funnel'
    ],
    function (ec) {
        regWay();
    }
);

//1.注册方式统计
function regWay(){
	$.ajax({
		url:"../operator/regStatistics",
		type:"post",
		dataType:'json',
        success:function(data){
        	var way = new Array();
        	var values = new Array();
        	var wayValues = new Array();
        	if(data!=''){
        		$(data.regWay).each(function(i,o){
        			way.push(o.regway=='qq'?"QQ":o.regway=='wechat'?"微信":o.regway=='weibo'?"微博":"邮箱");
        			values.push(o.value);
        			wayValues.push({value:o.value, name:o.regway=='qq'?"QQ":o.regway=='wechat'?"微信":o.regway=='weibo'?"微博":"邮箱"});
				});        		
        	}
        	var cp = {
    			    title : {
    			        text: '来源',
    			        subtext: '统计各注册方式人数',
    			        x:'center',
    			        y:'top'
    			    },
    			    tooltip : {
    			    	show:true,
    			        trigger: 'item',
    			        formatter: "{a} <br/>{b} : {c} "
    			    },
    			    legend: {
    			 		x : 'left',
    			        data:way
    			    },
    			    toolbox: {
    			        show: true,
    			        feature : {
    			        	//mark : {show: true},
    			            dataView : {show: true, readOnly: false},
    			            magicType : {
    			                show: true, 
    			                type: ['pie', 'funnel'],
    			                option: {
    			                    funnel: {
    			                        x: '25%',
    			                        width: '50%',
    			                        funnelAlign: 'center',
    			                        max: 335
    			                    }
    			                }
    			            },
    			            restore : {show: true}
    			            //saveAsImage : {show: true}
    			        }
    			    },
    			    calculable : true,	   
    			    series : [
    			        {
    			            name:'注册方式',
    			            type:'pie',
    			            radius : '60%',
    			            center: ['50%', '60%'],
    			            data:wayValues,
    			            itemStyle:{ 
    			                  normal:{ 
    			                        label:{ 
    			                          show: true, 
    			                          formatter: '{b} : {c} ' 
    			                        }, 
    			                        labelLine :{show:true}
    			                      } 
    			                  } 
    			        }
    			    ]
    			};
    			//初始化插件
    			var ecConfig = require('echarts/config');
    		    var myChart = require('echarts').init($('#tt_jczn1').get(0));
    			myChart.setOption(cp);
        }
	});
	
}

function collectSort(){
	
}


/*********************公共****************************/
//loading
function doLoading(){
    $("<div class=\"datagrid-mask\"></div>").css({display:"block",width:"100%",height:$(window).height()}).appendTo("body");
    $("<div class=\"datagrid-mask-msg\"></div>").html("正在处理，请稍候...").appendTo("body").css({display:"block",left:($(document.body).outerWidth(true) - 190) / 2,top:($(window).height() - 45) / 2});
}
function doLoadEnd(){
    $(".datagrid-mask").remove();
    $(".datagrid-mask-msg").remove();       
}

//ready
$(document).ready(function() {
	/*//1.按钮样式切换
	$(".easyui-linkbutton").click(function(){
		$(".easyui-linkbutton").removeClass("c1");
		$(this).addClass("c1");
	});*/
	//2.统计分析.tab
	$('#tt_znwx').tabs({
	    border:false,
	    onSelect:function(title){
	    	doLoading();
	        if(title=="来源统计"){
	        	regWay();
	        	doLoadEnd();
	        }
	        else if(title=="文章统计"){
				$("#dg").datagrid({
					url:'../operator/articleStatistics',
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
			        remoteSort:true,   
			        idField:'id',
					singleSelect:true,
					selectOnCheck: true,
					checkOnSelect: true,
					autoRowHeight:false,
			        pagination:true,
			        rownumbers:true,
					fitColumns:true,
			   		loadMsg:'正在加载....',
			        columns:[[{align:'left',field:'artileTitle',width:'500',title:'标题'},
			                 {align:'center',field:'favoriteCount',sortable:true,title:'收藏数'},
			                 {align:'center',field:'thumbCount',sortable:true,title:'点赞数'}
			        ]],
			        sortName:'thumbCount',
			        sortOrder:'desc'
				});
				doLoadEnd();
	        }
	    }
	});
});