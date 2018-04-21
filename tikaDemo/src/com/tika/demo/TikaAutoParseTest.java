package com.tika.demo;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by zhangzhende on 2018/3/3.
 *
 * 测试tika自动按照文件类型选则合适的解析器解析
 */
public class TikaAutoParseTest {
    public static void main(String args[]) throws IOException, TikaException, SAXException {
//        UseTikaObject();
        UseParserInterfce();
    }

    /**
     * 方式1 ，使用Tika对象提取文档内容 主要方法parseToString（）；
     * @throws IOException
     * @throws TikaException
     */
    public static void UseTikaObject() throws IOException, TikaException {
        Tika tika=new Tika();
        //存放各种文件的files文件夹
        File fileDir =new File("files");
        if(!fileDir.exists()){
            System.out.println("文件夹不存在,");
            System.exit(0);
        }
        //获取文件夹下所有文件，存放在FIle数组中
        File[] fileArr =fileDir.listFiles();
        String fileContent;
        for (File f:fileArr  ) {
            fileContent=tika.parseToString(f);//自动解析
            System.out.println("Content:"+fileContent);
        }
    }

    /**
     * 使用Parser接口提取文档内容
     * @throws IOException
     * @throws TikaException
     * @throws SAXException
     */
    public static void UseParserInterfce() throws IOException, TikaException, SAXException {
        //新建存放各种文件的files文件夹
        File fileDir =new File("files");
        if(!fileDir.exists()){
            System.out.println("文件夹不存在,");
            System.exit(0);
        }
        //获取文件夹下所有文件，存放在FIle数组中
        File[] fileArr =fileDir.listFiles();
        //创建内容处理对象
        BodyContentHandler handler =new BodyContentHandler();
        //创建元数据对象
        Metadata metadata=new Metadata();
        FileInputStream inputStream=null;
        Parser parser =new AutoDetectParser();
        //自动检测分词器
        ParseContext context=new ParseContext();
        for (File f :fileArr             ) {
            inputStream =new FileInputStream(f);
            System.out.println(f.getPath());
            parser.parse(inputStream,handler,metadata,context);
            System.out.println(f.getName()+":\n"+handler.toString());
        }


    }
}
