<%@ page contentType="text/html;charset=UTF-8" %>
<%
    String path = request.getContextPath();
    String url = request.getRequestURI();
%>
<!DOCTYPE>
<html>
<head>
    <title>SSM</title>
</head>
<body>
<h4>登录</h4>
用户名：<input id="username" value="zain" />
密码:<input type="password" id="password" />
<button onclick="login()" >登录</button>
<h5>Power by Zain</h5>

<script charset="utf-8" type="text/javascript" src="/lib/jquery/1.9.1/jquery.js"></script>
<script charset="utf-8" type="text/javascript" src="/static/js/login.js"></script>
</body>
</html>
