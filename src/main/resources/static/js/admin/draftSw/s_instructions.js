var tabs = document.getElementById('tab').getElementsByTagName('button');
for(var i=0; i<tabs.length; i++) {
    tabs[i].onclick = function () {
        var tabId = this.id;
        //若当前按钮已选中则点击不再触发事件
        if($(this).hasClass('button')) {return};
        AjaxTool.post('draftSw/tabRequest', {
                tab: this.id, ym: 'swps'},function (data) {
                if(data.success) {
                    var str = "";
                    var drafts = data.content;
                    for(var i=0;i<drafts.length;i++) {
                        str += "<tr id='"+drafts[i].id+"'>";
                        str += "<td>"+drafts[i].wjbt+"</td>";
                        str += "<td>"+drafts[i].fqsj+"</td>";
                        str += "<td>"+drafts[i].swr+"</td>";
                        str += "<td>"+drafts[i].zt+"</td>";
                        switch(tabId){
                            case "dps":
                                str += "<td><div onclick='seePs(\""+ drafts[i].id +'\",\"' + tabId +"\")'>查看</div>" +
                                    "<div onclick='passPs(\""+ drafts[i].id +"\")'>通过</div></td>";
                                break;
                            default:
                                str += "<td><div onclick='seePs(\""+ drafts[i].id +'\",\"' + tabId +"\")'>查看</div></td>";
                                break;
                        }
                        str += "</tr>";
                    }
                    var mTable = $('#s-instructions-table').DataTable();
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


function seePs(id,tabId) {
    AjaxTool.html('draftSw/swPs',{draftSwId: id},function (html) {
        $('.portlet-body').html(html);
        switch (tabId) {
            case "yps":
                $('.operation').attr('style','display:none');
                break;
            default:
                break;
        }
        $('#back').data('tabId',tabId);
    });

}

function passPs(id) {
    AjaxTool.post('draftSw/ldps',{
        ldps: '通过', id:id},function (data) {
        alert(data.message);
        var table = $('#s-instructions-table').DataTable();
        if(data.success) {
            table.rows('#'+id).remove().draw();
            refreshSwps();
        }
    });
}

function refreshSwps() {
    var n = $('#收文批示').text();
    n--;
    if(n == 0){
        $('#收文批示').remove();
    }else{
        $('#收文批示').text(n);
    }
    var count = $('#收文管理').text();
    count--;
    if (count == 0){
        $('#收文管理').remove();
    }else{
        $('#收文管理').text(count);
    }
}


function init() {//dataTable初始化
    mTable=DatatableTool.initDatatable("s-instructions-table", [ {
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

$('#dps').click();