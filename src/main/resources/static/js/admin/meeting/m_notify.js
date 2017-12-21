

//附件查看
var n = 1;
$('#fjck').click(
    function () {
        var attachList = JSON.parse($('#attachList').val());
        var meetingId = $('#meetingId').val();
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
                        window.location = "meeting/meetingAttachmentDownload?attachmentId=" + this.id+"&meetingId="+meetingId;
                        // window.location = "attachment/download?id=" + this.id;
                    }
                }
            }
            else {
                $('.attList').hide();
            }
            n += 1;
        }
    }
);

$('#back').click(function () {
    var tabId = $('#back').data('tabId');
    AjaxTool.getHtml('meeting/hysp',function (html) {
        $('.page-content').html(html);
        $('#'+tabId).trigger('click');
    });
});

//多选
$("#jtgg").select2({
    tags: true,
    placeholder: "请选择",
});

//多选
$("#jtbm").select2({
    tags: true,
    placeholder: "请选择",
});

$("#gsbgs").select2({
    tags: true,
    placeholder: "请选择",
});

$(document).ready(function() {
    var ids = [];
    $('#uploadFile').click(function () {
        DatatableTool.modalShow("#upload-modal", "#fileUploadForm");
        var uploader = $("#fileUploadForm").FileUpload({
            url: "meeting/uploadMeetingAttachment",
            isMultiFile: true,
        });
        uploader.done(function(data) {
            ids.push(data.result.id);
            var li = document.createElement('li');
            var span = document.createElement('span');
            span.innerHTML = 'x';
            span.setAttribute('style','margin-left:10px;color:red;font-weight:bold;cursor:pointer');
            span.setAttribute('class','delete');
            span.setAttribute('id',data.result.id);
            li.innerHTML = data.result.name;
            li.appendChild(span);
            $('#fjlb').append(li);

            var deletes = document.getElementsByClassName('delete');
            for(var i=0;i<deletes.length;i++) {
                deletes[i].onclick = function () {
                    debugger;
                    ids.splice(ids.indexOf(this.id),1);
                    this.parentNode.setAttribute('style','display:none;');
                }
            }
        });
    });

    $('#tz').click(function (){
        var id = $('#id').html();
        AjaxTool.post('meeting/savenotify',$('#m_notify_form').serialize()+"&id="+id+"&fileId="+ids, function(data) {
            alert(data.message);
            if (data.obj != "" && data.obj != " "){
                Toast.show("通知对象保存提醒","通知对象重复");
            }
            $('#back').trigger('click');
        })
    })
});