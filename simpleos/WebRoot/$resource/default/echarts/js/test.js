var __f_247487475complex2DivId_obj = $('complex2DivId');
__f_247487475complex2DivId_obj.addClassName('echarts_main');
__f_247487475complex2DivId_obj.style.width = '600px';
__f_247487475complex2DivId_obj.style.height = '400px';
var __f_247487475complex2DivId_1 = echarts.init(__f_247487475complex2DivId_obj);
__f_247487475complex2DivId_1.setOption({
	title : {
		text : '测试',
		x : 'left',
		y : 'bottom'
	},
	dataZoom : {
		show : false,
		realtime : false,
		start : 0,
		end : 100
	},
	tooltip : {
		trigger : 'axis',
		axisPointer : {
			type : 'shadow'
		}
	},
	toolbox : {
		show : true,
		orient : 'horizontal',
		x : 'right',
		y : 'bottom',
		feature : {
			mark : {
				show : true
			},
			dataView : {
				show : true,
				readOnly : false
			},
			magicType : {
				show : true,
				type : [ 'line', 'bar', 'stack', 'tiled' ]
			},
			restore : {
				show : true
			},
			saveAsImage : {
				show : true
			}
		}
	},
	legend : {
		selectedMode : true,
		x : 'left',
		orient : 'vertical',
		data : [ '最新成交价', '预购队列' ]
	},
	xAxis : [ {
		type : 'category',
		scale : false,
		splitLine : true,
		splitArea : {
			show : true
		},
		data : (function() {
			var now = new Date();
			var res = [];
			var len = 10;
			while (len--) {
				res.unshift(now.toLocaleTimeString().replace(/^\D*/, ''));
				now = new Date(now - 2000);
			}
			return res;
		})()
	}, {
		type : 'category',
		scale : false,
		splitLine : true,
		splitArea : {
			show : true
		},
		data : (function() {
			var res = [];
			var len = 10;
			while (len--) {
				res.push(len + 1);
			}
			return res;
		})()
	} ],
	yAxis : [ {
		type : 'value',
		scale : true,
		splitLine : true,
		splitArea : {
			show : true
		},
		power : 1,
		precision : 1.0
	}, {
		type : 'value',
		scale : true,
		splitLine : true,
		splitArea : {
			show : true
		}
	} ],
	series : [ {
		type : 'bar',
		large : false,
		hoverable : true,
		roam : false,
		smooth : true,
		data : (function() {
			var res = [];
			var len = 10;
			while (len--) {

				res.push(Math.round(Math.random() * 1000));
			}
			return res;
		})(),
		name : '预购队列',
		xAxisIndex : 1,
		yAxisIndex : 1,
		itemStyle : {
			normal : {
				label : {
					show : true
				}
			}
		}
	}, {
		type : 'line',
		large : false,
		hoverable : true,
		roam : false,
		smooth : true,
		data : (function() {
			var res = [];
			var len = 10;
			while (len--) {
				res.push((Math.random() * 10 + 5).toFixed(1) - 0);
			}
			return res;
		})(),
		name : '最新成交价',
		xAxisIndex : 1,
		yAxisIndex : 1,
		itemStyle : {
			normal : {
				label : {
					show : true
				}
			}
		}
	} ],
	calculable : true
})