var tabs = document.getElementById('tab').getElementsByTagName('button');
var departmentName = $('#departmentName').val();
for(var i=0; i<tabs.length; i++) {
    tabs[i].onclick = function () {
        var tabId = this.id;
        //若当前按钮已选中则点击不再触发事件
        if($(this).hasClass('button')) {return};
        AjaxTool.get('meeting/sqjlTab', {
                lx: this.id, bz: 'sp'},function (data) {
                if(data.success) {
                    var str = "";
                    var cons = data.content;
                    for(var i=0;i<cons.length;i++) {
                        str += "<tr id='"+cons[i].id+"'>";
                        str += "<td>"+cons[i].hyzt+"</td>";
                        str += "<td>"+cons[i].sqsj+"</td>";
                        str += "<td>"+cons[i].userName+"</td>";
                        switch (tabId) {
                            case "dsp":
                                str += "<td>待审批</td>";
                                str += "<td><div onclick='seeApprove(\""+cons[i].id +'\",\"'+ tabId+"\")'>查看</div>" +
                                    "<div onclick='pass(\"" + cons[i].id +"\")'>通过</div></td>";
                                break;
                            case "ysp":
                                var hyshzt = cons[i].hyshzt;
                                if(hyshzt == '未通知'){
                                    str += "<td><div style='color:green'>"+hyshzt+"</div></td>";
                                    str += "<td><div onclick='notify(\""+ cons[i].id +'\",\"' + tabId +"\",\""+departmentName+"\")'>通知</div></td>";
                                    break;
                                }else if(hyshzt == "已通知"){
                                    str += "<td><div style='color:green'>"+hyshzt+"</div></td>";
                                    str += "<td><div onclick='seeApproveRecordNE(\""+ cons[i].id +'\",\"' + tabId +"\",\""+hyshzt+"\",\""+departmentName+"\")'>查看</div>"
                                }else if(hyshzt == '终止') {
                                    str += "<td><div style='color: red'>"+hyshzt+"</div></td>";
                                    str += "<td><div onclick='seeApproveRecordNE(\""+ cons[i].id +'\",\"' + tabId +"\",\""+hyshzt+"\",\""+departmentName+"\")'>查看</div></td>";
                                    break;
                                }
                            default:
                                break;
                        }
                        str += "</tr>";
                    }
                    var mTable = $('#m-approve-table').DataTable();
                    mTable.destroy();
                    $('#m-approve-tbody').html(str);
                    init();
                }
            }
        );
        $(this).removeClass("button-active").addClass("button");
        for(var j=0;j<tabs.length;j++) {
            if(tabs[j] != this) {
                $(tabs[j]).removeClass("button").addClass("button-active")
            }
        }
    }
}


function seeApprove(id,tabId) {
    AjaxTool.html('meeting/hyspckxq',{id: id},function (html) {
        $('.portlet-body').html(html);
        switch (tabId) {
            case "ysp":
                $('.operation').attr('style','display:none');
                break;
            default:
                break;
        }
        $('#back').data('tabId',tabId);
    });
}

function pass(id) {
    AjaxTool.post('meeting/passHysp',{
        cljg: 'tg', id:id},function (data) {
        alert(data.message);
        var table = $('#m-approve-table').DataTable();
        if(data.success) {
            table.rows('#'+id).remove().draw();
            refreshMeeting();
        }
    });
}

function refreshMeeting(){
    debugger;
    var n = $('#会议审批').text();
    n--;
    if(n == 0){
        $('#会议审批').remove();
    }else{
        $('#会议审批').text(n);
    }
    var count = $('#会议管理').text();
    count--;
    if (count == 0){
        $('#会议管理').remove();
    }else{
        $('#会议管理').text(count);
    }

}


function seeApproveRecordNE(id, tabId, hyshzt, departmentName){
    AjaxTool.html('meeting/approveXq', {id:id}, function(html){
        $('.portlet-body').html(html);
        if (hyshzt == "终止"){
            $("#tzxq").attr('style','display:none');
        }
        if(departmentName.indexOf("集团") != -1){
            $(".dsgj").attr('style','display:none');
        }else if(departmentName.indexOf("东山公交") != -1){
            $(".jt").attr('style','display:none');
        }
        $('#back').data('tabId',tabId);
    })
}

function notify(id,tabId,departmentName){
    AjaxTool.html('meeting/notify',{id: id},function (html) {
        $('.portlet-body').html(html);
        if(departmentName.indexOf("集团") != -1){
            $(".dsgj").attr('style','display:none');
        }else if(departmentName.indexOf("东山公交") != -1){
            $(".jt").attr('style','display:none');
        }
        $('#back').data('tabId',tabId);
    })
}

function init() {
    DatatableTool.initDatatable("m-approve-table", [{
        'width': '30%',
        'targets': 0
    },
        {
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
