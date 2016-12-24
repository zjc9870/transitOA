$('#searchCon').click(function () {
    AjaxTool.post('contract/searchContract',$('#htcxForm').serialize(),function (data) {
        if(data.success){
            var str = "";
            var cons = data.content;
            for(var i=0;i<cons.length;i++) {
                str += "<tr>";
                str += "<td>"+cons[i].htbt+"</td>";
                str += "<td>"+cons[i].bh+"</td>";
                str += "<td><div>"+cons[i].date+"</div><div>"+ cons[i].time +"</div></td>";
                str += "<td>"+cons[i].userName+"</td>";
                str += "<td>"+cons[i].htshzt+"</td>";
                str += "<td><div onclick='seeConDetail(\""+cons[i].id +"\")'>查看</div></td>";
                str += "</tr>";
            }
            $('#c-approve-tbody').html(str);
        }
    });
});


function seeConDetail(id) {
    AjaxTool.html('contract/ssxq',{id:id},function (html) {
        $('.portlet-body').html(html);
    });
}