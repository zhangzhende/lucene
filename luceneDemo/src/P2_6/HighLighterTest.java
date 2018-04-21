package P2_6;

import chapter2.P2_3.IKAnalyzer6x;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by zhangzhende on 2018/2/27.
 * 查询结果高亮显示，关键词高亮
 */
public class HighLighterTest {
    public static void main(String args[]) throws IOException, ParseException, InvalidTokenOffsetsException {
        String filed ="title";
        Path indexpath= Paths.get("indexdir");//得到索引路径
        Directory dir = FSDirectory.open(indexpath);//打开索引
        IndexReader indexReader= DirectoryReader.open(dir);//读取索引
        IndexSearcher indexSearcher =new IndexSearcher(indexReader);//新建索引查询
        Analyzer analyzer =new IKAnalyzer6x();//获取分词器
        QueryParser parser=new QueryParser(filed,analyzer);//指定要查询对应字段的分词器
        Query query =parser.parse("北大");//查询内容
        System.out.println("Query:"+query.toString());
        QueryScorer scorer=new QueryScorer(query,filed);
        //定制高亮标签
        SimpleHTMLFormatter shformate=new SimpleHTMLFormatter("<span style = \" color:red\">","</span>");
        //高亮分词器
        Highlighter highlighter=new Highlighter(shformate,scorer);
        TopDocs tds=indexSearcher.search(query,10);
        for (ScoreDoc sd:tds.scoreDocs) {
            Document doc=indexSearcher.doc(sd.doc);
            System.out.println("Docid:"+sd.doc);
            System.out.println("id:"+doc.get("id"));
            System.out.println("title"+doc.get("title"));
            System.out.println("content"+doc.get("content"));
            TokenStream tokenStream= TokenSources.getAnyTokenStream(indexSearcher.getIndexReader(),sd.doc,filed,analyzer);
            Fragmenter fragmenter=new SimpleSpanFragmenter(scorer);
            highlighter.setTextFragmenter(fragmenter);
            String str =highlighter.getBestFragment(tokenStream,doc.get(filed));
            System.out.print("高亮的片段:"+str);
        }
        dir.close();
        indexReader.close();
    }
}
