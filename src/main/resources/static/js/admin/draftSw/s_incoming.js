$(document).ready(function () {
    var ids = [];
    $('#uploadFile').click(function () {
        DatatableTool.modalShow("#upload-modal", "#fileUploadForm");
        var uploader = $("#fileUploadForm").FileUpload({
            url: "contract/uploadContractAttachment",
            isMultiFile: true,
        });
        uploader.done(function(data) {
            ids.push(data.result.id);
            var li = document.createElement('li');
            var span = document.createElement('span');
            span.innerHTML = 'x';
            span.setAttribute('style','margin-left:10px;color:red;font-weight:bold;cursor:pointer');
            span.setAttribute('class','delete');
            span.setAttribute('id',data.result.id);
            li.innerHTML = data.result.name;
            li.appendChild(span);
            $('#fjlb').append(li);

            var deletes = document.getElementsByClassName('delete');
            for(var i=0;i<deletes.length;i++) {
                deletes[i].onclick = function () {
                    ids.splice(ids.indexOf(this.id),1);
                    this.parentNode.setAttribute('style','display:none;');
                }
            }
        });
    });



    var buttons = document.getElementById('saveDoc').getElementsByTagName('button');
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
                    toSqjl();
                }
            )
        };
    }

    function toSqjl() {
        AjaxTool.getHtml('contract/sqjl',function (html) {
            $('.page-content').html(html);
        });
    }
});

