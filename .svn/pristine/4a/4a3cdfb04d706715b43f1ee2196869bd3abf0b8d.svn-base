$().ready(function () {
    var validator = $('#c_backfillForm').validate({
        errorElement: 'span',
        errorClass: 'error-tips',
        rules: {
            bh: {
                required: true,
                isBlank: '',
                maxlength: 20
            }
        },

        messages: {
            bh: {
                required: "请输入合同编号",
                isBlank: "请输入合同编号",
                maxlength: "合同编号至多20个字"
            }
        }
    });

    $('#submit').click(function () {
        if(validator.form()) {
            AjaxTool.post('contract/bhht', $('#c_backfillForm').serialize(), function (data) {
                    alert(data.message);
                    if(data.success) {
                        toBhht();
                    }
                }
            )
        } else {
           alert('提交失败!');
        }
    });

    function toBhht() {
        AjaxTool.getHtml('contract/getBhhtList',function (html) {
            $('.page-content').html(html);
        });
    }


    $('#back').click(function () {
        var tabId = $('#back').data('tabId');
        AjaxTool.getHtml('contract/getBhhtList',function (html) {
            $('.page-content').html(html);
            setTimeout(function () {
                $('#'+tabId).trigger("click");
            },100);
        });
    });

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
        }
    );
});
