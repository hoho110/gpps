
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
				"aaSorting" : [ [ 4, "desc" ] ]
				,
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
		"bJQueryUI" : false, //页面风格使用jQuery.
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



_$fd = function(longt) {
		var date = null;
		if (typeof longt == 'number') {
			date = new Date(longt);
		} else if (longt instanceof Date) {
			date = longt;
		} else {
			return null;
		}
		var yyyy = date.getFullYear();
		var MM = date.getMonth() + 1 >= 10 ? date.getMonth() + 1 : '0' + (date.getMonth() + 1);
		var dd = date.getDate() >= 10 ? date.getDate() : '0' + date.getDate();
		var HH = date.getHours() >= 10 ? date.getHours() : '0' + date.getHours();
		var mm = date.getMinutes() >= 10 ? date.getMinutes() : '0' + date.getMinutes();
		var ss = date.getSeconds() >= 10 ? date.getSeconds() : '0' + date.getSeconds();
		return {
			'yyyy' : yyyy,
			'MM' : MM,
			'dd' : dd,
			'HH' : HH,
			'mm' : mm,
			'ss' : ss
		}
	};

	formatDate = function(longt) {
		//yyyy-MM-dd T HH:mm:ss
		var r = _$fd(longt);
		var ldStr = r.yyyy + '-' + r.MM + '-' + r.dd + ' T ' + r.HH + ':' + r.mm + ':' + r.ss;
		return ldStr;
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
	var lenderService = EasyServiceClient.getRemoteProxy("/easyservice/gpps.service.ILenderService");
	var lender=lenderService.getCurrentUser();
	var content = $('<div></div>');
	var name = lender.name==null?lender.loginId : lender.name
	content.append('<p>您好'+name+'，您的积分是<span class="orange">'+lender.grade+'</span>分，会员等级为<span class="orange">level'+lender.level+'</span></p>');
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
	var refservice = EasyServiceClient.getRemoteProxy("/easyservice/gpps.service.IActivityRefService");
	var columns = [ {
		"sTitle" : "活动标题",
			"code" : "name"
	}, {
		"sTitle" : "申请截止时间",
		"code" : "state"
	}, {
		"sTitle" : "正式活动时间",
		"code" : "state"
	}, {
		"sTitle" : "状态",
		"code" : "state"
	}];
	
	var actstate = {1 : '报名中', 2 : '进行中', 3 : '已结束'};
	
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
		res = refservice.findByLender(cuser.id, iDisplayStart, iDisplayLength);
		var result = {};
		result.iTotalRecords = res.get('total');
		result.iTotalDisplayRecords = res.get('total');
		result.aaData = new Array();
		var items = res.get('result');
		if(items)
		{
			for(var i=0; i<items.size(); i++){
				var data=items.get(i);
				result.aaData.push(['<a href="'+data.activity.url+'?id='+data.activity.id+'" target="_blank">'+data.activity.name+'</a>',
				             formatDate(data.activity.applystarttime),
				             formatDate(data.activity.starttime),
				             actstate[data.activity.state]]);
			}
		}
		result.sEcho = sEcho;
		fnCallback(result);
		
		$('button.editactivity').click(function(e){
			var id = $(this).attr('id');
			window.open('editactivity.html?id='+id);
		})

		return res;
	}
	var mySettings = $.extend({}, defaultSettings, {
		"aoColumns" : columns,
		"fnServerData" : fnServerData
	});
	var table = $('<table></table>');
	container.append(table);
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
	var table = $('<table></table>');
	container.append(table);
	table.dataTable(mySettings);
}

var submittoafford = function(container){
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
		"sTitle" : "最迟支付时间",
		"code" : "repayed"
	}, {
		"sTitle" : "操作",
		"code" : "contract"
	}];
	var datas = null;
	datas = submitService.findMyAllWaitforPayingSubmits();
	var aaData = new Array();
	for(var i=0; i<datas.size(); i++){
		var data=datas.get(i);
		aaData.push(["<a href='productdetail.html?pid="+data.product.id+"'>"+data.product.govermentOrder.title+"("+data.product.productSeries.title+")</a>",
		                    "待支付",
		                    formatDate(data.createtime),
		                    data.amount.value,
		                    formatDate(data.payExpiredTime),
		                    "<a id="+data.id+" class='submittoafford' href='javascript:void(0);'>立即支付</a>"]);
	}
	var mySettings = $.extend({}, defaultSettings_noCallBack, {
		"aoColumns" : columns,
		"aaData" : aaData
	});
	var table = $('<table></table>');
	container.append(table);
	table.dataTable(mySettings);
}

var submittoaudit = function(){
	var content = $('<div></div>');
	var str = "";
	str += '<table class="table table-striped table-hover" style="min-width:300px;" id="dataTables-example">';
	str += '<thead>';	
	str += '<tr><td style="min-width:100px;">项目信息</td><td style="min-width:50px;">状态</td><td style="min-width:100px;">投标完成时间</td><td style="min-width:50px;">金额</td><td style="min-width:50px;">最迟审核时间</td></tr>';
	str += '</thead>';
	str += '<tbody>';
	str += '<tr><td><a href="productdetail.html" target="_blank">电脑工程企业经营借款</a></td><td>待审核</td><td>2014-8-5</td><td>500</td><td>2014-8-7</td></tr>';
	str += '<tr><td><a href="productdetail.html" target="_blank">电脑工程企业经营借款2</a></td><td>待审核</td><td>2014-8-6</td><td>200</td><td>2014-8-7</td></tr>';
	str += '<tr><td><a href="productdetail.html" target="_blank">电脑工程企业经营借款</a></td><td>待审核</td><td>2014-8-5</td><td>500</td><td>2014-8-7</td></tr>';
	str += '<tr><td><a href="productdetail.html" target="_blank">电脑工程企业经营借款2</a></td><td>待审核</td><td>2014-8-6</td><td>200</td><td>2014-8-7</td></tr>';
	str += '</tbody>';
	str += '</table>';
	content.append(str);
	return content;
}

var submitpayback = function(container){
	var submitService = EasyServiceClient.getRemoteProxy("/easyservice/gpps.service.ISubmitService");
	var paybackService = EasyServiceClient.getRemoteProxy("/easyservice/gpps.service.IPayBackService");
	var columns = [ {
		"sTitle" : "项目信息",
			"code" : "info"
	}, {
		"sTitle" : "状态",
		"code" : "state"
	}, {
		"sTitle" : "投标完成时间",
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
		"sTitle" : "回款明细",
		"code" : "repaydetail"
	}, {
		"sTitle" : "合同",
		"code" : "contract"
	}];
	
	var paybackState={0: '待还款', 1 : '正在还款', 2: '已还款', 3: '延期' , 5: '待审核'};
	
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
		res = submitService.findMyAllSubmitsByProductStates((2+16),iDisplayStart, iDisplayLength);
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
				                    "还款中",
				                    formatDate(item.product.govermentOrder.financingEndtime),
				                    item.amount.value,
				                    item.repayedAmount.value,
				                    item.waitforRepayAmount.value,
				                    "<a class='repaydetail' href='javascript:void(0);' id="+item.id+">查看</a>",
				                    "<a href='pdf/001.pdf' target='_blank'>合同</a>"]);
			}
		}
		result.sEcho = sEcho;
		fnCallback(result);
		
		$('a.repaydetail').click(function(e){
			var itemid = $(this).attr('id');
			var submititem = submitService.find(parseInt(itemid));
			var pays = paybackService.generatePayBacks(submititem.product.id, parseInt(submititem.amount.value));
			var str = "<table style='width:95%;'>";
			str+="<tr><td colspan=5>本笔投资总金额为："+submititem.amount.value+"元, 预期还款明细如下：</td></tr>";
			str+="<tr><td>序号</td><td>还款日期</td><td>总额</td><td>本金</td><td>利息</td><td>状态</td></tr>";
			for(var i=0; i<pays.size(); i++){
				var pay = pays.get(i);
				str+="<tr><td>"+(i+1)+"</td><td>"+formatDateToDay(pay.deadline)+"</td><td>"+(parseFloat(pay.chiefAmount.value)+parseFloat(pay.interest.value)).toFixed(2)+"</td><td>"+pay.chiefAmount.value+"</td><td>"+pay.interest.value+"</td><td>"+paybackState[pay.state]+"</td></tr>";
			}
			str+="</table>";
			$('#rdetail').html(str);
			$('#repaydetail').modal({
				  keyboard: false,
				  backdrop: true
			});
		})

		return res;
	}
	var mySettings = $.extend({}, defaultSettings, {
		"aoColumns" : columns,
		"fnServerData" : fnServerData
	});
	var table = $('<table></table>');
	container.append(table);
	table.dataTable(mySettings);
}
var submitretreat = function(container){
	var submitService = EasyServiceClient.getRemoteProxy("/easyservice/gpps.service.ISubmitService");
	var columns = [ {
		"sTitle" : "项目信息",
			"code" : "info"
	}, {
		"sTitle" : "状态",
		"code" : "state"
	}, {
		"sTitle" : "退订时间",
		"code" : "lastmodifytime"
	}, {
		"sTitle" : "金额",
		"code" : "amount"
	}, {
		"sTitle" : "备注",
		"code" : "contract"
	}];
	var datas = null;
	datas = submitService.findMyAllRetreatSubmits();
	var aaData = new Array();
	for(var i=0; i<datas.size(); i++){
		var data=datas.get(i);
		aaData.push(["<a href='productdetail.html?pid="+data.product.id+"'>"+data.product.govermentOrder.title+"("+data.product.productSeries.title+")</a>",
		                    "已退订",
		                    formatDate(data.lastmodifytime),
		                    data.amount.value,
		                    "支付失败"]);
	}
	var mySettings = $.extend({}, defaultSettings_noCallBack, {
		"aoColumns" : columns,
		"aaData" : aaData
	});
	var table = $('<table></table>');
	container.append(table);
	table.dataTable(mySettings);
}
var submitdone = function(container){
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
		res = submitService.findMyAllSubmitsByProductStates((8+32+64),iDisplayStart, iDisplayLength);
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
				                    "还款完毕",
				                    formatDate(item.createtime),
				                    item.amount.value,
				                    item.repayedAmount.value,
				                    (parseFloat(item.amount.value)-parseFloat(item.repayedAmount.value)).toFixed(2),
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
	var table = $('<table></table>');
	container.append(table);
	table.dataTable(mySettings);
}

var paybackall = function(container){
	var account = EasyServiceClient.getRemoteProxy("/easyservice/gpps.service.IAccountService");
	var repayedDetail=account.getLenderRepayedDetail();
	var willBeRepayedDetail=account.getLenderWillBeRepayedDetail();
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
	container.append(str);
}


var paybackhave = function(container){
	var account = EasyServiceClient.getRemoteProxy("/easyservice/gpps.service.IAccountService");
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
		res = account.findLenderRepayCashStream(iDisplayStart, iDisplayLength);
		var result = {};
		result.iTotalRecords = res.get('total');
		result.iTotalDisplayRecords = res.get('total');
		result.aaData = new Array();
		var datas = res.get('result');
		if(datas)
		{
			for(var i=0; i<datas.size(); i++){
				var data=datas.get(i);
				result.aaData.push(["<a href='productdetail.html?pid="+data.submit.product.id+"' >"+data.submit.product.govermentOrder.title+"("+data.submit.product.productSeries.title+")</a>",
				                    (parseFloat(data.chiefamount.value)+parseFloat(data.interest.value)).toFixed(2),
				                    data.chiefamount.value,
				                    data.interest.value,
				                    formatDate(data.createtime)]);
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
	var table = $('<table></table>');
	container.append(table);
	table.dataTable(mySettings);
}


var paybackto = function(container){
	var account = EasyServiceClient.getRemoteProxy("/easyservice/gpps.service.IAccountService");
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
		"sTitle" : "预计还款时间",
		"code" : "time"
	}];
	var datas = null;
	datas = account.findLenderWaitforRepay();
	var aaData = new Array();
	for(var i=0; i<datas.size(); i++){
		var data=datas.get(i);
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
	var table = $('<table></table>');
	container.append(table);
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
		res = account.findLenderCashStreamByActionAndState(action,state, iDisplayStart, iDisplayLength);
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
			                    cashs.get(i).fee.value, 
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
	var table = $('<table></table>');
	container.append(table);
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


var cashinvest = function(container){
	return cashProcessor(3,2,container);
}

var cashreceive = function(container){
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


var letterreaded = function(container){
	var letterS = EasyServiceClient.getRemoteProxy("/easyservice/gpps.service.ILetterService");
	var columns = [ {
		"sTitle" : "标题",
			"code" : "time"
	}, {
		"sTitle" : "发送者",
		"code" : "total"
	}, {
		"sTitle" : "发送时间",
		"code" : "bj"
	}, {
		"sTitle" : "状态",
		"code" : "state"
	}, {
		"sTitle" : "已读时间",
		"code" : "lx"
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
		res = letterS.findMyLetters(1, iDisplayStart, iDisplayLength);
		var result = {};
		result.iTotalRecords = res.get('total');
		result.iTotalDisplayRecords = res.get('total');
		result.aaData = new Array();
		var letters = res.get('result');
		if(letters){
		for(var i=0; i<letters.size(); i++){
			result.aaData.push([letters.get(i).title,
			                    "管理员",
			                    formatDate(letters.get(i).createtime), 
			                    letters.get(i).markRead==0?'未读':'已读',
			                    formatDate(letters.get(i).readtime)
			                   ]);
		}
		}
		result.sEcho = sEcho;
		fnCallback(result);
		
		
		
		$('button.readletter').click(function(e){
			var letterid = $(this).attr('id');
			var letter = letterS.find(parseInt(letterid));
			$('#ldetail').html(letter.content);
			$('#letterdetail').modal({
				  keyboard: false,
				  backdrop: true
			});
		})
		
		

		return res;
	}
	var mySettings = $.extend({}, defaultSettings, {
		"aoColumns" : columns,
		"fnServerData" : fnServerData
	});
	var table = $('<table></table>');
	container.append(table);
	table.dataTable(mySettings);
}


var letterunread_center = function(container){
	var letterS = EasyServiceClient.getRemoteProxy("/easyservice/gpps.service.ILetterService");
	var columns = [ {
		"sTitle" : "标题",
			"code" : "time"
	}, {
		"sTitle" : "发送者",
		"code" : "total"
	}, {
		"sTitle" : "发送时间",
		"code" : "bj"
	}, {
		"sTitle" : "状态",
		"code" : "state"
	}, {
		"sTitle" : "操作",
		"code" : "lx"
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
		res = letterS.findMyLetters(0, iDisplayStart, iDisplayLength);
		var result = {};
		result.iTotalRecords = res.get('total');
		result.iTotalDisplayRecords = res.get('total');
		result.aaData = new Array();
		var letters = res.get('result');
		if(letters)
		{
		for(var i=0; i<letters.size(); i++){
			result.aaData.push([letters.get(i).title,
			                    "管理员",
			                    formatDate(letters.get(i).createtime), 
			                    letters.get(i).markRead==0?'未读':'已读',
			                    "<button class='readletter' id='"+letters.get(i).id+"'>阅读</button>"
			                   ]);
		}
		}
		result.sEcho = sEcho;
		fnCallback(result);
		
		
		
		$('button.readletter').click(function(e){
			var letterid = $(this).attr('id');
			var letter = letterS.find(parseInt(letterid));
			
			$('button#letter').html('站内信('+(result.iTotalRecords-1)+')');
			$('#welcome').html(greet() + name+'<a href="javascript:void(0);" id="inner_letter"><span class="glyphicon glyphicon-envelope" style="margin-left:10px; color=red"></span>'+(result.iTotalRecords-1)+'</a>');
			
			
			$('#ldetail').html(letter.content);
			$('#letterdetail').modal({
				  keyboard: false,
				  backdrop: true
			});
		})
		
		

		return res;
	}
	var mySettings = $.extend({}, defaultSettings, {
		"aoColumns" : columns,
		"fnServerData" : fnServerData
	});
	var table = $('<table></table>');
	container.append(table);
	table.dataTable(mySettings);
	
	$('#lclose').unbind('click');
	$('#lclose').bind('click', function(e){
		$('#ldetail').html();
		$('#mycenter').trigger('click');
		$('ul.nav-second li a[data-sk="letter-unread-mycenter"]').trigger('click');
	});
	
}

var letterunread = function(container){
	var letterS = EasyServiceClient.getRemoteProxy("/easyservice/gpps.service.ILetterService");
	var columns = [ {
		"sTitle" : "标题",
			"code" : "time"
	}, {
		"sTitle" : "发送者",
		"code" : "total"
	}, {
		"sTitle" : "发送时间",
		"code" : "bj"
	}, {
		"sTitle" : "状态",
		"code" : "state"
	}, {
		"sTitle" : "操作",
		"code" : "lx"
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
		res = letterS.findMyLetters(0, iDisplayStart, iDisplayLength);
		var result = {};
		result.iTotalRecords = res.get('total');
		result.iTotalDisplayRecords = res.get('total');
		result.aaData = new Array();
		var letters = res.get('result');
		if(letters){
		for(var i=0; i<letters.size(); i++){
			result.aaData.push([letters.get(i).title,
			                    "管理员",
			                    formatDate(letters.get(i).createtime), 
			                    letters.get(i).markRead==0?'未读':'已读',
			                    "<button class='readletter' id='"+letters.get(i).id+"'>阅读</button>"
			                   ]);
		}
		}
		result.sEcho = sEcho;
		fnCallback(result);
		
		
		
		$('button.readletter').click(function(e){
			var letterid = $(this).attr('id');
			var letter = letterS.find(parseInt(letterid));
			
			$('button#letter').html('站内信('+(result.iTotalRecords-1)+')');
			$('#welcome').html(greet() + name+'<a href="javascript:void(0);" id="inner_letter"><span class="glyphicon glyphicon-envelope" style="margin-left:10px; color=red"></span>'+(result.iTotalRecords-1)+'</a>');
			
			
			$('#ldetail').html(letter.content);
			$('#letterdetail').modal({
				  keyboard: false,
				  backdrop: true
			});
		})
		
		

		return res;
	}
	var mySettings = $.extend({}, defaultSettings, {
		"aoColumns" : columns,
		"fnServerData" : fnServerData
	});
	var table = $('<table></table>');
	container.append(table);
	table.dataTable(mySettings);
	
	$('#lclose').unbind('click');
	$('#lclose').bind('click', function(e){
		$('#ldetail').html();
		$('#letter').trigger('click');
		$('ul.nav-second li a[data-sk="letter-unread"]').trigger('click');
	});
}



var noticeview = function(container){
	var nservice = EasyServiceClient.getRemoteProxy("/easyservice/gpps.service.INoticeService");
	var columns = [ {
		"sTitle" : "公告标题",
			"code" : "name"
	}, {
		"sTitle" : "发布时间",
		"code" : "state"
	}, {
		"sTitle" : "发布对象",
		"code" : "state"
	}, {
		"sTitle" : "用户级别",
		"code" : "state"
	}, {
		"sTitle" : "操作",
		"code" : "operate"
	}];
	
	var userType = {0 : '全部', 1 : '投资方', 2 : '融资方'};
	
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
		res = nservice.findAll(-1, iDisplayStart, iDisplayLength);
		var result = {};
		result.iTotalRecords = res.get('total');
		result.iTotalDisplayRecords = res.get('total');
		result.aaData = new Array();
		var items = res.get('result');
		if(items)
		{
			for(var i=0; i<items.size(); i++){
				var data=items.get(i);
				result.aaData.push([data.title,
				             formatDate(data.publishtime),
				             userType[data.usefor],
				                    data.level,
				                    "<button class='viewnotice' id='"+data.id+"'>查看</button>"]);
			}
		}
		result.sEcho = sEcho;
		fnCallback(result);
		
		$('button.viewnotice').click(function(e){
			var letterid = $(this).attr('id');
			var notice = nservice.find(parseInt(letterid));
			$('#nlabel').html(notice.title);
			$('#ndetail').html(notice.content);
			$('#noticedetail').modal({
				  keyboard: false,
				  backdrop: true
			});
		})

		return res;
	}
	var mySettings = $.extend({}, defaultSettings, {
		"aoColumns" : columns,
		"fnServerData" : fnServerData
	});
	var table = $('<table></table>');
	container.append(table);
	table.dataTable(mySettings);
}


var questionview = function(container){
	var helpservice = EasyServiceClient.getRemoteProxy("/easyservice/gpps.service.IHelpService");
	var columns = [ {
		"sTitle" : "提问问题",
			"code" : "question"
	}, {
		"sTitle" : "提问时间",
		"code" : "time"
	}, {
		"sTitle" : "提问者类型",
		"code" : "time"
	}, {
		"sTitle" : "提问者ID",
		"code" : "time"
	}, {
		"sTitle" : "是否回答",
		"code" : "time"
	}
	, {
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
		res = helpservice.findMyHelps(-1, iDisplayStart, iDisplayLength);
		var result = {};
		result.iTotalRecords = res.get('total');
		result.iTotalDisplayRecords = res.get('total');
		result.aaData = new Array();
		var items = res.get('result');
		if(items)
		{
			for(var i=0; i<items.size(); i++){
				var data=items.get(i);
				
				var datatype = data.type;
				var answertype = "";
				var operation = "";
				if(datatype==1){
					answertype="未回答";
					operation = "<button disabled='disabled'>查看</button>";
				}else{
					answertype="<font color=orange>已回答</font>";
					operation = "<button class='viewanswer' id='"+data.id+"'>查看</button>";
				}
				
				result.aaData.push([data.question,
				             formatDate(data.createtime),
				             data.questionerType,
				             data.questionerId,
				             answertype,
				                    operation]);
			}
		}
		result.sEcho = sEcho;
		fnCallback(result);
		
		$('button.viewanswer').click(function(e){
			var helpid = $(this).attr('id');
			
			var help = helpservice.find(parseInt(helpid));
			$('#nlabel').html(help.question);
			$('#ndetail').html(help.answer);
			
			$('#noticedetail').modal({
				  keyboard: false,
				  backdrop: true
			});
		})

		return res;
	}
	var mySettings = $.extend({}, defaultSettings, {
		"aoColumns" : columns,
		"fnServerData" : fnServerData
	});
	var table = $('<table></table>');
	container.append(table);
	table.dataTable(mySettings);
}


var nav2funtion = {
		"my-score" : myscore,
		"my-activity" : myactivity,
		"my-note" : mynote,
		"submit-all" : submitall,
		"submit-toafford" : submittoafford,
		"submit-toaudit" : submittoaudit,
		"submit-payback" : submitpayback,
		"submit-done" : submitdone,
		"submit-retreat" : submitretreat,
		"payback-all" : paybackall,
		"payback-have" : paybackhave,
		"payback-to" : paybackto,
		"cash-all" : cashall,
		"cash-recharge" : cashrecharge,
		"cash-withdraw" : cashwithdraw,
		"cash-invest" : cashinvest,
		"cash-receive" : cashreceive,
		"tools-fluxility" : "tools-fluxility",
		"tools-receive-statistics" : "tools-receive-statistics",
		
		
		"letter-unread-mycenter" : letterunread_center,
		"letter-unread" : letterunread,
		"letter-readed" : letterreaded,
		"notice-view" : noticeview,
		"question-view" : questionview
}