package com.lucene.filesearch.controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * Created by zhangzhende on 2018/3/3.
 */
public class FileDownloadServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String CONTENT_TYPE = "text/html;charset=utf-8";

    public FileDownloadServlet() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.getWriter().append("serverd at:").append(request.getContextPath());
        response.reset();
        response.setContentType(CONTENT_TYPE);
        String filename = request.getParameter("filename");;
        System.out.println(filename);
        String filePath = request.getServletContext().getRealPath("/files") + File.separator + filename;
        System.out.println("文件路径:" + filePath);
        File file = new File(filePath);
        System.out.println(file.getPath());
        //设置response的编码方式
        response.setContentType("application/octet-stream");
        //写明要下载的文件大小
        response.setContentLength((int) file.length());
        //解决中文乱码，向客户端发送返回页面的头信息
        //1.content-disposition是mime协议的拓展
        //2.attachment 表示作为附件下载
        //3.在客户端将会弹出下载框
        //4.这是文件下载的关键代码
        response.setHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes("UTF-8"), "iso8859-1"));
        //读出文件到I/O流
        FileInputStream inputStream=new FileInputStream(file);
        BufferedInputStream bufferedInputStream=new BufferedInputStream(inputStream);
        byte[] b =new byte[1024];//相当于我们额缓存
        int k=0;//该值用于计算当前下载了多少字节
        OutputStream out =response.getOutputStream();
        //开始循环下载
        while(-1 !=(k=inputStream.read(b,0,b.length))){
            //将b中的数据写入到客户端内存
            out.write(b,0,k);
        }
        //将写入到客户端内存里的数据刷新到磁盘
        out.flush();
        inputStream.close();
        bufferedInputStream.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req,resp);
    }
}
