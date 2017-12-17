$(document).ready(function () {
    var ids = [];
    $('#uploadFile').click(function () {
        DatatableTool.modalShow("#upload-modal", "#fileUploadForm");
        var uploader = $("#fileUploadForm").FileUpload({
            url: "contract/uploadContractAttachment",
            isMultiFile: true
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



    var buttons = document.getElementById('saveCon').getElementsByTagName('button');
    for(var i=0; i<buttons.length;i++) {
        buttons[i].onclick= function() {
            var formContents = document.getElementsByClassName('form-content');
            for(var i=1; i<formContents.length; i++ ) {
                if(/^\s*$/.test(formContents[i].value)) {
                    alert('请填写完整信息!');
                    return false;
                }
            }
            if(validator.form()) {
                AjaxTool.post('contract/saveContract', $('#c_apply_form').serialize()+"&bczl="+this.id+"&fileId="+ids, function (data) {
                        alert(data.message);
                        if(data.success) toSqjl();
                    }
                )
            }
        };
    }

    function toSqjl() {
        AjaxTool.getHtml('contract/sqjl',function (html) {
            $('.page-content').html(html);
        });
    }


    $('.date-picker').datetimepicker({
        format:'yyyy/mm/dd',
        language: 'zh-CN',
        weekStart: 1,
        todayBtn:  1,
        autoclose: 1,
        todayHighlight: 1,
        startView: 2,
        minView: 2,
        forceParse: 0
    });

    var validator = $('#c_apply_form').validate({
        errorElement: 'span', //default input error message container
        errorClass: 'error-tips', // default input error message class
        rules: {
            sbd:{
                maxlength: 10
            },
            htbt: {
                maxlength: 50
            },
            htnr: {
                maxlength: 300
            },
            nqdrq: {
                dateISO: true
            }
            // qx: {
            //     dateISO: true
            // }
        },
        messages: {
            sbd: {
                maxlength: "不超过10个字"
            },
            htbt: {
                maxlength: "标题超过50个字"
            },
            htnr: {
                maxlength: "内容超过300个字"
            },
            nqdrq: {
                dateISO: "日期格式不正确"
            }
            // qx: {
            //     dateISO: "日期格式不正确"
            // }
        },
        errorPlacement: function(error, element) { //错误信息位置设置方法
            if(element.attr('name')=='sbd'){
                error.appendTo(element.parent())
            } else {
                error.insertAfter(element);
            }
        }
    });
});
