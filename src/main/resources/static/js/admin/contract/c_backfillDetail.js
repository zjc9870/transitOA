function submitForm() {
    AjaxTool.post('contract/bhht', $('#c_backfillForm').serialize(), function (data) {
            alert(data.message);
        }
    )
}

