var AjaxTool = function() {

	var get = function(url, data, successFunction) {
		var promise = $.ajax({
			url : url,
			data : data,
			dataType : "json",
			type : "get"
		});
		promise.then(function(response) {
			if (successFunction != null) {
				successFunction(response);
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
			if (successFunction != null) {
				successFunction(response);
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
			if (successFunction != null) {
				successFunction(response);
			}
		});
	}
	
	var html = function(url, data, successFunction) {
		var promise = $.ajax({
			url : url,
			data : data,
			dataType : "html",
			type : "post"
		});
		promise.then(function(response) {
			if (successFunction != null) {
				successFunction(response);
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
