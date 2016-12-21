var tabs = document.getElementById('tab').getElementsByTagName('button');
for(var i=0; i<tabs.length; i++) {
    tabs[i].onclick = function () {
        var tabId = this.id;
        AjaxTool.get('contract/sqjlTab', {
            lx: this.id, bz: 'sp'},function (data) {
                if(data.success) {
                    var str = "";
                    var cons = data.content;
                    for(var i=0;i<cons.length;i++) {
                        str += "<tr>";
                        str += "<td>"+cons[i].htbt+"</td>";
                        str += "<td><div>"+cons[i].date+"</div><div>"+ cons[i].time +"</div></td>";
                        str += "<td>"+cons[i].userName+"</td>";
                        str += "<td>-</td>";
                        switch (tabId) {
                            case "dsp":
                                str += "<td>待审批</td>";
                                str += "<td><div onclick='seeApprove(\""+cons[i].id +'\",\"'+ tabId+"\")'>查看</div><div>通过</div></td>";
                                break;
                            case "yth":
                                str += "<td>已退回</td>";
                                str += "<td><div onclick='seeApprove(\""+cons[i].id +'\",\"'+ tabId+"\")'>查看</div></td>";
                                break;
                            default:
                                str += "<td>已审批</td>";
                                str += "<td><div onclick='seeApprove(\""+cons[i].id +'\",\"'+ tabId+"\")'>查看</div></td>";
                                break;
                        }
                        str += "</tr>";
                    }
                    $('#c-approve-tbody').html(str);
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
    });
}

function pass(id) {
    AjaxTool.post('contract/addLcrz',{
        cljg: '通过', id:id},function () {
        alert('操作成功!');
    });
}