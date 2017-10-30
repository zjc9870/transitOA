var tabs = document.getElementById('tab').getElementsByTagName('button');
for(var i=0; i<tabs.length; i++) {
    tabs[i].onclick = function () {
        var tabId = this.id;
        //若当前按钮已选中则点击不再触发事件
        if($(this).hasClass('button')) {return};
        AjaxTool.get('contract/sqjlTab', {
                lx: this.id, bz: 'sq'},function (data) {
                if(data.success) {
                    var str = "";
                    var cons = data.content;
                    for(var i=0;i<cons.length;i++) {
                        str += "<tr id='"+cons[i].id+"'>";
                        str += "<td>"+cons[i].htbt+"</td>";
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
                                str += "<td><div onclick='seeApplyRecordNE(\""+ cons[i].id +'\",\"' + tabId +"\")'>查看</div>" +
                                    "<div onclick='revoke(\""+ cons[i].id +"\")'>撤回</div></td>";
                                break;
                            case "ysp":
                                var htshzt = cons[i].htshzt;
                                if(htshzt == '通过'){
                                    str += "<td><div style='color:green'>"+htshzt+"</div></td>";
                                }
                                else if(htshzt == '终止') {
                                    str += "<td><div style='color: red'>"+htshzt+"</div></td>";
                                }
                                else if(htshzt =='已回填') {
                                    str += "<td><div style='color: yellowgreen'>"+htshzt+"</div></td>";
                                }

                                str += "<td><div onclick='seeApplyRecordNE(\""+ cons[i].id +'\",\"' + tabId +"\")'>查看</div></td>";
                                break;
                            case "yth":
                                str += "<td>已撤回</td>";
                                str += "<td><div onclick='seeApplyRecordNE(\""+ cons[i].id +'\",\"' + tabId +"\")'>查看</div></td>";
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
        if(tabId == 'yth') {
            $('#fjck').attr('style','display:none');
        }
        $('#back').data('tabId',tabId);
    });

}

function submitWtjForm(id) {
    AjaxTool.post('contract/submitWtj',{id: id}, function (data) {
            alert(data.message);
            var table = $('#c-apply-record-table').DataTable();
            if(data.success) {
                table.rows('#' + id).remove().draw();
            }
        }
    )
};

function deleteWtjCon(id) {
    AjaxTool.post('contract/deleteWjt',{
        id:id},function (data) {
        alert(data.message);
        var table = $('#c-apply-record-table').DataTable();
        if(data.success) {
            table.rows('#' + id).remove().draw();
        }
    })
}

function revoke(id) {
    var s = "<form id='revoke-form'><select id='select' class='form-control'>" +
        "<option value='提交人发现有误'>提交人发现有误</option>" +
        "<option value='资产部建议修改'>资产部建议修改</option>" +
        "<option value='其他'>其他</option></select>" +
        "<input type='text' name='reason' placeholder='选择其他时理由自填' class='form-control' style='margin:20px 0'/></form>";
    SweetAlert.swalContent("确认撤回",s,function () {
        var reason = $('#select').val();
        if(reason === '其他') {
            reason = $('#revoke-form input[name="reason"]').val();
        }
        AjaxTool.post('contract/revocationContract',{
            id:id,revocationReason:reason},function (data) {
            var table = $('#c-apply-record-table').DataTable();
            if(data.success) {
                table.rows('#' + id).remove().draw();
            }
        })
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

