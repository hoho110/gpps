var service = EasyServiceClient.getRemoteProxy("/easyservice/gpps.service.ILenderService");
var cuser = service.getCurrentUser();
function header(title){
	var divtitle = $('<div style="display:inline;"></div>');
	divtitle.append('<h2 style="float:left;" class="text-muted">政采贷<small>&nbsp;&nbsp;信用创造价值</small></h2>');
	var divusercontent = $('<div id="usercontent" style="float:right; font-size:16px; padding-top:30px;"></div>');
	
	if(title=='login' || title=='register'){
		if(cuser!=null)
		{
			window.location.href="myaccount.html";
		}
		else
			{
		divusercontent.html('');
			}
	}else if(title=='myaccount'){
		divusercontent.html('');
	}
	else{
	
	if(cuser==null){
		divusercontent.html('<a href="login.html">登陆</a><span>&nbsp;|&nbsp;</span><a href="register.html">注册</a>');
	}else{
		divusercontent.html('下午好：'+cuser.loginId+'&nbsp;&nbsp;|&nbsp;&nbsp;<a href="myaccount.html">我的账户</a>'+'&nbsp;&nbsp;|&nbsp;&nbsp;<a href="quit.html">退出</a>');
	}
		}
	divtitle.append(divusercontent);
	divtitle.append('<div style="clear:both;"></div>');
	
	var navul = $('<ul class="nav nav-justified"></ul>');
	navul.append('<li id="index"><a class="active" href="index.html">首页</a></li>');
	navul.append('<li id="productlist"><a href="productlist.html">我要理财</a></li>');
	navul.append('<li id="myaccount"><a href="myaccount.html">我的帐户</a></li>');
	navul.append('<li id="activity"><a href="activity.html">活动中心</a></li>');
	navul.append('<li id="loan"><a href="loan.html">我要融资</a></li>');
	
	$('div#header').html('');
	$('#header').append(divtitle);
	$('#header').append(navul);
	
	$('li[id="'+title+'"]').addClass('active');
}