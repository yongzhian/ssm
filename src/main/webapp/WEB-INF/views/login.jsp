<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%
    String path = request.getContextPath();
    if(StringUtils.isBlank(path)){
        path = String.valueOf(request.getAttribute("path"));
    }
    System.out.println(path+"+++++++++++++++++++++++++++++");
    String url = request.getRequestURI();
%>
<!DOCTYPE>
<html>
<head>
    <title>SSM</title>
</head>
<body>
<h4>登录<%=path%></h4><%=request.getContextPath()%>
用户名：<input id="username" value="zain"/>
密码:<input type="password" id="password"/>
<button onclick="login('<%=path%>')">登录</button>
<h5>Power by Zain</h5>

<script type="text/javascript" src="<%=path%>/lib/jquery/1.9.1/jquery.js"></script>
<script charset="utf-8" type="text/javascript" src="<%=path%>/static/js/login.js"></script>
</body>
</html>
