/**
 * 
 */
function initDetail() {
	
	
    
    var validator = $('#c_approve_form').validate({
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
            AjaxTool.post('addLcrz', $('#c_approve_form').serialize()+"&id="+$('#data').val(), function (data) {
                    alert(data.message);
                    toHtsp();
                }
            )
        } else {
            alert("提交失败!");
        }
    });

    function toHtsp() {
        AjaxTool.getHtml('htsp',function (html) {
            $('.page-content').html(html);
        });
    }
};