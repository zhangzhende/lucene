package P2_5;

import chapter2.P2_3.IKAnalyzer6x;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.IntPoint;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by zhangzhende on 2018/2/5.
 *
 * Query语句的查询测试
 */
public class QueryParseTest {
    public static void  main(String [] args) throws IOException, ParseException {
//        multiFieldQueryParser();
//        termQueryParser();
//        boolQueryParser();
//        rangeQueryParser();
//        prefixQueryParser();
//        phraseQueryParser();
//        fuzzyQueryParser();
        wildcardQueryParser();
    }

    /**
     * 普通查询，单子段，关键词，全匹配
     * @throws IOException
     * @throws ParseException
     */
    public static void queryParter()throws IOException, ParseException{
        String field="title";
        Path indexPath= Paths.get("indexdir");
        Directory dir= FSDirectory.open(indexPath);
        IndexReader indexReader= DirectoryReader.open(dir);
        IndexSearcher searcher=new IndexSearcher(indexReader);
        Analyzer analyzer=new IKAnalyzer6x();
        QueryParser parser=new QueryParser(field,analyzer);
        parser.setDefaultOperator(QueryParser.Operator.AND);
        Query query=parser.parse("北大");//查询关键词
        System.out.println();
        //返回前10条
        TopDocs topdocs=searcher.search(query,10);
        for (ScoreDoc scoreDoc:topdocs.scoreDocs) printResult(scoreDoc, searcher);
        dir.close();
        indexReader.close();
    }

    /**
     * 多域查询，查询多个字段是否包含同一个关键词,只要有一个包含
     * @throws IOException
     * @throws ParseException
     */
    public  static void multiFieldQueryParser() throws IOException, ParseException {
        String fields[] = {"title","content"};
        Path indexPath=Paths.get("indexdir");
        Directory dir = FSDirectory.open(indexPath);
        IndexReader indexReader=DirectoryReader.open(dir);
        IndexSearcher searcher=new IndexSearcher(indexReader);
        Analyzer analyzer=new IKAnalyzer6x(true);
        MultiFieldQueryParser parser=new MultiFieldQueryParser(fields,analyzer);
        parser.setDefaultOperator(QueryParser.Operator.AND);
        Query query=parser.parse("北大");
        System.out.println("query:"+query.toString());
        TopDocs topDocs =searcher.search(query,10);
        for (ScoreDoc scoreDoc:topDocs.scoreDocs) printResult(scoreDoc, searcher);
        dir.close();
        indexReader.close();
    }

    /**
     * 词项搜索，查询指定字段包含什么关键词的文档
     * @throws IOException
     * @throws ParseException
     */
    public  static void termQueryParser() throws IOException, ParseException {
        Term term =new Term("title","北大");
        Path indexPath=Paths.get("indexdir");
        Directory dir = FSDirectory.open(indexPath);
        IndexReader indexReader=DirectoryReader.open(dir);
        IndexSearcher searcher=new IndexSearcher(indexReader);
//        Analyzer analyzer=new IKAnalyzer6x(true);
//        MultiFieldQueryParser parser=new MultiFieldQueryParser(fields,analyzer);
//        parser.setDefaultOperator(QueryParser.Operator.AND);
        Query termquery=new TermQuery(term);
        System.out.println("query:"+termquery.toString());
        TopDocs topDocs =searcher.search(termquery,10);
        for (ScoreDoc scoreDoc:topDocs.scoreDocs) printResult(scoreDoc, searcher);
        dir.close();
        indexReader.close();
    }

    /**
     * 布尔查询，实则是各查询的组合，各条件关系有must，mustnot等等，
     * 【以下查询为智能分词后包含于不含词的查询，结果为空说明无合理条件】
     * @throws IOException
     * @throws ParseException
     */
    public  static void boolQueryParser() throws IOException, ParseException {
        Path indexPath=Paths.get("indexdir");
        Directory dir = FSDirectory.open(indexPath);
        IndexReader indexReader=DirectoryReader.open(dir);
        IndexSearcher searcher=new IndexSearcher(indexReader);
        Query termquery1=new TermQuery(new Term("title","北大"));
        Query termquery2=new TermQuery(new Term("content","不行"));
        BooleanClause bc1=new BooleanClause(termquery1, BooleanClause.Occur.MUST);
        BooleanClause bc2=new BooleanClause(termquery2, BooleanClause.Occur.MUST_NOT);
        BooleanQuery query=new BooleanQuery.Builder().add(bc1).add(bc2).build();
        System.out.println("query:"+query.toString());
        TopDocs topDocs =searcher.search(query,10);
        for (ScoreDoc scoreDoc:topDocs.scoreDocs) printResult(scoreDoc, searcher);
        dir.close();
        indexReader.close();
    }

    /**
     * 范围查询，查询指定范围内的文档
     * @throws IOException
     * @throws ParseException
     */
    public  static void rangeQueryParser() throws IOException, ParseException {
        Path indexPath=Paths.get("indexdir");
        Directory dir = FSDirectory.open(indexPath);
        IndexReader indexReader=DirectoryReader.open(dir);
        IndexSearcher searcher=new IndexSearcher(indexReader);
        Query rangeQuery= IntPoint.newRangeQuery("reply",1,1000);
        System.out.println("query:"+rangeQuery.toString());
        TopDocs topDocs =searcher.search(rangeQuery,10);
        for (ScoreDoc scoreDoc:topDocs.scoreDocs) printResult(scoreDoc, searcher);
        dir.close();
        indexReader.close();
    }

    /**
     * 前缀查询，此处为分词后的词中包含以学字开头的词的文档
     * @throws IOException
     * @throws ParseException
     */
    public  static void prefixQueryParser() throws IOException, ParseException {
        Path indexPath=Paths.get("indexdir");
        Directory dir = FSDirectory.open(indexPath);
        IndexReader indexReader=DirectoryReader.open(dir);
        IndexSearcher searcher=new IndexSearcher(indexReader);
        Term term =new Term("title","学");
        PrefixQuery preQuery= new PrefixQuery(term);
        System.out.println("query:"+preQuery.toString());
        TopDocs topDocs =searcher.search(preQuery,10);
        for (ScoreDoc scoreDoc:topDocs.scoreDocs) printResult(scoreDoc, searcher);
        dir.close();
        indexReader.close();
    }

    /**
     * 多关键词搜索，查询一个字段是否同时包含多个关键词，
     * @throws IOException
     * @throws ParseException
     */
    public  static void phraseQueryParser() throws IOException, ParseException {
        Path indexPath=Paths.get("indexdir");
        Directory dir = FSDirectory.open(indexPath);
        IndexReader indexReader=DirectoryReader.open(dir);
        IndexSearcher searcher=new IndexSearcher(indexReader);
        PhraseQuery.Builder builder=new PhraseQuery.Builder();
        builder.add(new Term("title","北大"));
        builder.add(new Term("title","学生"));
        builder.setSlop(9);//此为添加坡度，用于确定关键词之间是否允许或者允许多少个无关词汇的存在
        PhraseQuery phraseQuery=builder.build();
        System.out.println("query:"+phraseQuery.toString());
        TopDocs topDocs =searcher.search(phraseQuery,10);
        for (ScoreDoc scoreDoc:topDocs.scoreDocs) printResult(scoreDoc, searcher);
        dir.close();
        indexReader.close();
    }

    /**
     * 模糊查询，简单的识别两个相近的词语，也就是错一点点可以识别为同一个
     * @throws IOException
     * @throws ParseException
     */
    public  static void fuzzyQueryParser() throws IOException, ParseException {
        Path indexPath=Paths.get("indexdir");
        Directory dir = FSDirectory.open(indexPath);
        IndexReader indexReader=DirectoryReader.open(dir);
        IndexSearcher searcher=new IndexSearcher(indexReader);
        FuzzyQuery fuzzyQuery=new FuzzyQuery(new Term("title","4381"));
        System.out.println("query:"+fuzzyQuery.toString());
        TopDocs topDocs =searcher.search(fuzzyQuery,10);
        for (ScoreDoc scoreDoc:topDocs.scoreDocs) printResult(scoreDoc, searcher);
        dir.close();
        indexReader.close();
    }

    /**
     * 通配符查询
     * @throws IOException
     * @throws ParseException
     */
    public  static void wildcardQueryParser() throws IOException, ParseException {
        Path indexPath=Paths.get("indexdir");
        Directory dir = FSDirectory.open(indexPath);
        IndexReader indexReader=DirectoryReader.open(dir);
        IndexSearcher searcher=new IndexSearcher(indexReader);
        WildcardQuery wildcardQuery=new WildcardQuery(new Term("title","农*"));
        System.out.println("query:"+wildcardQuery.toString());
        TopDocs topDocs =searcher.search(wildcardQuery,10);
        for (ScoreDoc scoreDoc:topDocs.scoreDocs) printResult(scoreDoc, searcher);
        dir.close();
        indexReader.close();
    }
    /**
     * 输出查询结果方法
     * @param scoreDoc
     * @param searcher
     * @throws IOException
     */
    public static void printResult(ScoreDoc scoreDoc,IndexSearcher searcher)throws IOException{
        Document doc =searcher.doc(scoreDoc.doc);
        System.out.println("Docid:"+scoreDoc.doc);
        System.out.println("id:"+doc.get("id"));
        System.out.println("title"+doc.get("title"));
        System.out.println("content"+doc.get("content"));
        System.out.println("reply_display"+doc.get("reply_display"));
        System.out.println("文档评分："+scoreDoc.score);
    }
}
