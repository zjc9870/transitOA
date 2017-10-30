$().ready(function () {
    qmxs();
    var ids = [];
    $('#uploadFile').click(function () {
        DatatableTool.modalShow("#upload-modal", "#fileUploadForm");
        var uploader = $("#fileUploadForm").FileUpload({
            url: "personalCon/uploadIndividualAttachment",
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

    var validator = $('#c_person_form').validate({
        errorElement: 'span', //default input error message container
        errorClass: 'error-tips', // default input error message class
        rules: {
            password: {
                minlength: 6,
                maxlength: 12,
                isRightCode: ''
            },
            newPassword: {
                equalTo: "#password1"
            },
            phoneNumber1: {
                phoneUS: true
            },
            phoneNumber2: {
                phoneUS: true
            },
            email: {
                email: true
            },
        },

        messages: {
            password: {
                minlength: "密码长度至少6位",
                maxlength: "密码长度不能超过12位",
                isRightCode: "密码只能由英文字母和数字组成"
            },
            newPassword: {
                equalTo: "两次输入密码不一致"
            },
            phoneNumber1: {
                phoneUS: "请输入正确的手机号"
            },
            phoneNumber2: {
                phoneUS: "请输入正确的手机号"
            },
            email: {
                email: "请输入正确的邮箱"
            }
        }
    });

    $('#reset').click(function () {
        $("input[disabled!='disabled']").val('');
        $("input[name='sex']").attr('checked',false);
    });
    $('#saveCon').click(function () {

        SweetAlert.swalInput("密码验证","请输入原密码","请在此处输入原密码",function(inputValue){

            AjaxTool.get("personalCon/validation",{originalPassword:inputValue},function(response){
                if(response.result){
                    var userId = $('#userId').val();
                    // var qmlx = $(uploadFile).val();
                    if(validator.form()) {
                        AjaxTool.post('personalCon/updateIndividualMessage', $('#c_person_form').serialize()+"&id="+userId,function (data) {
                            notify();
                            alert(data.message);

                        })
                    } else {
                        alert("保存失败");
                    }
                }else{
                    Toast.show("原密码输入错误",'更新信息失败');
                    // notify();
                }
            });
        });


    });
    //附件查看
    // $('#downloadFile').click(function () {
    //     if ($('#attachmentVoslist').val() == "") {
    //         alert('无附件');
    //     }
    //     else {
    //         var attachmentVoslist = JSON.parse($('#attachmentVoslist').val());
    //         var t = attachmentVoslist.length-1;
    //         var span2 = document.createElement('span');
    //         span2.setAttribute('id', attachmentVoslist[t].id);
    //         window.location = "personalCon/downloadIndividualAttachment?attachmentVosId=" + span2.id;
    //     }
    // });

    $('#cancel').click(function () {
        notify();
    })

    function notify(){
        window.location.reload();
    }





});

function qmxs(){
    // AjaxTool.get("attachment/qmxs",function(response){
    //     if(response.result){
    $(".qmxs").attr("src","attachment/qmxs");
    // }
    // });
}