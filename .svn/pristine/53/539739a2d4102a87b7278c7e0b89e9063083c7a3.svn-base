var AjaxTool = function() {

	var get = function(url, data, successFunction) {
		var promise = $.ajax({
			url : url,
			data : data,
			dataType : "json",
			type : "get"
		});
		promise.then(function(response) {
			if(response.code && response.code == '100'){
				Toast.show("提醒",response.message);
			}else{
				if (successFunction != null) {
					successFunction(response);
				}
			}
		});
	}

	var getHtml = function(url, successFunction) {
		var promise = $.ajax({
			url : url,
			dataType : "html",
			type : "get"
		});
		promise.then(function(response) {
			if(response.code && response.code == '100'){
				Toast.show("提醒",response.message);
			}else{
				if (successFunction != null) {
					successFunction(response);
				}
			}
		});
	}

	var post = function(url, data, successFunction) {
		var promise = $.ajax({
			url : url,
			data : data,
			dataType : "json",
			type : "post"
		});
		promise.then(function(response) {
			if(response.code && response.code == '100'){
				Toast.show("提醒",response.message);
			}else{
				if (successFunction != null) {
					successFunction(response);
				}
			}
		});
	}
	
	var html = function(url, data, successFunction) {
		var promise = $.ajax({
			url : url,
			data : data,
			dataType : "html",
			type : "get"
		});
		promise.then(function(html) {
			try{
	            var jsonObject = JSON.parse(html);
	            if(jsonObject.code && jsonObject.code == '100'){
					Toast.show("提醒",jsonObject.message);
				}
	    	}catch(e){
	    		if (successFunction != null) {
					successFunction(html);
				}
	    	}
		});
	}

	return {
		get : get,
		getHtml : getHtml,
		post : post,
		html : html
	}
}();
