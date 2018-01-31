var tabs = document.getElementById('tab').getElementsByTagName('button');
var roleName = $('#roleName').val();
for(var i=0; i<tabs.length; i++) {
    tabs[i].onclick = function () {
        var tabId = this.id;
        //若当前按钮已选中则点击不再触发事件
        if($(this).hasClass('button')) {return};
        AjaxTool.get('contract/sqjlTab', {
            lx: this.id, bz: 'sp'},function (data) {
                if(data.success) {
                    var str = "";
                    var cons = data.content;
                    for(var i=0;i<cons.length;i++) {
                        str += "<tr id='"+cons[i].id+"'>";
                        str += "<td>"+cons[i].htbt+"</td>";
                        str += "<td><div>"+cons[i].date+"</div><div>"+ cons[i].time +"</div></td>";
                        str += "<td>"+cons[i].userName+"</td>";
                        if(cons[i].spyj == null || cons[i].spyj == undefined)
                        	str += "<td>-</td>";
                        else str += "<td>"+cons[i].spyj +"</td>";
                        switch (tabId) {
                            case "dsp":
                                str += "<td>待审批</td>";
                                str += "<td><div onclick='seeApprove(\""+cons[i].id +'\",\"'+ tabId+"\")'>查看</div>" +
                                    "<div onclick='pass(\"" + cons[i].id +"\")'>通过</div></td>";
                                break;
                            case "yth":
                                str += "<td>已撤回</td>";
                                str += "<td><div onclick='seeApprove(\""+cons[i].id +'\",\"'+ tabId+"\")'>查看</div></td>";
                                break;
                            default:
                                var htshzt = cons[i].htshzt;
                                // if(htshzt == '通过'){
                                //     str += "<td><div style='color: green'>"+htshzt+"</div></td>";
                                // }
                                // else if(htshzt == '不通过') {
                                //     str += "<td><div style='color: red'>"+htshzt+"</div></td>";
                                // }
                                if(htshzt == '通过'){
                                    str += "<td><div style='color:green'>"+htshzt+"</div></td>";
                                }
                                else if(htshzt == '终止') {
                                    str += "<td><div style='color: red'>"+htshzt+"</div></td>";
                                }
                                else{
                                    str += "<td><div>"+cons[i].htshzt+"</div></td>";
                                }
                                str += "<td><div onclick='seeApprove(\""+cons[i].id +'\",\"'+ tabId+"\")'>查看</div></td>";
                                break;
                        }
                        str += "</tr>";
                    }
                    var mTable = $('#c-approve-table').DataTable();
                    mTable.destroy();
                    $('#c-approve-tbody').html(str);
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
    AjaxTool.html('contract/htspckxq',{id: id},function (html) {
        $('.portlet-body').html(html);
        switch (tabId) {
            case "ysp":
                $('.operation').attr('style','display:none');
                break;
            default:
                break;
        }
        $('#back').data('tabId',tabId);
        // if(roleName !=='法务') {
        //     $('.yj-input').html("<input type='hidden' name='cljg' value='已审核'/>");
        // }
    });
}

function pass(id) {
    var isConfir =confirm('您确认通过吗?');
    if(isConfir==true){
        AjaxTool.post('contract/addLcrz',{
            cljg: '通过', id:id},function (data) {
            alert(data.message);
            var table = $('#c-approve-table').DataTable();
            if(data.success) {
                table.rows('#'+id).remove().draw();
                refreshContract();
            }
        });
    }

}

function refreshContract(){
    var n = $('#合同审批').text();
    n--;
    if(n == 0){
        $('#合同审批').remove();
    }else{
        $('#合同审批').text(n);
    }
    var count = $('#合同审办').text();
    count--;
    if (count == 0){
        $('#合同审办').remove();
    }else{
        $('#合同审办').text(count);
    }
}

function init() {
	DatatableTool.initDatatable("c-approve-table", [{
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
