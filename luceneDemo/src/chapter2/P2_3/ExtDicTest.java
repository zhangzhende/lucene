package chapter2.P2_3;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import java.io.IOException;
import java.io.StringReader;

/**
 * Created by zhangzhende on 2018/1/30.
 * <p>
 * 测试自定义词典，比如说向网络网络流行语词库中没有，可以在自定义词库【也是停用词词库】添加上，这样就能作为一个词出现，分解
 * 词库需要放在根目录下，不然他找不到
 */
public class ExtDicTest {
    private static String str = "厉害了我的哥！中国环保部门即将发布治理北京雾霾的方法！";

    public static void main(String[] args) throws IOException {
        Analyzer analyzer = new IKAnalyzer6x(true);
        StringReader reader = new StringReader(str);
        TokenStream tokenStream = analyzer.tokenStream(str, reader);
        tokenStream.reset();
        CharTermAttribute termAttribute = tokenStream.getAttribute(CharTermAttribute.class);
        System.out.println("分词结果是：");
        while (tokenStream.incrementToken()) {
            System.out.print(termAttribute.toString() + "|");
        }
        System.out.println("\n");
        analyzer.close();
    }
}
