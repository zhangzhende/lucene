package P2_4;

import chapter2.P2_3.IKAnalyzer6x;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by zhangzhende on 2018/1/31.
 *
 * 删除索引
 */
public class DeleteIndex {

    public static void main(String [] args){
    //删除title中含有关键词“美国”的文档
        deleteDoc("title","美国");
    }

    public static void deleteDoc(String field,String key){
        Analyzer analyzer=new IKAnalyzer6x();
        IndexWriterConfig iwc=new IndexWriterConfig(analyzer);
        Path indexPath= Paths.get("indexdir");
        Directory dir =null;


        try {
            dir= FSDirectory.open(indexPath);
            IndexWriter indexWriter=new IndexWriter(dir,iwc);
            indexWriter.deleteDocuments(new Term(field,key));
            indexWriter.commit();
            indexWriter.close();
            System.out.println("Delete Done!!");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
