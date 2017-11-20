function exoprt() {
    $.ajax({
        url: '/sysuser/export.do',
        type: "POST",
        data: {
            collectionName: $('#collectionName').val().trim(),
            pageNum: $('#pageNum').val().trim(),
            pageSize:$('#pageSize').val().trim()
        },
        timeout: 30000,
        dataType: "json",
        success: function (data) {
            if (data.result == 0) { //成功
                window.open(data.fileName);
            } else {
                alert("导出失败," + data.error_msg);
            }
        },
        error: function () {
            alert("系统错误。");
        }
    });
}

function exoprt2() {
   var collectionName= $('#collectionName').val().trim();
    var pageNum=$('#pageNum').val().trim();
    var pageSize=$('#pageSize').val().trim()
    window.open("/sysuser/export2.do?pageNum="+pageNum+"&pageSize="+pageSize+"&collectionName="+collectionName);
}