$('#searchCon').click(function () {
    AjaxTool.post('contract/searchContract',$('#htcxForm').serialize(),function (data) {
        if(data.success){
            var str = "";
            var cons = data.content;
            for(var i=0;i<cons.length;i++) {
                if(!cons[i].bh){
                    cons[i].bh = "待填写";
                }
                str += "<tr>";
                str += "<td>"+cons[i].htbt+"</td>";
                str += "<td>"+cons[i].bh+"</td>";
                str += "<td><div>"+cons[i].date+"</div><div>"+ cons[i].time +"</div></td>";
                str += "<td>"+cons[i].userName+"</td>";
                str += "<td>"+cons[i].htshzt+"</td>";
                str += "<td><div onclick='seeConDetail(\""+cons[i].id +"\")'>查看</div></td>";
                str += "</tr>";
            }
            var mTable = $('#c-find-table').DataTable();
            mTable.destroy();
            $('#c-approve-tbody').html(str);
            init();
        }
    });
});


function seeConDetail(id) {
    AjaxTool.html('contract/ssxq',{id:id},function (html) {
        $('.portlet-body').html(html);
    });
}

function init() {
    DatatableTool.initDatatable("c-find-table", [{
        'width': '30%',
        'targets': 0
    },
        {
            'orderable' : false,
            'targets' : [ 5 ]
        }, {
            "searchable" : false,
            "targets" : [ 5 ]
        }], [ [ 1, "desc" ] ]);
}

jQuery(document).ready(function() {
    init();
});

$('.date-picker').datetimepicker({
    format:'yyyy/mm/dd',
    language: 'zh-CN',
    weekStart: 1,
    todayBtn:  1,
    autoclose: 1,
    todayHighlight: 1,
    startView: 2,
    minView: 2,
    forceParse: 0
});