
$(document).ready(function () {
	AjaxTool.get('sqjlTab', {
        lx: 'dht', bz: 'ht'},function (data) {
        if(data.success) {
            var str = "";
            var cons = data.content;
            str+="<div class='weui_cell_hd'><label class='weui_label'>待回填</label></div>";
            for(var i=0;i<cons.length;i++) {
            	str+="<a class='weui_cell weui_cells_access' href='javascript:;'>";
            	str+="<div onclick='backfillDetail(\""+ cons[i].id +'\",\"'+'dht'+"\")' class='weui_cell_bd'>";
            	str+="<p>"+cons[i].htbt+"</p>";
            	str+="</div>";
            	str+="<div class='weui_cell_ft'>";
            	str+="</div>";
            	str+="</a>";
            }
            $('#weui_body_dht').html(str);
        }
    });
	AjaxTool.get('sqjlTab', {
        lx: 'yht', bz: 'ht'},function (data) {
        if(data.success) {
            var str = "";
            var cons = data.content;
            str+="<div class='weui_cell_hd'><label class='weui_label'>已回填</label></div>";
            for(var i=0;i<cons.length;i++) {
            	str+="<a class='weui_cell weui_cells_access' href='javascript:;'>";
            	str+="<div onclick='backfillDetail(\""+ cons[i].id +'\",\"'+'yht'+"\")' class='weui_cell_bd'>";
            	str+="<p>"+cons[i].htbt+"</p>";
            	str+="</div>";
            	str+="<div class='weui_cell_ft'>";
            	str+="</div>";
            	str+="</a>";
            }
            $('#weui_body_yht').html(str);
        }
    });
});

function backfillDetail(id,bh,tabId) {
    AjaxTool.html('bhhtxq',{id: id},function (html) {
        $('#pre-body').html(html);
        initDetail();
    });
}