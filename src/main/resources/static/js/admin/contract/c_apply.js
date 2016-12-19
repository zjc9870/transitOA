var buttons = document.getElementById('saveCon').getElementsByTagName('button');
for(var i=0; i<buttons.length;i++) {
    buttons[i].onclick= function() {
        var formContents = document.getElementsByClassName('form-content');
        for(var i=0; i<formContents.length; i++ ) {
            if(/^\s*$/.test(formContents[i].value)) {
                alert('请填写完整信息!');
                return false;
            }
        }
        var files=new Array(0);
        files.push($('#c_apply_form input[name= "files"]').attr("id"));
        FileTool.ajaxFileUpload('contract/saveContract',files, $('#c_apply_form').serialize()+"&bczl="+this.id, function (data) {
                alert(data.message);
            }
        )
    };
}