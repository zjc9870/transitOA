var tabs = document.getElementById('tab').getElementsByTagName('button');
for(var i=0; i<tabs.length; i++) {
    tabs[i].onclick = function () {
        AjaxTool.get('contract/sqjlTab', {
                lx: this.id, bz:'ht'},function (data) {
                if(data.success) {
                    var str = "";
                    var cons = data.content;
                    for(var i=0;i<cons.length;i++) {
                        str += "<tr>";
                        str += "<td>"+cons[i].htbt+"</td>";
                        str += "<td>"+cons[i].sqsj+"</td>";
                        str += "<td>"+cons[i].userName+"</td>";
                        str += "<td>-</td>";
                        str += "<td>-</td>";
                        str += "<td><div onclick='backfillDetail(\""+cons[i].id+"\")'>查看详情</div>" +
                            "<div onclick='print(\""+cons[i].id+"\")'>打印审办单</div></td>";
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

function backfillDetail(id) {
    AjaxTool.html('contract/bhhtxq',{id: id},function (html) {
        $('.portlet-body').html(html);
    });
}

function print(id) {
    AjaxTool.post('/wjsc',{
        xgdm:'jtht', xgid:id},function () {
        alert('下载成功');
    })
}