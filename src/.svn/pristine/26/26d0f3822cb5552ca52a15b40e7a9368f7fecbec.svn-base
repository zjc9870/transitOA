var tabs = document.getElementById('tab').getElementsByTagName('button');
for(var i=0; i<tabs.length; i++) {
    tabs[i].onclick = function () {
        var tabId = this.id;
        //若当前按钮已选中则点击不再触发事件
        if($(this).hasClass('button')) {return};
        AjaxTool.post('draftSw/tabRequest', {
                tab: this.id, ym: 'swcy'},function (data) {
                if(data.success) {
                    var str = "";
                    var drafts = data.content;
                    for(var i=0;i<drafts.length;i++) {
                        str += "<tr>";
                        str += "<td>"+drafts[i].wjbt+"</td>";
                        str += "<td>"+drafts[i].fqsj+"</td>";
                        str += "<td>"+drafts[i].swr+"</td>";
                        str += "<td>"+drafts[i].zt+"</td>";
                        str += "<td><div onclick='seeCy(\""+ drafts[i].id +'\",\"' + tabId +"\")'>查看</div></td>";
                        str += "</tr>";
                    }
                    var mTable = $('#s-circulate-table').DataTable();
                    mTable.destroy();
                    $('#c-approve-tbody').html(str);
                    init();
                }
            }
        );
        $(this).removeClass("button-active").addClass("button");
        for(var j=0;j<tabs.length;j++) {
            if(tabs[j] != this) {
                $(tabs[j]).removeClass("button").addClass("button-active");
            }
        }
    }
}


function seeCy(id,tabId) {
    AjaxTool.html('draftSw/swCy',{draftSwId: id},function (html) {
        $('.portlet-body').html(html);
        switch (tabId) {
            case "ycy":
                $('.operation').attr('style','display:none');
                break;
            default:
                break;
        }
        $('#back').data('tabId',tabId);
    });

}



function init() {//dataTable初始化
    mTable=DatatableTool.initDatatable("s-circulate-table", [ {
        'orderable' : false,
        'targets' : [ 4 ]
    }, {
        "searchable" : false,
        "targets" : [ 4 ]
    }], [ [ 1, "desc" ] ]);
}

jQuery(document).ready(function() {
    init();
});

$('#dcy').click();