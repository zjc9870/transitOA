var tabs = document.getElementById('tab').getElementsByTagName('button');
for(var i=0; i<tabs.length; i++) {
    tabs[i].onclick = function () {
        var tabId = this.id;
        //若当前按钮已选中则点击不再触发事件
        if($(this).hasClass('button')) {return};
        AjaxTool.get('document/sqjlTab', {
                lx: this.id, bz: 'sq'},function (data) {
                if(data.success) {
                    var str = "";
                    var cons = data.content;
                    for(var i=0;i<cons.length;i++) {
                        str += "<tr>";
                        str += "<td>"+cons[i].bt+"</td>";
                        str += "<td><div>"+cons[i].date+"</div><div>"+ cons[i].time +"</div></td>";
                        switch(tabId){
                            case "wtj":
                                str += "<td>未提交</td>";
                                str += "<td><div onclick='seeApplyRecordE(\""+ cons[i].id +'\",\"' + tabId +"\")'>查看</div>" +
                                    "<div onclick='submitWtjForm(\""+ cons[i].id +"\")'>提交</div>" +
                                    "<div onclick='deleteWtjCon(\""+ cons[i].id +"\")'>删除</div></td>";
                                break;
                            case "dsp":
                                str += "<td>待审批</td>";
                                str += "<td><div onclick='seeApplyRecordNE(\""+ cons[i].id +'\",\"' + tabId +"\")' >查看</div></td>";
                                break;
                            case "ysp":
                                str += "<td><div>"+cons[i].gwshzt+"</div></td>";
                                if(cons[i].gwshzt=="通过"){
                                    str += "<td><div onclick='seeApplyRecordNE(\""+ cons[i].id +'\",\"' + tabId +"\")'>查看</div>" +
                                        "<div onclick='printGw(\""+ cons[i].id +'\",\"' + tabId +"\")'>打印</div>" +
                                        "<div onclick='notify(\""+ cons[i].id +'\",\"' + tabId +"\")'>通知</div></td>";
                                    break;
                                }
                                if(cons[i].gwshzt=="打回"){
                                    str += "<td><div onclick='seeApplyRecordE(\""+ cons[i].id +'\",\"' + tabId +"\")'>查看</div>" +
                                        "<div onclick='submitWtjForm(\""+ cons[i].id +"\")'>提交</div></td>";
                                    break;
                                }

                            case "yth":
                                str += "<td>已退回</td>";
                                str += "<td><div onclick='seeApplyRecordE(\""+ cons[i].id +'\",\"' + tabId +"\")'>查看</div></td>";
                                break;
                            default:
                                break;
                        }
                        str += "</tr>";
                    }
//                    mTable.fnClearTable();
                    var mTable = $('#d-apply-record-table').DataTable();
                    mTable.destroy();
                    $('#d-approve-tbody').html(str);
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
    AjaxTool.html('document/sqjlxqE',{id: id},function (html) {
        $('.portlet-body').html(html);
        if(tabId == "yth") {
            $('#splc').attr('style', 'display:block');
        }
        $('#back').data('tabId',tabId);
    });
}

function seeApplyRecordNE(id,tabId) {
    AjaxTool.html('document/sqjlxqNE',{id: id},function (html) {
        $('.portlet-body').html(html);
        if(tabId == "dsp") {
            $('#tzxq').attr('style','display:none');
        }
        $('#back').data('tabId',tabId);
    });

}

function submitWtjForm(id) {
    AjaxTool.post('document/submitWtj',{id: id}, function (data) {
            alert(data.message);
            window.location.reload();
        }
    )
};

function printGw(id,tabId){
    AjaxTool.html('document/printTg',{id: id},function (html) {
        $('.portlet-body').html(html);
        $('#back').data('tabId',tabId);
    });

}

function notify(id,tabId){
    AjaxTool.html('document/notify',{id: id},function (html) {
        $('.portlet-body').html(html);
        $('#back').data('tabId',tabId);
    })
}
function deleteWtjCon(id) {
    AjaxTool.post('document/deleteWjt',{
        id:id},function (data) {
        alert(data.message);
        window.location.reload();
    })
}

function init() {//dataTable初始化
    mTable=DatatableTool.initDatatable("d-apply-record-table", [ {
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

