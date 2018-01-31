$('#yd').click(function(){
    var id=$('#id').html();
    var fwtzid=$('#fwtzid').html();
    AjaxTool.post('document/updateFwtz',{fwtzid:fwtzid},function(data){
        alert(data.message);
        if(data.success){
            toNotifyAll();
            refreshFwtz();
        }
    })
} );
 //附件查看
var n = 1;
$('#fjck').click(
    function () {
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
                        window.location = "attachment/download?id=" + this.id;
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
    var tabId = ($('#back').data('tabId'));
    AjaxTool.getHtml('document/fwtz',function (html) {
        $('.page-content').html(html);
        $('#'+tabId).trigger('click');
    });
});

function toNotifyAll() {
    AjaxTool.getHtml('document/fwtz',function (html) {
        $('.page-content').html(html);
    });
}

function refreshFwtz(){
    var n = $('#发文通知').text();
    n--;
    if(n == 0){
        $('#发文通知').remove();
    }else{
        $('#发文通知').text(n);
    }
    var count = $('#发文管理').text();
    count--;
    if (count == 0){
        $('#发文管理').remove();
    }else{
        $('#发文管理').text(count);
    }
}
