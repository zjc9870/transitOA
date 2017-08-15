var tabs = document.getElementById('tab').getElementsByTagName('button');
for(var i=0; i<tabs.length; i++) {
    tabs[i].onclick = function () {
        var tabId = this.id;
        //若当前按钮已选中则点击不再触发事件
        if($(this).hasClass('button')) {return};
        AjaxTool.get('meeting/sqjlTab', {
                lx: this.id, bz: 'sq'},function (data) {
                if(data.success) {
                    var str = "";
                    var m = data.content;
                    for(var i=0;i<m.length;i++) {
                        str += "<tr>";
                        str += "<td>"+m[i].bt+"</td>";
                        str += "<td>"+m[i].sj+"</td>";
                        switch(tabId){
                            case "wtj":
                                str += "<td>未提交</td>";
                                str += "<td><div onclick='seeApplyRecordE(\""+ m[i].id +'\",\"' + tabId +"\")'>查看</div>" +
                                    "<div onclick='submitWtjForm(\""+ m[i].id +"\")'>提交</div>" +
                                    "<div onclick='deleteWtjCon(\""+ m[i].id +"\")'>删除</div></td>";
                                break;
                            case "dsp":
                                str += "<td>待审批</td>";
                                str += "<td><div onclick='seeApplyRecordNE(\""+ m[i].id +'\",\"' + tabId +"\")' >查看</div></td>";
                                break;
                            case "ysp":
                                str += "<td><div>"+m[i].hyshzt+"</div></td>";
                                if(m[i].hyshzt=="通过"){
                                    str += "<td><div onclick='seeApplyRecordNE(\""+ m[i].id +'\",\"' + tabId +"\")' >查看</div></td>";
                                    break;
                                }
                                if(m[i].hyshzt=="打回"){
                                    str += "<td><div onclick='seeApplyRecordE(\""+ m[i].id +'\",\"' + tabId +"\")'>查看</div>" +
                                        "<div onclick='submitWtjForm(\""+ m[i].id +"\")'>提交</div></td>";
                                    break;
                                }

                            case "yth":
                                str += "<td>已退回</td>";
                                str += "<td><div onclick='seeApplyRecordE(\""+ m[i].id +'\",\"' + tabId +"\")'>查看</div></td>";
                                break;
                            default:
                                break;
                        }
                        str += "</tr>";
                    }
//                    mTable.fnClearTable();
                    var mTable = $('#m-apply-record-table').DataTable();
                    mTable.destroy();
                    $('#m-approve-tbody').html(str);
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
    AjaxTool.html('meeting/sqjlxqE',{id: id},function (html) {
        $('.portlet-body').html(html);
        if(tabId == "yth") {
            $('#splc').attr('style','display:block');
        }
        $('#back').data('tabId',tabId);
    });
}

function seeApplyRecordNE(id,tabId) {
    AjaxTool.html('meeting/sqjlxqNE',{id: id},function (html) {
        $('.portlet-body').html(html);
        $('#back').data('tabId',tabId);
    });

}

function submitWtjForm(id) {
    AjaxTool.post('meeting/submitWtj',{id: id}, function (data) {
            alert(data.message);
            window.location.reload();
        }
    )
};


function deleteWtjCon(id) {
    AjaxTool.post('meeting/deleteWjt',{
        id:id},function (data) {
        alert(data.message);
        window.location.reload();
    })
}

function init() {//dataTable初始化
    mTable=DatatableTool.initDatatable("m-apply-record-table", [ {
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
