var tabs = document.getElementById('tab').getElementsByTagName('button');
for(var i=0; i<tabs.length; i++) {
    tabs[i].onclick = function () {
        var tabId = this.id;
        //若当前按钮已选中则点击不再触发事件
        AjaxTool.get('document/fwtzRecordTab', {
                lx: this.id},function (data) {
                if(data.success) {
                    var str = "";
                    var cons = data.content;
                    var userRole=$('#userrole').val();
                    for(var i=0;i<cons.length;i++) {
                        str += "<tr>";
                        str += "<td>"+cons[i].bt+"</td>";
                        switch(tabId){
                            case "yd":
                                str += "<td>已读</td>";
                                str += "<td>"+cons[i].tzlx+"</td>";
                                str += "<td>"+cons[i].tzdx+"</td>";
                                if (!cons[i].ydsj){
                                    str += "<td>-</td>";
                                }
                                else
                                    str += "<td>"+cons[i].ydsj+"</td>";
                                break;
                            case "wd":
                                str += "<td>未读</td>";
                                str += "<td>"+cons[i].tzlx+"</td>";
                                str += "<td>"+cons[i].tzdx+"</td>";
                                if (!cons[i].tzsj){
                                    str +="<td>-</td>"
                                }
                                else
                                    str +="<td>"+cons[i].tzsj+"</td>";
                                break;
                            default:
                                break;
                        }
                        str += "</tr>";
                    }
//                    mTable.fnClearTable();
                    var mTable = $('#d-notify-record-table').DataTable();
                    mTable.destroy();
                    $('#d-notify-record-tbody').html(str);
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

function init() {//dataTable初始化
    mTable=DatatableTool.initDatatable("d-notify-record-table", [ {
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