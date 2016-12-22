var tabs = document.getElementById('tab').getElementsByTagName('button');
for(var i=0; i<tabs.length; i++) {
    tabs[i].onclick = function () {
        var tabId = this.id;
        AjaxTool.get('contract/sqjlTab', {
                lx: this.id, bz:'ht'},function (data) {
                if(data.success) {
                    var str = "";
                    var cons = data.content;
                    for(var i=0;i<cons.length;i++) {
                        str += "<tr>";
                        str += "<td>"+cons[i].htbt+"</td>";
                        str += "<td><div>"+cons[i].date+"</div><div>"+ cons[i].time +"</div></td>";
                        str += "<td>"+cons[i].userName+"</td>";
                        str += "<td>-</td>";
                        str += "<td>-</td>";
                        str += "<td><div onclick='backfillDetail(\""+cons[i].id +'\",\"'+cons[i].bh + "\")'>查看</div>" +
                            "<div onclick='print(\""+cons[i].id+"\")'>打印</div></td>";
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

function backfillDetail(id,bh) {
    AjaxTool.html('contract/bhhtxq',{id: id},function (html) {
        $('.portlet-body').html(html);
        if(bh) {
            $('#htbh').val(bh);
        }
    });
}

function print(id) {
	window.location = "/wjsc?xgdm=jtht&xgid=" +id; 
//    AjaxTool.post('/wjsc',{
//        xgdm:'jtht', xgid:id},function () {
//        alert('下载成功');
//    })
}
