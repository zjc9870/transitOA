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
                var tree = $('#person-tree').jstree({
                    "plugins" : [ 
                                 "themes","json_data"
                             ],
                             "themes":{
                                 "theme" : "classic",
                                 "dots" : true,
                                 "icons" : true
                             }
                });
                var ids = tree.get_bottom_checked();
                var names = [];
                var comfirmMsg = "未添加任何人员";
                for(var i=0;i<ids.length;i++){
                    var name = ids[i].split(",")[1];
                    if(typeof(name)!="undefined"){
                        names.push(name);
                    }
                }
                if(names.length>0) {
                    comfirmMsg = "将添加以下人员:";
                    for (var j = 0; j < names.length; j++) {
                        comfirmMsg = comfirmMsg + "\n" + names[j];
                    }
                }
                var Comfirm = window.confirm(comfirmMsg);
                if(Comfirm){
                	AjaxTool.post('draftSw/addCyr',"draftSwId="+draftSwId+"&ryfl="+button.id+"&userIdList="+ids,function (data) {
                        if(data.success) {
                            alert(data.message);
                            toSwjl();
                        }
                    })
                }
            })
        }
    });

    $('#end').click(function () {
        AjaxTool.post('draftSw/terminate',{draftSwId:draftSwId},function (data) {
            if(data.success) {
                alert(data.message);
                toSwjl();
            }
        })
    })

    function toSwjl() {
        AjaxTool.getHtml('draftSw/swRecord',function (html) {
            $('.page-content').html(html);
        });
    }


    //附件查看
    var n = 1;
    $('#fjck').click(
        function () {
            var attachList = JSON.parse($('#attachList').val());
            var draftSwId = $('#draftSwId').val();
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

    $('#back').click(function () {
        var tabId = $('#back').data('tabId');
        AjaxTool.getHtml('draftSw/swRecord',function (html) {
            $('.page-content').html(html);
            $('#'+tabId).trigger('click');
        });
    });


})();