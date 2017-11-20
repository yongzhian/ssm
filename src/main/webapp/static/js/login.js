function login() {
    $.ajax({
        url: '/sysuser/login.do',
        type: "POST",
        data: {
            username: $('#username').val().trim(),
            password:$('#password').val().trim()
        },
        timeout: 5000,
        dataType: "json",
        success: function (data) {
            if (data.result == 0) { //成功
                window.self.location = "/pages/index.jsp";
            } else {
                alert("登录失败," + data.error_msg);
            }
        },
        error: function () {
            alert("系统错误。");
        }
    });
}