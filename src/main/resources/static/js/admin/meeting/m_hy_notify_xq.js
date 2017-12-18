$('#yd').click(function(){
    var id=$('#id').html();
    var hytzid=$('#hytzid').html();
    AjaxTool.post('meeting/updateHytz',{meetingId:id,hytzid:hytzid},function(data){
        alert(data.message);
        if(data.success){
            toNotifyAll();
        }
    })
} );


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
                            window.location = "meeting/download?id=" + this.id;
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

})