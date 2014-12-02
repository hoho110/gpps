

function header(title){
	var divtitle = $('<div style="display:inline;"></div>');
	divtitle.append('<h2 style="float:left;" class="text-muted">政采贷<small>&nbsp;&nbsp;信用创造价值</small></h2>');
	var divhelp = $('<div id="help" style="float:left; margin-right:20px; padding-top:30px;"><a href="intro/help.html" target="_blank">帮助中心</a></div>');
	var divusercontent = $('<div id="usercontent" style="float:right; font-size:14px; color:#333; padding-top:30px;"></div>');
	
	var divright = $('<div id="divright" style="min-width:150px; float:right;"></div>');
	divright.append(divhelp);
	divright.append(divusercontent);
	divright.append('<div style="clear:both;"></div>');
	
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
	}else if(title=='loan'){
		if(cuser!=null){
			window.location.href="myaccount.html";
		}else if(buser!=null){
			divusercontent.html(greet()+"企业用户"+buser.loginId+'&nbsp;|&nbsp;信用级别:<a href="baccount.html?fid=bcenter">level'+buser.level+'</a>&nbsp;&nbsp;|&nbsp;&nbsp;<a href="baccount.html">我的账户</a>'+'&nbsp;&nbsp;|&nbsp;&nbsp;<a href="quit.html">退出</a>');
		}else{
			divusercontent.html('<a href="login.html">登陆</a><span>&nbsp;|&nbsp;</span><a href="register.html">注册</a>');
		}
	}
	else{
	if(cuser!=null){
		divusercontent.html(greet()+cuser.loginId+'&nbsp;|&nbsp;会员级别:<a href="myaccount.html?fid=mycenter">level'+cuser.level+'</a>&nbsp;&nbsp;|&nbsp;&nbsp;<a href="myaccount.html">我的账户</a>'+'&nbsp;&nbsp;|&nbsp;&nbsp;<a href="quit.html">退出</a>');
	}else if(buser!=null){
		divusercontent.html(greet()+"企业用户"+buser.loginId+'&nbsp;|&nbsp;信用级别:<a href="baccount.html?fid=bcenter">level'+buser.level+'</a>&nbsp;&nbsp;|&nbsp;&nbsp;<a href="baccount.html">我的账户</a>'+'&nbsp;&nbsp;|&nbsp;&nbsp;<a href="quit.html">退出</a>');
	}else{
		divusercontent.html('<a href="login.html">登陆</a><span>&nbsp;|&nbsp;</span><a href="register.html">注册</a>');
	}
		}
	divtitle.append(divright);
	divtitle.append('<div style="clear:both;"></div>');
	
	var navul = $('<ul class="nav nav-justified"></ul>');
	navul.append('<li id="index"><a class="active" href="index.html">首页</a></li>');
	navul.append('<li id="productlist"><a href="productlist.html">我要理财</a></li>');
	navul.append('<li id="myaccount"><a href="myaccount.html">我的帐户</a></li>');
	navul.append('<li id="activity"><a href="activity.html">活动中心</a></li>');
	navul.append('<li id="loan"><a href="loan.html">我要融资</a></li>');
	
	var navDiv = $('<div class="hidden-sm hidden-xs col-md-12" style="padding-left:0px; padding-right:0px;"></div>');
	navDiv.append(navul);
	
	$('div#header').html('');
	$('#header').append(divtitle);
	$('#header').append(navDiv);
	
	var navSMDiv = $('<div class="navbar navbar-fixed-top"></div>');
	navSMDiv.append('<a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse"><span class="icon-bar"></span><span class="icon-bar"></span><span class="icon-bar"></span></a>');
	var navuldiv = $('<div class="nav-collapse collapse"></div>');
	var navsmul = $('<ul id="nav-list" class="nav pull-right"></ul>');
	navsmul.append('<li><a href="#home">活动概况</a></li>');
	navsmul.append('<li><a href="#about">详细介绍</a></li>');
	navsmul.append('<li><a href="#updates">行程安排</a></li>');
	navsmul.append('<li><a href="#rule">规则介绍</a></li>');
	navsmul.append('<li><a href="#screenshots">相关图片</a></li>');
	navsmul.append('<li><a href="#contact">我要报名</a></li>');
	navuldiv.append(navsmul);
	navSMDiv.append(navuldiv);
	
	$('#header').append(navSMDiv);
	
	$('li[id="'+title+'"]').addClass('active');
}