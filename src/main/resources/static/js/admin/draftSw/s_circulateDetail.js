$(document).ready(function () {
    var mjVal = $('#mjVal').val();
    var mjInput = $('input[name="mj"]');
    for(var i=0;i<mjInput.length;i++) {
        if(mjInput[i].value === mjVal) {
            mjInput[i].setAttribute('checked','checked');
            break;
        }
    }

    var validator = $('#s_cyForm').validate({
        errorElement: 'div',
        errorClass: 'error-tips',
        rules: {
            yy: {
                required: true
            },
            cyrYj: {
                maxlength: 300
            }
        },
        messages: {
            yy: {
                required: "还未批阅"
            },
            cyrYj: {
                maxlength: "字数不能超过300字"
            }
        },
        errorPlacement: function(error, element) {
            error.appendTo(element.parent());
        }
    });

    $('#submit').click(function () {
        if(validator.form()) {
            AjaxTool.post('draftSw/py', $('#s_cyForm').serialize()+"&draftSwId="+$('#data').val(), function (data) {
                alert(data.message);
                toSwcy();
                refreshSwcy();
            })
        } else {
            alert("提交失败!");
        }
    });

    function refreshSwcy() {
        var n = $('#收文传阅').text();
        n--;
        if(n == 0){
            $('#收文传阅').remove();
        }else{
            $('#收文传阅').text(n);
        }
        var count = $('#收文管理').text();
        count--;
        if (count == 0){
            $('#收文管理').remove();
        }else{
            $('#收文管理').text(count);
        }
    }

    function toSwcy() {
        AjaxTool.getHtml('draftSw/swCyRecord',function (html) {
            $('.page-content').html(html);
        });
    }


    $('#back').click(function () {
        var tabId = $('#back').data('tabId');
        AjaxTool.getHtml('draftSw/swCyRecord',function (html) {
            $('.page-content').html(html);
            $('#'+tabId).trigger('click');
        });
    });


    //附件查看
    var n = 1;
    $('#fjck').click(
        function () {
            var attachList = JSON.parse($('#attachList').val());
            var draftSwId = $('#draftSwId').val();
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



})
