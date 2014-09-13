_$fd = function(longt) {
		var date = null;
		if (typeof longt == 'number') {
			date = new Date(longt);
		} else if (longt instanceof Date) {
			date = longt;
		} else {
			return null;
		}
		var yyyy = date.getFullYear();
		var MM = date.getMonth() + 1 >= 10 ? date.getMonth() + 1 : '0' + (date.getMonth() + 1);
		var dd = date.getDate() >= 10 ? date.getDate() : '0' + date.getDate();
		var HH = date.getHours() >= 10 ? date.getHours() : '0' + date.getHours();
		var mm = date.getMinutes() >= 10 ? date.getMinutes() : '0' + date.getMinutes();
		var ss = date.getSeconds() >= 10 ? date.getSeconds() : '0' + date.getSeconds();
		return {
			'yyyy' : yyyy,
			'MM' : MM,
			'dd' : dd,
			'HH' : HH,
			'mm' : mm,
			'ss' : ss
		}
	};

	formatDateToDay = function(longt) {
		//yyyy-MM-dd
		var r = _$fd(longt);
		var ldStr = r.yyyy + '-' + r.MM + '-' + r.dd;
		return ldStr;
	};
	
	formatDate = function(longt) {
		//yyyy-MM-dd T HH:mm:ss
		var r = _$fd(longt);
		var ldStr = r.yyyy + '-' + r.MM + '-' + r.dd + ' T ' + r.HH + ':' + r.mm + ':' + r.ss;
		return ldStr;
	};