var createProductLine = function(cont, title, label1, des1, label2, des2, description, menkan, qixian, rate, style){
	var container = $(cont).addClass('newProjectItem').addClass('newProjectInfoBox');
	var row1 = $('<div></div>').addClass('row').css(
			{
				"padding-bottom" : "20px",
				"padding-left" : "0px",
				"padding-right" : "0px"
			}
			);
	var row1_col1 = $('<div></div>').addClass('col-xs-12 col-sm-12 col-md-2');
	var stateBtnDiv = $('<div></div>').addClass('stateBtn');
	stateBtnDiv.html('<p class="stateBtnProcess"><a title="简介" class="productline" href="plIntroduction.html" target="_blank"><span class="stateText">'+title+'</span></a></p>');
	stateBtnDiv.appendTo(row1_col1);
	
	
	var row1_col2 = $('<div></div>').addClass('col-xs-12 col-sm-12 col-md-12');
	var BtnListDiv1 = $('<div></div>').addClass('newProjectTitleRightBtnList');
	BtnListDiv1.html('<p class="BtnNormal">'+label1+'</p><div class="promptQX promptQXBox">'+des1+'</div>');
	BtnListDiv1.appendTo(row1_col2);
	
	var BtnListDiv2 = $('<div></div>').addClass('newProjectTitleRightBtnList');
	BtnListDiv2.html('<p class="BtnNormal">'+label2+'</p><div class="promptQX promptQXBox">'+des2+'</div>');
	BtnListDiv2.appendTo(row1_col2);
	
	$('<div class="clear"></div>').appendTo(row1_col2);
	row1.append(row1_col1);	
	row1.append(row1_col2);
	container.append(row1);
	
	
	
	
	var row2 = $('<div></div>').addClass('row');
	
	var row2_col1 = $('<div></div>').addClass('col-xs-3 col-sm-3 col-md-4');
	
	
	var a = $('<a href="#" title="产品列表"></a>');
	row2_col1.append($('<div class="seemall"></div>').append(a));
	a.click(function(e){
		window.parent.toward("productlist");
	});
	
	var row2_col2 = $('<div></div>').addClass('col-xs-9 col-sm-9 col-md-8');
	row2_col2.html('<div class="row"><div class="col-xs-12 col-sm-12 col-md-12">'+description+'</div></div>');
	
	var row2_col3 = $('<div></div>').addClass('col-xs-12 col-sm-12 col-md-12');
	row2_col3.html('<p class="itemTextStyle">投标门槛：<span class="orangeStyle">'+menkan+'</span>万元</p>');
	
	var row2_col4 = $('<div></div>').addClass('col-xs-12 col-sm-12 col-md-12');
	row2_col4.html('<p class="itemTextStyle">融资期限：<span class="orangeStyle">'+qixian+'</span></p>');

	var row2_col5 = $('<div></div>').addClass('col-xs-12 col-sm-12 col-md-12');
	row2_col5.html('<div class="itemTextStyle">年化收益：<span class="orangeStyle">'+rate+'</span></div>');
	
	var row2_col6 = $('<div></div>').addClass('col-xs-12 col-sm-12 col-md-12');
	row2_col6.html('<p class="itemTextStyle">还款方式：<span class="orangeStyle">'+style+'</span></p>');
	
	row2.append(row2_col1);
	row2.append(row2_col2);
	row2.append(row2_col3);
	row2.append(row2_col4);
	row2.append(row2_col5);
	row2.append(row2_col6);
	
	container.append(row2);
	
	return container;
}

var cssStatus = {
		online : 'online-status', 
		audit  : 'done-status',
		payback: 'done-status',
		done   : 'done-status'
	}
var buttonStatus = {
		online : ['btns', '投资'], 
		audit  : ['done-success','审核中'],
		payback: ['done-success','还款中'],
		done   : ['done-success','还款成功']
}
var dateStr = {
		online : '截止', 
		audit  : '截止',
		payback: '计息',
		done   : '关闭'
}


var createStructureProduct = function(title, stat, time, rates, due, totalamount, remainamount, investamount){
	var li = $('<li></li>').addClass('clearfix');
	var productStatus = $('<div></div>').addClass('product-status');
	productStatus.append('<p class="product-name"><a href="productdetail.html"  target="_blank">'+title+'[结构化]</a></p>');

	var productInfoWrap = $('<div></div>').addClass("product-info-wrap clearfix");
	
	var p = $('<p></p>').addClass("product-info-l");
	var index=0;
	for(var i in rates)
	{
	if(index>0)
		{
		p.append('<br>');
		}
	p.append('<span class="interest-rate">'+i+'年化利率：'+rates[i]+'</span>');
	index++;
	}
	productInfoWrap.append(p);
	productInfoWrap.append('<p class="product-info-l"><span class="invest-price">投资期限：'+due+'</span></p>');
	productInfoWrap.append('<p class="product-info-r"><span class="invest-price">项目本金：'+totalamount+'元</span><span class="remain-price">剩余可投金额：'+remainamount+'元</span></p>');
	productStatus.append(productInfoWrap);
	li.append(productStatus);
	
	var operateStatus = $('<div></div>').addClass("operate-status");
	
	var status = $('<div></div>').addClass(cssStatus[stat]+" clearfix");
	var currentAmount = $('<div></div>').addClass("current-amount");
	
	var str = "";
	if(stat=='online')
		{
		str="起";
		}
	currentAmount.append('<strong>投资金额</strong><span class="cur">'+investamount+'</span> 元'+str);
	status.append(currentAmount);
	status.append('<a href="productdetail.html" target="_blank" class="'+buttonStatus[stat][0]+'">'+buttonStatus[stat][1]+'</a>');
	operateStatus.append(status);
	
	operateStatus.append('<div class="publish-time">'+time+dateStr[stat]+'</div>');
	 
	
	li.append(operateStatus);
	
	return li;
}

var createStructureProduct2 = function(title, stat, time, rates, due, totalamount, doneamount, investamount){
	var li = $('<li></li>').addClass('clearfix');
	var productStatus = $('<div></div>').addClass('product-status');
	productStatus.append('<p class="product-name"><a style="text-decoration:none;">'+title+'[结构化]</a></p>');

	var productInfoWrap = $('<div></div>').addClass("product-info-wrap clearfix");
	
	var dtop = $('<div style="width:100%;"></div>');
	
	
	dtop.append('<p class="product-info-l"><span class="interest-rate">投资期限'+due+'</span></p>');
	dtop.append('<p class="product-info-l"><span class="invest-price">项目本金：'+totalamount+'元</span></p>');
	dtop.append('<p class="product-info-r"><span class="remain-price">已融资金额：'+doneamount+'元</span></p>');
	dtop.append('<div style="clear:both;"></div>');
	
	productInfoWrap.append(dtop);
	
	
	var a = $('<a href="javascript: void(0);" class="append btns">展开</a>');
	productInfoWrap.append($('<div style="width:100%; padding-top:5px; text-align: center;"></div>').append(a));
	
	productStatus.append(productInfoWrap);
	li.append(productStatus);
	
	var operateStatus = $('<div></div>').addClass("operate-status");
	
	var status = $('<div></div>').addClass(cssStatus[stat]+" clearfix");
	var currentAmount = $('<div></div>').addClass("current-amount");
	
	
	currentAmount.append('<strong>可投金额</strong><span class="cur">'+investamount+'</span>');
	status.append(currentAmount);
	
	var str = stat=='online'?buttonStatus[stat][1]+'中':buttonStatus[stat][1];
	var aa = $('<a class="'+buttonStatus[stat][0]+'">'+str+'</a>');
	status.append(aa);
	operateStatus.append(status);
	
	operateStatus.append('<div class="publish-time">'+time+dateStr[stat]+'</div>');
	 
	
	li.append(operateStatus);
	
	li.append('<div style="clear:both;"></div>');
	
	
	var table = $('<table width=100%></table>');
	var tr = $('<tr></tr>');
	table.append(tr);
	var append_text = $($('<div class="append_text" style="margin-top:10px; width:100%; display:none;"></div>').append(table));
	
	
	var pro1 = $('<div></div>').addClass("operate-status");
	var td1 = $('<td></td>');
	tr.append(td1);
	var pro1_status = $('<div></div>').addClass(cssStatus[stat]+" clearfix");
	var pro1_currentAmount = $('<div></div>').addClass("current-amount");
	
	
	pro1_currentAmount.append('<strong>进取型<span class="cur">VIP</span></strong>年化利率<span class="cur">24%</span><br>期限未定<br>到期还本付息');
	pro1_status.append(pro1_currentAmount);
	pro1_status.append('<a href="productdetail3.html" class="'+buttonStatus[stat][0]+'">'+buttonStatus[stat][1]+'</a>');
	pro1.append(pro1_status);
	td1.append(pro1);
	
	
	var pro2 = $('<div></div>').addClass("operate-status");
	var td2 = $('<td></td>');
	tr.append(td2);
	var pro2_status = $('<div></div>').addClass(cssStatus[stat]+" clearfix");
	var pro2_currentAmount = $('<div></div>').addClass("current-amount");
	
	
	pro2_currentAmount.append('<strong>均衡型</strong>年华利率<span class="cur">15%</span><br>固定期限<br>按月摊还本息');
	pro2_status.append(pro2_currentAmount);
	pro2_status.append('<a href="productdetail3.html" class="'+buttonStatus[stat][0]+'">'+buttonStatus[stat][1]+'</a>');
	pro2.append(pro2_status);
	td2.append(pro2);
	
	
	
	
	var pro3 = $('<div></div>').addClass("operate-status");
	var td3 = $('<td></td>');
	tr.append(td3);
	var pro3_status = $('<div></div>').addClass(cssStatus[stat]+" clearfix");
	var pro3_currentAmount = $('<div></div>').addClass("current-amount");
	
	
	pro3_currentAmount.append('<strong>稳健型</strong>年华利率<span class="cur">10%</span><br>有担保<br>按月还息，到期还本');
	pro3_status.append(pro3_currentAmount);
	pro3_status.append('<a href="productdetail3.html" class="'+buttonStatus[stat][0]+'">'+buttonStatus[stat][1]+'</a>');
	pro3.append(pro3_status);
	td3.append(pro3);
	
	
	
	
	
	
	li.append(append_text);
	
	a.click(function(e){
		var displayText = append_text.css('display');
		if(displayText=='none'){
			if(stat=='online')
				{
			aa.removeClass(buttonStatus[stat][0]);
			aa.addClass('btns-grey');
				}
			$(this).removeClass(buttonStatus[stat][0]);
			$(this).addClass('btns-grey');
			$(this).html('收起');
			append_text.css('display', 'block');
			li.css('border', 'red solid 2px');
		}else{
			if(stat=='online')
			{
			aa.removeClass('btns-grey');
			aa.addClass(buttonStatus[stat][0]);
			}
			$(this).removeClass('btns-grey');
			$(this).addClass(buttonStatus[stat][0]);
			$(this).html('展开');
			append_text.css('display', 'none');
			li.css('border', '#d9d9d9 1px solid');
		}
	});
	
	return li;
}


var createSingleProduct = function(title, stat, time, pl, rate, due, totalamount, remainamount, investamount){
	var li = $('<li></li>').addClass('clearfix');
	var productStatus = $('<div></div>').addClass('product-status');
	productStatus.append('<p class="product-name"><a href="productdetail3.html">'+title+'['+pl+']</a></p>');

	var productInfoWrap = $('<div></div>').addClass("product-info-wrap clearfix");
	productInfoWrap.append('<p class="product-info-l"><span class="interest-rate">预期年化利率：'+rate+'</span><br>');
	productInfoWrap.append('<p class="product-info-l"><span class="invest-price">投资期限：'+due+'</span></p>');
	productInfoWrap.append('<p class="product-info-r"><span class="invest-price">项目本金：'+totalamount+'元</span></p>');
	productStatus.append(productInfoWrap);
	li.append(productStatus);
	
	var operateStatus = $('<div></div>').addClass("operate-status");
	
	
	var status = $('<div></div>').addClass(cssStatus[stat]+" clearfix");
	var currentAmount = $('<div></div>').addClass("current-amount");
	
	
	currentAmount.append('<strong>剩余可投金额</strong><span class="cur">'+remainamount+'</span> 元');
	status.append(currentAmount);
	status.append('<a href="productdetail3.html" class="'+buttonStatus[stat][0]+'">'+buttonStatus[stat][1]+'</a>');
	operateStatus.append(status);
	
	operateStatus.append('<div class="publish-time">'+time+dateStr[stat]+'</div>');
	 
	
	li.append(operateStatus);
	
	return li;
}

var createTotalIntro = function(){
	var p = '<p>预期年化利率 ：<span style="color:#ff0000;">6.0%~24.0% </span>&nbsp; &nbsp; &nbsp; &nbsp;同一个订单：不同的融资产品  &nbsp; &nbsp; &nbsp; &nbsp;投资期限： 3-12个月 &nbsp; &nbsp; &nbsp; &nbsp;灵活的收益方式 &nbsp; &nbsp; &nbsp;<a href="plIntroduction.html" target="_blank">了解详情</a></p>';
	return p;
}
var createWJIntro = function(){
	var p = '<p>预期年化利率 ：<span style="color:#ff0000;">6.0%~12.0% </span>&nbsp; &nbsp; &nbsp; &nbsp;担保机构担保 &nbsp; &nbsp; &nbsp; &nbsp;投资期限： 3-6个月 &nbsp; &nbsp; &nbsp; &nbsp;收益方式：按月还息，到期还本金 &nbsp; &nbsp; &nbsp;<a href="plIntroduction.html" target="_blank">了解详情</a></p>';
	return p;
}
var createJHIntro = function(){
	var p = '<p>预期年化利率 ：<span style="color:#ff0000;">10.0%~18.0% </span>&nbsp; &nbsp; &nbsp; &nbsp;高回款现金流 &nbsp; &nbsp; &nbsp; &nbsp;投资期限： 3-9个月 &nbsp; &nbsp; &nbsp; &nbsp;收益方式：按月摊还本息 &nbsp; &nbsp; &nbsp;<a href="plIntroduction.html" target="_blank">了解详情</a></p>';
	return p;
}
var createJQIntro = function(){
	var p = '<p>预期年化利率 ：<span style="color:#ff0000;">16.0%~24.0% </span>&nbsp; &nbsp; &nbsp; &nbsp;超高收益率 &nbsp; &nbsp; &nbsp; &nbsp;投资期限： 6-12个月 &nbsp; &nbsp; &nbsp; &nbsp;收益方式：按月还息，到期还本金 &nbsp; &nbsp; &nbsp;<a href="plIntroduction.html" target="_blank">了解详情</a></p>';
	return p;
}

var createNavLevel2 = function(nav){
	var btn_group = $('<div class="btn-group" style="margin-left:-15px;"></div>');
	if(nav=='myaccount'){
		var button1 = $('<button type="button" class="btn btn-default btn-lg">个人信息</button>');
		var button2 = $('<button type="button" class="btn btn-default btn-lg">我的积分</button>');
		var button3 = $('<button type="button" class="btn btn-default btn-lg">我的活动</button>');
		
		var btn_group2 = $('<div class="btn-group"></div>');
		btn_group2.append('<button type="button" class="btn btn-default dropdown-toggle btn-lg" data-toggle="dropdown">投资推荐<span class="caret"></span></button>');
		var ul = $('<ul class="dropdown-menu" role="menu"></ul>');
		ul.append('<li><a href="#">稳健型</a></li>');
		ul.append('<li><a href="#">均衡型</a></li>');
		ul.append('<li><a href="#">进取型</a></li>');
		btn_group2.append(ul);
		btn_group.append(button1);
		btn_group.append(button2);
		btn_group.append(button3);
		btn_group.append(btn_group2);
	}else if(nav=='submit'){
		var button1 = $('<button type="button" class="btn btn-default btn-lg">全部</button>');
		var button2 = $('<button type="button" class="btn btn-default btn-lg">待支付</button>');
		var button3 = $('<button type="button" class="btn btn-default btn-lg">待审核</button>');
		var button4 = $('<button type="button" class="btn btn-default btn-lg">还款中</button>');
		var button5 = $('<button type="button" class="btn btn-default btn-lg">还款完毕</button>');
		btn_group.append(button1);
		btn_group.append(button2);
		btn_group.append(button3);
		btn_group.append(button4);
		btn_group.append(button5);
	}else if(nav=='payback'){
		var button1 = $('<button type="button" class="btn btn-default btn-lg">已回款</button>');
		var button2 = $('<button type="button" class="btn btn-default btn-lg">待回款</button>');
		btn_group.append(button1);
		btn_group.append(button2);
	}else if(nav=='cash'){
		var button1 = $('<button type="button" class="btn btn-default btn-lg">全部</button>');
		var button2 = $('<button type="button" class="btn btn-default btn-lg">充值</button>');
		var button3 = $('<button type="button" class="btn btn-default btn-lg">提现</button>');
		var button4 = $('<button type="button" class="btn btn-default btn-lg">投标</button>');
		var button5 = $('<button type="button" class="btn btn-default btn-lg">回款</button>');
		btn_group.append(button1);
		btn_group.append(button2);
		btn_group.append(button3);
		btn_group.append(button4);
		btn_group.append(button5);
	}else if(nav=='tools'){
		var button1 = $('<button type="button" class="btn btn-default btn-lg">流动性分析</button>');
		var button2 = $('<button type="button" class="btn btn-default btn-lg">回款统计</button>');
		var button3 = $('<button type="button" class="btn btn-default btn-lg">风险分析</button>');
		var button4 = $('<button type="button" class="btn btn-default btn-lg">收益率统计</button>');
		btn_group.append(button1);
		btn_group.append(button2);
		btn_group.append(button3);
		btn_group.append(button4);
	}
	
	return btn_group;
}
