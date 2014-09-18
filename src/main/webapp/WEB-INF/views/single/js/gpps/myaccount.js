
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


var myscore = function(){
	var lenderService = EasyServiceClient.getRemoteProxy("/easyservice/gpps.service.ILenderService");
	var lender=lenderService.getCurrentUser();
	var content = $('<div></div>');
	content.append('<p>您好'+lender.name+'，您的积分是<span class="orange">'+lender.grade+'</span>分，等级为<span class="orange">钻石会员</span></p>');
	content.append('<br><span class="orange">积分规则：</span>');
	content.append('<p>如何获取积分的说明</p>');
	var str = "";
	str += '<table class="table table-striped table-hover" style="min-width:300px;">';
	str += '<thead>';	
	str += '<tr><td style="min-width:100px;">会员等级</td><td style="min-width:50px;">对应积分</td><td style="min-width:100px;">有效期</td><td style="min-width:50px;">最低贡献值</td><td style="min-width:50px;">说明</td></tr>';
	str += '</thead>';
	str += '<tbody>';
	str += '<tr><td>钻石VIP会员</td><td>10000000以上</td><td>3个月</td><td>1000000</td><td>有最低消费</td></tr>';
	str += '<tr><td>白金VIP会员</td><td>1000000-10000000</td><td>3个月</td><td>300000</td><td>有最低消费</td></tr>';
	str += '<tr><td>黄金VIP会员</td><td>200000-1000000</td><td>半年</td><td>100000</td><td>有最低消费</td></tr>';
	str += '<tr><td>白银VIP会员</td><td>50000-200000</td><td>一年</td><td>10000</td><td>有最低消费</td></tr>';
	str += '<tr><td>VIP会员</td><td>10000-50000</td><td>永久</td><td>无</td><td>无最低消费</td></tr>';
	str += '<tr><td>普通会员</td><td>0-10000</td><td>永久</td><td>无</td><td>无最低消费</td></tr>';
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
	str1 += '<tr><td>钻石VIP会员</td><td>10000000以上</td><td>3个月</td><td>1000000</td><td>有最低消费</td></tr>';
	str1 += '<tr><td>白金VIP会员</td><td>1000000-10000000</td><td>3个月</td><td>300000</td><td>有最低消费</td></tr>';
	str1 += '<tr><td>黄金VIP会员</td><td>200000-1000000</td><td>半年</td><td>100000</td><td>有最低消费</td></tr>';
	str1 += '<tr><td>白银VIP会员</td><td>50000-200000</td><td>一年</td><td>10000</td><td>有最低消费</td></tr>';
	str1 += '<tr><td>VIP会员</td><td>10000-50000</td><td>永久</td><td>无</td><td>无最低消费</td></tr>';
	str1 += '<tr><td>普通会员</td><td>0-10000</td><td>永久</td><td>无</td><td>无最低消费</td></tr>';
	str1 += '</tbody>';
	str1 += '</table>';
	content.append(str1);
	return content;
}
var myactivity = function(){
	var content = $('<div></div>');
	var str = "";
	str += '<table class="table table-striped table-hover" style="min-width:300px;">';
	str += '<thead>';	
	str += '<tr><td style="min-width:100px;">活动名称</td><td style="min-width:50px;">活动日期</td><td style="min-width:100px;">活动类型</td><td style="min-width:50px;">主办方</td><td style="min-width:50px;">状态</td></tr>';
	str += '</thead>';
	str += '<tbody>';
	str += '<tr><td><a href="../activity/index.html" target="_blank">XXXX杯跑步活动</td><td>2014-9-31</td><td>长跑</td><td>春蕾资产管理</td><td><span class="orange">待参加</span></td></tr>';
	str += '<tr><td><a href="../activity/index.html" target="_blank">XXXX杯跑步活动</td><td>2014-9-31</td><td>徒步</td><td>春蕾资产管理</td><td><span class="orange">待参加</span></td></tr>';
	str += '<tr><td><a href="../activity/index.html" target="_blank">XXXX杯跑步活动</td><td>2014-9-31</td><td>自驾游</td><td>春蕾资产管理</td><td>已参加</td></tr>';
	str += '<tr><td><a href="../activity/index.html" target="_blank">XXXX杯跑步活动</td><td>2014-9-31</td><td>自驾游</td><td>春蕾资产管理</td><td>已参加</td></tr>';
	str += '<tr><td><a href="../activity/index.html" target="_blank">XXXX杯跑步活动</td><td>2014-9-31</td><td>旅游</td><td>春蕾资产管理</td><td>已参加</td></tr>';
	str += '<tr><td><a href="../activity/index.html" target="_blank">XXXX杯跑步活动</td><td>2014-9-31</td><td>旅游</td><td>春蕾资产管理</td><td>未参加</td></tr>';
	str += '</tbody>';
	str += '</table>';
	content.append(str);
	return content;
}

var submitall = function(){
//	var content = $('<div></div>');
//	var str = "";
//	str += '<table class="table table-striped table-hover" style="min-width:300px;" id="dataTables-example">';
//	str += '<thead>';	
//	str += '<tr><td style="min-width:100px;">项目信息</td><td style="min-width:50px;">状态</td><td style="min-width:100px;">投标完成时间</td><td style="min-width:50px;">金额</td><td style="min-width:50px;">已回款</td><td style="min-width:50px;">待回款</td><td style="min-width:50px;">合同</td></tr>';
//	str += '</thead>';
//	str += '<tbody>';
//	str += '<tr><td><a href="productdetail.html" target="_blank">电脑工程企业经营借款</a></td><td>回款中</td><td>2014-8-5</td><td>500</td><td>0</td><td>23</td><td><a href="pdf/001.pdf" target="_blank">合同</a></td></tr>';
//	str += '<tr><td><a href="productdetail.html" target="_blank">电脑工程企业经营借款2</a></td><td>回款中</td><td>2014-7-31</td><td>200</td><td>0</td><td>9.87</td><td><a href="pdf/001.pdf" target="_blank">合同</a></td></tr>';
//	str += '<tr><td><a href="productdetail.html" target="_blank">电脑工程企业经营借款3</a></td><td>回款中</td><td>2014-7-16</td><td>300</td><td>0.57</td><td>13</td><td><a href="pdf/001.pdf" target="_blank">合同</a></td></tr>';
//	str += '<tr><td><a href="productdetail.html" target="_blank">电脑工程企业经营借款4</a></td><td>回款中</td><td>2014-7-3</td><td>1500</td><td>15</td><td>150</td><td><a href="pdf/001.pdf" target="_blank">合同</a></td></tr>';
//	str += '<tr><td><a href="productdetail.html" target="_blank">电脑工程企业经营借款5</a></td><td>回款中</td><td>2014-6-18</td><td>1000</td><td>20</td><td>123</td><td><a href="pdf/001.pdf" target="_blank">合同</a></td></tr>';
//	str += '<tr><td><a href="productdetail.html" target="_blank">电脑工程企业经营借款6</a></td><td>投标中</td><td>2014-8-5</td><td>500</td><td>0</td><td>23</td><td><a href="pdf/001.pdf" target="_blank">合同</a></td></tr>';
//	str += '<tr><td><a href="productdetail.html" target="_blank">电脑工程企业经营借款7</a></td><td>回款中</td><td>2014-7-31</td><td>200</td><td>0</td><td>9.87</td><td><a href="pdf/001.pdf" target="_blank">合同</a></td></tr>';
//	str += '<tr><td><a href="productdetail.html" target="_blank">电脑工程企业经营借款8</a></td><td>回款中</td><td>2014-7-16</td><td>300</td><td>0.57</td><td>13</td><td><a href="pdf/001.pdf" target="_blank">合同</a></td></tr>';
//	str += '<tr><td><a href="productdetail.html" target="_blank">电脑工程企业经营借款9</a></td><td>回款中</td><td>2014-7-3</td><td>1500</td><td>15</td><td>150</td><td><a href="pdf/001.pdf" target="_blank">合同</a></td></tr>';
//	str += '<tr><td><a href="productdetail.html" target="_blank">电脑工程企业经营借款10</a></td><td>回款中</td><td>2014-6-18</td><td>1000</td><td>20</td><td>123</td><td><a href="pdf/001.pdf" target="_blank">合同</a></td></tr>';
//	str += '</tbody>';
//	str += '</table>';
//	content.append(str);
//	return content;
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
		res = submitService.findMyAllSubmits(iDisplayStart, iDisplayLength);
		var result = {};
		result.iTotalRecords = res.get('total');
		result.iTotalDisplayRecords = res.get('total');
		result.aaData = new Array();
		var items = res.get('result');
		if(items)
		{
			for(var i=0; i<items.size(); i++){
				var item=items.get(i);
				result.aaData.push(["<a href='productdetail.html?pid="+item.product.id+"' >"+item.product.govermentOrder.title+"</a>",
				                    item.product.state,
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
	var content = $('<div></div>');
	var table = $('<table class="table table-striped table-hover" style="min-width:300px;"></table>').appendTo(content);
	table.dataTable(mySettings);
	return content;
}

var submittoafford = function(){
//	var content = $('<div></div>');
//	var str = "";
//	str += '<table class="table table-striped table-hover" style="min-width:300px;" id="dataTables-example">';
//	str += '<thead>';	
//	str += '<tr><td style="min-width:100px;">项目信息</td><td style="min-width:50px;">状态</td><td style="min-width:100px;">投标完成时间</td><td style="min-width:50px;">金额</td><td style="min-width:50px;">最迟支付时间</td><td style="min-width:50px;">操作</td></tr>';
//	str += '</thead>';
//	str += '<tbody>';
//	str += '<tr><td><a href="productdetail.html" target="_blank">电脑工程企业经营借款</a></td><td>待支付</td><td>2014-8-5</td><td>500</td><td>2014-8-7</td><td><a href="#">立即支付</a></td></tr>';
//	str += '<tr><td><a href="productdetail.html" target="_blank">电脑工程企业经营借款2</a></td><td>待支付</td><td>2014-8-6</td><td>200</td><td>2014-8-7</td><td><a href="#">立即支付</a></td></tr>';
//	str += '</tbody>';
//	str += '</table>';
//	content.append(str);
//	return content;
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
		aaData.push(["<a href='productdetail.html?pid="+data.product.id+"'>"+data.product.govermentOrder.title+"</a>",
		                    "待支付",
		                    formatDate(data.createtime),
		                    data.amount.value,
		                    formatDate(data.payExpiredTime),
		                    "<a href='/account/buy/request?submitId="+data.id+"'>立即支付</a>"]);
	}
	var mySettings = $.extend({}, defaultSettings_noCallBack, {
		"aoColumns" : columns,
		"aaData" : aaData
	});
	var content = $('<div></div>');
	var table = $('<table class="table table-striped table-hover" style="min-width:300px;"></table>').appendTo(content);
	table.dataTable(mySettings);
	return content;
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

var submitpayback = function(){
//	var content = $('<div></div>');
//	var str = "";
//	str += '<table class="table table-striped table-hover" style="min-width:300px;" id="dataTables-example">';
//	str += '<thead>';	
//	str += '<tr><td style="min-width:100px;">项目信息</td><td style="min-width:50px;">状态</td><td style="min-width:100px;">投标完成时间</td><td style="min-width:50px;">金额</td><td style="min-width:50px;">已回款</td><td style="min-width:50px;">待回款</td><td style="min-width:50px;">合同</td></tr>';
//	str += '</thead>';
//	str += '<tbody>';
//	str += '<tr><td><a href="productdetail.html" target="_blank">电脑工程企业经营借款</a></td><td>回款中</td><td>2014-8-5</td><td>500</td><td>0</td><td>23</td><td><a href="pdf/001.pdf" target="_blank">合同</a></td></tr>';
//	str += '<tr><td><a href="productdetail.html" target="_blank">电脑工程企业经营借款2</a></td><td>回款中</td><td>2014-7-31</td><td>200</td><td>0</td><td>9.87</td><td><a href="pdf/001.pdf" target="_blank">合同</a></td></tr>';
//	str += '<tr><td><a href="productdetail.html" target="_blank">电脑工程企业经营借款3</a></td><td>回款中</td><td>2014-7-16</td><td>300</td><td>0.57</td><td>13</td><td><a href="pdf/001.pdf" target="_blank">合同</a></td></tr>';
//	str += '<tr><td><a href="productdetail.html" target="_blank">电脑工程企业经营借款4</a></td><td>回款中</td><td>2014-7-3</td><td>1500</td><td>15</td><td>150</td><td><a href="pdf/001.pdf" target="_blank">合同</a></td></tr>';
//	str += '<tr><td><a href="productdetail.html" target="_blank">电脑工程企业经营借款5</a></td><td>回款中</td><td>2014-6-18</td><td>1000</td><td>20</td><td>123</td><td><a href="pdf/001.pdf" target="_blank">合同</a></td></tr>';
//	str += '<tr><td><a href="productdetail.html" target="_blank">电脑工程企业经营借款7</a></td><td>回款中</td><td>2014-7-31</td><td>200</td><td>0</td><td>9.87</td><td><a href="pdf/001.pdf" target="_blank">合同</a></td></tr>';
//	str += '<tr><td><a href="productdetail.html" target="_blank">电脑工程企业经营借款8</a></td><td>回款中</td><td>2014-7-16</td><td>300</td><td>0.57</td><td>13</td><td><a href="pdf/001.pdf" target="_blank">合同</a></td></tr>';
//	str += '<tr><td><a href="productdetail.html" target="_blank">电脑工程企业经营借款9</a></td><td>回款中</td><td>2014-7-3</td><td>1500</td><td>15</td><td>150</td><td><a href="pdf/001.pdf" target="_blank">合同</a></td></tr>';
//	str += '<tr><td><a href="productdetail.html" target="_blank">电脑工程企业经营借款10</a></td><td>回款中</td><td>2014-6-18</td><td>1000</td><td>20</td><td>123</td><td><a href="pdf/001.pdf" target="_blank">合同</a></td></tr>';
//	str += '</tbody>';
//	str += '</table>';
//	content.append(str);
//	return content;
	var submitService = EasyServiceClient.getRemoteProxy("/easyservice/gpps.service.ISubmitService");
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
				result.aaData.push(["<a href='productdetail.html?pid="+item.product.id+"' >"+item.product.govermentOrder.title+"</a>",
				                    "还款中",
				                    formatDate(item.product.govermentOrder.financingEndtime),
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
	var content = $('<div></div>');
	var table = $('<table class="table table-striped table-hover" style="min-width:300px;"></table>').appendTo(content);
	table.dataTable(mySettings);
	return content;
}

var submitdone = function(){
//	var content = $('<div></div>');
//	var str = "";
//	str += '<table class="table table-striped table-hover" style="min-width:300px;" id="dataTables-example">';
//	str += '<thead>';	
//	str += '<tr><td style="min-width:100px;">项目信息</td><td style="min-width:50px;">状态</td><td style="min-width:100px;">投标完成时间</td><td style="min-width:50px;">金额</td><td style="min-width:50px;">总利息</td><td style="min-width:50px;">合同</td></tr>';
//	str += '</thead>';
//	str += '<tbody>';
//	str += '<tr><td><a href="productdetail.html" target="_blank">电脑工程企业经营借款</a></td><td>已关闭</td><td>2014-8-5</td><td>500</td><td>100</td><td><a href="pdf/001.pdf" target="_blank">合同</a></td></tr>';
//	str += '<tr><td><a href="productdetail.html" target="_blank">电脑工程企业经营借款2</a></td><td>已关闭</td><td>2014-7-31</td><td>200</td><td>40</td><td><a href="pdf/001.pdf" target="_blank">合同</a></td></tr>';
//	str += '<tr><td><a href="productdetail.html" target="_blank">电脑工程企业经营借款3</a></td><td>已关闭</td><td>2014-7-16</td><td>300</td><td>60</td><td><a href="pdf/001.pdf" target="_blank">合同</a></td></tr>';
//	str += '<tr><td><a href="productdetail.html" target="_blank">电脑工程企业经营借款4</a></td><td>已关闭</td><td>2014-7-3</td><td>1500</td><td>300</td><td><a href="pdf/001.pdf" target="_blank">合同</a></td></tr>';
//	str += '<tr><td><a href="productdetail.html" target="_blank">电脑工程企业经营借款5</a></td><td>已关闭</td><td>2014-6-18</td><td>1000</td><td>200</td><td><a href="pdf/001.pdf" target="_blank">合同</a></td></tr>';
//	str += '<tr><td><a href="productdetail.html" target="_blank">电脑工程企业经营借款7</a></td><td>已关闭</td><td>2014-7-31</td><td>200</td><td>40</td><td><a href="pdf/001.pdf" target="_blank">合同</a></td></tr>';
//	str += '</tbody>';
//	str += '</table>';
//	content.append(str);
//	return content;
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
				result.aaData.push(["<a href='productdetail.html?pid="+item.product.id+"' >"+item.product.govermentOrder.title+"</a>",
				                    "还款中",
				                    formatDate(item.createtime),
				                    item.amount.value,
				                    item.repayedAmount.value,
				                    (parseFloat(item.amount.value)-parseFloat(item.repayedAmount.value)),
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
	table.dataTable(mySettings);
	return content;
}

var paybackall = function(){
//	var content = $('<div></div>');
//	var str = "";
//	str += '<table class="table table-striped table-hover" style="min-width:300px;" id="dataTables-example">';
//	str += '<thead>';	
//	str += '<tr><td style="min-width:100px;">已回款统计</td><th style="min-width:50px;">最近一年</th><th style="min-width:100px;">最近半年</th><th style="min-width:50px;">最近三个月</th><th style="min-width:50px;">最近两个月</th><th style="min-width:50px;">最近一个月</th></tr>';
//	str += '</thead>';
//	str += '<tbody>';
//	str += '<tr><td>本金</td><td>30000</td><td>20000</td><td>15000</td><td>10000</td><td>5000</td></tr>';
//	str += '<tr><td>利息</td><td>6000</td><td>4000</td><td>3000</td><td>2000</td><td>1000</td></tr>';
//	str += '<tr><td>总计</td><td>36000</td><td>24000</td><td>18000</td><td>12000</td><td>6000</td></tr>';
//	str += '</tbody>';
//	str += '</table>';
//	
//	str += '<br>';
//	str += '<br>';
//	
//	
//	str += '<table class="table table-striped table-hover" style="min-width:300px;" id="dataTables-example">';
//	str += '<thead>';	
//	str += '<tr><td style="min-width:100px;">待回款统计</td><th style="min-width:50px;">未来一年</th><th style="min-width:100px;">未来半年</th><th style="min-width:50px;">未来三个月</th><th style="min-width:50px;">未来两个月</th><th style="min-width:50px;">未来一个月</th></tr>';
//	str += '</thead>';
//	str += '<tbody>';
//	str += '<tr><td>本金</td><td>30000</td><td>20000</td><td>15000</td><td>10000</td><td>5000</td></tr>';
//	str += '<tr><td>利息</td><td>6000</td><td>4000</td><td>3000</td><td>2000</td><td>1000</td></tr>';
//	str += '<tr><td>总计</td><td>36000</td><td>24000</td><td>18000</td><td>12000</td><td>6000</td></tr>';
//	str += '</tbody>';
//	str += '</table>';
//	
//	content.append(str);
//	return content;
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
								 +(parseFloat(willBeRepayedDetail.get("onemonth").chiefAmount.value)+parseFloat(repayedDetail.get("onemonth").interest.value)).toFixed(2)+'</td></tr>';
	str += '</tbody>';
	str += '</table>';
	
	content.append(str);
	return content;
}


var paybackhave = function(){
//	var content = $('<div></div>');
//	var str = "";
//	str += '<table class="table table-striped table-hover" style="min-width:300px;" id="dataTables-example">';
//	str += '<thead>';	
//	str += '<tr><td style="min-width:100px;">项目信息</td><td style="min-width:50px;">还款额</td><td>本金</td><td>利息</td><td style="min-width:100px;">还款时间</td></tr>';
//	str += '</thead>';
//	str += '<tbody>';
//	str += '<tr><td><a href="productdetail.html" target="_blank">电脑工程企业经营借款</a></td><td>300</td><td>250</td><td>50</td><td>2014-8-5</td></tr>';
//	str += '<tr><td><a href="productdetail.html" target="_blank">电脑工程企业经营借款2</a></td><td>500</td><td>400</td><td>100</td><td>2014-7-31</td></tr>';
//	str += '<tr><td><a href="productdetail.html" target="_blank">电脑工程企业经营借款3</a></td><td>2000</td><td>2000</td><td>0</td><td>2014-7-16</td></tr>';
//	str += '<tr><td><a href="productdetail.html" target="_blank">电脑工程企业经营借款4</a></td><td>12000</td><td>11900</td><td>100</td><td>2014-7-3</td></tr>';
//	str += '<tr><td><a href="productdetail.html" target="_blank">电脑工程企业经营借款5</a></td><td>20</td><td>0</td><td>20</td><td>2014-6-18</td></tr>';
//	str += '<tr><td><a href="productdetail.html" target="_blank">电脑工程企业经营借款7</a></td><td>17</td><td>0</td><td>17</td><td>2014-7-31</td></tr>';
//	str += '<tr><td><a href="productdetail.html" target="_blank">电脑工程企业经营借款8</a></td><td>700</td><td>700</td><td>0</td><td>2014-7-16</td></tr>';
//	str += '<tr><td><a href="productdetail.html" target="_blank">电脑工程企业经营借款9</a></td><td>90</td><td>0</td><td>90</td><td>2014-7-3</td></tr>';
//	str += '<tr><td><a href="productdetail.html" target="_blank">电脑工程企业经营借款10</a></td><td>12</td><td>0</td><td>12</td><td>2014-6-18</td></tr>';
//	str += '</tbody>';
//	str += '</table>';
//	content.append(str);
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
				result.aaData.push(["<a href='productdetail.html?pid="+data.submit.product.id+"' >"+data.submit.product.govermentOrder.title+"</a>",
				                    parseFloat(data.chiefamount.value)+parseFloat(data.interest.value),
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
	var content = $('<div></div>');
	var table = $('<table class="table table-striped table-hover" style="min-width:300px;"></table>').appendTo(content);
	table.dataTable(mySettings);
	return content;
	
}


var paybackto = function(){
//	var content = $('<div></div>');
//	var str = "";
//	str += '<table class="table table-striped table-hover" style="min-width:300px;" id="dataTables-example">';
//	str += '<thead>';	
//	str += '<tr><td style="min-width:100px;">项目信息</td><td style="min-width:50px;">还款额</td><td>本金</td><td>利息</td><td style="min-width:100px;">预计还款时间</td></tr>';
//	str += '</thead>';
//	str += '<tbody>';
//	str += '<tr><td><a href="productdetail.html" target="_blank">电脑工程企业经营借款</a></td><td>300</td><td>250</td><td>50</td><td>2014-8-5</td></tr>';
//	str += '<tr><td><a href="productdetail.html" target="_blank">电脑工程企业经营借款2</a></td><td>500</td><td>400</td><td>100</td><td>2014-7-31</td></tr>';
//	str += '<tr><td><a href="productdetail.html" target="_blank">电脑工程企业经营借款3</a></td><td>2000</td><td>2000</td><td>0</td><td>2014-7-16</td></tr>';
//	str += '<tr><td><a href="productdetail.html" target="_blank">电脑工程企业经营借款4</a></td><td>12000</td><td>11900</td><td>100</td><td>2014-7-3</td></tr>';
//	str += '<tr><td><a href="productdetail.html" target="_blank">电脑工程企业经营借款5</a></td><td>20</td><td>0</td><td>20</td><td>2014-6-18</td></tr>';
//	str += '<tr><td><a href="productdetail.html" target="_blank">电脑工程企业经营借款7</a></td><td>17</td><td>0</td><td>17</td><td>2014-7-31</td></tr>';
//	str += '<tr><td><a href="productdetail.html" target="_blank">电脑工程企业经营借款8</a></td><td>700</td><td>700</td><td>0</td><td>2014-7-16</td></tr>';
//	str += '<tr><td><a href="productdetail.html" target="_blank">电脑工程企业经营借款9</a></td><td>90</td><td>0</td><td>90</td><td>2014-7-3</td></tr>';
//	str += '<tr><td><a href="productdetail.html" target="_blank">电脑工程企业经营借款10</a></td><td>12</td><td>0</td><td>12</td><td>2014-6-18</td></tr>';
//	str += '</tbody>';
//	str += '</table>';
//	content.append(str);
//	return content;
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
		aaData.push(["<a href='productdetail.html?pid="+data.product.id+"' >"+data.product.govermentOrder.title+"</a>",
		                    parseFloat(data.chiefAmount.value)+parseFloat(data.interest.value),
		                    data.chiefAmount.value,
		                    data.interest.value,
		                    formatDate(data.deadline)]);
	}
	var mySettings = $.extend({}, defaultSettings_noCallBack, {
		"aoColumns" : columns,
		"aaData" : aaData
	});
	var content = $('<div></div>');
	var table = $('<table class="table table-striped table-hover" style="min-width:300px;"></table>').appendTo(content);
	table.dataTable(mySettings);
	return content;
}
var cashProcessor=function(action,state){
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
			result.aaData.push([formatDate(cashs.get(i).createtime), cashs.get(i).chiefamount.value, cashs.get(i).chiefamount.value, cashs.get(i).interest.value, 0, cashstate[cashs.get(i).action], cashs.get(i).description]);
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
	table.dataTable(mySettings);
	return content;
}
var cashall = function(){
	return cashProcessor(-1,-1);
}

var cashrecharge = function(){
	return cashProcessor(0,-1);
}

var cashwithdraw = function(){
	return cashProcessor(5,-1);
}


var cashinvest = function(){
	return cashProcessor(3,-1);
}

var cashreceive = function(){
	return cashProcessor(4,-1);
}

var mynote = function(){
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
	return content;
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
		"payback-all" : paybackall,
		"payback-have" : paybackhave,
		"payback-to" : paybackto,
		"cash-all" : cashall,
		"cash-recharge" : cashrecharge,
		"cash-withdraw" : cashwithdraw,
		"cash-invest" : cashinvest,
		"cash-receive" : cashreceive,
		"tools-fluxility" : "tools-fluxility",
		"tools-receive-statistics" : "tools-receive-statistics"
}