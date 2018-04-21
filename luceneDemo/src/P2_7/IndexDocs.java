package P2_7;

import chapter2.P2_3.IKAnalyzer6x;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;

/**
 * Created by zhangzhende on 2018/2/27.
 * Lucence新闻高频词提取1_索引文档代码部分，即将文档索引上
 *
 */
public class IndexDocs {
    public static void main(String args[]) throws IOException{
        File newsfile=new File("testfile/lucence.txt");
        String text=textToString(newsfile);
        Analyzer analyzer=new IKAnalyzer6x(true);
        IndexWriterConfig indexWriterConfig=new IndexWriterConfig(analyzer);
        indexWriterConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        //索引的存储路径
        Directory directory=null;
        //索引的增删改由indexwriter创建
        IndexWriter indexWriter=null;
        directory= FSDirectory.open(Paths.get("indexdir"));
        indexWriter=new IndexWriter(directory,indexWriterConfig);
        //新建fieldtype
        FieldType fieldType=new FieldType();
        //索引时保存文档、词项频率、位置信息、偏移信息
        fieldType.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS);
        fieldType.setStored(true);
        fieldType.setStoreTermVectors(true);
        fieldType.setTokenized(true);
        Document doc=new Document();
        Field field =new Field("content",text,fieldType);
        doc.add(field);
        indexWriter.addDocument(doc);
        indexWriter.close();
        directory.close();


    }

    public static String textToString(File file){
        StringBuilder stringBuilder=new StringBuilder();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String str=null;
            while((str=bufferedReader.readLine())!=null){
                stringBuilder.append(System.lineSeparator()+str);
            }
            bufferedReader.close();
        } catch (Exception e){
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
}
