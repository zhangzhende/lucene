<%--
  Created by IntelliJ IDEA.
  User: zhangzhende
  Date: 2018/3/3
  Time: 14:47
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf-8" import="java.util.Calendar" %>
<%
  Calendar calendar=Calendar.getInstance();
  int year= calendar.get(Calendar.YEAR);
%>
<html>
  <head>
    <title>lucene文件检索</title>
    <link type="text/css" rel="stylesheet" href="css/index.css">
  </head>
  <body>
  <h1 class="title">文件检索</h1>
  <div>
    <div class="content">
      <form action="SearchFile" method="get">
        <input type="text" name="query">
        <input type="submit" value="SEARCH">
      </form>
    </div>
    <div class="foot">
      <p>基于Lucene的文件检索系统</p>
      <br>
      <p>&copy;<%=year > 2016 ?(2016+"-"+year) : year%> 张某人 All Rights Reserved</p>
    </div>
  </div>
  </body>
</html>
