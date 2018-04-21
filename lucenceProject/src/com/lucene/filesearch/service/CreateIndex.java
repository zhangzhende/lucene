package com.lucene.filesearch.service;

import com.lucene.filesearch.model.FileModel;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zhangzhende on 2018/3/3.
 */
public class CreateIndex {
    public static void main(String args[]) throws IOException {
        Analyzer analyzer=new IKAnalyzer6x();
        IndexWriterConfig icw=new IndexWriterConfig(analyzer);
        icw.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        Directory dir=null;
        IndexWriter indexWriter=null;
        Path indexPath = Paths.get("web/indexdir");
        FieldType fieldType=new FieldType();
        fieldType.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS);
        fieldType.setStored(true);
        fieldType.setTokenized(true);
        fieldType.setStoreTermVectors(true);
        fieldType.setStoreTermVectorPositions(true);
        fieldType.setStoreTermVectorOffsets(true);
        //记录索引建立开始时间
        Date start =new Date();
        if(!Files.isReadable(indexPath)){
            System.out.println(indexPath.toAbsolutePath()+"不存在或者不可读");
            System.exit(1);
        }
        dir= FSDirectory.open(indexPath);
        indexWriter=new IndexWriter(dir,icw);
        List<FileModel> fileModelist=extractFile();
        //遍历fileModelList ，建立索引
        for (FileModel filemodel:fileModelist             ) {
            Document doc =new Document();
            doc.add(new Field("title",filemodel.getTitle(),fieldType));
            doc.add(new Field("content",filemodel.getContent(),fieldType));
            indexWriter.addDocument(doc);
        }
        indexWriter.commit();
        indexWriter.close();
        dir.close();
        Date end =new Date();
        //打印建立索引消耗时间
        System.out.println("索引文档完成，共消耗时间："+(end.getTime()-start.getTime())+"毫秒");

    }

    public static List<FileModel> extractFile() {
        List<FileModel> list = new ArrayList<>();
        File filedir = new File("web/files");
        File[] allFiles = filedir.listFiles();
        for (File f : allFiles) {
            FileModel sf = new FileModel(f.getName(), ParserExtraction(f));
            list.add(sf);
        }
        return list;
    }

    private static String ParserExtraction(File file) {
        String fileContent = "";
        BodyContentHandler handler = new BodyContentHandler();
        Parser parser = new AutoDetectParser();
        Metadata metadata = new Metadata();
        FileInputStream inputStream;
        try {
            inputStream = inputStream = new FileInputStream(file);
            ParseContext context = new ParseContext();
            parser.parse(inputStream, handler, metadata, context);
            fileContent = handler.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (TikaException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileContent;
    }
}
