/**
 * Created by qifeng on 17/3/22.
 */
$('#tz').click(function (){
    var id=$('#id').html();
    AjaxTool.post('document/savenotify',$('#d_apply_notify_form').serialize()+"&id="+id, function(data){
        alert(data.message);
        if (data.obj !="" && data.obj !=" "){
            Toast.show("通知保存提醒","通知对象重复");
        };
        $('#back').trigger('click');
        })

});

//附件查看
var n = 1;
$('#fjck').click(function () {
        var attachList = JSON.parse($('#attachList').val());
        var contractId = $('#contractId').val();
        if(attachList.length == 0) {
            alert('无附件');
        } else {
            if(n%2==1) {
                for (var i = 0; i < attachList.length; i++) {
                    var li = document.createElement('li');
                    var div = document.createElement('div');
                    div.innerHTML = attachList[i].name;
                    div.setAttribute('style', 'cursor:pointer;');
                    div.setAttribute('class', 'attachment');
                    li.appendChild(div);
                    li.setAttribute('class', 'attList')
                    this.parentNode.appendChild(li);
                    div.id = attachList[i].id;              //将变量保存给对象,避免循环闭包
                    div.onclick = function () {
                        // window.location = "contract/contractAttachmentDownload?attachmentId=" + this.id+"&contractId="+contractId;
                        window.location = "attachment/download?id=" + this.id;
                    }
                }
            }
            else {
                $('.attList').hide();
            }
            n += 1;
        }
    });


//多选
$("#zsjtgg").select2({
    tags: true,
    placeholder: "请选择",
});

//多选
$("#zsjtbm").select2({
    tags: true,
    placeholder: "请选择",
});

$("#zsqtgsbgs").select2({
    tags: true,
    placeholder: "请选择",
});

$("#cbjtgg").select2({
    tags: true,
    placeholder: "请选择",
});

$("#cbjtbm").select2({
    tags: true,
    placeholder: "请选择",
});

$("#cbqtgsbgs").select2({
    tags: true,
    placeholder: "请选择",
});

$("#csjtgg").select2({
    tags: true,
    placeholder: "请选择",
});
$("#csjtbm").select2({
    tags: true,
    placeholder: "请选择",
});
$("#csqtgsbgs").select2({
    tags: true,
    placeholder: "请选择",
});

// $('#back').click(function () {
//     var tabId = ($('#back').data('tabId'));
//     AjaxTool.getHtml('document/sqjl',function (html) {
//         $('.page-content').html(html);
//         $('#'+tabId).trigger('click');
//     });
// })
$('#back').click(function () {
    var tabId = ($('#back').data('tabId'));
    AjaxTool.getHtml('document/sqjl',function (html) {
        $('.page-content').html(html);
        $('#'+tabId).trigger('click');
    });
});

function toSqjl() {
    AjaxTool.getHtml('document/sqjl',function (html) {
        $('.page-content').html(html);
    });

}