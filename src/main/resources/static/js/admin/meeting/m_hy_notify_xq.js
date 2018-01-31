$('#yd').click(function(){
    var id=$('#id').html();
    var hytzid=$('#hytzid').html();
    AjaxTool.post('meeting/updateHytz',{meetingId:id,hytzid:hytzid},function(data){
        alert(data.message);
        if(data.success){
            toNotifyAll();
            refreshHytz();
        }
    })
} );

function refreshHytz(){
    var n = $('#会议通知').text();
    n--;
    if(n == 0){
        $('#会议通知').remove();
    }else{
        $('#会议通知').text(n);
    }
    var count = $('#会议管理').text();
    count--;
    if (count == 0){
        $('#会议管理').remove();
    }else{
        $('#会议管理').text(count);
    }
}

$('#back').click(function () {
    var tabId = ($('#back').data('tabId'));
    AjaxTool.getHtml('meeting/hytz',function (html) {
        $('.page-content').html(html);
        $('#'+tabId).trigger('click');
    });
});
function toNotifyAll() {
    AjaxTool.getHtml('meeting/hytz',function (html) {
        $('.page-content').html(html);
    });
}





$(document).ready(function () {
    //附件查看
    // var n = 1;
    // $('#tzfjck').click(
    //     function () {
    //         var attachList = JSON.parse($('#attachList').val());
    //         var meetingId = $('#meetingId').val();
    //         if(attachList.length == 0) {
    //             alert('无附件');
    //         } else {
    //             if(n%2==1) {
    //                 for (var i = 0; i < attachList.length; i++) {
    //                     var li = document.createElement('li');
    //                     var div = document.createElement('div');
    //                     div.innerHTML = attachList[i].name;
    //                     div.setAttribute('style', 'cursor:pointer;');
    //                     div.setAttribute('class', 'attachment');
    //                     li.appendChild(div);
    //                     li.setAttribute('class', 'attList')
    //                     this.parentNode.appendChild(li);
    //                     div.id = attachList[i].id;              //将变量保存给对象,避免循环闭包
    //                     div.onclick = function () {
    //                         window.location = "meeting/meetingAttachmentDownload?attachmentId=" + this.id+"&meetingId="+meetingId;
    //                     }
    //                 }
    //             }
    //             else {
    //                 $('.attList').hide();
    //             }
    //             n += 1;
    //         }
    //     }
    // );
    //附件查看
    var n = 1;
    $('#tzfjck').click(
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
})