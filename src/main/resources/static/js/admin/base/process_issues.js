$(".process").click(function (e) {
    if(e.target.title) {
        swal({
            title: "处理情况",
            text: e.target.title,
            html: true
        });
    }
})
