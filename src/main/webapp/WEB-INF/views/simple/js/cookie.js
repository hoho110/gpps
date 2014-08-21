function saveCookies(name, value, i){
	var cookietime = new Date(); 
	cookietime.setTime(date.getTime() + (i * 60 * 1000));//coockie保存i分钟
	$.cookie(name, value,{expires:cookietime}); 
}
function readCookies(name){
	return $.cookie(name);
}