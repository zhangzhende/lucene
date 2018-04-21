package P2_4;

import chapter2.P2_3.IKAnalyzer6x;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
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
 * 更新索引
 */
public class UpdateIndex {
    public static void main(String[] args) {
        Analyzer analyzer = new IKAnalyzer6x();
        IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
        Path indexPath = Paths.get("indexdir");
        Directory dir = null;
        try {
            dir = FSDirectory.open(indexPath);
            IndexWriter indexWriter = new IndexWriter(dir, iwc);
            Document doc = new Document();
            doc.add(new TextField("id", "2", Field.Store.YES));
            doc.add(new TextField("title", "北大开学喜迎大学僧", Field.Store.YES));
            doc.add(new TextField("content", "好多人啊，真是强到不行！！", Field.Store.YES));
            indexWriter.updateDocument(new Term("title", "北大"), doc);
            indexWriter.commit();
            indexWriter.close();
            System.out.println("更新成功！！");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
