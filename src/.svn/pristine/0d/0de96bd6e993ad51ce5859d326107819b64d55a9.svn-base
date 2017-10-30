
$('.date-time-picker').datetimepicker({format: 'yyyy-mm-dd hh:ii'});

$(".js-example-basic-multiple").select2();

$(document).ready(function () {
    var ids = [];
    $('#uploadFile').click(function () {
        DatatableTool.modalShow("#upload-modal", "#fileUploadForm");
        var uploader = $("#fileUploadForm").FileUpload({
            url: "meeting/uploadMeetingAttachment",
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
                    alert('请填写完整信息!');
                    return false;
                }
            }
            if(validator.form()) {
                AjaxTool.post('meeting/saveMeeting', $('#m_apply_form').serialize()+"&bczl="+this.id+"&fileId="+ids, function (data) {
                        alert(data.message);
                        if(data.success) toSqjl();

                    }
                )
            }
        };
    }

    function toSqjl() {
        AjaxTool.getHtml('meeting/sqjl',function (html) {
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

    var validator = $('#m_apply_form').validate({
        errorElement: 'span', //default input error message container
        errorClass: 'error-tips', // default input error message class
        rules: {
            bt: {
                maxlength: 50
            },
            nr: {
                maxlength: 300
            },
            sqbgs: {
                maxlength: 50
            },
            sj:  {
                date: true
            },
            rs: {
                digits:true
            },
            rymd: {
                required:true
            }
        },
        messages: {
            bt: {
                maxlength: "标题超过50个字"
            },
            nr: {
                maxlength: "内容超过300个字"
            },
            sqbgs: {
                maxlength: "申请办公室不超过50个字"
            },
            sj:  {
                dateISO: "请输入有效的日期"
            },
            rs: {
                digits: "请输入整数"
            },
            rymd: {
                required: "请输入人员名单"
            }
        }
    });
});

