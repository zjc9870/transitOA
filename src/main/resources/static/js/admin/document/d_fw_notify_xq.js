$('#yd').click(function(){
    var id=$('#id').html();
    var fwtzid=$('#fwtzid').html();
    AjaxTool.post('document/updateFwtz',{fwtzid:fwtzid},function(data){
        alert(data.message);
    })
} );

//附件查看
var n = 1;
$('#fjck').click(
    function () {
        if(n%2==1) {
            var attachList = JSON.parse($('#attachList').val());
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
);

$('#back').click(function () {
    var tabId = ($('#back').data('tabId'));
    AjaxTool.getHtml('document/fwtz',function (html) {
        $('.page-content').html(html);
        $('#'+tabId).trigger('click');
    });
});