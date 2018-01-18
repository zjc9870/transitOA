

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

//多选
$("#dsgjgg").select2({
    tags: true,
    placeholder: "请选择",
});

//多选
$("#dsgjbm").select2({
    tags: true,
    placeholder: "请选择",
});

$("#gsbgs").select2({
    tags: true,
    placeholder: "请选择",
});

$(document).ready(function() {

    var checkbox = document.getElementsByName("hygg");
    var hygg = $("#hygg").val();
    var hyggStr = new Array();
    hyggStr = hygg.split(",");
    if(hyggStr.length != 0){
        for(i = 0; i < hyggStr.length; i++){
            for(j = 0;j < checkbox.length; j++){
                if(hyggStr[i] == checkbox[j].value){
                    checkbox[j].checked = true;
                }
            }
        }
    }

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
        var jtgg=$('#jtgg').val();
        var jtbm=$('#jtbm').val();
        var dsgjgg = $('#dsgjgg').val();
        var dsgjbm = $('#dsgjbm').val();
        var gsbgs = $('#gsbgs').val()
        var wbdw=$('#wbdw').val();
        if(jtgg != null || jtbm != null || dsgjgg != null || dsgjbm != null || gsbgs != null || wbdw !="" ){
            AjaxTool.post('meeting/savenotify',$('#m_notify_form').serialize()+"&id="+id+"&fileId="+ids, function(data) {
                alert(data.message);
                if ($.trim(data.obj) != ""){
                    Toast.show("通知对象保存提醒","通知对象重复");
                }
                $('#back').trigger('click');
            });
        }else{
            alert("请填入通知人信息！");
        }
    });

    var validator = $('#m_notify_form').validate({
        errorElement: 'span', //default input error message container
        errorClass: 'error-tips', // default input error message class
        rules: {
            wbdw: {
                maxlength: 100
            }

        },
        messages: {
            wbdw: {
                maxlength: "(不超过100个字)"
            }
        }
    });
});