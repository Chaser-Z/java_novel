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
        'echarts/chart/map'
    ],
    function (ec) {
        t1();
    }
);

//1.微信用户分布
function t1(){
    jQuery.ajax({
        type:'post',
        url:'../weixin/statUserSpread',
        dataType:'json',
        success:function(data){
		 	//配置
			var cp = {
			    title : {
			        text: '微信用户分布',
			        subtext: '微信平台用户大洲分布情况',
			        sublink : '',
			        x:'center',
			        y:'top'
			    },
			    tooltip : {
			        trigger: 'item',
			        formatter : function (params) {
			            var value = (params.value + '').split('.');
			            if(value=="-"){value="0"}
			            return params.seriesName + '<br/>' + params.name + ' : ' + value;
			        }
			    },
			    toolbox: {
			        show: true,
			        feature: {
			            restore: {show: true}
			        }
			    },
			    dataRange: {
			        min: 0,
			        max: 1000,
			        text:['高','低'],
			        realtime: false,
			        calculable : true,
			        color: ['orangered','yellow','lightskyblue']
			    },
			    //地区遗留数据:'French Southern and Antarctic Lands','West Bank','Northern Cyprus','Falkland Islands','Greenland','Kosovo','South Sudan'
			    series : [
			        {
			            name: '微信用户数量',
			            type: 'map',
			            mapType: 'world',
			            roam: true,
			            mapLocation: {
			                y : 60
			            },
			            itemStyle:{
			                emphasis:{label:{show:true}}
			            },
			            data:data.dqfb
			        }
			    ]
			};
			//初始化插件
			var ecConfig = require('echarts/config');
            var myChart = require('echarts').init($('#c1').get(0));
            myChart.on(ecConfig.EVENT.CLICK, function (param) {
                if(param.name=="China"){
                	//点击中国
                }
            });
			myChart.setOption(cp);
			//require('echarts').init($('#c1').get(0)).setOption(cp);
        },
        error:function(){
			alert("请求错误！");
        }
    });
}

//2.活跃用户排行
function t2(){
    jQuery.ajax({
        type:'post',
        url:'../weixin/msgStatistics',
        dataType:'json',
        success:function(data){
			//数据
			var hyhn=new Array();
			var hyhs=new Array();
			var hyhl=new Array();
            if(data.hyh!=''){
				$(data.hyh).each(function(i,o){
					hyhn.push(o.nickname);
					hyhs.push(parseFloat(o.sl));
					hyhl.push({xAxis:i, y:470, name:o.nickname});
				});
			}
               //配置
			var op = {
			    title: {
			        x: 'center',
			        text: '活跃用户排行',
			        subtext: '用户互动消息数量总数统计',
			        link: ''
			    },
			    tooltip: {
			        trigger: 'item'
			    },
			    toolbox: {
			        show: true,
			        feature: {
			            restore: {show: true}
			        }
			    },
			    calculable: true,
			    grid: {
			        borderWidth: 0,
			        y: 80,
			        y2: 60
			    },
			    xAxis: [
			        {
			            type: 'category',
			            show: false,
			            data: hyhn
			        }
			    ],
			    yAxis: [
			        {
			            type: 'value',
			            show: false
			        }
			    ],
			    series: [
			        {
			            name: '互动消息数量',
			            type: 'bar',
			            itemStyle: {
			                normal: {
			                    color: function(params) {
			                        var colors = ['#E87C25','#27727B','#FE8463','#9BCA63','#60C0DD','#D7504B','#C6E579','#F4E001','#F0805A','#26C0C0'];
			                        return colors[params.dataIndex]
			                    },
			                    label: {
			                        show: true,
			                        position: 'top',
			                        formatter: '{b}\n{c}'
			                    }
			                }
			            },
			            data: hyhs,
			            markPoint: {
			                tooltip: {
			                    trigger: 'item',
			                    backgroundColor: 'rgba(0,0,0,0)',
			                    formatter: function(params){
			                        //return '<img style="border-radius:50%;width:200px;" src="' + params.data.symbol.replace('image://', '') + '"/>';
			                    }
			                },
			                data:hyhl
			            }
			        }
			    ]
			};
			//初始化插件
			require('echarts').init($('#c2').get(0)).setOption(op);
        },
        error:function(){
			alert("请求错误！");
        }
    });
}

//3.消息时段分析
function t3(){
    jQuery.ajax({
        type:'post',
        url:'../weixin/statMsgHours',
        dataType:'json',
        success:function(data){
			var hyhl=new Array();
            if(data.sdfx!=''){
				$(data.sdfx).each(function(i,o){
					hyhl.push(o.name+"点");
				});
			}
            //配置
			var op = 
			{
			    title: {
			        x: 'center',
			        text: '消息时段分析',
			        subtext: '每个小时用户发送消息数量统计',
			        link: ''
			    },
			    tooltip : {
			        trigger: 'axis'
			    },
			    toolbox: {
			        show: true,
			        feature: {
			            restore: {show: true}
			        }
			    },
			    calculable : true,
			    xAxis : [
			        {
			            type : 'category',
			            data : hyhl
			        }
			    ],
			    yAxis : [
			        {
			            type : 'value',
			            axisLabel : {
			                formatter: '{value} 次'
			            }
			        }
			    ],
			    series : [
			        {
			            name:'消息数量',
			            markPoint : {
			                data : [
			                    {type : 'max', name: '最大值'},
			                    {type : 'min', name: '最小值'}
			                ]
			            },
			            type:'line',
			            data:data.sdfx
			        }
			    ]
			}
			//初始化插件
			require('echarts').init($('#c3').get(0)).setOption(op);
        },
        error:function(){
			alert("请求错误！");
        }
    });
}

//4.搜索词语排行
function t4(){
    jQuery.ajax({
        type:'post',
        url:'../weixin/msgStatistics',
        dataType:'json',
        success:function(data){
			//数据
			var hyhn=new Array();
			var hyhs=new Array();
            if(data.gjz!=''){
				$(data.gjz).each(function(i,o){
					hyhn.push(o.Content);
					hyhs.push(parseFloat(o.sl));
				});
			}
            //配置
			var op = 
			{
			    title: {
			        x: 'center',
			        text: '搜索词语排行',
			        subtext: '热门词语搜索次数统计',
			        link: ''
			    },
			    tooltip : {
			        trigger: 'axis'
			    },
			    toolbox: {
			        show: true,
			        feature: {
			            restore: {show: true}
			        }
			    },
			    calculable : true,
			    xAxis : [
			        {
			            type : 'category',
			            data : hyhn
			        }
			    ],
			    yAxis : [
			        {
			            type : 'value',
			            axisLabel : {
			                formatter: '{value} 次'
			            }
			        }
			    ],
			    series : [
			        {
			            name:'搜索次数',
        			    itemStyle: {
			                normal: {
			                    color: function(params) {
			                        var colors = ['#E87C25','#27727B','#FE8463','#9BCA63','#60C0DD','#D7504B','#C6E579','#F4E001','#F0805A','#26C0C0'];
			                        return colors[params.dataIndex]
			                    },
			                    label: {
			                        show: true,
			                        position: 'top',
			                        formatter: '{c}'
			                    }
			                }
			            },
			            type:'bar',
			            data:hyhs
			        }
			    ]
			}
			//初始化插件
			require('echarts').init($('#c4').get(0)).setOption(op);
        },
        error:function(){
			alert("请求错误！");
        }
    });
}

//5.资源浏览分析
function t5(){
    jQuery.ajax({
        type:'post',
        url:'../weixin/statResources',
        dataType:'json',
        success:function(data){
            //配置.教师必看
			var op1 =
			{
			    title: {
			        x: 'center',
			        text: '最新推送浏览量',
			        subtext: '',
			        link: ''
			    },
			    tooltip : {
			        trigger: 'item',
			        formatter: "{a} <br/>{b} : {c}次浏览"
			    },
			    toolbox: {
			        show : true
			    },
			    calculable : true,
			    series : [
			        {
			            name:'点击量',
			            type:'pie',
			            radius : '70%',
			            center: ['50%', '60%'],
			            data:data.jsbk
			        }
			    ]
			}
			//配置.微信商城
//			var op2 = {
//			    title : {
//			        text: '微信商城点击量',
//			        subtext: '',
//			        x:'center'
//			    },
//			    tooltip : {
//			        trigger: 'item',
//			        formatter: "{a} <br/>{b} : {c}次浏览"
//			    },
//			    toolbox: {
//			        show : true
//			    },
//			    calculable : true,
//			    series : [
//			        {
//			            name:'点击量',
//			            type:'pie',
//			            radius : '70%',
//			            center: ['50%', '60%'],
//			            data:data.wxsc
//			        }
//			    ]
//			};
			//初始化插件
			require('echarts').init($('#c51').get(0)).setOption(op1);
			//require('echarts').init($('#c52').get(0)).setOption(op2);
        },
        error:function(){
			alert("请求错误！");
        }
    });
}

$(document).ready(function(){
	$('#tt_wx').tabs({
	    border:false,
	    onSelect:function(title){
	        if(title=="微信用户分布"){//1.微信用户分布
                t1();
	        }
	        else if(title=="活跃用户排行"){ //2.活跃用户排行
				t2();
	        }
	        else if(title=="消息时段分析"){ //3.消息时段分析
	        	t3();
	        }
	        else if(title=="搜索词语排行"){ //4.搜索词语排行
				t4();
	        }
	        else if(title=="资源浏览分析"){ //5.资源浏览分析
				t5();
	        }
	    }
	});
});
