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

	return {
		get : function(url, data, successFunction) {
			get(url, data, successFunction);
		},
		post : function(url, data, successFunction) {
			post(url, data, successFunction);
		}
	}
}();
