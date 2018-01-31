$(document).ready(function () {
    var validator = $('#d_approveForm').validate({
        errorElement: 'div',
        errorClass: 'error-tips',
        rules: {
            cljg: {
                required: true
            },
            yj: {
                maxlength: 300
            }
        },
        messages: {
            cljg: {
                required: "请选择处理结果"
            },
            yj: {
                maxlength: "意见字数不能超过300字"
            }
        },
        errorPlacement: function(error, element) {
            error.appendTo(element.parent());
        }
    });

    $('#submit').click(function () {
        if(validator.form()) {
            AjaxTool.post('document/addLcrz', $('#d_approveForm').serialize()+"&id="+$('#data').val(), function (data) {
                    alert(data.message);
                    toGwsp();
                    refreshDocument();
                }
            )
        } else {
            alert("提交失败!");
        }
    });

    function toGwsp() {
        AjaxTool.getHtml('document/fwsp',function (html) {
            $('.page-content').html(html);
        });
    }


    $('#back').click(function () {
        var tabId = $('#back').data('tabId');
        AjaxTool.getHtml('document/fwsp',function (html) {
            $('.page-content').html(html);
            $('#'+tabId).trigger('click');
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
