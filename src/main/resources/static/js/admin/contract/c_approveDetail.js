var yj = document.getElementsByClassName('yj-disagree')[0];
function show() {
    yj.setAttribute('style', 'visibility:visible');
}
function hide() {
    yj.setAttribute('style', 'visibility:hidden');
}

function submitForm() {
    AjaxTool.post('contract/addLcrz', $('#c_approveForm').serialize()+"&id="+$('#data').val(), function (data) {
            alert(data.message);
        }
    )
}