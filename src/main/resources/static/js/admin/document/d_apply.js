$(document).ready(function () {
    var ids = [];
    $('#uploadFile').click(function () {
        DatatableTool.modalShow("#upload-modal", "#fileUploadForm");
        var uploader = $("#fileUploadForm").FileUpload({
            url: "document/uploadDocumentAttachment",
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



    var buttons = document.getElementById('saveCon').getElementsByTagName('button');
    for(var i=0; i<buttons.length;i++) {
        buttons[i].onclick= function() {
            var formContents = document.getElementsByClassName('form-content');
            for(var i=0; i<formContents.length; i++ ) {
                if(/^\s*$/.test(formContents[i].value)) {
                    sweetAlert('请填写完整信息!');
                    return false;
                }
            }
            if(validator.form()) {
                AjaxTool.post('document/saveDocument', $('#d_apply_form').serialize()+"&bczl="+this.id+"&fileId="+ids, function (data) {
                    sweetAlert(data.message);
                        if(data.success) toSqjl();

                    }
                )
            }
        };
    }

    function toSqjl() {
        AjaxTool.getHtml('document/sqjl',function (html) {
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

    var validator = $('#d_apply_form').validate({
        errorElement: 'span', //default input error message container
        errorClass: 'error-tips', // default input error message class
        rules: {
            zgs: {
                maxlength: 10
            },
            bt: {
                maxlength: 20
            },
            ztc: {
                maxlength: 300
            },
            userName :{
                maxlength: 10
            },
            mj:  {
                required : true,
            // },
            // sffb: {
            //     required : true
            }
        },
        messages: {
            zgs: {
                maxlength: "不超过10个字"
            },
            bt: {
                maxlength: "标题超过20个字"
            },
            ztc: {
                maxlength: "内容超过300个字"
            },
            userName: {
                maxlength: "主办人超过10个字"
            },
            mj: {
                required: "请选择密级"
            // },
            // sffb: {
            //     required: "请选择是否发布网站"
            }
        },
        errorPlacement: function(error, element) { //错误信息位置设置方法
            if (element.is(':radio') || element.is(':checkbox')) { //如果是radio或checkbox
                var eid = element.attr('name'); //获取元素的name属性
                error.appendTo(element.parent().parent()); //将错误信息添加当前元素的父结点后面
            } else if(element.attr('name')=='zgs'){
                error.appendTo(element.parent())
            } else {
                error.insertAfter(element);
            }
        }
    });
});
