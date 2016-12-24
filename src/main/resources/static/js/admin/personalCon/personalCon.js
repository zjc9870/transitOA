$('#password1').blur(function () {
    if(this.value != $('#password2').val()) {
        $('#passwordTips').text('密码与上一步输入的密码不一致');
        $('#passwordTips').css('color','red');
    } else if(this.value=='' && $('#password2').val()=='') {
        $('#passwordTips').text('重复输入的密码必须和上一步输入的密码保持一致');
        $('#passwordTips').css('color','rgb(150,150,150)');
    } else {
        $('#passwordTips').text('密码正确');
        $('#passwordTips').css('color','rgb(67,175,60)');
    }
});

$('#password2').blur(function () {
    if(this.value != $('#password1').val()) {
        $('#passwordTips').text('密码与上一步输入的密码不一致');
        $('#passwordTips').css('color','red');
    } else if(this.value=='' && $('#password1').val()=='') {
        $('#passwordTips').text('重复输入的密码必须和上一步输入的密码保持一致');
        $('#passwordTips').css('color','rgb(150,150,150)');
    } else {
        $('#passwordTips').text('密码正确');
        $('#passwordTips').css('color','rgb(67,175,60)');
    }
});


$('#reset').click(function () {
    $("input[disabled!='disabled']").val('');
    $("input[name='sex']").attr('checked',false);
});

$('#saveCon').click(function () {
    AjaxTool.post('personalCon/updateIndividualMessage', $('#c_person_form').serialize(),function () {
        alert('保存成功!');
    });
});