
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

var borrowerstate = {
		10 : '新注册',
		11 : '申请净调',
		12 : '净调通过',
		14 : '净调拒绝'
}

var cashstate = {
		0 : '充值',
		1 : '冻结',
		2 : '解冻',
		3 : '投标',
		4 : '还款',
		5 : '提现'
}
var productstate = {
		0:'未发布',
		1:'融资中',
		2:'还款中',
		4:'流标',
		8:'还款完成',
		16:'还款中', 
		32:'还款完成',
		64:'还款完成'
}
var orderstate = {
		0:'未发布',
		1: "融资中",
		2:"预发布",
		4:"还款中",
		8:"待关闭",
		16:"流标",
		32:"已关闭"
}
	
var createAdminNavLevel2 = function(nav){
	var ul = $('<ul class="nav nav-second nav-tabs" style="float:right;" role="tablist"></ul>');
	if(nav=='lender'){
		var li2 = $('<li role="presentation" class="active"><a href="javascript:void(0)" data-sk="lender-view">用户浏览</a></li>');
		ul.append(li2);
	}else if(nav=='borrower'){
		var li2 = $('<li role="presentation"><a href="javascript:void(0)" data-sk="borrower-new">新注册企业</a></li>');
		var li3 = $('<li role="presentation" class="active"><a href="javascript:void(0)" data-sk="borrower-request">申请净调企业</a></li>');
		var li4 = $('<li role="presentation"><a href="javascript:void(0)" data-sk="borrower-pass">审核通过企业</a></li>');
		var li5 = $('<li role="presentation"><a href="javascript:void(0)" data-sk="borrower-refuse">审核拒绝企业</a></li>');
		ul.append(li2);
		ul.append(li3);
		ul.append(li4);
		ul.append(li5);
	}else if(nav=='request'){
		var li1 = $('<li role="presentation" class="active"><a href="javascript:void(0)" data-sk="request-all">全部</a></li>');
		var li2 = $('<li role="presentation"><a href="javascript:void(0)" data-sk="request-tohandle">待处理</a></li>');
		var li3 = $('<li role="presentation"><a href="javascript:void(0)" data-sk="request-pass">审核通过</a></li>');
		var li4 = $('<li role="presentation"><a href="javascript:void(0)" data-sk="request-refuse">拒绝</a></li>');
		ul.append(li1);
		ul.append(li2);
		ul.append(li3);
		ul.append(li4);
	}else if(nav=='order'){
		var li1 = $('<li role="presentation" class="active"><a href="javascript:void(0)" data-sk="order-all">全部</a></li>');
		var li2 = $('<li role="presentation"><a href="javascript:void(0)" data-sk="order-unpublish">未发布</a></li>');
		var li3 = $('<li role="presentation"><a href="javascript:void(0)" data-sk="order-preview">预览</a></li>');
		var li4 = $('<li role="presentation"><a href="javascript:void(0)" data-sk="order-financing">融资中</a></li>');
		var li5 = $('<li role="presentation"><a href="javascript:void(0)" data-sk="order-paying">还款中</a></li>');
		var li6 = $('<li role="presentation"><a href="javascript:void(0)" data-sk="order-toclose">待关闭</a></li>');
		var li7 = $('<li role="presentation"><a href="javascript:void(0)" data-sk="order-closed">已关闭</a></li>');
		var li8 = $('<li role="presentation"><a href="javascript:void(0)" data-sk="order-quit">流标</a></li>');
		ul.append(li1);
		ul.append(li2);
		ul.append(li3);
		ul.append(li4);
		ul.append(li5);
		ul.append(li6);
		ul.append(li7);
		ul.append(li8);
	}else if(nav=='other'){
		var li1 = $('<li role="presentation" class="active"><a href="javascript:void(0)" data-sk="message">消息管理</a></li>');
		var li2 = $('<li role="presentation"><a href="javascript:void(0)" data-sk="activity">活动管理</a></li>');
		var li3 = $('<li role="presentation"><a href="javascript:void(0)" data-sk="help">帮助信息</a></li>');
		ul.append(li1);
		ul.append(li2);
		ul.append(li3);
	}
	
	return ul;
}

var borrowernew = function(container){

	var bService = EasyServiceClient.getRemoteProxy("/easyservice/gpps.service.IBorrowerService");
		var borrowers = bService.findFinancingBorrower(10);
		
		
		var columns = [ {
			"sTitle" : "企业名称",
				"code" : "name"
		}, {
			"sTitle" : "营业执照",
			"code" : "state"
		}, {
			"sTitle" : "手机号",
			"code" : "phone"
		}, {
			"sTitle" : "申请时间",
			"code" : "requesttime"
		}, {
			"sTitle" : "状态",
			"code" : "repayed"
		}];
		var aaData = new Array();
		for(var i=0; i<borrowers.size(); i++){
			var data=borrowers.get(i);
			aaData.push([data.companyName,
			                    data.license,
			                    data.tel,
			                    formatDate(data.createtime),
			                    borrowerstate[data.privilege]]);
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

var borrowerrequest = function(container){
	var bService = EasyServiceClient.getRemoteProxy("/easyservice/gpps.service.IBorrowerService");
		var borrowers = bService.findFinancingBorrower(11);
		
		
		var columns = [ {
			"sTitle" : "企业名称",
				"code" : "name"
		}, {
			"sTitle" : "营业执照",
			"code" : "state"
		}, {
			"sTitle" : "手机号",
			"code" : "phone"
		}, {
			"sTitle" : "申请时间",
			"code" : "requesttime"
		}, {
			"sTitle" : "状态",
			"code" : "repayed"
		}, {
			"sTitle" : "编辑",
			"code" : "编辑"
		}, {
			"sTitle" : "操作",
			"code" : "operate"
		}];
		var aaData = new Array();
		for(var i=0; i<borrowers.size(); i++){
			var data=borrowers.get(i);
			aaData.push([data.companyName,
			                    data.license,
			                    data.tel,
			                    formatDate(data.createtime),
			                    borrowerstate[data.privilege],
			                    '<button id='+data.id+' class="borroweredit">编辑</button>',
			                    '<button id='+data.id+' class="borrowerpass">通过</button><button id='+data.id+' class="borrowerrefuse">拒绝</button>']);
		}
		var mySettings = $.extend({}, defaultSettings_noCallBack, {
			"aoColumns" : columns,
			"aaData" : aaData
		});
		var content = $('<div></div>');
		var table = $('<table class="table table-striped table-hover" style="min-width:300px;"></table>').appendTo(content);
		container.append(content);
		table.dataTable(mySettings);
		
		$('button.borroweredit').click(function(e){
			var id = $(this).attr('id');
			window.open("borroweredit.html?id="+id);
		});
		
		$('button.borrowerpass').click(function(e){
			try{
			bService.passFundingApplying(parseInt($(this).attr('id')));
			window.location.href="opadmin.html?fid=borrower&sid=borrower-pass";
			}catch(e){
				alert(e.message);
			}
		});
		
		$('button.borrowerrefuse').click(function(e){
			try{
			bService.refuseFundingApplying(parseInt($(this).attr('id')));
			window.location.href="opadmin.html?fid=borrower&sid=borrower-refuse";
			}catch(e){
				alert(e.message);
			}
		});
}

var borrowerpass = function(container){
	var bService = EasyServiceClient.getRemoteProxy("/easyservice/gpps.service.IBorrowerService");
		var borrowers = bService.findFinancingBorrower(12);
		
		
		var columns = [ {
			"sTitle" : "企业名称",
				"code" : "name"
		}, {
			"sTitle" : "营业执照",
			"code" : "state"
		}, {
			"sTitle" : "手机号",
			"code" : "phone"
		}, {
			"sTitle" : "申请时间",
			"code" : "requesttime"
		}, {
			"sTitle" : "状态",
			"code" : "repayed"
		}, {
			"sTitle" : "编辑",
			"code" : "编辑"
		}];
		var aaData = new Array();
		for(var i=0; i<borrowers.size(); i++){
			var data=borrowers.get(i);
			aaData.push([data.companyName,
			                    data.license,
			                    data.tel,
			                    formatDate(data.createtime),
			                    borrowerstate[data.privilege],
			                    '<button id='+data.id+' class="borroweredit">编辑</button>']);
		}
		var mySettings = $.extend({}, defaultSettings_noCallBack, {
			"aoColumns" : columns,
			"aaData" : aaData
		});
		var content = $('<div></div>');
		var table = $('<table class="table table-striped table-hover" style="min-width:300px;"></table>').appendTo(content);
		container.append(content);
		table.dataTable(mySettings);
		
		$('button.borroweredit').click(function(e){
			var id = $(this).attr('id');
			window.open("borroweredit.html?id="+id);
		});
}

var borrowerrefuse = function(container){
	var bService = EasyServiceClient.getRemoteProxy("/easyservice/gpps.service.IBorrowerService");
		var borrowers = bService.findFinancingBorrower(14);
		
		
		var columns = [ {
			"sTitle" : "企业名称",
				"code" : "name"
		}, {
			"sTitle" : "营业执照",
			"code" : "state"
		}, {
			"sTitle" : "手机号",
			"code" : "phone"
		}, {
			"sTitle" : "申请时间",
			"code" : "requesttime"
		}, {
			"sTitle" : "状态",
			"code" : "repayed"
		}];
		var aaData = new Array();
		for(var i=0; i<borrowers.size(); i++){
			var data=borrowers.get(i);
			aaData.push([data.companyName,
			                    data.license,
			                    data.tel,
			                    formatDate(data.createtime),
			                    borrowerstate[data.privilege]]);
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

var lenderview = function(container){
	var lenderService = EasyServiceClient.getRemoteProxy("/easyservice/gpps.service.ILenderService");
	var columns = [ {
		"sTitle" : "姓名",
			"code" : "name"
	}, {
		"sTitle" : "电话",
		"code" : "tel"
	}, {
		"sTitle" : "身份证",
		"code" : "identityCard"
	}, {
		"sTitle" : "级别",
		"code" : "level"
	}, {
		"sTitle" : "汇付账户",
		"code" : "thirdPartyAccount"
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
		res = lenderService.findByPrivilegeWithPaging(-1,iDisplayStart, iDisplayLength);
		var result = {};
		result.iTotalRecords = res.get('total');
		result.iTotalDisplayRecords = res.get('total');
		result.aaData = new Array();
		var items = res.get('result');
		if(items)
		{
			for(var i=0; i<items.size(); i++){
				var data=items.get(i);
				result.aaData.push([data.name,
				                    data.tel,
				                    data.identityCard,
				                    data.level,
				                    data.thirdPartyAccount]);
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

var handlerequest = function(state, container){

	var bService = EasyServiceClient.getRemoteProxy("/easyservice/gpps.service.IBorrowerService");
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
		res = bService.findAllFinancingRequests(state,iDisplayStart, iDisplayLength);
		var result = {};
		result.iTotalRecords = res.get('total');
		result.iTotalDisplayRecords = res.get('total');
		result.aaData = new Array();
		var items = res.get('result');
		if(items)
		{
			for(var i=0; i<items.size(); i++){
				var data=items.get(i);
				var str = '待审核';
				if(data.state==1){
					str = '审核通过';
				}else if(data.state==2){
					str = '审核未通过';
				}
				result.aaData.push([data.govermentOrderName,
				                    data.applyFinancingAmount,
				                    data.rate,
				                    formatDate(data.createtime),
				                    str]);
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


var requestall = function(container){
	handlerequest(-1, container);
}
var requesttohandle = function(container){
	var bService = EasyServiceClient.getRemoteProxy("/easyservice/gpps.service.IBorrowerService");
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
	}, {
		"sTitle" : "编辑",
		"code" : "edit"
	}, {
		"sTitle" : "操作",
		"code" : "operation"
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
		res = bService.findAllFinancingRequests(0,iDisplayStart, iDisplayLength);
		var result = {};
		result.iTotalRecords = res.get('total');
		result.iTotalDisplayRecords = res.get('total');
		result.aaData = new Array();
		var items = res.get('result');
		if(items)
		{
			for(var i=0; i<items.size(); i++){
				var data=items.get(i);
				var str = '待审核';
				if(data.state==1){
					str = '审核通过';
				}else if(data.state==2){
					str = '审核未通过';
				}
				result.aaData.push([data.govermentOrderName,
				                    data.applyFinancingAmount,
				                    data.rate,
				                    formatDate(data.createtime),
				                    str,
				                    '<button id='+data.id+' class="requestedit">编辑</button>',
				                    '<button id='+data.id+' class="requestpass">通过</button><button id='+data.id+' class="requestrefuse">拒绝</button>']);
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
	
	$('button.requestedit').click(function(e){
		window.open('requestedit.html?id='+$(this).attr('id'));
	});
	$('button.requestpass').click(function(e){
		var rid = $(this).attr('id');
		bService.passFinancingRequest(parseInt(rid));
		window.location.href='opadmin.html?fid=request&sid=request-pass';
	});
	
	$('button.requestrefuse').click(function(e){
		var rid = $(this).attr('id');
		bService.refuseFinancingRequest(parseInt(rid));
		window.location.href='opadmin.html?fid=request&sid=request-refuse';
	});
	
}

var requestpass = function(container){
	handlerequest(1, container);
}

var requestrefuse = function(container){
	handlerequest(2, container);
}


var ordershow = function(state, container){
	var orderService = EasyServiceClient.getRemoteProxy("/easyservice/gpps.service.IGovermentOrderService");
	var orderDao = EasyServiceClient.getRemoteProxy("/easyservice/gpps.dao.IGovermentOrderDao");
	var productService = EasyServiceClient.getRemoteProxy("/easyservice/gpps.service.IProductService");
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
		"sTitle" : "预期金额",
		"code" : "expect"
	}, {
		"sTitle" : "已融金额",
		"code" : "real"
	}, {
		"sTitle" : "状态",
		"code" : "repayed"
	}, {
		"sTitle" : "详情",
		"code" : "view"
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
		res = orderService.findByStatesByPage(state,iDisplayStart, iDisplayLength);
		var result = {};
		result.iTotalRecords = res.get('total');
		result.iTotalDisplayRecords = res.get('total');
		result.aaData = new Array();
		var items = res.get('result');
		if(items)
		{
			for(var i=0; i<items.size(); i++){
				var data=items.get(i);
				var products = data.products;
				var totalamount = 0;
				var real = 0;
				for(var j=0; j<products.size(); j++){
					totalamount += parseInt(products.get(j).expectAmount.value);
					real += parseInt(products.get(j).realAmount.value);
				}
				result.aaData.push([data.title,
				             formatDate(data.financingStarttime),
				             formatDate(data.financingEndtime),
				                    totalamount,
				                    real,
				                    orderstate[data.state],
				                    "<button class='vieworder' id='"+data.id+"'>查看</button>"]);
			}
		}
		result.sEcho = sEcho;
		fnCallback(result);
		
		
		
		
		$('button.vieworder').click(function(e){
			var ntr = $(this).parents('tr').next('tr');
			if(ntr.prop("className")=='information'){
				$(this).parents('tr').removeAttr("style");
				ntr.remove();
				return;
			}
			
			$(this).parents('tr').css('backgroundColor', 'orange');
			
			var orderid = parseInt($(this).attr('id'));
			var order = orderDao.find(orderid);
			var products = productService.findByGovermentOrder(orderid);
			
		
			var table = $('<table class="ui-list-invest" id="products" style="width:95%"></table>');
			var tr = $('<tr id="header" style="padding-left:0px; padding-right:0px;"></tr>');
			tr.append('<td class="color-gray-text text-center">产品类型</td>');
			tr.append('<td class="color-gray-text text-center">年利率</td>');
			tr.append('<td class="color-gray-text text-center">预期金额</td>');
			tr.append('<td class="color-gray-text text-center">已融金额</td>');
			tr.append('<td class="color-gray-text text-center">期限</td>');
			tr.append('<td class="color-gray-text text-center">进度</td>');
			tr.append('<td class="color-gray-text text-center">状态</td>');
			table.append('<tr><td class="color-gray-text text-center" colspan=7>'+order.description+'</td></tr>');
			table.append(tr);
			
	    	   for(var i=0; i<products.size(); i++){
	    		   var product = products.get(i);
	    		   product.govermentOrder = order;
	    		   table.append(createSingleSubProduct(product));
	    	   }
			
			
			var ftr = $('<tr class="information"></tr>');
			var ftd = $('<td colspan=7 align=center></td>');
			ftr.append(ftd);
			ftd.append(table);
			
			
			$(this).parents('tr').after(ftr);
			
		});
		
		
		

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


var orderall = function(container){
	ordershow(-1, container)
}

var orderunpublish = function(container){

	var orderService = EasyServiceClient.getRemoteProxy("/easyservice/gpps.service.IGovermentOrderService");
	var orderDao = EasyServiceClient.getRemoteProxy("/easyservice/gpps.dao.IGovermentOrderDao");
	var productService = EasyServiceClient.getRemoteProxy("/easyservice/gpps.service.IProductService");
	var orders = orderService.findAllUnpublishOrders();
	
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
		"sTitle" : "预期金额",
		"code" : "expect"
	}, {
		"sTitle" : "已融金额",
		"code" : "real"
	}, {
		"sTitle" : "状态",
		"code" : "repayed"
	}, {
		"sTitle" : "详情",
		"code" : "view"
	}];
	var aaData = new Array();
	for(var i=0; i<orders.size(); i++){
		var data=orders.get(i);
		var products = data.products;
		var totalamount = 0;
		var real = 0;
		for(var j=0; j<products.size(); j++){
			totalamount += parseInt(products.get(j).expectAmount.value);
			real += parseInt(products.get(j).realAmount.value);
		}
		aaData.push([data.title,
		             formatDate(data.financingStarttime),
		             formatDate(data.financingEndtime),
		                    totalamount,
		                    real,
		                    orderstate[data.state],
		                    "<button class='vieworder' id='"+data.id+"'>查看</button>"]);
	}
	var mySettings = $.extend({}, defaultSettings_noCallBack, {
		"aoColumns" : columns,
		"aaData" : aaData
	});
	var content = $('<div></div>');
	var table = $('<table class="table table-striped table-hover" style="min-width:300px;"></table>').appendTo(content);
	container.append(content);
	table.dataTable(mySettings);
	
	$('button.vieworder').click(function(e){
		var ntr = $(this).parents('tr').next('tr');
		if(ntr.prop("className")=='information'){
			$(this).parents('tr').removeAttr("style");
			ntr.remove();
			return;
		}
		
		$(this).parents('tr').css('backgroundColor', 'orange');
		
		var orderid = parseInt($(this).attr('id'));
		var order = orderDao.find(orderid);
		var products = productService.findByGovermentOrder(orderid);
		
	
		var table = $('<table class="ui-list-invest" id="products" style="width:95%"></table>');
		var tr = $('<tr id="header" style="padding-left:0px; padding-right:0px;"></tr>');
		tr.append('<td class="color-gray-text text-center">产品类型</td>');
		tr.append('<td class="color-gray-text text-center">年利率</td>');
		tr.append('<td class="color-gray-text text-center">预期金额</td>');
		tr.append('<td class="color-gray-text text-center">已融金额</td>');
		tr.append('<td class="color-gray-text text-center">期限</td>');
		tr.append('<td class="color-gray-text text-center">进度</td>');
		tr.append('<td class="color-gray-text text-center">状态</td>');
		table.append('<tr><td class="color-gray-text text-center" colspan=7>'+order.description+'</td></tr>');
		table.append(tr);
		
    	   for(var i=0; i<products.size(); i++){
    		   var product = products.get(i);
    		   product.govermentOrder = order;
    		   table.append(createUnpublishProduct(product));
    	   }
		
		
		var ftr = $('<tr class="information"></tr>');
		var ftd = $('<td colspan=7 align=center></td>');
		ftr.append(ftd);
		ftd.append(table);
		
		
		$(this).parents('tr').after(ftr);
		
	});
}

var orderpreview = function(container){

	var orderService = EasyServiceClient.getRemoteProxy("/easyservice/gpps.service.IGovermentOrderService");
	var orderDao = EasyServiceClient.getRemoteProxy("/easyservice/gpps.dao.IGovermentOrderDao");
	var productService = EasyServiceClient.getRemoteProxy("/easyservice/gpps.service.IProductService");
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
		"sTitle" : "预期金额",
		"code" : "expect"
	}, {
		"sTitle" : "已融金额",
		"code" : "real"
	}, {
		"sTitle" : "状态",
		"code" : "repayed"
	}, {
		"sTitle" : "详情",
		"code" : "view"
	}, {
		"sTitle" : "操作",
		"code" : "operate"
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
		res = orderService.findByStatesByPage(2,iDisplayStart, iDisplayLength);
		var result = {};
		result.iTotalRecords = res.get('total');
		result.iTotalDisplayRecords = res.get('total');
		result.aaData = new Array();
		var items = res.get('result');
		if(items)
		{
			for(var i=0; i<items.size(); i++){
				var data=items.get(i);
				var products = data.products;
				var totalamount = 0;
				var real = 0;
				for(var j=0; j<products.size(); j++){
					totalamount += parseInt(products.get(j).expectAmount.value);
					real += parseInt(products.get(j).realAmount.value);
				}
				result.aaData.push([data.title,
				             formatDate(data.financingStarttime),
				             formatDate(data.financingEndtime),
				                    totalamount,
				                    real,
				                    orderstate[data.state],
				                    "<button class='vieworder' id='"+data.id+"'>查看</button>",
				                    "<button class='startfinancing' id='"+data.id+"'>开始融资</button>"]);
			}
		}
		result.sEcho = sEcho;
		fnCallback(result);
		
		
		$('button.startfinancing').click(function(e){
			var orderid = parseInt($(this).attr('id'));
			orderService.startFinancing(orderid);
			window.location.href="opadmin.html?fid=order&sid=order-financing";
		});
		
		$('button.vieworder').click(function(e){
			var ntr = $(this).parents('tr').next('tr');
			if(ntr.prop("className")=='information'){
				$(this).parents('tr').removeAttr("style");
				ntr.remove();
				return;
			}
			
			$(this).parents('tr').css('backgroundColor', 'orange');
			
			var orderid = parseInt($(this).attr('id'));
			var order = orderDao.find(orderid);
			var products = productService.findByGovermentOrder(orderid);
			
		
			var table = $('<table class="ui-list-invest" id="products" style="width:95%"></table>');
			var tr = $('<tr id="header" style="padding-left:0px; padding-right:0px;"></tr>');
			tr.append('<td class="color-gray-text text-center">产品类型</td>');
			tr.append('<td class="color-gray-text text-center">年利率</td>');
			tr.append('<td class="color-gray-text text-center">预期金额</td>');
			tr.append('<td class="color-gray-text text-center">已融金额</td>');
			tr.append('<td class="color-gray-text text-center">期限</td>');
			tr.append('<td class="color-gray-text text-center">进度</td>');
			tr.append('<td class="color-gray-text text-center">状态</td>');
			table.append('<tr><td class="color-gray-text text-center" colspan=7>'+order.description+'</td></tr>');
			table.append(tr);
			
	    	   for(var i=0; i<products.size(); i++){
	    		   var product = products.get(i);
	    		   product.govermentOrder = order;
	    		   table.append(createSingleSubProduct(product));
	    	   }
			
			
			var ftr = $('<tr class="information"></tr>');
			var ftd = $('<td colspan=7 align=center></td>');
			ftr.append(ftd);
			ftd.append(table);
			
			
			$(this).parents('tr').after(ftr);
			
		});
		
		
		

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

var orderfinancing = function(container){
	var orderService = EasyServiceClient.getRemoteProxy("/easyservice/gpps.service.IGovermentOrderService");
	var orderDao = EasyServiceClient.getRemoteProxy("/easyservice/gpps.dao.IGovermentOrderDao");
	var productService = EasyServiceClient.getRemoteProxy("/easyservice/gpps.service.IProductService");
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
		"sTitle" : "预期金额",
		"code" : "expect"
	}, {
		"sTitle" : "已融金额",
		"code" : "real"
	}, {
		"sTitle" : "状态",
		"code" : "repayed"
	}, {
		"sTitle" : "详情",
		"code" : "view"
	}, {
		"sTitle" : "操作",
		"code" : "operate"
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
		res = orderService.findByStatesByPage(1,iDisplayStart, iDisplayLength);
		var result = {};
		result.iTotalRecords = res.get('total');
		result.iTotalDisplayRecords = res.get('total');
		result.aaData = new Array();
		var items = res.get('result');
		if(items)
		{
			for(var i=0; i<items.size(); i++){
				var data=items.get(i);
				var products = data.products;
				var totalamount = 0;
				var real = 0;
				for(var j=0; j<products.size(); j++){
					totalamount += parseInt(products.get(j).expectAmount.value);
					real += parseInt(products.get(j).realAmount.value);
				}
				result.aaData.push([data.title,
				             formatDate(data.financingStarttime),
				             formatDate(data.financingEndtime),
				                    totalamount,
				                    real,
				                    orderstate[data.state],
				                    "<button class='vieworder' id='"+data.id+"'>查看</button>",
				                    "<button class='startpay' id='"+data.id+"'>开始还款</button><button class='quit' id='"+data.id+"'>流标</button>"]);
			}
		}
		result.sEcho = sEcho;
		fnCallback(result);
		
		
		$('button.startpay').click(function(e){
			var orderid = parseInt($(this).attr('id'));
			var products = productService.findByGovermentOrder(orderid);
			for(var i=0; i<products.size(); i++){
				productService.startRepaying(products.get(i).id);
			}
			orderService.startRepaying(orderid);
			window.location.href="opadmin.html?fid=order&sid=order-paying";
		});
		
		$('button.quit').click(function(e){
			var orderid = parseInt($(this).attr('id'));
			orderService.quitFinancing(orderid);
			window.location.href="opadmin.html?fid=order&sid=order-quit";
		});
		
		$('button.vieworder').click(function(e){
			var ntr = $(this).parents('tr').next('tr');
			if(ntr.prop("className")=='information'){
				$(this).parents('tr').removeAttr("style");
				ntr.remove();
				return;
			}
			
			$(this).parents('tr').css('backgroundColor', 'orange');
			
			var orderid = parseInt($(this).attr('id'));
			var order = orderDao.find(orderid);
			var products = productService.findByGovermentOrder(orderid);
			
		
			var table = $('<table class="ui-list-invest" id="products" style="width:95%"></table>');
			var tr = $('<tr id="header" style="padding-left:0px; padding-right:0px;"></tr>');
			tr.append('<td class="color-gray-text text-center">产品类型</td>');
			tr.append('<td class="color-gray-text text-center">年利率</td>');
			tr.append('<td class="color-gray-text text-center">预期金额</td>');
			tr.append('<td class="color-gray-text text-center">已融金额</td>');
			tr.append('<td class="color-gray-text text-center">期限</td>');
			tr.append('<td class="color-gray-text text-center">进度</td>');
			tr.append('<td class="color-gray-text text-center">状态</td>');
			table.append('<tr><td class="color-gray-text text-center" colspan=7>'+order.description+'</td></tr>');
			table.append(tr);
			
	    	   for(var i=0; i<products.size(); i++){
	    		   var product = products.get(i);
	    		   product.govermentOrder = order;
	    		   table.append(createSingleSubProduct(product));
	    	   }
			
			
			var ftr = $('<tr class="information"></tr>');
			var ftd = $('<td colspan=7 align=center></td>');
			ftr.append(ftd);
			ftd.append(table);
			
			
			$(this).parents('tr').after(ftr);
			
		});
		
		
		

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

var orderpaying = function(container){
	ordershow(4, container)
}

var ordertoclose = function(container){
	var orderService = EasyServiceClient.getRemoteProxy("/easyservice/gpps.service.IGovermentOrderService");
	var orderDao = EasyServiceClient.getRemoteProxy("/easyservice/gpps.dao.IGovermentOrderDao");
	var productService = EasyServiceClient.getRemoteProxy("/easyservice/gpps.service.IProductService");
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
		"sTitle" : "预期金额",
		"code" : "expect"
	}, {
		"sTitle" : "已融金额",
		"code" : "real"
	}, {
		"sTitle" : "状态",
		"code" : "repayed"
	}, {
		"sTitle" : "详情",
		"code" : "view"
	}, {
		"sTitle" : "操作",
		"code" : "operate"
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
		res = orderService.findByStatesByPage(8,iDisplayStart, iDisplayLength);
		var result = {};
		result.iTotalRecords = res.get('total');
		result.iTotalDisplayRecords = res.get('total');
		result.aaData = new Array();
		var items = res.get('result');
		if(items)
		{
			for(var i=0; i<items.size(); i++){
				var data=items.get(i);
				var products = data.products;
				var totalamount = 0;
				var real = 0;
				for(var j=0; j<products.size(); j++){
					totalamount += parseInt(products.get(j).expectAmount.value);
					real += parseInt(products.get(j).realAmount.value);
				}
				result.aaData.push([data.title,
				             formatDate(data.financingStarttime),
				             formatDate(data.financingEndtime),
				                    totalamount,
				                    real,
				                    orderstate[data.state],
				                    "<button class='vieworder' id='"+data.id+"'>查看</button>",
				                    "<button class='closeorder' id='"+data.id+"'>关闭</button>"]);
			}
		}
		result.sEcho = sEcho;
		fnCallback(result);
		
		
		$('button.closeorder').click(function(e){
			var orderid = parseInt($(this).attr('id'));
			orderService.closeComplete(orderid);
			window.location.href="opadmin.html?fid=order&sid=order-closed";
		});
		
		
		$('button.vieworder').click(function(e){
			var ntr = $(this).parents('tr').next('tr');
			if(ntr.prop("className")=='information'){
				$(this).parents('tr').removeAttr("style");
				ntr.remove();
				return;
			}
			
			$(this).parents('tr').css('backgroundColor', 'orange');
			
			var orderid = parseInt($(this).attr('id'));
			var order = orderDao.find(orderid);
			var products = productService.findByGovermentOrder(orderid);
			
		
			var table = $('<table class="ui-list-invest" id="products" style="width:95%"></table>');
			var tr = $('<tr id="header" style="padding-left:0px; padding-right:0px;"></tr>');
			tr.append('<td class="color-gray-text text-center">产品类型</td>');
			tr.append('<td class="color-gray-text text-center">年利率</td>');
			tr.append('<td class="color-gray-text text-center">预期金额</td>');
			tr.append('<td class="color-gray-text text-center">已融金额</td>');
			tr.append('<td class="color-gray-text text-center">期限</td>');
			tr.append('<td class="color-gray-text text-center">进度</td>');
			tr.append('<td class="color-gray-text text-center">状态</td>');
			table.append('<tr><td class="color-gray-text text-center" colspan=7>'+order.description+'</td></tr>');
			table.append(tr);
			
	    	   for(var i=0; i<products.size(); i++){
	    		   var product = products.get(i);
	    		   product.govermentOrder = order;
	    		   table.append(createSingleSubProduct(product));
	    	   }
			
			
			var ftr = $('<tr class="information"></tr>');
			var ftd = $('<td colspan=7 align=center></td>');
			ftr.append(ftd);
			ftd.append(table);
			
			
			$(this).parents('tr').after(ftr);
			
		});
		
		
		

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

var orderclosed = function(container){
	ordershow(32, container)
}

var orderquit = function(container){
	ordershow(16, container)
}




var nav2funtion = {
		'lender-view' : lenderview,
		'borrower-request' : borrowerrequest,
		'borrower-new' : borrowernew,
		'borrower-pass' : borrowerpass,
		'borrower-refuse' : borrowerrefuse,
		'request-all' : requestall,
		'request-tohandle' : requesttohandle,
		'request-pass' : requestpass,
		'request-refuse' : requestrefuse,
		"order-all" : orderall,
		"order-unpublish" : orderunpublish,
		"order-preview" : orderpreview,
		"order-financing" : orderfinancing,
		"order-paying" : orderpaying,
		"order-toclose" : ordertoclose,
		"order-closed" : orderclosed,
		"order-quit" : orderquit
}