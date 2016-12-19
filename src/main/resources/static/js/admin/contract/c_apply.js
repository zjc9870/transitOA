(function () {
    var ids = [];
    $('#uploadFile').click(function () {
        DatatableTool.modalShow("#upload-modal", "#fileUploadForm");
        var uploader = $("#fileUploadForm").FileUpload({
            url: "contract/uploadContractAttachment",
            isMultiFile: true,
        });
        uploader.done(function(data) {
            ids.push(data.result.id);
        });
    });


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
        AjaxTool.post('contract/saveContract', $('#c_apply_form').serialize()+"&bczl="+this.id+"&fileId="+ids, function (data) {
                alert(data.message);
            }
        )
    };
}

})();
