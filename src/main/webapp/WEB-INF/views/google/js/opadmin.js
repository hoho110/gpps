
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
		var li5 = $('<li role="presentation"><a href="javascript:void(0)" data-sk="order-payback">还款中</a></li>');
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

var nav2funtion = {
		'borrower-request' : borrowerrequest,
		'borrower-new' : borrowernew,
		'borrower-pass' : borrowerpass,
		'borrower-refuse' : borrowerrefuse
}