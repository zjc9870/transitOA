var tabs = document.getElementById('tab').getElementsByTagName('button');
for(var i=0; i<tabs.length; i++) {
    tabs[i].onclick = function () {
        var tabId = this.id;
        //若当前按钮已选中则点击不再触发事件
        AjaxTool.get('meeting/hytzTab', {
                lx: this.id},function (data) {
                if(data.success) {
                    var str = "";
                    var cons = data.content;
                    for(var i=0;i<cons.length;i++) {
                        fl = cons[i].hydd;
                        str += "<tr>";
                        str += "<td>"+cons[i].hyzt+"</td>";
                        switch(tabId){
                            case "yd":
                                str += "<td>已读</td>";
                                if (!cons[i].ydsj){
                                    str += "<td>-</td>";
                                }
                                else
                                    str += "<td>"+cons[i].ydsj+"</td>";

                                break;
                            case "wd":
                                str += "<td>未读</td>";
                                if (!cons[i].tzsj){
                                    str += "<td>-</td>";
                                }
                                else
                                    str += "<td>"+cons[i].tzsj+"</td>";
                                break;
                            default:
                                break;
                        }
                        str += "<td><div onclick='seeHyNotify(\""+ cons[i].id +'\",\"' + tabId +"\",\"" + cons[i].hytzid +"\",\""+fl+"\")'>查看</div></td>";
                        str += "</tr>";
                    }
//                    mTable.fnClearTable();
                    var mTable = $('#m-hy-notify-table').DataTable();
                    mTable.destroy();
                    $('#m-hy-notify-tbody').html(str);
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

function seeHyNotify(id,tabId,hytzid,fl) {
    AjaxTool.html('meeting/hytzNE',{id: id, hytzid:hytzid},function (html) {
        $('.portlet-body').html(html);
        if(tabId == "yd"){
            $('#yd').attr('style','display:none');
        }
        if(fl.indexOf("集团") != -1){
            $(".dsgj").attr('style','display:none');
        }else if(fl.indexOf("东山公交") != -1){
            $(".jt").attr('style','display:none');
        }
        $('#back').data('tabId',tabId);
    });
}


function init() {//dataTable初始化
    mTable=DatatableTool.initDatatable("m-hy-notify-table", [ {
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

$( '#m-hy-notify-table' ).each(function(i) {

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

