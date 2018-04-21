package chapter2.P2_3;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Tokenizer;

/**
 * Created by zhangzhende on 2018/1/30.
 */
public class IKAnalyzer6x extends Analyzer {
    private boolean useSmart;

    public boolean getUseSmart() {
        return useSmart;
    }

    public void setUseSmart(boolean useSmart) {
        this.useSmart = useSmart;
    }

    public IKAnalyzer6x(boolean useSmart) {
        super();
        this.useSmart = useSmart;
    }

    public IKAnalyzer6x() {
        this(false);
    }

    @Override
    protected TokenStreamComponents createComponents(String fieldName) {
        Tokenizer IKTokenizer = new IKTokenizer6x(this.getUseSmart());
        return new TokenStreamComponents(IKTokenizer);
    }
}
