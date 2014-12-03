

function header(title){
	var divtitle = $('<div style="display:inline;"></div>');
	divtitle.append('<h2 style="float:left;" class="text-muted">春蕾投资<small>&nbsp;&nbsp;信用创造价值</small></h2>');
	var divusercontent = $('<div id="usercontent" style="float:right; font-size:14px; color:#333; padding-top:30px;"></div>');
	
	var divright = $('<div id="divright" style="min-width:150px; float:right;"></div>');
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
	
	
	var navulstr = '<nav class="navbar navbar-default" role="navigation">';
	navulstr += '<div class="container-fluid">';
	navulstr += '<div class="navbar-header">';
	navulstr += '<button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">';
	navulstr += '<span class="sr-only">Toggle navigation</span>';
	navulstr += '<span class="icon-bar"></span>';
	navulstr += '<span class="icon-bar"></span>';
	navulstr += '<span class="icon-bar"></span>';
	navulstr += '</button>';
	navulstr += '<span class="navbar-brand" href="#">政采贷</span>';
	navulstr += '</div>';
	navulstr += '<div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">';
	navulstr += '<ul class="nav navbar-nav">';
	navulstr += '<li id="index" class="active"><a href="index.html">首页</a></li>';
	navulstr += '<li id="productlist"><a href="productlist.html">我要理财</a></li>';
	navulstr += '<li id="myaccount"><a href="myaccount.html">我的账户</a></li>';
	navulstr += '<li id="activity"><a href="activity.html">活动中心</a></li>';
	navulstr += '<li id="loan"><a href="loan.html">我要融资</a></li>';
	navulstr += '</ul>';
	navulstr += '<ul class="nav navbar-nav navbar-right">';
	navulstr += '<li><a href="#">导航</a></li>';
	navulstr += '<li class="dropdown">';
	navulstr += '<a href="#" class="dropdown-toggle" data-toggle="dropdown">帮助<span class="caret"></span></a>';
	navulstr += '<ul class="dropdown-menu" role="menu">';
	navulstr += '<li><a href="#">新手帮助</a></li>';
	navulstr += '<li><a href="#">平台公告</a></li>';
	navulstr += '<li><a href="#">新闻资讯</a></li>';
	navulstr += '<li class="divider"></li>';
	navulstr += '<li><a href="#">常见问题</a></li>';
	navulstr += '<li class="divider"></li>';
	navulstr += '<li><a href="#">等等等等</a></li>';
	navulstr += '</ul>';
	navulstr += '</li>';
	navulstr += '</ul>';
	navulstr += '</div><!-- /.navbar-collapse -->';
	navulstr += '</div><!-- /.container-fluid -->';
	navulstr += '</nav>';
	
	
		
	  
	      
	      
	    

	    
	      
	        
	        
	        
	        
	        
	        
	      
	      
	        
	        
	          
	          
	            
	            
	            
	            
	            
	            
	            
	          
	        
	      
	    
	  
	
	
	
	
	
	
	
	var navDiv = $('<div class="col-md-12" style="padding-left:0px; padding-right:0px;"></div>');
	navDiv.append(navulstr);
	
	$('div#header').html('');
	$('#header').append(divtitle);
	$('#header').append(navDiv);
	$('li').removeClass('active');
	$('li[id="'+title+'"]').addClass('active');
}