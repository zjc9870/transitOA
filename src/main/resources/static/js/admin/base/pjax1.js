var Pjax = function() {

	var init = function(aSelector, containerSelector, baseUrl, defaultUrl,
			startFunction, endFunction) {
		var $a = $(aSelector);
		$a.unbind("click");
		$a.bind("click", function() {
			var $current = $(this);
			if (startFunction) {
				startFunction($current);
			}
			var url = $(this).data("url");
			if(!url){
				return;
			}
			if(url==''){
				url=defaultUrl;
			}
			var promise = $.ajax({
				url : url,
				type : "get",
				dataType : "html"
			});
			promise.then(function(html) {
				$(containerSelector).html(html);
				pushPage(url, baseUrl);
				if (endFunction) {
					endFunction($current);
				}
			});
		});
	}

	var refresh = function(containerSelector, baseUrl, defaultUrl, endFunction) {
		var url = window.location.href;
		var index = url.indexOf(baseUrl);
		url = url.substring(index + baseUrl.length + 1, url.length);
		url = $.base64.decode(url);
		if(url==''){
			url=defaultUrl;
		}
		var promise = $.ajax({
			url : url,
			type : "get",
			dataType : "html"
		});
		promise.then(function(html) {
			$(containerSelector).html(html);
			if (endFunction) {
				endFunction(url);
			}
		});
	}

	var onpopstate = function(containerSelector, baseUrl, defaultUrl, endFunction) {
		window.addEventListener("popstate", function() {
			var currentState = history.state;
			if (currentState) {
				var index = currentState.indexOf(baseUrl);
				currentState = currentState.substring(index + baseUrl.length + 1, currentState.length);
				currentState = $.base64.decode(currentState);
				var promise = $.ajax({
					url : currentState,
					type : "get",
					dataType : "html"
				});
				promise.then(function(html) {
					$(containerSelector).html(html);
					if (endFunction) {
						endFunction(currentState);
					}
				});
			} else {
				var currentUrl = window.location.href;
				var index = currentUrl.indexOf(baseUrl);
				if (index >= 0) {
					currentUrl = currentUrl
							.substring(0, index + baseUrl.length);
				}
				window.location.href = currentUrl;
			}
		});
	}

	function pushPage(url, baseUrl) {
		url = $.base64.encode(url, true);
		var currentUrl = window.location.href;
		var index = currentUrl.indexOf(baseUrl);
		currentUrl = currentUrl.substring(0, index + baseUrl.length);
		currentUrl = currentUrl + "?" + url;
		history.pushState(currentUrl, null, currentUrl);
	}

	return {
		init : function(aSelector, containerSelector, baseUrl, defaultUrl, startFunction,
				endFunction) {
			init(aSelector, containerSelector, baseUrl, defaultUrl, startFunction,
					endFunction);
		},
		refresh : function(baseUrl, defaultUrl, containerSelector, endFunction) {
			refresh(baseUrl, defaultUrl, containerSelector, endFunction);
		},
		onpopstate : function(baseUrl, defaultUrl, containerSelector, endFunction) {
			onpopstate(baseUrl, defaultUrl, containerSelector, endFunction);
		}
	}
}();