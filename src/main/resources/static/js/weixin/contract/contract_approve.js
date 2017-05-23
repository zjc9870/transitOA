
var roleName = $('#roleName').val();
$(document).ready(function () {
	AjaxTool.get('contract/sqjlTab', {
        lx: 'dsp', bz: 'sp'},function (data) {
        if(data.success) {
            var str = "";
            var cons = data.content;
            str+="<div class='weui-cell__hd'><div class='weui-cells__title'>待审批</div>";
            for(var i=0;i<cons.length;i++) {
            	str+="<a class='weui-cell weui-cells_access' href='javascript:;'>";
            	str+="<div onclick='seeApprove(\""+ cons[i].id +'\",\"'+'dsp'+"\")' class='weui-cell__bd weui-cell_primary'>";
            	str+="<p>"+cons[i].htbt+"</p>";
            	str+="</div>";
            	str+="<div class='weui-cell__ft'>";
            	str+="</div>";
            	str+="</a>";
            }
            $('#weui_body_dsp').html(str);
        }
    });
	AjaxTool.get('contract/sqjlTab', {
        lx: 'ysp', bz: 'sp'},function (data) {
        if(data.success) {
            var str = "";
            var cons = data.content;
            str+="<div class='weui-cell__hd'><div class='weui-cells__title'>已审批</div>";
            for(var i=0;i<cons.length;i++) {
            	str+="<a class='weui-cell weui-cells_access' href='javascript:;'>";
            	str+="<div onclick='seeApprove(\""+ cons[i].id +'\",\"'+'ysp'+"\")' class='weui-cell__bd weui-cell_primary'>";
            	str+="<p>"+cons[i].htbt+"</p>";
            	str+="</div>";
            	str+="<div class='weui-cell__ft'>";
            	str+="</div>";
            	str+="</a>";
            }
            $('#weui_body_ysp').html(str);
        }
    });
	AjaxTool.get('contract/sqjlTab', {
        lx: 'yth', bz: 'sp'},function (data) {
        if(data.success) {
            var str = "";
            var cons = data.content;
            str+="<div class='weui-cell__hd'><div class='weui-cells__title'>已退回</div>";
            for(var i=0;i<cons.length;i++) {
            	str+="<a class='weui-cell weui-cells_access' href='javascript:;'>";
            	str+="<div onclick='seeApprove(\""+ cons[i].id +'\",\"'+'yth'+"\")' class='weui-cell__bd weui-cell_primary'>";
            	str+="<p>"+cons[i].htbt+"</p>";
            	str+="</div>";
            	str+="<div class='weui-cell__ft'>";
            	str+="</div>";
            	str+="</a>";
            }
            $('#weui_body_yth').html(str);
        }
    });
	$('#weui_body_ysp').hide();
	$('#weui_body_dsp').hide();
	$('#weui_body_yth').hide();
	$('#ysp_tab').click(function() {
		$('#weui_body_ysp').show();
		$('#weui_body_dsp').hide();
		$('#weui_body_yth').hide();
	});
	$('#dsp_tab').click(function() {
		$('#weui_body_ysp').hide();
		$('#weui_body_dsp').show();
		$('#weui_body_yth').hide();
	});
	$('#yth_tab').click(function() {
		$('#weui_body_ysp').hide();
		$('#weui_body_dsp').hide();
		$('#weui_body_yth').show();
	});
});

function seeApprove(id,tabId) {
//    AjaxTool.html('htspckxq',{id: id},function (html) {
//    	$('#pre_body').html(html);
//
//        switch (tabId) {
//            case "ysp":
//                $('#c_approve_form').attr('style','display:none');
//                $('#submit').attr('style','display:none');
//                break;
//            case "yth":
//                $('#c_approve_form').attr('style','display:none');
//                $('#submit').attr('style','display:none');
//                break;
//            default:
//                break;
//        }
//    	initDetail();
    	location.href="/weixin/contract/htspckxq?id="+id+"&lx="+tabId;
//        $('.portlet-body').html(html);
//        switch (tabId) {
//            case "ysp":
//                $('.operation').attr('style','display:none');
//                break;
//            default:
//                break;
//        }
//        $('#back').data('tabId',tabId);
//    	if(roleName=='法务') {
//            $('#yj_input').html("<input type='hidden' name='cljg' value='已审核'/>");
//        }
//    });
}