var tabs = document.getElementById('tab').getElementsByTagName('button');
for(var i=0; i<tabs.length; i++) {
    tabs[i].onclick = function () {
        var tabId = this.id;
        //若当前按钮已选中则点击不再触发事件
        if($(this).hasClass('button')) {return};
        if(tabId == 'yht') {
            $('#table-title').prepend("<th id='htbh'>合同编号</th>");
        } else {
            $('#htbh').remove();
        }
        AjaxTool.get('contract/sqjlTab', {
                lx: this.id, bz:'ht'},function (data) {
                if(data.success) {
                    var str = "";
                    var cons = data.content;
                    for(var i=0;i<cons.length;i++) {
                        str += "<tr>";
                        if(tabId == 'yht') {
                            str += "<td>"+cons[i].bh+"</td>";
                        }
                        str += "<td>"+cons[i].htbt+"</td>";
                        str += "<td><div>"+cons[i].date+"</div><div>"+ cons[i].time +"</div></td>";
                        str += "<td>"+cons[i].userName+"</td>";
                        str += "<td><div onclick='backfillDetail(\""+cons[i].id +'\",\"'+cons[i].bh + '\",\"'+tabId + "\")'>查看</div>" +
                            "<div onclick='print(\""+cons[i].id+"\")'>打印</div></td>";
                        str += "</tr>";
                    }
                    var mTable = $('#c-backfill-table').DataTable();
                    mTable.destroy();
                    $('#c-approve-tbody').html(str);
                    init(tabId);
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

function backfillDetail(id,bh,tabId) {
    AjaxTool.html('contract/bhhtxq',{id: id},function (html) {
        $('.portlet-body').html(html);
        if(bh) {
            $('#htbh').val(bh);
        }
        $('#back').data('tabId',tabId);
    });
}

function print(id) {
	window.location = "/wjsc?xgdm=jtht&xgid=" +id; 
}

function init(tabId) {
    mTable=DatatableTool.initDatatable("c-backfill-table",
        [ {
            'orderable' : false,
            'targets' : [ 3 ]
        }, {
            "searchable" : false,
            "targets" : [ 3 ]
        }],
        [ tabId=='yht'?[2,"desc"]:[ 1, "desc" ] ]);


}

jQuery(document).ready(function() {
	init();
});
