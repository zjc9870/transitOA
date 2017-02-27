
var roleName = $('#roleName').val();
$(document).ready(function () {
	AjaxTool.get('sqjlTab', {
        lx: 'dsp', bz: 'sp'},function (data) {
        if(data.success) {
            var str = "";
            var cons = data.content;
            str+="<div class='weui_cell_hd'><label class='weui_label'>待审批</label></div>";
            for(var i=0;i<cons.length;i++) {
            	str+="<a class='weui_cell weui_cells_access' href='javascript:;'>";
            	str+="<div onclick='seeApprove(\""+ cons[i].id +'\",\"'+'dsp'+"\")' class='weui_cell_bd'>";
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
            str+="<div class='weui_cell_hd'><label class='weui_label'>已审批</label></div>";
            for(var i=0;i<cons.length;i++) {
            	str+="<a class='weui_cell weui_cells_access' href='javascript:;'>";
            	str+="<div onclick='seeApprove(\""+ cons[i].id +'\",\"'+'ysp'+"\")' class='weui_cell_bd'>";
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
            str+="<div class='weui_cell_hd'><label class='weui_label'>已退回</label></div>";
            for(var i=0;i<cons.length;i++) {
            	str+="<a class='weui_cell weui_cells_access' href='javascript:;'>";
            	str+="<div onclick='seeApprove(\""+ cons[i].id +'\",\"'+'yth'+"\")' class='weui_cell_bd'>";
            	str+="<p>"+cons[i].htbt+"</p>";
            	str+="</div>";
            	str+="<div class='weui_cell_ft'>";
            	str+="</div>";
            	str+="</a>";
            }
            $('#weui_body_yth').html(str);
        }
    });
});

function seeApprove(id,tabId) {
    AjaxTool.html('htspckxq',{id: id},function (html) {
    	$('#pre_body').html(html);
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