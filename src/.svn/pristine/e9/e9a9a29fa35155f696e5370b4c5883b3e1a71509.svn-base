$(document).ready(function () {
    var ids = [];
    $('#uploadFile').click(function () {
        DatatableTool.modalShow("#upload-modal", "#fileUploadForm");
        var uploader = $("#fileUploadForm").FileUpload({
            url: "draftSw/uploadDraftSwAttachment",
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
            if(validator.form()) {
                var ldId = document.getElementsByName('ldId')[0].value;
                AjaxTool.post('draftSw/saveSw', $('#s_incoming_form').serialize()+"&ldId="+ldId+"&bczl="+this.id+"&fileId="+ids, function (data) {
                        alert(data.message);
                        toSqjl();
                    }
                )
            }
        };
    }
    function toSqjl() {
        AjaxTool.getHtml('draftSw/swRecord',function (html) {
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

    var validator = $('#s_incoming_form').validate({
        errorElement: 'span', //default input error message container
        errorClass: 'error-tips', // default input error message class
        rules: {
            wjbt: {
                maxlength: 50
            },
            lwdw: {
                maxlength: 50
            },
            wh: {
                maxlength: 50
            },
            mj: {
                required: true
            },
            rq: {
                dateISO: true
            }
        },
        messages: {
            wjbt: {
                maxlength: "标题超过50个字"
            },
            lwdw: {
                maxlength: "单位超过50个字"
            },
            wh: {
                maxlength: "文号超过50个字"
            },
            mj: {
                required: "请选择密级"
            },
            rq: {
                dateISO: "日期格式不正确"
            }
        },
        errorPlacement: function(error, element) {
            error.appendTo(element.closest('li'));
        },
    });

    var mjVal = $('#mjVal').val();
    var mjInput = $('input[name="mj"]');
    for(var i=0;i<mjInput.length;i++) {
        if(mjInput[i].value === mjVal) {
            mjInput[i].setAttribute('checked','checked');
            break;
        }
    }


    $('#back').click(function () {
        var tabId = $('#back').data('tabId');
        AjaxTool.getHtml('draftSw/swRecord',function (html) {
            $('.page-content').html(html);
            $('#'+tabId).trigger('click');
        });
    });
});


