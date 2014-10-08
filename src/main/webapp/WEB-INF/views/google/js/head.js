var service = EasyServiceClient.getRemoteProxy("/easyservice/gpps.service.ILenderService");
var bservice = EasyServiceClient.getRemoteProxy("/easyservice/gpps.service.IBorrowerService");

var cuser = null;
try{
cuser = service.getCurrentUser();
}catch(e){
	
}

var buser = null;

if(cuser==null){
try{
buser = bservice.getCurrentUser();
}catch(e){
	
}
}

function header(title){
	var divtitle = $('<div style="display:inline;"></div>');
	divtitle.append('<h2 style="float:left;" class="text-muted">政采贷<small>&nbsp;&nbsp;信用创造价值</small></h2>');
	var divusercontent = $('<div id="usercontent" style="float:right; font-size:14px; color:#333; padding-top:30px;"></div>');
	
	if(title=='login' || title=='register'){
		if(cuser!=null)
		{
			window.location.href="myaccount.html";
		}else if(buser!=null){
			window.location.href="index.html";
		}
		else
			{
		divusercontent.html('');
			}
	}else if(title=='myaccount'){
		divusercontent.html('');
	}
	else{
	if(cuser!=null){
		divusercontent.html(greet()+cuser.loginId+'&nbsp;|&nbsp;会员级别:<a href="myaccount.html?fid=mycenter">level'+cuser.level+'</a>&nbsp;&nbsp;|&nbsp;&nbsp;<a href="myaccount.html">我的账户</a>'+'&nbsp;&nbsp;|&nbsp;&nbsp;<a href="quit.html">退出</a>');
	}else if(buser!=null){
		divusercontent.html(greet()+"企业用户"+buser.loginId+'&nbsp;|&nbsp;信用级别:<a href="#">level'+buser.creditValue+'</a>&nbsp;&nbsp;|&nbsp;&nbsp;<a href="#">我的账户</a>'+'&nbsp;&nbsp;|&nbsp;&nbsp;<a href="quit.html">退出</a>');
	}else{
		divusercontent.html('<a href="login.html">登陆</a><span>&nbsp;|&nbsp;</span><a href="register.html">注册</a>');
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