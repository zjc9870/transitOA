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
                        str += "<td>"+cons[i].sqsj+"</td>";
                        str += "<td>"+cons[i].htshzt+"</td>";
                        switch(tabId){
                            case "wtj":
                                str += "<td><div onclick='seeApplyRecord(\""+ cons[i].id +"\")'>查看</div>" +
                                    "<div>提交</div><div>删除</div></td>";
                                break;
                            default:
                                str += "<td><div onclick='seeApplyRecord(\""+ cons[i].id +"\")'>查看</div></td>";
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
                $(tabs[j]).removeClass("button").addClass("button-active");
            }
        }
    }
}

function seeApplyRecord(id) {
    AjaxTool.post('contract/sqjlxq',{id: id},function (html) {
        $('.portlet-body').html(html);
    });
}