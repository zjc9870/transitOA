function submitForm() {
    AjaxTool.post('contract/bhht', $('#c_approveForm').serialize(), function (data) {
            alert(data.message);
        }
    )
}