package chapter2.P2_3;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cjk.CJKAnalyzer;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import java.io.IOException;
import java.io.StringReader;

/**
 * Created by zhangzhende on 2018/1/29.
 *
 * 测试一下多种分词器的效果
 */
public class VariousAnalyzers {
    private static String strCN = "中华人民共和国简称中国，是一个拥有人口的国家";
    public static void main(String[] args) throws IOException {
        Analyzer analyzer=null;
        analyzer= new StandardAnalyzer();
        System.out.println("标准分词："+analyzer.getClass());
        printAnalyzer(analyzer);

        analyzer=new WhitespaceAnalyzer();
        System.out.println("空格分词："+analyzer.getClass());
        printAnalyzer(analyzer);

        analyzer=new SimpleAnalyzer();
        System.out.println("简单分词:"+analyzer.getClass());
        printAnalyzer(analyzer);

        analyzer=new CJKAnalyzer();
        System.out.println("二分法分词:"+analyzer.getClass());
        printAnalyzer(analyzer);

        analyzer=new KeywordAnalyzer();
        System.out.println("关键词分词:"+analyzer.getClass());
        printAnalyzer(analyzer);

        analyzer=new StopAnalyzer();
        System.out.println("停用词分词分词:"+analyzer.getClass());
        printAnalyzer(analyzer);

        analyzer=new SmartChineseAnalyzer();
        System.out.println("中智能分词:"+analyzer.getClass());
        printAnalyzer(analyzer);

    }

    public static void printAnalyzer(Analyzer analyzer) throws IOException {
        StringReader reader=new StringReader(strCN);
        TokenStream tokenStream=analyzer.tokenStream(strCN,reader);
        tokenStream.reset();//清空流
        CharTermAttribute termAttribute=tokenStream.getAttribute(CharTermAttribute.class);
        while(tokenStream.incrementToken()){
            System.out.print(termAttribute.toString()+"|");
        }
        System.out.println("\n");
        analyzer.close();
    }
}
