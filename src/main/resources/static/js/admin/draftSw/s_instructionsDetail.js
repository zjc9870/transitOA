$(document).ready(function () {
    var mjVal = $('#mjVal').val();
    var mjInput = $('input[name="mj"]');
    for(var i=0;i<mjInput.length;i++) {
        if(mjInput[i].value === mjVal) {
            mjInput[i].setAttribute('checked','checked');
            break;
        }
    }

    var validator = $('#s_psForm').validate({
        errorElement: 'div',
        errorClass: 'error-tips',
        rules: {
            ldps: {
                maxlength: 300
            }
        },
        messages: {
            ldps: {
                maxlength: "字数不能超过300字"
            }
        },
        errorPlacement: function(error, element) {
            error.appendTo(element.parent());
        }
    });

    $('#submit').click(function () {
        if(validator.form()) {
            AjaxTool.post('draftSw/ldps', $('#s_psForm').serialize()+"&id="+$('#data').val(), function (data) {
                alert(data.message);
                toSwps();
            })
        } else {
            alert("提交失败!");
        }
    });

    function toSwps() {
        AjaxTool.getHtml('draftSw/swPsRecord',function (html) {
            $('.page-content').html(html);
        });
    }


    $('#back').click(function () {
        var tabId = $('#back').data('tabId');
        AjaxTool.getHtml('draftSw/swPsRecord',function (html) {
            $('.page-content').html(html);
            $('#'+tabId).trigger('click');
        });
    });


//附件查看
    var n = 1;
    $('#fjck').click(
        function () {
            if(n%2==1) {
                var attachList = JSON.parse($('#attachList').val());
                var contractId = $('#contractId').val();
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
                        window.location = "contract/contractAttachmentDownload?attachmentId=" + this.id+"&contractId="+contractId;
                    }
                }
            }
            else {
                $('.attList').hide();
            }
            n += 1;
        }
    );
})
