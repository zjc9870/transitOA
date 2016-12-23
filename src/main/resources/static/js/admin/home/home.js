$(document).ready(function() {
	showAvatar();
	Pjax.init(".nav-pajx", ".page-content", "admin/home","home1", function() {
	}, function($current) {
		clearNavStatus($current);
	});
	Pjax.refresh(".page-content", "admin/home","home1", function(functionUrl) {
		functionUrl = Tools.replaceAll(functionUrl, "/", "");
		clearNavStatus($("." + functionUrl));
	});
	Pjax.onpopstate(".page-content", "admin/home","home1", function(functionUrl) {
		functionUrl = Tools.replaceAll(functionUrl, "/", "");
		clearNavStatus($("." + functionUrl));
	});
});
function clearNavStatus($current) {
	$(".nav-item.active").removeClass("active");
	$current.parent().addClass("active");
	if ($current.find(".third-nav")) {
		$current.parent().parent().parent().addClass("active");
		$current.parent().parent().parent().parent().parent().addClass("active");
	}
}
//头像显示
function showAvatar(){
	var userId=$("input[name='userId']").val();
	AjaxTool.get("user/checkAvatar",{
		userId:userId
	},function(response){
		if(response.result){
			$(".user-avatar").attr("src","user/showAvatar?userId="+userId);
		}
	});
}
