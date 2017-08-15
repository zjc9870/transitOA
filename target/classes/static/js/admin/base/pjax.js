var Pjax = function() {

    var init = function(aSelector, containerSelector, baseUrl, startFunction, endFunction) {
        var $a = $(aSelector);
        $a.unbind("click");
        $a.bind("click",
        function() {
            var $current = $(this);
            if (startFunction) {
                startFunction($current);
            }
            var url = $(this).data("url");
            if (!url) {
                return;
            }
            if (url == '') {
            	return;
            }
            var promise = $.ajax({
                url: checkUrl(url),
                type: "get",
                dataType: "html"
            });
            promise.then(function(html, textStatus, jqXHR) {
            	if (!redirectTimeoutUrl(html,jqXHR)) {
           		 	$(containerSelector).html(html);
                    pushPage(url, baseUrl);
                    if (endFunction) {
                        endFunction($current);
                    }
               }
            });
            promise.then(function(){
            	console.info("error");
            });
            promise.then(function(){
            	console.info("complete");
            });
        });
    }

    var refresh = function(containerSelector, baseUrl, endFunction) {
        var url = getCurrentUrl(baseUrl);
        if (url == '') {
        	return;
        }
        var navUrl = getNavUrl(url);
        var requestUrl = getRequestUrl(url);
        var promise = $.ajax({
            url: checkUrl(requestUrl),
            type: "get",
            dataType: "html"
        });
        promise.then(function(html, textStatus, jqXHR) {
        	if (!redirectTimeoutUrl(html,jqXHR)) {
        		$(containerSelector).html(html);
                if (endFunction) {
                    if (navUrl) {
                        endFunction(navUrl);
                    } else {
                        endFunction(requestUrl);
                    }
                }
            }
        });
    }

    var onpopstate = function(containerSelector, baseUrl, endFunction) {
        window.addEventListener("popstate",
        function() {
            var currentState = history.state;
            if (currentState) {
                var index = currentState.indexOf(baseUrl);
                currentState = currentState.substring(index + baseUrl.length + 1, currentState.length);
                currentState = $.base64.decode(currentState);
                var requestUrl = getRequestUrl(currentState);
                var navUrl = getNavUrl(currentState);

                var promise = $.ajax({
                    url: checkUrl(requestUrl),
                    type: "get",
                    dataType: "html"
                });
                promise.then(function(html, textStatus, jqXHR) {
                	if (!redirectTimeoutUrl(html,jqXHR)) {
                		$(containerSelector).html(html);
                        if (endFunction) {
                            if (navUrl) {
                                endFunction(navUrl);
                            } else {
                                endFunction(requestUrl);
                            }
                        }
                    }
                });
            } else {
                var currentUrl = window.location.href;
                var index = currentUrl.indexOf(baseUrl);
                if (index >= 0) {
                    currentUrl = currentUrl.substring(0, index + baseUrl.length);
                }
                window.location.href = currentUrl;
            }
        });
    }

    var add = function(aSelector, containerSelector, baseUrl, currentUrl, startFunction, endFunction) {
        var $a = $(aSelector);
        $a.unbind("click");
        $a.bind("click",function() {
            var $current = $(this);
            if (startFunction) {
                startFunction($current);
            }
            var url = null;
            if(currentUrl == null){
            	url = $(this).data("url");
            }else{
            	url = currentUrl;
            }
            if(url == null || url == ""){
            	return;
            }
            var promise = $.ajax({
                url: checkUrl(url),
                type: "get",
                dataType: "html"
            });
            promise.then(function(html, textStatus, jqXHR) {
                if (!redirectTimeoutUrl(html,jqXHR)) {
                	$(containerSelector).html(html);
                    var navUrl = getCurrentUrl(baseUrl);
                    if (navUrl) {
                        url = navUrl + "##" + url;
                    }
                    pushPage(url, baseUrl);
                    if (endFunction) {
                        endFunction($current);
                    }
                }
            });
        });
    }

    function getCurrentUrl(baseUrl) {
        var url = window.location.href;
        var index = url.indexOf(baseUrl);
        url = url.substring(index + baseUrl.length + 1, url.length);
        url = $.base64.decode(url);
        return url;
    }

    function getRequestUrl(url) {
        //截取最后一段url，加载页面
        var lastIndex = url.lastIndexOf("##");
        if (lastIndex >= 0) {
            url = url.substring(lastIndex + 2);
        }
        return url;
    }

    function getNavUrl(url) {
        //截取第一级url，加上导航的选中样式
        var index = url.indexOf("##");
        var navUrl = null;
        if (index >= 0) {
            navUrl = url.substring(0, index);
        }
        return navUrl;
    }

    function pushPage(url, baseUrl) {
        url = $.base64.encode(url, true);
        var currentUrl = window.location.href;
        var index = currentUrl.indexOf(baseUrl);
        currentUrl = currentUrl.substring(0, index + baseUrl.length);
        currentUrl = currentUrl + "?" + url;
        history.pushState(currentUrl, null, currentUrl);
    }

    function checkUrl(url) {
        var ctxObj = $("input[name='ctx']");
        if (ctxObj.length > 0) {
            var ctx = ctxObj.val();
            url = ctx + "/" + url;
        }
        return url;
    }
    
    function redirectTimeoutUrl(obj,jqXHR) {
    	//获取Location字段，如果不为null，并且指向登录页面，就代表session已经过期
    	var location=jqXHR.getResponseHeader("Location");
    	if(location && location.indexOf("login")>=0){
    		window.location.href = location;
    		return true;
    	}
    	try{
            var jsonObject = JSON.parse(obj);
            window.location.href = checkUrl(jsonObject.obj);
            return true;
    	}catch(e){
            return false;
    	}
    }

    return {
        init: function(aSelector, containerSelector, baseUrl, startFunction, endFunction) {
            init(aSelector, containerSelector, baseUrl, startFunction, endFunction);
        },
        add: function(aSelector, containerSelector, baseUrl, currentUrl, startFunction, endFunction) {
            add(aSelector, containerSelector, baseUrl, currentUrl, startFunction, endFunction);
        },
        refresh: function(containerSelector, baseUrl, endFunction) {
            refresh(containerSelector, baseUrl, endFunction);
        },
        onpopstate: function(containerSelector, baseUrl, endFunction) {
            onpopstate(containerSelector, baseUrl, endFunction);
        },
        redirectAdd: function(aSelector) {
            add(aSelector, ".page-content", "admin/home");
        },
        redirectSave: function() {
            add(".save-button", ".page-content", "admin/home");
        },
        redirectUpdate: function() {
            add(".update-button", ".page-content", "admin/home");
        }
    }
} ();