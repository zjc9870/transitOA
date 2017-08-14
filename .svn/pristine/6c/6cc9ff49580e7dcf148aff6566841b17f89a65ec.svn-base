;(function() {
	var SinglePage = function(ele, options) {
		var $element;
		this.$element = ele;
		var defaults = {
			pagination:false,
			info:false,
			open:function(){
				$(".single-page-wrap .single-page-content").html("无内容");
			}
		};
		var options = $.extend({}, defaults, options);
		
		var html='<div class="single-page-wrap single-page"><div class="single-page-content"></div>'
				+'<div class="single-page-navigation-wrap"><div class="single-page-navigation">'
				+'<div class="single-page-close"></div><div class="single-page-next"></div>'
				+'<div class="single-page-prev"></div><div class="single-page-counter"></div></div></div></div>';

		this.init = function() {
			$(document.body).append(html);
			$element = this.$element;
			$element.unbind("click");
			$element.bind("click",function(){
				$(".single-page-wrap").addClass("single-page-active");
				var param=$(this).data("param");
				if(options.open){
					options.open($(".single-page-wrap .single-page-content"),param);
				}
			});
			$(".single-page-wrap .single-page-close").click(function(){
				$(".single-page-wrap").removeClass("single-page-active");
			});
			if(options.info){
				$(".single-page-counter").hide();
			}else{
				$(".single-page-counter").hide();
			}
			if(options.pagination){
				$(".single-page-next").show();
				$(".single-page-prev").show();
			}else{
				$(".single-page-next").hide();
				$(".single-page-prev").hide();
			}
		}
		
		this.open = function(open){
			if(open){
				options.open=open;
			}
		}

	};

	$.fn.SinglePage = function(options) {
		var singlePage = new SinglePage(this, options);
		singlePage.init();
		return singlePage;
	};

})();
