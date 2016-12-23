$('#tj').click(function() {
    var formContents = document.getElementsByClassName('form-content');
    for(var i=0; i<formContents.length; i++ ) {
        if(/^\s*$/.test(formContents[i].value)) {
            alert('请填写完整信息!');
            return false;
        }
    }
    AjaxTool.post('contract/saveContract', $('#c_apply_form').serialize()+"&bczl="+this.id, function (data) {
            alert(data.message);
        }
    )
});

$('#back').click(function () {
    AjaxTool.getHtml('contract/sqjl',function (html) {
        $('.page-content').html(html);
    });
});

//附件查看
$('#fjck').click(function () {
    var attachList = JSON.parse($('#attachList').val());
    for (var i = 0; i < attachList.length; i++) {
        var li = document.createElement('li');
        var div = document.createElement('div');
        div.innerHTML = attachList[i].name;
        div.setAttribute('style', 'cursor:pointer;');
        div.setAttribute('class', 'attachment');
        li.appendChild(div);
        this.parentNode.appendChild(li);
        div.id = attachList[i].id;              //将变量保存给对象,避免循环闭包
        div.onclick = function () {
            window.location = "attachment/download?id=" + this.id;
        }
    }
});


