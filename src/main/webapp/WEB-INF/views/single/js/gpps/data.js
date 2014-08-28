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
	row2_col1.html('<div class="seemall"><a href="productlist.html" title="产品列表" target="_blank"></a></div>');
	
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


var createSingleProduct = function(title, stat, time, pl, rate, due, totalamount, remainamount, investamount){
	var li = $('<li></li>').addClass('clearfix');
	var productStatus = $('<div></div>').addClass('product-status');
	productStatus.append('<p class="product-name"><a href="productdetail.html"  target="_blank">'+title+'['+pl+']</a></p>');

	var productInfoWrap = $('<div></div>').addClass("product-info-wrap clearfix");
	productInfoWrap.append('<p class="product-info-l"><span class="interest-rate">预期年化利率：'+rate+'</span><br>');
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
