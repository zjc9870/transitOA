$(document).ready(function () {
	AjaxTool.get('sqjlTab', {
        lx: 'wtj', bz: 'sq'},function (data) {
        if(data.success) {
            var str = "";
            var cons = data.content;
            str+="<div class='weui_cell_hd'><label class='weui_label'>未提交</label></div>";
            for(var i=0;i<cons.length;i++) {
            	str+="<a class='weui_cell weui_cells_access' href='javascript:;'>";
            	str+="<div onclick='seeApplyRecordE(\""+ cons[i].id +'\",\"'+'wtj'+"\")' class='weui_cell_bd'>";
            	str+="<p>"+cons[i].htbt+"</p>";
            	str+="</div>";
            	str+="<div class='weui_cell_ft'>";
            	str+="</div>";
            	str+="</a>";
            }
            $('#weui_body_wtj').html(str);
        }
    });
	AjaxTool.get('sqjlTab', {
        lx: 'dsp', bz: 'sq'},function (data) {
        if(data.success) {
            var str = "";
            var cons = data.content;
            str+="<div class='weui_cell_hd'><label class='weui_label'>待审批</label></div>";
            for(var i=0;i<cons.length;i++) {
            	str+="<a class='weui_cell weui_cells_access' href='javascript:;'>";
            	str+="<div onclick='seeApplyRecordNE(\""+ cons[i].id +'\",\"'+'dsp'+"\")' class='weui_cell_bd'>";
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
        lx: 'ysp', bz: 'sq'},function (data) {
        if(data.success) {
            var str = "";
            var cons = data.content;
            str+="<div class='weui_cell_hd'><label class='weui_label'>已审批</label></div>";
            for(var i=0;i<cons.length;i++) {
            	str+="<a class='weui_cell weui_cells_access' href='javascript:;'>";
            	str+="<div onclick='seeApplyRecordNE(\""+ cons[i].id +'\",\"'+'ysp'+"\")' class='weui_cell_bd'>";
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
        lx: 'yth', bz: 'sq'},function (data) {
        if(data.success) {
            var str = "";
            var cons = data.content;
            str+="<div class='weui_cell_hd'><label class='weui_label'>已退回</label></div>";
            for(var i=0;i<cons.length;i++) {
            	str+="<a class='weui_cell weui_cells_access' href='javascript:;'>";
            	str+="<div onclick='seeApplyRecordE(\""+ cons[i].id +'\",\"'+'yth'+"\")' class='weui_cell_bd'>";
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

function seeApplyRecordE(id,tabId) {
    AjaxTool.html('sqjlxqE',{id: id},function (html) {
        document.write(html);
    });
}

function seeApplyRecordNE(id,tabId) {
    AjaxTool.html('sqjlxqNE',{id: id},function (html) {
        document.write(html);
    });

}