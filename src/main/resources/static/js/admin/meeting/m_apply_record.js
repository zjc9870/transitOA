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
                    var mees = data.content;
                    for(var i=0;i<mees.length;i++) {
                        str += "<tr id='"+mees[i].id+"'>";
                        str += "<td>"+mees[i].hyzt+"</td>";
                        str += "<td>"+mees[i].sqsj+"</td>";
                        switch(tabId){
                            case "wtj":
                                str += "<td>未提交</td>";
                                str += "<td><div onclick='seeApplyRecordE(\""+ mees[i].id +'\",\"' + tabId +"\")'>查看</div>" +
                                    "<div onclick='submitWtjForm(\""+ mees[i].id +"\")'>提交</div>" +
                                    "<div onclick='deleteWtjCon(\""+ mees[i].id +"\")'>删除</div></td>";
                                break;
                            case "dsp":
                                str += "<td>待审批</td>";
                                str += "<td><div onclick='seeApplyRecordNE(\""+ mees[i].id +'\",\"' + tabId +"\")'>查看</div>" +
                                    "<div onclick='revoke(\""+ mees[i].id +"\")'>撤回</div></td>";
                                break;
                            case "ysp":
                                var hyshzt = mees[i].hyshzt;
                                if(hyshzt == '通过'){
                                    str += "<td><div style='color:green'>"+hyshzt+"</div></td>";
                                }
                                else if(hyshzt == '终止') {
                                    str += "<td><div style='color: red'>"+hyshzt+"</div></td>";
                                }
                                str += "<td><div onclick='seeApplyRecordNE(\""+ mees[i].id +'\",\"' + tabId +"\")'>查看</div></td>";
                                break;
                            case "ych":
                                str += "<td>已撤回</td>";
                                str += "<td><div onclick='seeApplyRecordNE(\""+ mees[i].id +'\",\"' + tabId +"\")'>查看</div></td>";
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
        if(tabId == "ych") {
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
            var table = $('#m-apply-record-table').DataTable();
            if(data.success) {
                table.rows('#' + id).remove().draw();
            }
        }
    )
};


function deleteWtjCon(id) {
    AjaxTool.post('meeting/deleteWjt',{
        id:id},function (data) {
        alert(data.message);
        var table = $('#m-apply-record-table').DataTable();
        if(data.success) {
            table.rows('#' + id).remove().draw();
        }
    })
}

function revoke(id) {
    var s = "<form id='revoke-form'><select id='select' class='form-control'>" +
        "<option value='申请人发现有误'>提交人发现有误</option>" +
        "<option value='其他'>其他</option></select>" +
        "<input type='text' name='reason' placeholder='选择其他时理由自填' class='form-control' style='margin:20px 0'/></form>";
    SweetAlert.swalContent("确认撤回",s,function () {
        var reason = $('#select').val();
        if(reason === '其他') {
            reason = $('#revoke-form input[name="reason"]').val();
        }
        AjaxTool.post('meeting/revocationMeeting',{
            id:id,revocationReason:reason},function (data) {
            var table = $('#m-apply-record-table').DataTable();
            if(data.success) {
                table.rows('#' + id).remove().draw();
            }
        })
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
