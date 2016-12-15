function checkCon() {
    var formContents = document.getElementsByClassName('form-content');
    for(var i=0; i<formContents.length; i++ ) {
        if(/^\s*$/.test(formContents[i].value)) {
            alert('请填写完整信息!');
            return false;
        }
    }
    document.getElementById('c_apply_form').submit();
}