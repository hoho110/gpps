
_defaultDataTableOLanguage = {
		"sProcessing" : "<img src ='images/waiting.gif' height = 18/>正在查询中，请稍后......",
		"sLengthMenu" : "每页 _MENU_ 条记录",
		"sZeroRecords" : "无数据",
		"sInfo" : "当前第 _START_ 到  _END_ 条记录 共_TOTAL_条记录",
		"sInfoEmpty" : " ",
		"sSearch" : "查找： ",
		"oPaginate" : {
			"sFirst" : "首页",
			"sLast" : "末页",
			"sNext" : "下一页",
			"sPrevious" : "上一页"
		},
		"sInfoFiltered" : "(在 _MAX_ 条记录中查找)"
	};

var defaultSettings = {
				"bServerSide" : true,
				"bAutoWidth" : true,
				"bStateSave" : false, //保存状态到cookie ******很重要 ， 当搜索的时候页面一刷新会导致搜索的消失。使用这个属性设置为true就可避免了 
				"bPaginate" : true, // 是否使用分页
				"bProcessing" : false, //是否显示正在处理的提示 
				"bLengthChange" : true, //是否启用设置每页显示记录数 
				"iDisplayLength" : 10, //默认每页显示的记录数
				"aLengthMenu" : [ 10, 15, 25, 50 ],
				"bFilter" : false, //是否使用搜索 
				"bJQueryUI" : true, //页面风格使用jQuery.
				// "sScrollY": 200,//竖向滚动条 tbody区域的高度
				"sScrollX" : "100%", //横向滚动条 
				"sScrollXInner" : "100%",
				"bScrollCollapse" : true,
				"aoColumns" : [],
				"aaData" : [],
				"sPaginationType": "full_numbers", //分页样式
				"bAutoWidth" : true, //列的宽度会根据table的宽度自适应 
				"bSort" : false, //是否使用排序 
				"aoColumnDefs" : [ {
					"bSortable" : false,
					"aTargets" :["_all"]
				} ],
				"aaSorting" : [ [ 4, "desc" ] ],
				"oLanguage" : _defaultDataTableOLanguage
			};
var defaultSettings_noCallBack = {
		"bServerSide" : false,
		"bAutoWidth" : true,
		"bStateSave" : false, //保存状态到cookie ******很重要 ， 当搜索的时候页面一刷新会导致搜索的消失。使用这个属性设置为true就可避免了 
		"bPaginate" : true, // 是否使用分页
		"bProcessing" : false, //是否显示正在处理的提示 
		"bLengthChange" : true, //是否启用设置每页显示记录数 
		"iDisplayLength" : 10, //默认每页显示的记录数
		"aLengthMenu" : [ 10, 15, 25, 50 ],
		"bFilter" : false, //是否使用搜索 
		"bJQueryUI" : true, //页面风格使用jQuery.
		// "sScrollY": 200,//竖向滚动条 tbody区域的高度
		"sScrollX" : "100%", //横向滚动条 
		"sScrollXInner" : "100%",
		"bScrollCollapse" : true,
		"aoColumns" : [],
		"aaData" : [],
		"sPaginationType": "full_numbers", //分页样式
		"bAutoWidth" : true, //列的宽度会根据table的宽度自适应 
		"bSort" : false, //是否使用排序 
		"aoColumnDefs" : [ {
			"bSortable" : false,
			"aTargets" :["_all"]
		} ],
		"aaSorting" : [ [ 4, "desc" ] ],
		"oLanguage" : _defaultDataTableOLanguage
	};
	
	var cashstate = {
			0 : '充值',
			1 : '冻结',
			2 : '解冻',
			3 : '投标',
			4 : '还款',
			5 : '提现'
	}
	var productstate = {
			1:'融资中',
			2:'还款中',
			4:'流标',
			8:'还款完成',
			16:'还款中', 
			32:'还款完成',
			64:'还款完成'
	}


var myscore = function(container){
	var bService = EasyServiceClient.getRemoteProxy("/easyservice/gpps.service.IBorrowerService");
	var borrower=bService.getCurrentUser();
	var content = $('<div></div>');
	var name = borrower.name==null?borrower.loginId : borrower.name
	content.append('<p>您好'+name+'，您的信用值是<span class="orange">'+borrower.creditValue+'</span>分，信用等级为<span class="orange">level'+borrower.level+'</span></p>');
	content.append('<br><span class="orange">积分规则：</span>');
	content.append('<p>如何获取积分的说明</p>');
	var str = "";
	str += '<table class="table table-striped table-hover" style="min-width:300px;">';
	str += '<thead>';	
	str += '<tr><td style="min-width:100px;">会员等级</td><td style="min-width:50px;">对应积分</td><td style="min-width:100px;">有效期</td><td style="min-width:50px;">最低贡献值</td><td style="min-width:50px;">说明</td></tr>';
	str += '</thead>';
	str += '<tbody>';
	str += '<tr><td>level5</td><td>10000000以上</td><td>3个月</td><td>1000000</td><td>有最低消费</td></tr>';
	str += '<tr><td>level4</td><td>1000000-10000000</td><td>3个月</td><td>300000</td><td>有最低消费</td></tr>';
	str += '<tr><td>level3</td><td>200000-1000000</td><td>半年</td><td>100000</td><td>有最低消费</td></tr>';
	str += '<tr><td>level2</td><td>50000-200000</td><td>一年</td><td>10000</td><td>有最低消费</td></tr>';
	str += '<tr><td>level1</td><td>10000-50000</td><td>永久</td><td>无</td><td>无最低消费</td></tr>';
	str += '<tr><td>level0</td><td>0-10000</td><td>永久</td><td>无</td><td>无最低消费</td></tr>';
	str += '</tbody>';
	str += '</table>';
	content.append(str);
	
	
	
	content.append('<br><span class="orange">会员特权：</span>');
	
	
	var str1 = "";
	str1 += '<table class="table table-striped table-hover" style="min-width:300px;">';
	str1 += '<thead>';	
	str1 += '<tr><td style="min-width:100px;">会员等级</td><td style="min-width:50px;">特权一</td><td style="min-width:100px;">特权二</td><td style="min-width:50px;">特权三</td><td style="min-width:50px;">说明</td></tr>';
	str1 += '</thead>';
	str1 += '<tbody>';
	str1 += '<tr><td>level5</td><td>10000000以上</td><td>3个月</td><td>1000000</td><td>有最低消费</td></tr>';
	str1 += '<tr><td>level4</td><td>1000000-10000000</td><td>3个月</td><td>300000</td><td>有最低消费</td></tr>';
	str1 += '<tr><td>level3</td><td>200000-1000000</td><td>半年</td><td>100000</td><td>有最低消费</td></tr>';
	str1 += '<tr><td>level2</td><td>50000-200000</td><td>一年</td><td>10000</td><td>有最低消费</td></tr>';
	str1 += '<tr><td>level1</td><td>10000-50000</td><td>永久</td><td>无</td><td>无最低消费</td></tr>';
	str1 += '<tr><td>level0</td><td>0-10000</td><td>永久</td><td>无</td><td>无最低消费</td></tr>';
	str1 += '</tbody>';
	str1 += '</table>';
	content.append(str1);
	container.append(content);
}
var myactivity = function(container){
	
	var content = $('<div></div>');
	var str = "";
	str += '<table class="ui-list-invest" style="width:100%">';
	str += '<tr id="header" style="padding-left:0px; padding-right:0px;">';
	str += '<td class="color-gray-text text-center">活动名称</td>';
	str += '<td class="color-gray-text text-center">活动日期</td>';
	str += '<td class="color-gray-text text-center">活动类型</td>';
	str += '<td class="color-gray-text text-center">主办方</td>';
	str += '<td class="color-gray-text text-center">状态</td>';
	str += '</tr>';
	
	str += '<tr class="ui-list-item" id="header" style="padding-left:0px; padding-right:0px;">';
	str += '<td class="text-big">';
	str += '<div style="max-width:150px; overflow:hidden;">';
	str += '<a href="../activity/index.html" target="_blank">XXXX杯跑步活动</a>';
	str += '</td>';
	str += '<td class="ui-list-field text-center">2014-9-31</td>';
	str += '<td class="ui-list-field num text-right pr10"><em class="value">长跑</em></td>';
	str += '<td class="ui-list-field num text-right pr10"><em class="value">春蕾资产管理</em></td>';
	str += '<td class="ui-list-field num text-right pr10"><em class="value">待参加</em></td>';
	str += '</tr>';
	
	str += '<tr class="ui-list-item" id="header" style="padding-left:0px; padding-right:0px;">';
	str += '<td class="text-big">';
	str += '<div style="max-width:150px; overflow:hidden;">';
	str += '<a href="../activity/index.html" target="_blank">XXXX杯跑步活动</a>';
	str += '</td>';
	str += '<td class="ui-list-field text-center">2014-9-31</td>';
	str += '<td class="ui-list-field num text-right pr10"><em class="value">长跑</em></td>';
	str += '<td class="ui-list-field num text-right pr10"><em class="value">春蕾资产管理</em></td>';
	str += '<td class="ui-list-field num text-right pr10"><em class="value">待参加</em></td>';
	str += '</tr>';
	
	str += '<tr class="ui-list-item" id="header" style="padding-left:0px; padding-right:0px;">';
	str += '<td class="text-big">';
	str += '<div style="max-width:150px; overflow:hidden;">';
	str += '<a href="../activity/index.html" target="_blank">XXXX杯跑步活动</a>';
	str += '</td>';
	str += '<td class="ui-list-field text-center">2014-9-31</td>';
	str += '<td class="ui-list-field num text-right pr10"><em class="value">长跑</em></td>';
	str += '<td class="ui-list-field num text-right pr10"><em class="value">春蕾资产管理</em></td>';
	str += '<td class="ui-list-field num text-right pr10"><em class="value">待参加</em></td>';
	str += '</tr>';
	
	str += '<tr class="ui-list-item" id="header" style="padding-left:0px; padding-right:0px;">';
	str += '<td class="text-big">';
	str += '<div style="max-width:150px; overflow:hidden;">';
	str += '<a href="../activity/index.html" target="_blank">XXXX杯跑步活动</a>';
	str += '</td>';
	str += '<td class="ui-list-field text-center">2014-9-31</td>';
	str += '<td class="ui-list-field num text-right pr10"><em class="value">长跑</em></td>';
	str += '<td class="ui-list-field num text-right pr10"><em class="value">春蕾资产管理</em></td>';
	str += '<td class="ui-list-field num text-right pr10"><em class="value">待参加</em></td>';
	str += '</tr>';
	
	str += '</table>';
	content.append(str);
	container.append(content);
}

var requestall = function(container){
	var bService = EasyServiceClient.getRemoteProxy("/easyservice/gpps.service.IBorrowerService");
	var requests = bService.findMyFinancingRequests(-1);
	
	var columns = [ {
		"sTitle" : "订单名称",
			"code" : "name"
	}, {
		"sTitle" : "申请额度",
		"code" : "state"
	}, {
		"sTitle" : "预期利率",
		"code" : "financingEndtime"
	}, {
		"sTitle" : "申请时间",
		"code" : "amount"
	}, {
		"sTitle" : "状态",
		"code" : "repayed"
	}];
	var aaData = new Array();
	for(var i=0; i<requests.size(); i++){
		var data=requests.get(i);
		var str = '待审核';
		if(data.state==1){
			str = '审核通过';
		}else if(data.state==2){
			str = '审核未通过';
		}
		aaData.push([data.govermentOrderName,
		                    data.applyFinancingAmount,
		                    data.rate,
		                    formatDate(data.createtime),
		                    str]);
	}
	var mySettings = $.extend({}, defaultSettings_noCallBack, {
		"aoColumns" : columns,
		"aaData" : aaData
	});
	var content = $('<div></div>');
	var table = $('<table class="table table-striped table-hover" style="min-width:300px;"></table>').appendTo(content);
	container.append(content);
	table.dataTable(mySettings);
	
}

var requesttohandle = function(container){
	var bService = EasyServiceClient.getRemoteProxy("/easyservice/gpps.service.IBorrowerService");
	var requests = bService.findMyFinancingRequests(0);
	
	var columns = [ {
		"sTitle" : "订单名称",
			"code" : "name"
	}, {
		"sTitle" : "申请额度",
		"code" : "state"
	}, {
		"sTitle" : "预期利率",
		"code" : "financingEndtime"
	}, {
		"sTitle" : "申请时间",
		"code" : "amount"
	}, {
		"sTitle" : "状态",
		"code" : "repayed"
	}];
	var aaData = new Array();
	for(var i=0; i<requests.size(); i++){
		var data=requests.get(i);
		aaData.push([data.govermentOrderName,
		                    data.applyFinancingAmount,
		                    data.rate,
		                    formatDate(data.createtime),
		                    '待审核']);
	}
	var mySettings = $.extend({}, defaultSettings_noCallBack, {
		"aoColumns" : columns,
		"aaData" : aaData
	});
	var content = $('<div></div>');
	var table = $('<table class="table table-striped table-hover" style="min-width:300px;"></table>').appendTo(content);
	container.append(content);
	table.dataTable(mySettings);
}


var requestpass = function(container){
	var bService = EasyServiceClient.getRemoteProxy("/easyservice/gpps.service.IBorrowerService");
	var requests = bService.findMyFinancingRequests(1);
	
	var columns = [ {
		"sTitle" : "订单名称",
			"code" : "name"
	}, {
		"sTitle" : "申请额度",
		"code" : "state"
	}, {
		"sTitle" : "预期利率",
		"code" : "financingEndtime"
	}, {
		"sTitle" : "申请时间",
		"code" : "amount"
	}, {
		"sTitle" : "状态",
		"code" : "repayed"
	}];
	var aaData = new Array();
	for(var i=0; i<requests.size(); i++){
		var data=requests.get(i);
		
		aaData.push([data.govermentOrderName,
		                    data.applyFinancingAmount,
		                    data.rate,
		                    formatDate(data.createtime),
		                    '审核通过']);
	}
	var mySettings = $.extend({}, defaultSettings_noCallBack, {
		"aoColumns" : columns,
		"aaData" : aaData
	});
	var content = $('<div></div>');
	var table = $('<table class="table table-striped table-hover" style="min-width:300px;"></table>').appendTo(content);
	container.append(content);
	table.dataTable(mySettings);
}


var requestrefuse = function(container){
	var bService = EasyServiceClient.getRemoteProxy("/easyservice/gpps.service.IBorrowerService");
	var requests = bService.findMyFinancingRequests(2);
	
	var columns = [ {
		"sTitle" : "订单名称",
			"code" : "name"
	}, {
		"sTitle" : "申请额度",
		"code" : "state"
	}, {
		"sTitle" : "预期利率",
		"code" : "financingEndtime"
	}, {
		"sTitle" : "申请时间",
		"code" : "amount"
	}, {
		"sTitle" : "状态",
		"code" : "repayed"
	}];
	var aaData = new Array();
	for(var i=0; i<requests.size(); i++){
		var data=requests.get(i);
		aaData.push([data.govermentOrderName,
		                    data.applyFinancingAmount,
		                    data.rate,
		                    formatDate(data.createtime),
		                    '审核未通过']);
	}
	var mySettings = $.extend({}, defaultSettings_noCallBack, {
		"aoColumns" : columns,
		"aaData" : aaData
	});
	var content = $('<div></div>');
	var table = $('<table class="table table-striped table-hover" style="min-width:300px;"></table>').appendTo(content);
	container.append(content);
	table.dataTable(mySettings);
}

var orderall = function(container){
	var orderService = EasyServiceClient.getRemoteProxy("/easyservice/gpps.service.IGovermentOrderService");
	var orders = orderService.findBorrowerOrderByStates(-1);
	
	var columns = [ {
		"sTitle" : "订单名称",
			"code" : "name"
	}, {
		"sTitle" : "融资起始时间",
		"code" : "state"
	}, {
		"sTitle" : "融资截止时间",
		"code" : "financingEndtime"
	}, {
		"sTitle" : "描述",
		"code" : "amount"
	}, {
		"sTitle" : "状态",
		"code" : "repayed"
	}];
	var aaData = new Array();
	for(var i=0; i<orders.size(); i++){
		var data=orders.get(i);
		aaData.push([data.title,
		             formatDate(data.financingStarttime),
		             formatDate(data.financingEndtime),
		                    data.description,
		                    data.state]);
	}
	var mySettings = $.extend({}, defaultSettings_noCallBack, {
		"aoColumns" : columns,
		"aaData" : aaData
	});
	var content = $('<div></div>');
	var table = $('<table class="table table-striped table-hover" style="min-width:300px;"></table>').appendTo(content);
	container.append(content);
	table.dataTable(mySettings);
}

var orderpreview = function(container){
	var orderService = EasyServiceClient.getRemoteProxy("/easyservice/gpps.service.IGovermentOrderService");
	var orders = orderService.findBorrowerOrderByStates(2);
	
	var columns = [ {
		"sTitle" : "订单名称",
			"code" : "name"
	}, {
		"sTitle" : "融资起始时间",
		"code" : "state"
	}, {
		"sTitle" : "融资截止时间",
		"code" : "financingEndtime"
	}, {
		"sTitle" : "描述",
		"code" : "amount"
	}, {
		"sTitle" : "状态",
		"code" : "repayed"
	}];
	var aaData = new Array();
	for(var i=0; i<orders.size(); i++){
		var data=orders.get(i);
		aaData.push([data.title,
		             formatDate(data.financingStarttime),
		             formatDate(data.financingEndtime),
		                    data.description,
		                    data.state]);
	}
	var mySettings = $.extend({}, defaultSettings_noCallBack, {
		"aoColumns" : columns,
		"aaData" : aaData
	});
	var content = $('<div></div>');
	var table = $('<table class="table table-striped table-hover" style="min-width:300px;"></table>').appendTo(content);
	container.append(content);
	table.dataTable(mySettings);
}


var orderfinancing = function(container){
	var orderService = EasyServiceClient.getRemoteProxy("/easyservice/gpps.service.IGovermentOrderService");
	var orders = orderService.findBorrowerOrderByStates(1);
	
	var columns = [ {
		"sTitle" : "订单名称",
			"code" : "name"
	}, {
		"sTitle" : "融资起始时间",
		"code" : "state"
	}, {
		"sTitle" : "融资截止时间",
		"code" : "financingEndtime"
	}, {
		"sTitle" : "描述",
		"code" : "amount"
	}, {
		"sTitle" : "状态",
		"code" : "repayed"
	}];
	var aaData = new Array();
	for(var i=0; i<orders.size(); i++){
		var data=orders.get(i);
		aaData.push([data.title,
		             formatDate(data.financingStarttime),
		             formatDate(data.financingEndtime),
		                    data.description,
		                    data.state]);
	}
	var mySettings = $.extend({}, defaultSettings_noCallBack, {
		"aoColumns" : columns,
		"aaData" : aaData
	});
	var content = $('<div></div>');
	var table = $('<table class="table table-striped table-hover" style="min-width:300px;"></table>').appendTo(content);
	container.append(content);
	table.dataTable(mySettings);
}

var orderpaying = function(container){
	var orderService = EasyServiceClient.getRemoteProxy("/easyservice/gpps.service.IGovermentOrderService");
	var orders = orderService.findBorrowerOrderByStates(4);
	
	var columns = [ {
		"sTitle" : "订单名称",
			"code" : "name"
	}, {
		"sTitle" : "融资起始时间",
		"code" : "state"
	}, {
		"sTitle" : "融资截止时间",
		"code" : "financingEndtime"
	}, {
		"sTitle" : "描述",
		"code" : "amount"
	}, {
		"sTitle" : "状态",
		"code" : "repayed"
	}];
	var aaData = new Array();
	for(var i=0; i<orders.size(); i++){
		var data=orders.get(i);
		aaData.push([data.title,
		             formatDate(data.financingStarttime),
		             formatDate(data.financingEndtime),
		                    data.description,
		                    data.state]);
	}
	var mySettings = $.extend({}, defaultSettings_noCallBack, {
		"aoColumns" : columns,
		"aaData" : aaData
	});
	var content = $('<div></div>');
	var table = $('<table class="table table-striped table-hover" style="min-width:300px;"></table>').appendTo(content);
	container.append(content);
	table.dataTable(mySettings);
}

var ordertoclose = function(container){
	var orderService = EasyServiceClient.getRemoteProxy("/easyservice/gpps.service.IGovermentOrderService");
	var orders = orderService.findBorrowerOrderByStates(8);
	
	var columns = [ {
		"sTitle" : "订单名称",
			"code" : "name"
	}, {
		"sTitle" : "融资起始时间",
		"code" : "state"
	}, {
		"sTitle" : "融资截止时间",
		"code" : "financingEndtime"
	}, {
		"sTitle" : "描述",
		"code" : "amount"
	}, {
		"sTitle" : "状态",
		"code" : "repayed"
	}];
	var aaData = new Array();
	for(var i=0; i<orders.size(); i++){
		var data=orders.get(i);
		aaData.push([data.title,
		             formatDate(data.financingStarttime),
		             formatDate(data.financingEndtime),
		                    data.description,
		                    data.state]);
	}
	var mySettings = $.extend({}, defaultSettings_noCallBack, {
		"aoColumns" : columns,
		"aaData" : aaData
	});
	var content = $('<div></div>');
	var table = $('<table class="table table-striped table-hover" style="min-width:300px;"></table>').appendTo(content);
	container.append(content);
	table.dataTable(mySettings);
}


var orderquit = function(container){
	var orderService = EasyServiceClient.getRemoteProxy("/easyservice/gpps.service.IGovermentOrderService");
	var orders = orderService.findBorrowerOrderByStates(16);
	
	var columns = [ {
		"sTitle" : "订单名称",
			"code" : "name"
	}, {
		"sTitle" : "融资起始时间",
		"code" : "state"
	}, {
		"sTitle" : "融资截止时间",
		"code" : "financingEndtime"
	}, {
		"sTitle" : "描述",
		"code" : "amount"
	}, {
		"sTitle" : "状态",
		"code" : "repayed"
	}];
	var aaData = new Array();
	for(var i=0; i<orders.size(); i++){
		var data=orders.get(i);
		aaData.push([data.title,
		             formatDate(data.financingStarttime),
		             formatDate(data.financingEndtime),
		                    data.description,
		                    data.state]);
	}
	var mySettings = $.extend({}, defaultSettings_noCallBack, {
		"aoColumns" : columns,
		"aaData" : aaData
	});
	var content = $('<div></div>');
	var table = $('<table class="table table-striped table-hover" style="min-width:300px;"></table>').appendTo(content);
	container.append(content);
	table.dataTable(mySettings);
}


var orderclosed = function(container){
	var orderService = EasyServiceClient.getRemoteProxy("/easyservice/gpps.service.IGovermentOrderService");
	var orders = orderService.findBorrowerOrderByStates(32);
	
	var columns = [ {
		"sTitle" : "订单名称",
			"code" : "name"
	}, {
		"sTitle" : "融资起始时间",
		"code" : "state"
	}, {
		"sTitle" : "融资截止时间",
		"code" : "financingEndtime"
	}, {
		"sTitle" : "描述",
		"code" : "amount"
	}, {
		"sTitle" : "状态",
		"code" : "repayed"
	}];
	var aaData = new Array();
	for(var i=0; i<orders.size(); i++){
		var data=orders.get(i);
		aaData.push([data.title,
		             formatDate(data.financingStarttime),
		             formatDate(data.financingEndtime),
		                    data.description,
		                    data.state]);
	}
	var mySettings = $.extend({}, defaultSettings_noCallBack, {
		"aoColumns" : columns,
		"aaData" : aaData
	});
	var content = $('<div></div>');
	var table = $('<table class="table table-striped table-hover" style="min-width:300px;"></table>').appendTo(content);
	container.append(content);
	table.dataTable(mySettings);
}



var submitall = function(container){
	var submitService = EasyServiceClient.getRemoteProxy("/easyservice/gpps.service.ISubmitService");
	var columns = [ {
		"sTitle" : "项目信息",
			"code" : "info"
	}, {
		"sTitle" : "状态",
		"code" : "state"
	}, {
		"sTitle" : "购买时间",
		"code" : "financingEndtime"
	}, {
		"sTitle" : "金额",
		"code" : "amount"
	}, {
		"sTitle" : "已回款",
		"code" : "repayed"
	}, {
		"sTitle" : "待回款",
		"code" : "willBeRepayed"
	}, {
		"sTitle" : "合同",
		"code" : "contract"
	}];
	
	var fnServerData = function(sSource, aoData, fnCallback, oSettings) {
		var sEcho = "";
		var iDisplayStart = 0;
		var iDisplayLength = 0;
		for ( var i = 0; i < aoData.length; i++) {
			var data = aoData[i];
			if (data.name == "sEcho")
				sEcho = data.value;
			if (data.name == "iDisplayStart")
				iDisplayStart = data.value;
			if (data.name == "iDisplayLength")
				iDisplayLength = data.value;
		}
		var res = null;
		res = submitService.findMyAllSubmitsByProductStates(-1,iDisplayStart, iDisplayLength);
		var result = {};
		result.iTotalRecords = res.get('total');
		result.iTotalDisplayRecords = res.get('total');
		result.aaData = new Array();
		var items = res.get('result');
		if(items)
		{
			for(var i=0; i<items.size(); i++){
				var item=items.get(i);
				result.aaData.push(["<a href='productdetail.html?pid="+item.product.id+"' >"+item.product.govermentOrder.title+"("+item.product.productSeries.title+")</a>",
				                    productstate[item.product.state],
				                    formatDate(item.createtime),
				                    item.amount.value,
				                    item.repayedAmount.value,
				                    item.waitforRepayAmount.value,
				                    "<a href='pdf/001.pdf' target='_blank'>合同</a>"]);
			}
		}
		result.sEcho = sEcho;
		fnCallback(result);

		return res;
	}
	var mySettings = $.extend({}, defaultSettings, {
		"aoColumns" : columns,
		"fnServerData" : fnServerData
	});
	var content = $('<div></div>');
	var table = $('<table class="table table-striped table-hover" style="min-width:300px;"></table>').appendTo(content);
	container.append(content);
	table.dataTable(mySettings);
}




var paybackall = function(container){
	var account = EasyServiceClient.getRemoteProxy("/easyservice/gpps.service.IAccountService");
	var repayedDetail=account.getBorrowerRepayedDetail();
	var willBeRepayedDetail=account.getBorrowerWillBeRepayedDetail();
	var content = $('<div></div>');
	var str = "";
	str += '<table class="table table-striped table-hover" style="min-width:300px;" id="dataTables-example">';
	str += '<thead>';	
	str += '<tr><td style="min-width:100px;">已回款统计</td><th style="min-width:50px;">最近一年</th><th style="min-width:100px;">最近半年</th><th style="min-width:50px;">最近三个月</th><th style="min-width:50px;">最近两个月</th><th style="min-width:50px;">最近一个月</th></tr>';
	str += '</thead>';
	str += '<tbody>';
	str += '<tr><td>本金</td><td>'+repayedDetail.get("oneyear").chiefAmount.value+'</td><td>'
								 +repayedDetail.get("halfyear").chiefAmount.value+'</td><td>'
								 +repayedDetail.get("threemonth").chiefAmount.value+'</td><td>'
								 +repayedDetail.get("twomonth").chiefAmount.value+'</td><td>'
								 +repayedDetail.get("onemonth").chiefAmount.value+'</td></tr>';
	str += '<tr><td>利息</td><td>'+repayedDetail.get("oneyear").interest.value+'</td><td>'
	 							 +repayedDetail.get("halfyear").interest.value+'</td><td>'
 							 	 +repayedDetail.get("threemonth").interest.value+'</td><td>'
 							 	 +repayedDetail.get("twomonth").interest.value+'</td><td>'
 							 	 +repayedDetail.get("onemonth").interest.value+'</td></tr>';
	str += '<tr><td>总计</td><td>'+(parseFloat(repayedDetail.get("oneyear").chiefAmount.value)+parseFloat(repayedDetail.get("oneyear").interest.value)).toFixed(2)+'</td><td>'
	 							 +(parseFloat(repayedDetail.get("halfyear").chiefAmount.value)+parseFloat(repayedDetail.get("halfyear").interest.value)).toFixed(2)+'</td><td>'
	 							 +(parseFloat(repayedDetail.get("threemonth").chiefAmount.value)+parseFloat(repayedDetail.get("threemonth").interest.value)).toFixed(2)+'</td><td>'
	 							 +(parseFloat(repayedDetail.get("twomonth").chiefAmount.value)+parseFloat(repayedDetail.get("twomonth").interest.value)).toFixed(2)+'</td><td>'
	 							 +(parseFloat(repayedDetail.get("onemonth").chiefAmount.value)+parseFloat(repayedDetail.get("onemonth").interest.value)).toFixed(2)+'</td></tr>';
	str += '</tbody>';
	str += '</table>';
	
	str += '<br>';
	str += '<br>';
	
	
	str += '<table class="table table-striped table-hover" style="min-width:300px;" id="dataTables-example">';
	str += '<thead>';	
	str += '<tr><td style="min-width:100px;">待回款统计</td><th style="min-width:50px;">未来一年</th><th style="min-width:100px;">未来半年</th><th style="min-width:50px;">未来三个月</th><th style="min-width:50px;">未来两个月</th><th style="min-width:50px;">未来一个月</th></tr>';
	str += '</thead>';
	str += '<tbody>';
	str += '<tr><td>本金</td><td>'+willBeRepayedDetail.get("oneyear").chiefAmount.value+'</td><td>'
								 +willBeRepayedDetail.get("halfyear").chiefAmount.value+'</td><td>'
								 +willBeRepayedDetail.get("threemonth").chiefAmount.value+'</td><td>'
								 +willBeRepayedDetail.get("twomonth").chiefAmount.value+'</td><td>'
								 +willBeRepayedDetail.get("onemonth").chiefAmount.value+'</td></tr>';
	str += '<tr><td>利息</td><td>'+willBeRepayedDetail.get("oneyear").interest.value+'</td><td>'
								 +willBeRepayedDetail.get("halfyear").interest.value+'</td><td>'
							 	 +willBeRepayedDetail.get("threemonth").interest.value+'</td><td>'
							 	 +willBeRepayedDetail.get("twomonth").interest.value+'</td><td>'
							 	 +willBeRepayedDetail.get("onemonth").interest.value+'</td></tr>';
	str += '<tr><td>总计</td><td>'+(parseFloat(willBeRepayedDetail.get("oneyear").chiefAmount.value)+parseFloat(willBeRepayedDetail.get("oneyear").interest.value)).toFixed(2)+'</td><td>'
								 +(parseFloat(willBeRepayedDetail.get("halfyear").chiefAmount.value)+parseFloat(willBeRepayedDetail.get("halfyear").interest.value)).toFixed(2)+'</td><td>'
								 +(parseFloat(willBeRepayedDetail.get("threemonth").chiefAmount.value)+parseFloat(willBeRepayedDetail.get("threemonth").interest.value)).toFixed(2)+'</td><td>'
								 +(parseFloat(willBeRepayedDetail.get("twomonth").chiefAmount.value)+parseFloat(willBeRepayedDetail.get("twomonth").interest.value)).toFixed(2)+'</td><td>'
								 +(parseFloat(willBeRepayedDetail.get("onemonth").chiefAmount.value)+parseFloat(willBeRepayedDetail.get("onemonth").interest.value)).toFixed(2)+'</td></tr>';
	str += '</tbody>';
	str += '</table>';
	
	content.append(str);
	container.append(content);
}

var paybackcanpay = function(container){

	var paybackService = EasyServiceClient.getRemoteProxy("/easyservice/gpps.service.IPayBackService");
	var paybacks = paybackService.findBorrowerCanBeRepayedPayBacks();
	var columns = [ {
		"sTitle" : "项目信息",
			"code" : "product"
	}, {
		"sTitle" : "还款额",
		"code" : "total"
	}, {
		"sTitle" : "本金",
		"code" : "bj"
	}, {
		"sTitle" : "利息",
		"code" : "lx"
	}, {
		"sTitle" : "最迟还款时间",
		"code" : "time"
	}, {
		"sTitle" : "操作",
		"code" : "operate"
	}];
	var aaData = new Array();
	for(var i=0; i<paybacks.size(); i++){
		var data=paybacks.get(i);
		aaData.push(["<a href='productdetail.html?pid="+data.product.id+"' >"+data.product.govermentOrder.title+"("+data.product.productSeries.title+")</a>",
	                    (parseFloat(data.chiefAmount.value)+parseFloat(data.interest.value)).toFixed(2),
	                    data.chiefAmount.value,
	                    data.interest.value,
	                    formatDateToDay(data.deadline),
	                    "<button class='pay' id="+data.id+">还款</button>"
	                    ]);
	}
	var mySettings = $.extend({}, defaultSettings_noCallBack, {
		"aoColumns" : columns,
		"aaData" : aaData
	});
	var content = $('<div></div>');
	var table = $('<table class="table table-striped table-hover" style="min-width:300px;"></table>').appendTo(content);
	container.append(content);
	table.dataTable(mySettings);
	
	$('button.pay').click(function(e){
		var paybackid = $(this).attr('id');
		$('#myModal').modal({
			  keyboard: false,
			  backdrop: false
		});
		window.open('/account/repay/request?paybackId='+paybackid);
	});
}

var paybackcanapply = function(container){
	var paybackService = EasyServiceClient.getRemoteProxy("/easyservice/gpps.service.IPayBackService");
	var paybacks = paybackService.findBorrowerCanBeRepayedInAdvancePayBacks();
	var columns = [ {
		"sTitle" : "项目信息",
			"code" : "product"
	}, {
		"sTitle" : "还款额",
		"code" : "total"
	}, {
		"sTitle" : "本金",
		"code" : "bj"
	}, {
		"sTitle" : "利息",
		"code" : "lx"
	}, {
		"sTitle" : "还款时间",
		"code" : "time"
	}];
	var aaData = new Array();
	for(var i=0; i<paybacks.size(); i++){
		var data=paybacks.get(i);
		aaData.push(["<a href='productdetail.html?pid="+data.product.id+"' >"+data.product.govermentOrder.title+"("+data.product.productSeries.title+")</a>",
	                    (parseFloat(data.chiefAmount.value)+parseFloat(data.interest.value)).toFixed(2),
	                    data.chiefAmount.value,
	                    data.interest.value,
	                    formatDateToDay(data.deadline)]);
	}
	var mySettings = $.extend({}, defaultSettings_noCallBack, {
		"aoColumns" : columns,
		"aaData" : aaData
	});
	var content = $('<div></div>');
	var table = $('<table class="table table-striped table-hover" style="min-width:300px;"></table>').appendTo(content);
	container.append(content);
	table.dataTable(mySettings);
}

var paybackhave = function(container){
	var paybackService = EasyServiceClient.getRemoteProxy("/easyservice/gpps.service.IPayBackService");
	var columns = [ {
		"sTitle" : "项目信息",
			"code" : "product"
	}, {
		"sTitle" : "还款额",
		"code" : "total"
	}, {
		"sTitle" : "本金",
		"code" : "bj"
	}, {
		"sTitle" : "利息",
		"code" : "lx"
	}, {
		"sTitle" : "还款时间",
		"code" : "time"
	}];
	
	var fnServerData = function(sSource, aoData, fnCallback, oSettings) {
		var sEcho = "";
		var iDisplayStart = 0;
		var iDisplayLength = 0;
		for ( var i = 0; i < aoData.length; i++) {
			var data = aoData[i];
			if (data.name == "sEcho")
				sEcho = data.value;
			if (data.name == "iDisplayStart")
				iDisplayStart = data.value;
			if (data.name == "iDisplayLength")
				iDisplayLength = data.value;
		}
		var res = null;
		res = paybackService.findBorrowerPayBacks(2, -1, -1, iDisplayStart, iDisplayLength);
		var result = {};
		result.iTotalRecords = res.get('total');
		result.iTotalDisplayRecords = res.get('total');
		result.aaData = new Array();
		var datas = res.get('result');
		if(datas)
		{
			for(var i=0; i<datas.size(); i++){
				var data=datas.get(i);
				result.aaData.push(["<a href='productdetail.html?pid="+data.product.id+"' >"+data.product.govermentOrder.title+"("+data.product.productSeries.title+")</a>",
			                    (parseFloat(data.chiefAmount.value)+parseFloat(data.interest.value)).toFixed(2),
			                    data.chiefAmount.value,
			                    data.interest.value,
			                    formatDateToDay(data.deadline)]);
			}
		}
		result.sEcho = sEcho;
		fnCallback(result);

		return res;
	}
	var mySettings = $.extend({}, defaultSettings, {
		"aoColumns" : columns,
		"fnServerData" : fnServerData
	});
	var content = $('<div></div>');
	var table = $('<table class="table table-striped table-hover" style="min-width:300px;"></table>').appendTo(content);
	container.append(content);
	table.dataTable(mySettings);
}


var paybackto = function(container){

	var paybackService = EasyServiceClient.getRemoteProxy("/easyservice/gpps.service.IPayBackService");
	var columns = [ {
		"sTitle" : "项目信息",
			"code" : "product"
	}, {
		"sTitle" : "还款额",
		"code" : "total"
	}, {
		"sTitle" : "本金",
		"code" : "bj"
	}, {
		"sTitle" : "利息",
		"code" : "lx"
	}, {
		"sTitle" : "还款时间",
		"code" : "time"
	}];
	
	var fnServerData = function(sSource, aoData, fnCallback, oSettings) {
		var sEcho = "";
		var iDisplayStart = 0;
		var iDisplayLength = 0;
		for ( var i = 0; i < aoData.length; i++) {
			var data = aoData[i];
			if (data.name == "sEcho")
				sEcho = data.value;
			if (data.name == "iDisplayStart")
				iDisplayStart = data.value;
			if (data.name == "iDisplayLength")
				iDisplayLength = data.value;
		}
		var res = null;
		res = paybackService.findBorrowerPayBacks(0, -1, -1, iDisplayStart, iDisplayLength);
		var result = {};
		result.iTotalRecords = res.get('total');
		result.iTotalDisplayRecords = res.get('total');
		result.aaData = new Array();
		var datas = res.get('result');
		if(datas)
		{
			for(var i=0; i<datas.size(); i++){
				var data=datas.get(i);
				result.aaData.push(["<a href='productdetail.html?pid="+data.product.id+"' >"+data.product.govermentOrder.title+"("+data.product.productSeries.title+")</a>",
			                    (parseFloat(data.chiefAmount.value)+parseFloat(data.interest.value)).toFixed(2),
			                    data.chiefAmount.value,
			                    data.interest.value,
			                    formatDateToDay(data.deadline)]);
			}
		}
		result.sEcho = sEcho;
		fnCallback(result);

		return res;
	}
	var mySettings = $.extend({}, defaultSettings, {
		"aoColumns" : columns,
		"fnServerData" : fnServerData
	});
	var content = $('<div></div>');
	var table = $('<table class="table table-striped table-hover" style="min-width:300px;"></table>').appendTo(content);
	container.append(content);
	table.dataTable(mySettings);
}
var cashProcessor=function(action,state,container){
	var account = EasyServiceClient.getRemoteProxy("/easyservice/gpps.service.IAccountService");
	var columns = [ {
		"sTitle" : "时间",
			"code" : "time"
	}, {
		"sTitle" : "总金额",
		"code" : "total"
	}, {
		"sTitle" : "本金",
		"code" : "bj"
	}, {
		"sTitle" : "利息",
		"code" : "lx"
	}, {
		"sTitle" : "手续费",
		"code" : "fee"
	}, {
		"sTitle" : "动作",
		"code" : "action"
	}, {
		"sTitle" : "备注",
		"code" : "description"
	}];
	
	var fnServerData = function(sSource, aoData, fnCallback, oSettings) {
		var sEcho = "";
		var iDisplayStart = 0;
		var iDisplayLength = 0;
		for ( var i = 0; i < aoData.length; i++) {
			var data = aoData[i];
			if (data.name == "sEcho")
				sEcho = data.value;
			if (data.name == "iDisplayStart")
				iDisplayStart = data.value;
			if (data.name == "iDisplayLength")
				iDisplayLength = data.value;
		}
		var res = null;
		res = account.findBorrowerCashStreamByActionAndState(action,state, iDisplayStart, iDisplayLength);
		var result = {};
		result.iTotalRecords = res.get('total');
		result.iTotalDisplayRecords = res.get('total');
		result.aaData = new Array();
		var cashs = res.get('result');
		for(var i=0; i<cashs.size(); i++){
			result.aaData.push([formatDate(cashs.get(i).createtime), 
			                    (parseFloat(cashs.get(i).chiefamount.value)+parseFloat(cashs.get(i).interest.value)).toFixed(2), 
			                    cashs.get(i).chiefamount.value, 
			                    cashs.get(i).interest.value, 
			                    0, 
			                    cashstate[cashs.get(i).action], 
			                    cashs.get(i).description]);
		}
		result.sEcho = sEcho;
		fnCallback(result);

		return res;
	}
	var mySettings = $.extend({}, defaultSettings, {
		"aoColumns" : columns,
		"fnServerData" : fnServerData
	});
	var content = $('<div></div>');
	var table = $('<table class="table table-striped table-hover" style="min-width:300px;"></table>').appendTo(content);
	container.append(content);
	table.dataTable(mySettings);
}
var cashall = function(container){
	return cashProcessor(-1,2,container);
}

var cashrecharge = function(container){
	return cashProcessor(0,2,container);
}

var cashwithdraw = function(container){
	return cashProcessor(5,2,container);
}


var cashfinancing = function(container){
	return cashProcessor(3,2,container);
}

var cashpayback = function(container){
	return cashProcessor(4,2,container);
}

var mynote = function(container){
	var content = $('<div></div>');
	var str = "";
	str += '<table class="table table-striped table-hover" style="min-width:300px;">';
	str += '<thead>';	
	str += '<tr><td style="min-width:100px;">标题</td><td style="min-width:50px;">发布日期</td><td style="min-width:100px;">公告类型</td></tr>';
	str += '</thead>';
	str += '<tbody>';
	str += '<tr><td><a href="#">新一期产品即将火爆抢购</td><td>2014-9-31</td><td>理财推荐</td></tr>';
	str += '<tr><td><a href="#">XXXX杯跑步活动</td><td>2014-10-5</td><td>春蕾活动</td></tr>';
	str += '<tr><td><a href="#">系统二期功能模块开放</td><td>2014-10-15</td><td>平台资讯</td></tr>';
	str += '</tbody>';
	str += '</table>';
	content.append(str);
	container.append(content);
}


var bnav2funtion = {
		"my-score" : myscore,
		"my-activity" : myactivity,
		"my-note" : mynote,
		"request-all" : requestall,
		"request-tohandle" : requesttohandle,
		"request-pass" : requestpass,
		"request-refuse" : requestrefuse,
		"payback-all" : paybackall,
		"payback-canpay" : paybackcanpay,
		"payback-canapply" : paybackcanapply,
		"payback-to" : paybackto,
		"payback-have" : paybackhave,
		"cash-all" : cashall,
		"cash-recharge" : cashrecharge,
		"cash-withdraw" : cashwithdraw,
		"cash-financing" : cashfinancing,
		"cash-payback" : cashpayback,
		"order-all" : orderall,
		"order-preview" : orderpreview,
		"order-financing" : orderfinancing,
		"order-paying" : orderpaying,
		"order-toclose" : ordertoclose,
		"order-closed" : orderclosed,
		"order-quit" : orderquit
}