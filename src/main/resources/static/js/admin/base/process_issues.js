$(".process").click(function (e) {
    if(e.target.title) {
        swal({
            title: "处理意见",
            text: e.target.title,
            html: true
        });
    }
})
