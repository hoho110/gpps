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

