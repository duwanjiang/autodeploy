<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2018/3/2
  Time: 11:22
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title></title>
  </head>
  <body>
    <form action="/syncFlow" method="post" enctype="multipart/form-data">
        上传文件：<input type="file" name="upload" /><br>
        用户名：<input type="text" name="username"><br>
        密码：<input type="text" name="password"><br>
        <button type="submit">上传</button>
    </form>
  </body>
</html>
