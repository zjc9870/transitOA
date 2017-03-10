
var roleName = $('#roleName').val();
$(document).ready(function () {
	AjaxTool.get('sqjlTab', {
        lx: 'dsp', bz: 'sp'},function (data) {
        if(data.success) {
            var str = "";
            var cons = data.content;
            str+="<div class='weui_cell_hd'><div class='weui_cells_title'>待审批</div>";
            for(var i=0;i<cons.length;i++) {
            	str+="<a class='weui_cell weui_cells_access' href='javascript:;'>";
            	str+="<div onclick='seeApprove(\""+ cons[i].id +'\",\"'+'dsp'+"\")' class='weui_cell_bd weui_cell_primary'>";
            	str+="<p>"+cons[i].htbt+"</p>";
            	str+="</div>";
            	str+="<div class='weui_cell_ft'>";
            	str+="</div>";
            	str+="</a>";
            }
            $('#weui_body_dsp').html(str);
        }
    });
	AjaxTool.get('sqjlTab', {
        lx: 'ysp', bz: 'sp'},function (data) {
        if(data.success) {
            var str = "";
            var cons = data.content;
            str+="<div class='weui_cell_hd'><div class='weui_cells_title'>已审批</div>";
            for(var i=0;i<cons.length;i++) {
            	str+="<a class='weui_cell weui_cells_access' href='javascript:;'>";
            	str+="<div onclick='seeApprove(\""+ cons[i].id +'\",\"'+'ysp'+"\")' class='weui_cell_bd weui_cell_primary'>";
            	str+="<p>"+cons[i].htbt+"</p>";
            	str+="</div>";
            	str+="<div class='weui_cell_ft'>";
            	str+="</div>";
            	str+="</a>";
            }
            $('#weui_body_ysp').html(str);
        }
    });
	AjaxTool.get('sqjlTab', {
        lx: 'yth', bz: 'sp'},function (data) {
        if(data.success) {
            var str = "";
            var cons = data.content;
            str+="<div class='weui_cell_hd'><div class='weui_cells_title'>已退回</div>";
            for(var i=0;i<cons.length;i++) {
            	str+="<a class='weui_cell weui_cells_access' href='javascript:;'>";
            	str+="<div onclick='seeApprove(\""+ cons[i].id +'\",\"'+'yth'+"\")' class='weui_cell_bd weui_cell_primary'>";
            	str+="<p>"+cons[i].htbt+"</p>";
            	str+="</div>";
            	str+="<div class='weui_cell_ft'>";
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
    AjaxTool.html('htspckxq',{id: id},function (html) {
    	$('#pre_body').html(html);

        switch (tabId) {
            case "ysp":
                $('#c_approve_form').attr('style','display:none');
                $('#submit').attr('style','display:none');
                break;
            case "yth":
                $('#c_approve_form').attr('style','display:none');
                $('#submit').attr('style','display:none');
                break;
            default:
                break;
        }
    	initDetail();
    	
//        $('.portlet-body').html(html);
//        switch (tabId) {
//            case "ysp":
//                $('.operation').attr('style','display:none');
//                break;
//            default:
//                break;
//        }
//        $('#back').data('tabId',tabId);
    	if(roleName=='法务') {
            $('#yj_input').html("<input type='hidden' name='cljg' value='已审核'/>");
        }
    });
}