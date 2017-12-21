var tabs = document.getElementById('tab').getElementsByTagName('button');
for(var i=0; i<tabs.length; i++) {
    tabs[i].onclick = function () {
        var tabId = this.id;
        //若当前按钮已选中则点击不再触发事件
        if($(this).hasClass('button')) {return};
        AjaxTool.post('draftSw/tabRequest', {
                tab: this.id, ym: 'swjl'},function (data) {
                if(data.success) {
                    var str = "";
                    var drafts = data.content;
                    for(var i=0;i<drafts.length;i++) {
                        str += "<tr>";
                        str += "<td>"+drafts[i].wjbt+"</td>";
                        str += "<td>"+drafts[i].fqsj+"</td>";
                        str += "<td>"+drafts[i].zt+"</td>";
                        switch(tabId){
                            case "wtj":
                                str += "<td><div onclick='seeSwRecordWtj(\""+ drafts[i].id +'\",\"' + tabId +"\")'>查看</div>" +
                                    "<div onclick='submitWtjForm(\""+ drafts[i].id +"\")'>提交</div>" +
                                    "<div onclick='deleteWtjDraft(\""+ drafts[i].id +"\")'>删除</div></td>";
                                break;
                            case "dcl":
                                str += "<td><div onclick='seeSwRecordE(\""+ drafts[i].id +'\",\"' + tabId +"\")'>查看</div></td>";
                                break;
                            case "ycl":
                                str += "<td><div onclick='seeSwRecordNE(\""+ drafts[i].id +'\",\"' + tabId +"\")'>查看</div></td>";
                                break;
                            case "ywc":
                                str += "<td><div onclick='seeSwRecordNE(\""+ drafts[i].id +'\",\"' + tabId +"\")'>查看</div>"+
                                    "<div onclick='printSw(\""+ drafts[i].id +'\",\"' + tabId +"\")'>打印</div></td>";
                                break;
                        }
                        str += "</tr>";
                    }
                    var mTable = $('#s-records-table').DataTable();
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

function seeSwRecordWtj(id,tabId) {
    AjaxTool.html('draftSw/swjlWtj',{id: id},function (html) {
        $('.portlet-body').html(html);
        $('#back').data('tabId',tabId);
    });
}

function seeSwRecordE(id,tabId) {
    AjaxTool.html('draftSw/swjlxq',{id: id},function (html) {
        $('.portlet-body').html(html);
        $('#back').data('tabId',tabId);
    });
}

function seeSwRecordNE(id,tabId) {
    AjaxTool.html('draftSw/swjlxqNE',{id: id},function (html) {
        $('.portlet-body').html(html);
        $('#back').data('tabId',tabId);
    });

}

function submitWtjForm(id) {
    AjaxTool.post('draftSw/submitWtj',{id: id}, function (data) {
            alert(data.message);
            window.location.reload();
        }
    )
};

function deleteWtjDraft(id) {
    AjaxTool.post('draftSw/deleteWtj',{
        id:id},function (data) {
        alert(data.message);
        window.location.reload();
    })
}
//进去收文打印页面
function printSw(id,tabId){
    AjaxTool.html('draftSw/printTg',{id: id},function (html) {
        $('.portlet-body').html(html);
        $('#back').data('tabId',tabId);
    });

}

// function printSw(id){
//     // AjaxTool.html('draftSw/printsw',{id: id},function (html) {
//     //     $('.portlet-body').html(html);
//     //     $('#back').data('tabId',tabId);
//     // });
//     window.location = "/wjsc?xgdm=jtsw&xgid=" +id;
//
// }

function init() {//dataTable初始化
    mTable=DatatableTool.initDatatable("s-records-table", [ {
        'orderable' : false,
        'targets' : [ 3 ]
    }, {
        "searchable" : false,
        "targets" : [ 3 ]
    }], [ [ 1, "desc" ] ]);
}

jQuery(document).ready(function() {
    init();
});
