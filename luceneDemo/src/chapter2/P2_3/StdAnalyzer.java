package chapter2.P2_3;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import java.io.IOException;
import java.io.StringReader;

/**
 * Created by zhangzhende on 2018/1/29.
 * <p>
 * 测试标准分词，把句子分成一个一个单个的字词
 */
public class StdAnalyzer {
    private static String strCN = "中华人民共和国简称中国，是一个拥有人口的国家";
    private static String strEn = "Dogs can not achieve a place,eyes can reach";

    public static void main(String[] args) throws IOException {
        System.out.println("StandardAnalyzer对中文分词：");
        stdAnalyzer(strCN);
        System.out.println("StandardAnalyzer对英文分词");
        stdAnalyzer(strEn);
    }

    public static void stdAnalyzer(String str) throws IOException {
        Analyzer analyzer = null;
        analyzer = new StandardAnalyzer();
        StringReader reader = new StringReader(str);
        TokenStream tokenStream = analyzer.tokenStream(str, reader);
        tokenStream.reset();//清空流
        CharTermAttribute termAttribute = tokenStream.getAttribute(CharTermAttribute.class);
        System.out.println("分词结果：");
        while (tokenStream.incrementToken()) {
            System.out.print(termAttribute.toString() + "|");
        }
        System.out.println("\n");
        analyzer.close();
    }

}
