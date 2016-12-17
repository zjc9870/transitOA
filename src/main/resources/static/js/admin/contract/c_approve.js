var tabs = document.getElementById('tab').getElementsByTagName('button');
for(var i=0; i<tabs.length; i++) {
    tabs[i].onclick = function () {
        AjaxTool.post('contract/test41', {
            lx: this.id},function (data) {
                if(data.success) {
                    var str = "<tr>";
                    var cons = data.content;
                    for(var i=0;i<cons.length;i++) {
                        str += "<td>"+cons[i].htbt+"</td>";
                        str += "<td>"+cons[i].htbt+"</td>";
                        str += "<td>"+cons[i].htbt+"</td>";
                        str += "<td>"+cons[i].htbt+"</td>";
                        str += "<td>"+cons[i].htbt+"</td>";
                        str += "<td>"+cons[i].htbt+"</td>";
                        str += "<td>"+cons[i].htbt+"</td>";
                    }
                    str += "</tr>";
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