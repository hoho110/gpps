var service = EasyServiceClient.getRemoteProxy("/easyservice/gpps.service.ILenderService");
var bservice = EasyServiceClient.getRemoteProxy("/easyservice/gpps.service.IBorrowerService");
var letterDao = EasyServiceClient.getRemoteProxy("/easyservice/gpps.dao.ILetterDao");
var lettercount = 0;

var cuser = null;
try{
cuser = service.getCurrentUser();
if(cuser!=null)
lettercount = letterDao.countByReceiver(0, 0, cuser.id);
}catch(e){
	
}

var buser = null;

if(cuser==null){
try{
buser = bservice.getCurrentUser();
if(buser!=null)
lettercount = letterDao.countByReceiver(0, 1, cuser.id);
}catch(e){
	
}
}