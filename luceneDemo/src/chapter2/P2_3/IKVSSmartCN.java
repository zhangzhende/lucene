package chapter2.P2_3;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import java.io.IOException;
import java.io.StringReader;

/**
 * Created by zhangzhende on 2018/1/30.
 * <p>
 * 对比lucene中自带的SmartChineseAnalyzer和IKAnalyzer，看一下哪个的效果更好
 */
public class IKVSSmartCN {
    private static String str1 = "公路局正在治理解放大道路面积水问题。";
    private static String str2 = "IKAnalyzer 是一个开源的，基于JAVA语言开发的轻量级的中文分词工具包。";

    public static void main(String[] args) throws IOException {
        Analyzer analyzer = null;
        System.out.println("句子一：" + str1);
        System.out.println("SmartChineseAnalyzer的分析结果：");
        analyzer = new SmartChineseAnalyzer();
        printAnalyzer(analyzer, str1);
        System.out.println("IKAnalyzer的分析结果：");
        analyzer = new IKAnalyzer6x(true);
        printAnalyzer(analyzer, str1);
        System.out.println("-------------------------------");
        System.out.println("句子二：" + str2);
        System.out.println("SmartChineseAnalyzer的分析结果：");
        analyzer = new SmartChineseAnalyzer();
        printAnalyzer(analyzer, str2);
        System.out.println("IKAnalyzer的分析结果：");
        analyzer = new IKAnalyzer6x(true);
        printAnalyzer(analyzer,str2);
        analyzer.close();

    }


    public static void printAnalyzer(Analyzer analyzer, String str) throws IOException {
        StringReader reader = new StringReader(str);

        TokenStream tokenStream = analyzer.tokenStream(str, reader);
        tokenStream.reset();//清空流
        CharTermAttribute termAttribute = tokenStream.getAttribute(CharTermAttribute.class);
        while (tokenStream.incrementToken()) {
            System.out.print(termAttribute.toString() + "|");
        }
        System.out.println("\n");
    }
}
