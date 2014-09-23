var createProductLine = function(cont, id, title, label1, des1, label2, des2, description, menkan, qixian, rate, style){
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
	
	
	var a = $('<a href="productlist.html?sid='+id+'" title="产品列表"></a>');
	row2_col1.append($('<div class="seemall"></div>').append(a));
	
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
		1 : 'online-status', 
		4  : 'done-status',
		2: 'done-status',
		8   : 'done-status'
	}
var buttonStatus = {
		1 : ['btns', '投资'], 
		4  : ['done-success','审核中'],
		2: ['done-success','还款中'],
		8   : ['done-success','还款成功']
}
var dateStr = {
		1 : '截止', 
		4  : '截止',
		2: '计息',
		8   : '关闭'
}

var createStructureProduct = function(title, stat, time, rates, due, totalamount, doneamount){
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
	
	
	currentAmount.append('<strong>可投金额</strong><span class="cur">'+(totalamount-doneamount)+'</span>');
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
	
	var num=0
	for(var name in rates){
		var pro1 = $('<div></div>').addClass("operate-status");
		var td1 = $('<td></td>');
		tr.append(td1);
		var pro1_status = $('<div></div>').addClass(cssStatus[stat]+" clearfix");
		var pro1_currentAmount = $('<div></div>').addClass("current-amount");
		
		var isVIP = (rates[name][1]>=3)?'<span class="cur">VIP</span>':'';
		pro1_currentAmount.append('<strong>'+name+isVIP+'</strong>年化利率<span class="cur">'+rates[name][0]+'</span><br>期限未定<br>到期还本付息');
		pro1_status.append(pro1_currentAmount);
		pro1_status.append('<a href="productdetail3.html" class="'+buttonStatus[stat][0]+'">'+buttonStatus[stat][1]+'</a>');
		pro1.append(pro1_status);
		td1.append(pro1);
		num++;
	}
	if(num==2){
		var pro = $('<div></div>').addClass("operate-status");
		var td = $('<td></td>');
		tr.append(td);
		var pro_status = $('<div style="height:50px;"></div>').addClass(cssStatus[stat]+" clearfix");
		td.append(pro);
		pro.append(pro_status);
	}
	
	
	
	
	
	
	
	
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
			window.parent.justify(document.body.scrollHeight);
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
			window.parent.justify(document.body.scrollHeight);
		}
	});
	
	return li;
}

var createProduct2 = function(order){
	var pros = order.products;
	if(pros.size()==1){
		return createSingleProduct2(order);
	}else if(pros.size()>1){
		return createStructureProduct2(order);
	}else
		{
		return null;
		}
}

var createStructureProduct2 = function(order){
	var pros = order.products;
	var state = order.state;
	var maxdays = 0;
	var mindays = 100000;
	
	
	var totalamount = 0;
	var realamount = 0;
	for(var i=0; i<pros.size(); i++){
		var days = parseInt((pros.get(i).incomeEndtime-order.incomeStarttime+23*3600*1000)/(24*3600*1000)+'');
		if(days>maxdays)
			{
			maxdays = days;
			}
		if(days<mindays){
			mindays = days;
		}
		totalamount += parseInt(pros.get(i).expectAmount.value);
		realamount += parseInt(pros.get(i).realAmount.value);
	}
	
	
	var str = '<li class="clearfix">';
	str    += '<div class="product-status">';
	str    += '<p class="product-name">';
	str    += '<a class="product-title constructure" href="#" id="'+order.id+'">'+order.title+'[结构化]'+'</a>';
	str    += '</p>';
	str    += '<div class="product-info-wrap clearfix">';
	str    += '<p class="product-info-l">';
	str    += '<span style="margin-right:26px;">投资期限：'+mindays+'-'+maxdays+'天</span>';
	str    += '<span>项目本金：'+totalamount+'元</span>';
	str    += '</p>';
	str    += '<p>';
	str    += '<span class="invest-price">已投金额：'+realamount+'元</span>';
	str    += '</p>';
	str    += '</div>';
	str    += '</div>';
	
	
	var status = '';
	var stateCls = '';
	var stateName = '';
	
	if(state==1){
		status = 'online-status';
		stateName = '投资中';
		stateCls = 'btns';
		str    += '<div class="operate-status '+status+' clearfix">';
		str    += '<div>';
		str    += '<div class="current-amount">';
		str    += '<strong>可投金额</strong>';
		str    += '<span class="cur">'+(totalamount- realamount)+'</span> 元            </div>';
		str    += '<a id='+order.id+' class="stat '+stateCls+'">'+stateName+'</a>';
		str    += '<div style="clear:both;"></div>';
		str    += '</div>';
		str    += '<div class="publish-time" style="margin-top:10px; padding-top:5px;">'+formatDateToDay(order.financingEndtime)+'截止</div>';
	}else if(state==2){
		status = 'done-status';
	stateName = '预发布';
	stateCls = 'btns-green';
		str    += '<div class="operate-status '+status+' clearfix">';
		str    += '<div>';
		str    += '<div class="current-amount">';
		str    += '<strong>可投金额</strong>';
		str    += '<span class="cur">'+(totalamount- realamount)+'</span> 元            </div>';
		str    += '<a id='+order.id+' class="stat '+stateCls+'">'+stateName+'</a>';
		str    += '<div style="clear:both;"></div>';
		str    += '</div>';
		str    += '<div class="publish-time" style="margin-top:10px; padding-top:5px;">'+formatDateToDay(order.financingStarttime)+'开始</div>';	
	}else if(state==4){
		status = 'done-status';
		stateName = '还款中';
		stateCls = 'btns-grey';
		str    += '<div class="operate-status '+status+' clearfix">';
		str    += '<div>';
		str    += '<div class="current-amount">';
		str    += '<strong>可投金额</strong>';
		str    += '<span class="cur">'+(totalamount- realamount)+'</span> 元           </div>';
		str    += '<a id='+order.id+' class="stat '+stateCls+'">'+stateName+'</a>';
		str    += '<div style="clear:both;"></div>';
		str    += '</div>';
		str    += '<div class="publish-time" style="margin-top:10px; padding-top:5px;">'+formatDateToDay(order.incomeStarttime)+'开始还款</div>';	
	}else if(state==8){
		status = 'done-status';
		stateName = '待关闭';
		stateCls = 'btns-grey';
		str    += '<div class="operate-status '+status+' clearfix">';
		str    += '<div>';
		str    += '<div class="current-amount">';
		str    += '<strong>可投金额</strong>';
		str    += '<span class="cur">'+(totalamount- realamount)+'</span> 元           </div>';
		str    += '<a id='+order.id+' class="stat '+stateCls+'">'+stateName+'</a>';
		str    += '<div style="clear:both;"></div>';
		str    += '</div>';
		str    += '<div class="publish-time" style="margin-top:10px; padding-top:5px;">'+formatDateToDay(order.lastModifytime)+'还款完毕</div>';	
	}else if(state==16){
		status = 'done-status';
		stateName = '流标';
		stateCls = 'btns-grey';
		str    += '<div class="operate-status '+status+' clearfix">';
		str    += '<div>';
		str    += '<div class="current-amount">';
		str    += '<strong>流标原因</strong>';
		str    += '融资额度未满  </div>';
		str    += '<a id='+order.id+' class="stat '+stateCls+'">'+stateName+'</a>';
		str    += '<div style="clear:both;"></div>';
		str    += '</div>';
		str    += '<div class="publish-time" style="margin-top:10px; padding-top:5px;">'+formatDateToDay(order.lastModifytime)+'流标</div>';	
	}else if(state==32){
		status = 'done-status';
		stateName = '已关闭';
		stateCls = 'btns-grey';
		str    += '<div class="operate-status '+status+' clearfix">';
		str    += '<div>';
		str    += '<div class="current-amount">';
		str    += '<strong>可投金额</strong>';
		str    += '<span class="cur">'+(totalamount- realamount)+'</span> 元           </div>';
		str    += '<a id='+order.id+' class="stat '+stateCls+'">'+stateName+'</a>';
		str    += '<div style="clear:both;"></div>';
		str    += '</div>';
		str    += '<div class="publish-time" style="margin-top:10px; padding-top:5px;">'+formatDateToDay(order.lastModifytime)+'关闭</div>';	
	}
	str    += '</div>';
	str    += '<div style="clear:both;"></div>';
	str    += '<div class="productinfo" style="width:100%; display:none;">';
	
	for(var i=0; i<pros.size(); i++){
		str    += '<div class="operate-status '+status+' clearfix">';
		str    += '<div>';
		str    += '<div class="current-amount">';
		str    += '<strong>'+seriesname[pros.get(i).productseriesId]+'</strong>';
		str    += '年华利率<span class="cur">'+parseInt(parseFloat(pros.get(i).rate.value)*100)+"%"+'</span><br>';
		str    +=  '预融资金'+pros.get(i).expectAmount.value+'元<br>';
		str    +=  '已融资金'+pros.get(i).realAmount.value+'元';
		str    +=  '</div>';
		if(state==1)
			{
				if(parseInt(pros.get(i).expectAmount.value)==parseInt(pros.get(i).realAmount.value))
					{
				str    += '<a href="productdetail.html?pid='+pros.get(i).id+'" class="btns">售罄</a>';
					}else{
				str    += '<a href="productdetail.html?pid='+pros.get(i).id+'" class="btns">投资</a>';
					}
			}else if(state==2){
				str    += '<a href="productdetail.html?pid='+pros.get(i).id+'" class="btns-green">预发布</a>';
			}
			else if(state==4){
				if(pros.get(i).state==8)
					str    += '<a href="productdetail.html?pid='+pros.get(i).id+'" class="btns-grey">还款完毕</a>';
				else
				str    += '<a href="productdetail.html?pid='+pros.get(i).id+'" class="btns-grey">还款中</a>';
			}else if(state==8){
				str    += '<a href="productdetail.html?pid='+pros.get(i).id+'" class="btns-grey">还款完毕</a>';
			}else if(state==16){
				str    += '<a href="productdetail.html?pid='+pros.get(i).id+'" class="btns-grey">流标</a>';
			}else if(state==32){
				str    += '<a href="productdetail.html?pid='+pros.get(i).id+'" class="btns-grey">已关闭</a>';
			}
		str    += '<div style="clear:both;"></div>';
		str    += '</div>';
		str    += '</div>';
	}
	
	str    += '</div>';
	str    += '</li>';
	
	return str;
}


var createSingleProduct2 = function(order){
	var pro = order.products.get(0);
	var state = order.state;
	var days = parseInt((pro.incomeEndtime-order.incomeStarttime+23*3600*1000)/(24*3600*1000)+'');
	
	
	var str = '<li class="clearfix">';
	str    += '<div class="product-status">';
	str    += '<p class="product-name">';
	str    += '<a class="product-title" href="productdetail.html?pid='+pro.id+'">'+order.title+'['+seriesname[pro.productseriesId]+']'+'</a>';
	str    += '</p>';
	str    += '<div class="product-info-wrap clearfix">';
	str    += '<p class="product-info-l">';
	str    += '<span style="margin-right:26px;">预期年化利率：'+parseInt(parseFloat(pro.rate.value)*100)+"%"+'</span>';
	str    += '<span>投资期限：'+days+'天</span>';
	str    += '</p>';
	str    += '<p>';
	str    += '<span class="invest-price">项目本金：'+pro.expectAmount.value+'元</span>';
	if(state==1 || state==2)
		{
	str    += '<span>剩余可投金额：'+(parseInt(pro.expectAmount.value)- parseInt(pro.realAmount.value))+'元</span>';
		}else{
	str    += '<span>剩余可投金额：0元</span>';
		}
	str    += '</p>';
	str    += '</div>';
	str    += '</div>';
	
	if(state==1){
		str    += '<div class="operate-status online-status clearfix">';
		str    += '<div>';
		str    += '<div class="current-amount">';
		str    += '<strong>投资金额</strong>';
		str    += '<span class="cur">'+pro.minimum+'</span> 元起            </div>';
		if(parseInt(pro.expectAmount.value)==parseInt(pro.realAmount.value))
			str    += '<a href="productdetail.html?pid='+pro.id+'" class="btns">售罄</a>';
		else
			str    += '<a href="productdetail.html?pid='+pro.id+'" class="btns">投资中</a>';
		str    += '<div style="clear:both;"></div>';
		str    += '</div>';
		str    += '<div class="publish-time" style="margin-top:10px; padding-top:5px;">'+formatDateToDay(order.financingEndtime)+'截止</div>';
	}else if(state==2){
		str    += '<div class="operate-status done-status clearfix">';
		str    += '<div>';
		str    += '<div class="current-amount">';
		str    += '<strong>投资金额</strong>';
		str    += '<span class="cur">'+pro.minimum+'</span> 元起            </div>';
		str    += '<a href="productdetail.html?pid='+pro.id+'" class="btns-green">预发布</a>';
		str    += '<div style="clear:both;"></div>';
		str    += '</div>';
		str    += '<div class="publish-time" style="margin-top:10px; padding-top:5px;">'+formatDateToDay(order.financingStarttime)+'开始</div>';	
	}else if(state==4){
		str    += '<div class="operate-status done-status clearfix">';
		str    += '<div>';
		str    += '<div class="current-amount">';
		str    += '<strong>已融金额</strong>';
		str    += '<span class="cur">'+pro.realAmount.value+'</span> 元           </div>';
		str    += '<a href="productdetail.html?pid='+pro.id+'" class="btns-grey">还款中</a>';
		str    += '<div style="clear:both;"></div>';
		str    += '</div>';
		str    += '<div class="publish-time" style="margin-top:10px; padding-top:5px;">'+formatDateToDay(order.incomeStarttime)+'开始还款</div>';	
	}else if(state==8){
		str    += '<div class="operate-status done-status clearfix">';
		str    += '<div>';
		str    += '<div class="current-amount">';
		str    += '<strong>已融金额</strong>';
		str    += '<span class="cur">'+pro.realAmount.value+'</span> 元           </div>';
		str    += '<a href="productdetail.html?pid='+pro.id+'" class="btns-grey">待关闭</a>';
		str    += '<div style="clear:both;"></div>';
		str    += '</div>';
		str    += '<div class="publish-time" style="margin-top:10px; padding-top:5px;">'+formatDateToDay(pro.financingEndtime)+'还款完毕</div>';	
	}else if(state==16){
		str    += '<div class="operate-status done-status clearfix">';
		str    += '<div>';
		str    += '<div class="current-amount">';
		str    += '<strong>流标</strong>';
		str    += '</div>';
		str    += '<a href="productdetail.html?pid='+pro.id+'" class="btns-grey">流标</a>';
		str    += '<div style="clear:both;"></div>';
		str    += '</div>';
		str    += '<div class="publish-time" style="margin-top:10px; padding-top:5px;">'+formatDateToDay(order.lastModifytime)+'流标</div>';	
	}else if(state==32){
		str    += '<div class="operate-status done-status clearfix">';
		str    += '<div>';
		str    += '<div class="current-amount">';
		str    += '<strong>已融金额</strong>';
		str    += '<span class="cur">'+pro.realAmount.value+'</span> 元           </div>';
		str    += '<a href="productdetail.html?pid='+pro.id+'" class="btns-grey">已关闭</a>';
		str    += '<div style="clear:both;"></div>';
		str    += '</div>';
		str    += '<div class="publish-time" style="margin-top:10px; padding-top:5px;">'+formatDateToDay(order.lastModifytime)+'关闭</div>';	
	}
	str    += '</div>';
	str    += '<div style="clear:both;"></div>';
	str    += '</li>';
	return str;
}


var createSingleProduct = function(title, stat, time, pl, rate, due, totalamount, remainamount){
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
	
	
	currentAmount.append('<strong>剩余可投金额</strong><span class="cur">'+(parseInt(totalamount)-parseInt(remainamount))+'</span> 元');
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
	var ul = $('<ul class="nav nav-tabs" style="float:right;" role="tablist"></ul>');
	if(nav=='myaccount'){
		var li2 = $('<li role="presentation" class="active"><a href="#" data-sk="my-score">积分等级</a></li>');
		var li3 = $('<li role="presentation"><a href="#" data-sk="my-activity">我的活动</a></li>');
		
		var li5 = $('<li role="presentation"><a href="#" data-sk="my-note">系统通知</a></li>');
		
		
	//	var li4 = $('<li role="presentation" class="dropdown"></li>');
	//	li4.append('<a class="dropdown-toggle" data-toggle="dropdown" data-sk="recommend" href="#">投资推荐<span class="caret"></span></a>');
	//	var ul2 = $('<ul class="dropdown-menu" role="menu"></ul>');
	//	ul2.append('<li><a href="#" data-sk="recommend-wj">稳健型</a></li>');
	//	ul2.append('<li><a href="#" data-sk="recommend-jh">均衡型</a></li>');
	//	ul2.append('<li><a href="#" data-sk="recommend-jq">进取型</a></li>');
	//	li4.append(ul2);
		ul.append(li2);
		ul.append(li3);
	//	ul.append(li4);
		ul.append(li5);
	}else if(nav=='submit'){
		var li1 = $('<li role="presentation" class="active"><a href="#" data-sk="submit-all">全部</a></li>');
		var li2 = $('<li role="presentation"><a href="#" data-sk="submit-toafford">待支付</a></li>');
//		var li3 = $('<li role="presentation"><a href="#" data-sk="submit-toaudit">待审核</a></li>');
		var li4 = $('<li role="presentation"><a href="#" data-sk="submit-payback">还款中</a></li>');
		var li5 = $('<li role="presentation"><a href="#" data-sk="submit-done">还款完毕</a></li>');
		ul.append(li1);
		ul.append(li2);
//		ul.append(li3);
		ul.append(li4);
		ul.append(li5);
	}else if(nav=='payback'){
		var li1 = $('<li role="presentation" class="active"><a href="#" data-sk="payback-all">总览</a></li>');
		var li2 = $('<li role="presentation"><a href="#" data-sk="payback-to">待回款</a></li>');
		var li3 = $('<li role="presentation"><a href="#" data-sk="payback-have">已回款</a></li>');
		ul.append(li1);
		ul.append(li2);
		ul.append(li3);
	}else if(nav=='cash'){
		var li1 = $('<li role="presentation" class="active"><a href="#" data-sk="cash-all">全部</a></li>');
		var li2 = $('<li role="presentation"><a href="#" data-sk="cash-recharge">充值</a></li>');
		var li3 = $('<li role="presentation"><a href="#" data-sk="cash-withdraw">提现</a></li>');
		var li4 = $('<li role="presentation"><a href="#" data-sk="cash-invest">投标</a></li>');
		var li5 = $('<li role="presentation"><a href="#" data-sk="cash-receive">回款</a></li>');
		ul.append(li1);
		ul.append(li2);
		ul.append(li3);
		ul.append(li4);
		ul.append(li5);
	}else if(nav=='tools'){
		var li1 = $('<li role="presentation" class="active"><a href="#" data-sk="tools-fluxility">流动性分析</a></li>');
		var li2 = $('<li role="presentation"><a href="#" data-sk="tools-receive-statistics">回款统计</a></li>');
		var li3 = $('<li role="presentation"><a href="#" data-sk="tools-analysis">风险分析</a></li>');
		var li4 = $('<li role="presentation"><a href="#" data-sk="tools-rate">收益率统计</a></li>');
		ul.append(li1);
		ul.append(li2);
		ul.append(li3);
		ul.append(li4);
	}
	
	return ul;
}
