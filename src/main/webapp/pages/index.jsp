<%@ page contentType="text/html;charset=UTF-8" %>
<%
    String path = request.getContextPath();
    String url = request.getRequestURI();
%>
<!DOCTYPE>
<html>
<head>
    <title>SSM index</title>
</head>
<body>
<h2>springmvc+spring+mybatis框架</h2>
<h3>欢迎${sysUser.username}</h3>


    collection:<input id="collectionName" value="http_api_log">
    page num:<input id="pageNum" value="1">
    page Size:<input id="pageSize" value="20">
    <input type="button" value="导出" onclick="exoprt()">

    <input type="button" value="流导出" onclick="exoprt2()">


<h5>Power by Zain</h5>

<script charset="utf-8" type="text/javascript" src="/lib/jquery/1.9.1/jquery.js"></script>
<script charset="utf-8" type="text/javascript" src="/static/js/index.js"></script>
</body>
</html>