var tabs = document.getElementById('tab').getElementsByTagName('button');
for(var i=0; i<tabs.length; i++) {
    tabs[i].onclick = function () {
        var tabId = this.id;
        //若当前按钮已选中则点击不再触发事件
        if($(this).hasClass('button')) {return};
        AjaxTool.get('draftSw/tabRequest', {
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
                                str += "<td><div onclick='seeApplyRecordE(\""+ drafts[i].id +'\",\"' + tabId +"\")'>查看</div>" +
                                    "<div onclick='submitWtjForm(\""+ drafts[i].id +"\")'>提交</div>" +
                                    "<div onclick='deleteWtjCon(\""+ drafts[i].id +"\")'>删除</div></td>";
                                break;
                            case "dsp":
                                str += "<td><div onclick='seeApplyRecordNE(\""+ drafts[i].id +'\",\"' + tabId +"\")'>查看</div></td>";
                                break;
                            case "ysp":
                                str += "<td><div onclick='seeApplyRecordNE(\""+ drafts[i].id +'\",\"' + tabId +"\")'>查看</div></td>";
                                break;
                            case "yth":
                                str += "<td><div onclick='seeApplyRecordE(\""+ drafts[i].id +'\",\"' + tabId +"\")'>查看</div></td>";
                                break;
                            default:
                                break;
                        }
                        str += "</tr>";
                    }
//                    mTable.fnClearTable();
                    var mTable = $('#c-apply-record-table').DataTable();
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

function seeApplyRecordE(id,tabId) {
    AjaxTool.html('contract/sqjlxqE',{id: id},function (html) {
        $('.portlet-body').html(html);
        if(tabId == "yth") {
            $('#splc').attr('style','display:block');
        }
        $('#back').data('tabId',tabId);
    });
}

function seeApplyRecordNE(id,tabId) {
    AjaxTool.html('contract/sqjlxqNE',{id: id},function (html) {
        $('.portlet-body').html(html);
        $('#back').data('tabId',tabId);
    });

}

function submitWtjForm(id) {
    AjaxTool.post('contract/submitWtj',{id: id}, function (data) {
            alert(data.message);
            window.location.reload();
        }
    )
};

function deleteWtjCon(id) {
    AjaxTool.post('contract/deleteWjt',{
        id:id},function (data) {
        alert(data.message);
        window.location.reload();
    })
}

function init() {//dataTable初始化
    mTable=DatatableTool.initDatatable("c-apply-record-table", [ {
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

