<%--
  Created by IntelliJ IDEA.
  User: zhangzhende
  Date: 2018/3/4
  Time: 13:35
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf-8"
         import="java.util.ArrayList"
         import="java.util.List"
         import="com.lucene.filesearch.model.FileModel"
         import="java.util.regex.*"
         import="com.lucene.filesearch.service.RegexHtml"
         import="java.util.Iterator" %>
<%
    String path = request.getContextPath();//获取工程根目录
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
    String regE_html = "<[^>]+>";
    //创建pattern对象
    Pattern pattern = Pattern.compile(regE_html);
    //创建matcher对象
    RegexHtml regexHtml = new RegexHtml();//为后面去除title中的HTML标签用
    List<FileModel> resultList = (List<FileModel>) request.getAttribute("resultList");
    String queryback = (String) request.getAttribute("queryback");

%>

<html>
<head>
    <title>搜索结果</title>
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta http-equiv="keyworlds" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="This is my Page">
    <meta http-equiv="Content-Type" content="text/html" charset="UTF-8">
    <base href="<%=basePath%>">

    <link type="text/css" rel="stylesheet" href="css/result.css">
</head>
<body>
<div class="searchbox">
    <div class="logo">
        <a href="index.jsp"><img src="images/search.png"></a>
    </div>
    <div class="searchform">
        <form action="SearchFile" method="get">
            <input type="text" name="query" value="<%=queryback%>">
            <input type="submit" value="SEARCH">
        </form>
    </div>
</div>
<div class="result">
    <h4>总共搜索到<span style="color:red;font-weight: bold;"><%=resultList.size()%></span>条结果</h4>
    <%
        if (resultList.size() > 0) {
            for (FileModel fileModel : resultList) {
    %>
    <div class="item">
        <div class="itemtop">
            <h4>
                <img alt="PDF" src="images/<%=fileModel.getTitle().split("\\.")[1]%>.png" class="doclogo">
                <%=fileModel.getTitle().split("\\.")[0]%>
            </h4>
            <h3>
                <a href="FileDownloadServlet?filename=<%=regexHtml.delHtmlTag(fileModel.getTitle())%>">单击下载</a>
            </h3>
        </div>
        <div class="itembuttom">
            <p><%=fileModel.getContent().length() > 210 ? fileModel.getContent().substring(0, 210) : fileModel.getContent()%>
                ...</p>
            <hr class="itemline">
        </div>
    </div>

    <%
            }
        }
    %>
</div>
<div>
    <p>Lucene 项目小案例</p>
    <p>&copy;张某人</p>
</div>

</body>
</html>
