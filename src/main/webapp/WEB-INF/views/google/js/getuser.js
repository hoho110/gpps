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