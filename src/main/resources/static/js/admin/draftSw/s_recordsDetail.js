(function () {
    var draftSwId = $('#draftSwId').val();
    var mjVal = $('#mjVal').val();
    var mjInput = $('input[name="mj"]');
    for(var i=0;i<mjInput.length;i++) {
        if(mjInput[i].value === mjVal) {
            mjInput[i].setAttribute('checked','checked');
            break;
        }
    }

    $('#selectCB').click(function(e){
        var button = e.target;
        if(button.name === "button") {
            var title = button.id =="chyr" ? "选择传阅人":"选择办理人";
            var s = "<form id='selectForm'> <div id='search-box'></div> "+
            "<div id='person-tree' ></div></form>";
            SweetAlert.swalPselect(title,s,function () {
                var tree = $('#person-tree').jstree(true);
                var ids = JSON.stringify(tree.get_bottom_checked());
                AjaxTool.post('draftSw/addCyr',{
                    draftSwId:draftSwId,ryfl:button.id,userIdList:ids},function (data) {
                    if(data.success) {
                        alert(data.message);
                        toSwjl();
                    }
                })
            })
        }
    });

    function toSwjl() {
        AjaxTool.getHtml('draftSw/swRecord',function (html) {
            $('.page-content').html(html);
        });
    }


    // $('#submit').click(function () {
    //     if(validator.form()) {
    //         AjaxTool.post('draftSw/addCyr', $('#c_approveForm').serialize()+"&id="+$('#data').val(), function (data) {
    //                 alert(data.message);
    //                 toHtsp();
    //             }
    //         )
    //     } else {
    //         alert("提交失败!");
    //     }
    // });

})();