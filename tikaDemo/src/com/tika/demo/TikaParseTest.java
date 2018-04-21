package com.tika.demo;

import org.apache.tika.exception.TikaException;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AbstractParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.asm.ClassParser;
import org.apache.tika.parser.html.HtmlParser;
import org.apache.tika.parser.microsoft.ooxml.OOXMLParser;
import org.apache.tika.parser.pdf.PDFParser;
import org.apache.tika.parser.txt.TXTParser;
import org.apache.tika.parser.xml.XMLParser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;

import java.io.*;

/**
 * Created by zhangzhende on 2018/3/3.
 * <p>
 * 测试tika从各种文件中提取内容并展示
 */
public class TikaParseTest {
    public static void main(String args[]) throws IOException, TikaException, SAXException {
//        PDF文件解析
//        PDFParser parser = new PDFParser();
//        String filepath = "files/hadoop-2.7.3+zookeeper-3.4.8+hadoop-2.7.3分布式环境搭建整理.pdf";
//        MS Office 文件解析，仅仅对于DOCX,PPTX等新文件格式
//        String filepath = "files/hadoop-2.7.3+zookeeper-3.4.8+hadoop-2.7.3分布式环境搭建整理.pptx";
//        OOXMLParser parser = new OOXMLParser();
//        TXT文件解析
//        String filepath = "files/hadoop-2.7.3+zookeeper-3.4.8+hadoop-2.7.3分布式环境搭建整理.txt";
//        TXTParser parser = new TXTParser();
//        HTML文件解析
//        String filepath = "files/Apache Download Mirrors.html";
//        HtmlParser parser = new HtmlParser();
//         XML文件解析
//        String filepath = "files/hdfs-site.xml";
//        XMLParser parser = new XMLParser();
//      class文件解析
        String filepath = "files/Pv.class";
        ClassParser parser = new ClassParser();
        tikaParse(parser,filepath);
    }
    /**
     * 文件的解析
     * @throws IOException
     * @throws TikaException
     * @throws SAXException
     */
    public static void tikaParse(AbstractParser fileParse,String filepath) throws IOException, TikaException, SAXException {
        //文件路径
//        String filepath = "files/hadoop-2.7.3+zookeeper-3.4.8+hadoop-2.7.3分布式环境搭建整理.pptx";
        //新建file对象
        File pdffile = new File(filepath);
        //创建文件内容处理对象
        BodyContentHandler handler = new BodyContentHandler();
        //创建元数据对象
        Metadata metadata = new Metadata();
        //读入文件
        FileInputStream inputStream = new FileInputStream(pdffile);
        //InputStream inputStream1= TikaInputStream.get(pdffile);
        //创建内容解析对象
        ParseContext parseContext = new ParseContext();
        //实例化OOXMLParser对象,该对象用于解析DOCX,PPTX等新格式
//        OOXMLParser parser = new OOXMLParser();
        //调用parse()方法解析文件
        fileParse.parse(inputStream, handler, metadata, parseContext);
        //遍历元数据内容
        printResult(metadata, handler);
    }
    /**
     * 输出文件内容的统一方法
     * @param metadata
     * @param handler
     */
    public static void printResult(Metadata metadata, BodyContentHandler handler) {
        System.out.println("文件属性信息：");
        for (String name : metadata.names()) {
            System.out.println(name + ":" + metadata.get(name));
        }
        //打印PDF文件内容
        System.out.println("打印PDF文件内容");
        System.out.println(handler.toString());
    }
    /**
     * PDF 文件的解析
     */
    public static void tikaParsePDF() throws IOException, TikaException, SAXException {
        //文件路径
        String filepath = "files/hadoop-2.7.3+zookeeper-3.4.8+hadoop-2.7.3分布式环境搭建整理.pdf";
        //新建file对象
        File pdffile = new File(filepath);
        //创建文件内容处理对象
        BodyContentHandler handler = new BodyContentHandler();
        //创建元数据对象
        Metadata metadata = new Metadata();
        //读入文件
        FileInputStream inputStream = new FileInputStream(pdffile);
        //InputStream inputStream1= TikaInputStream.get(pdffile);
        //创建内容解析对象
        ParseContext parseContext = new ParseContext();
        //实例化PDFParser对象
        PDFParser parser = new PDFParser();
        //调用parse()方法解析文件
        parser.parse(inputStream, handler, metadata, parseContext);
        //遍历元数据内容
        printResult(metadata, handler);
    }

    /**
     * MS Office 文件的解析
     * @throws IOException
     * @throws TikaException
     * @throws SAXException
     */
    public static void tikaParseOffice() throws IOException, TikaException, SAXException {
        //文件路径
        String filepath = "files/hadoop-2.7.3+zookeeper-3.4.8+hadoop-2.7.3分布式环境搭建整理.pptx";
        //新建file对象
        File pdffile = new File(filepath);
        //创建文件内容处理对象
        BodyContentHandler handler = new BodyContentHandler();
        //创建元数据对象
        Metadata metadata = new Metadata();
        //读入文件
        FileInputStream inputStream = new FileInputStream(pdffile);
        //InputStream inputStream1= TikaInputStream.get(pdffile);
        //创建内容解析对象
        ParseContext parseContext = new ParseContext();
        //实例化OOXMLParser对象,该对象用于解析DOCX,PPTX等新格式
        OOXMLParser parser = new OOXMLParser();
        //调用parse()方法解析文件
        parser.parse(inputStream, handler, metadata, parseContext);
        //遍历元数据内容
        printResult(metadata, handler);
    }

}
