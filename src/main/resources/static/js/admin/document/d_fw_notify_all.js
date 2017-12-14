var tabs = document.getElementById('tab').getElementsByTagName('button');
for(var i=0; i<tabs.length; i++) {
    tabs[i].onclick = function () {
        var tabId = this.id;
        //若当前按钮已选中则点击不再触发事件
        AjaxTool.get('document/fwtzTab', {
                lx: this.id},function (data) {
                if(data.success) {
                    var str = "";
                    var cons = data.content;
                    for(var i=0;i<cons.length;i++) {
                        str += "<tr>";
                        str += "<td>"+cons[i].bt+"</td>";
                        switch(tabId){
                            case "yd":
                                str += "<td>已读</td>";
                                str +="<td>"+cons[i].tzlx+"</td>";
                                if (!cons[i].ydsj){
                                    str += "<td>-</td>";
                                }
                                else
                                    str += "<td>"+cons[i].ydsj+"</td>";

                                break;
                            case "wd":
                                str += "<td>未读</td>";
                                str +="<td>"+cons[i].tzlx+"</td>";
                                if (!cons[i].tzsj){
                                    str += "<td>-</td>";
                                }
                                else
                                    str += "<td>"+cons[i].tzsj+"</td>";
                                break;
                            default:
                                break;
                        }
                        // if(cons[i].loginUserRole.indexOf('其他公司办公室') !=-1&& tabId=='yd'){
                        //     str += "<td><div onclick='seeFwNotify(\""+ cons[i].id +'\",\"' + tabId +"\",\"" + cons[i].fwtzid +"\")'>查看</div>" +
                        //         "<div onclick='notify(\""+ cons[i].id +'\",\"' + tabId +"\")'>通知</div></td>";
                        // }else{
                        //     str += "<td><div onclick='seeFwNotify(\""+ cons[i].id +'\",\"' + tabId +"\",\"" + cons[i].fwtzid +"\")'>查看</div></td>";
                        // }
                        str += "<td><div onclick='seeFwNotify(\""+ cons[i].id +'\",\"' + tabId +"\",\"" + cons[i].fwtzid +"\")'>查看</div></td>";
                        str += "</tr>";
                    }
//                    mTable.fnClearTable();
                    var mTable = $('#d-fw-notify-table').DataTable();
                    mTable.destroy();
                    $('#d-fw-notify-tbody').html(str);
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

function seeFwNotify(id,tabId,fwtzid) {
    // 由于后续的需求改动，这里更新的发文通知其实是这个人这条公文所有的发文
    console.log(fwtzid)
    AjaxTool.html('document/fwtzNE',{id: id, fwtzid:fwtzid},function (html) {
        $('.portlet-body').html(html);
        if(tabId == "yd"){
            $('#yd').attr('style','display:none');
        }
        $('#back').data('tabId',tabId);
    });

}
function notify(id,tabId){
    AjaxTool.html('document/gsnotify',{id: id},function (html) {
        $('.portlet-body').html(html);
        $('#back').data('tabId',tabId);

    })
}

function init() {//dataTable初始化
    mTable=DatatableTool.initDatatable("d-fw-notify-table", [ {
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

$( '#d-fw-notify-table' ).each(function( i ) {

    var worktable = $(this);
    var num_head_columns = worktable.find('thead tr th').length;
    var rows_to_validate = worktable.find('tbody tr');

    rows_to_validate.each( function (i) {

        var row_columns = $(this).find('td').length;
        for (i = $(this).find('td').length; i < num_head_columns; i++) {
            $(this).append('<td class="hidden"></td>');
        }

    });

});

$