var Login = function() {

    var handleLogin = function() {

        $('.login-form').validate({
            errorElement: 'span', //default input error message container
            errorClass: 'help-block', // default input error message class
            focusInvalid: false, // do not focus the last invalid input
            rules: {
                username: {
                    required: true
                },
                password: {
                    required: true
                },
                remember: {
                    required: false
                }
            },

            messages: {
                username: {
                    required: "请输入用户名"
                },
                password: {
                    required: "请输入密码"
                }
            },

            invalidHandler: function(event, validator) { //display error alert on form submit   
//                $('.alert-danger', $('.login-form')).show();
            },

            highlight: function(element) { // hightlight error inputs
                $(element)
                    .closest('.form-group').addClass('has-error'); // set error class to the control group
            },

            success: function(label) {
                label.closest('.form-group').removeClass('has-error');
                label.remove();
            },

            errorPlacement: function(error, element) {
                error.insertAfter(element.closest('.input-icon'));
            },

            submitHandler: function(form) {
                form.submit(); // form validation success, call ajax form submit
                if($("input[name='remember-me']").is(':checked')) {
                    setCookies();
                }else {
                    removeCookies();
                }
            }
        });

        $('.login-form input').keypress(function(e) {
            if (e.which == 13) {
                if ($('.login-form').validate().form()) {
                    $('.login-form').submit(); //form validation success, call ajax form submit
                }
                return false;
            }
        });
    }

    var setCookies=function () {
        $.cookie('username',$("input[name='username']").val(),{expires:7});
        $.cookie('password',$("input[name='password']").val(),{expires:7});
    }

    var removeCookies=function () {
        $.removeCookie('username');
        $.removeCookie('password');
    }

    var getCookies=function(){
        var username=$.cookie('username');
        if(username){
            var password=$.cookie('password');
            $("input[name='username']").val(username);
            $("input[name='password']").val(password);
            $("input[name='remember-me']").attr("checked","checked");
        }
    }

    return {
        init: function() {
            handleLogin();
            getCookies();
        }

    };

}();

jQuery(document).ready(function() {
    Login.init();
});

