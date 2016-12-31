var tabs = document.getElementById('tab').getElementsByTagName('button');
for(var i=0; i<tabs.length; i++) {
    tabs[i].onclick = function () {
        var tabId = this.id;
        AjaxTool.get('contract/sqjlTab', {
                lx: this.id, bz: 'sq'},function (data) {
                if(data.success) {
                    var str = "";
                    var cons = data.content;
                    for(var i=0;i<cons.length;i++) {
                        str += "<tr>";
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
                                str += "<td><div onclick='seeApplyRecordNE(\""+ cons[i].id +'\",\"' + tabId +"\")'>查看</div></td>";
                                break;
                            case "ysp":
                                str += "<td><div>"+cons[i].htshzt+"</div></td>";
                                str += "<td><div onclick='seeApplyRecordNE(\""+ cons[i].id +'\",\"' + tabId +"\")'>查看</div></td>";
                                break;
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

