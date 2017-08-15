/**
 * Created by qifeng on 17/3/6.
 */
(function () {
    function createDelete() {
        var span = document.createElement('span');
        span.innerHTML = 'x';
        span.setAttribute('style','margin-left:10px;color:red;font-weight:bold;cursor:pointer');
        span.setAttribute('class','delete');
        return span;
    }
    function deletefj() {
        var deletes = document.getElementsByClassName('delete');
        for(var i=0;i<deletes.length;i++) {
            deletes[i].onclick = function () {
                ids.splice(ids.indexOf(this.id),1);
                this.parentNode.setAttribute('style','display:none;');
            }
        }
    }

    var ids = [];
    var s1 = createDelete();
    var fjlbLi = $('#fjlb li');
    fjlbLi.append(s1);
    for (var i=0;i<fjlbLi.length;i++) {
        ids.push(fjlbLi[i].id);
    }
    deletefj();


    $('#uploadFile').click(function () {
        DatatableTool.modalShow("#upload-modal", "#fileUploadForm");
        var uploader = $("#fileUploadForm").FileUpload({
            url: "document/uploadDocumentAttachment",
            isMultiFile: true,
        });
        uploader.done(function(data) {
            ids.push(data.result.id);
            var li = document.createElement('li');
            var s2 = createDelete();
            s2.setAttribute('id',data.result.id);
            li.innerHTML = data.result.name;
            li.appendChild(s2);
            $('#fjlb').append(li);
            deletefj();
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


    //提交
    $('#tj').click(function() {
        var formContents = document.getElementsByClassName('form-content');
        for(var i=0; i<formContents.length; i++ ) {
            if(/^\s*$/.test(formContents[i].value)) {
                alert('请填写完整信息!');
                return false;
            }
        }
        AjaxTool.post('document/saveDocument', $('#d_apply_form').serialize()+"&bczl="+this.id+"&fileId="+ids, function (data) {
                alert(data.message);
                toSqjl();
            }
        )
    });

    function toSqjl() {
        AjaxTool.getHtml('document/sqjl',function (html) {
            $('.page-content').html(html);
        });
    }


    $('#back').click(function () {
        var tabId = ($('#back').data('tabId'));
        AjaxTool.getHtml('document/sqjl',function (html) {
            $('.page-content').html(html);
            $('#'+tabId).trigger('click');
        });
    });

})();


