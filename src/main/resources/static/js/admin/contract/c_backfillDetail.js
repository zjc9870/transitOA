function submitForm() {
    AjaxTool.post('contract/bhht', $('#c_backfillForm').serialize(), function (data) {
            alert(data.message);
        }
    )
}


$('#back').click(function () {
    AjaxTool.getHtml('contract/getBhhtList',function (html) {
        $('.page-content').html(html);
    });
});
