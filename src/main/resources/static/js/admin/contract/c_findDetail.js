(function () {
    //附件查看
    var n = 1;
    $('#fjck').click(
        function () {
            if(n%2==1) {
                var attachList = JSON.parse($('#attachList').val());
                var contractId = $('#contractId').val();
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
                        window.location = "contract/contractAttachmentDownload?attachmentId=" + this.id+"&contractId="+contractId;
                    }
                }
            }
            else {
                $('.attList').hide();
            }
            n += 1;
        }
    );



    $('#back').click(function () {
        AjaxTool.getHtml('contract/htcx',function (html) {
            $('.page-content').html(html);
        });
    });


})();


