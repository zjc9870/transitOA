$(function() {
	var ids = [];
	$('#tj').click(function() {
        if(validator.form()) {
		 AjaxTool.post('saveContract', $('#c_apply_form').serialize()+"&bczl="+this.id+"&fileId="+ids, function (data) {
             alert(data.message);
             if(data.success) toSqjl();
	     
         });
        }else{
        	alert("请检查合同格式");
        }
	});
	
	$('#bc').click(function() {
		 AjaxTool.post('saveContract', $('#c_apply_form').serialize()+"&bczl="+this.id+"&fileId="+ids, function (data) {
            alert(data.message);
            if(data.success) toSqjl();
        }
    )
	});
	
	var validator = $('#c_apply_form').validate({
        errorElement: 'span', //default input error message container
        errorClass: 'error-tips', // default input error message class
        rules: {
            htbt: {
                maxlength: 50
            },
            htnr: {
                maxlength: 300
            }
        },
        messages: {
            htbt: {
                maxlength: "标题超过50个字"
            },
            htnr: {
                maxlength: "内容超过300个字"
            }
        }
    });
	
    function toSqjl() {
        AjaxTool.getHtml('contract/sqjl',function (html) {
            $('.page-content').html(html);
        });
    }
})