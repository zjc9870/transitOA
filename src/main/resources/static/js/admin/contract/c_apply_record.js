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
                                str += "<td><div onclick='seeApplyRecordE(\""+ cons[i].id +"\")'>查看</div>" +
                                    "<div>提交</div>" +
                                    "<div onclick='deleteWtjCon(\""+ cons[i].id +"\")'>删除</div></td>";
                                break;
                            case "yth":
                                str += "<td><div onclick='seeApplyRecordE(\""+ cons[i].id +'\",\"' + tabId +"\")'>查看</div></td>";
                                break;
                            default:
                                str += "<td><div onclick='seeApplyRecordNE(\""+ cons[i].id +"\")'>查看</div></td>";
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

function seeApplyRecordE(id,tabId) {
    AjaxTool.html('contract/sqjlxqE',{id: id},function (html) {
        $('.portlet-body').html(html);
        if(tabId == "yth") {
            $('#splc').attr('style','display:block');
        }
    });
}

function seeApplyRecordNE(id) {
    AjaxTool.html('contract/sqjlxqNE',{id: id},function (html) {
        $('.portlet-body').html(html);
    });
}

function submitWtjForm() {
    AjaxTool.post('contract/saveContract', $('#c_apply_form').serialize()+"&bczl="+this.id, function (data) {
            alert(data.message);
        }
    )
};

function deleteWtjCon(id) {
    AjaxTool.post('contract/deleteContract',{
        id:id},function (data) {
        alert(data.message);
        window.location.reload();
    })
}

