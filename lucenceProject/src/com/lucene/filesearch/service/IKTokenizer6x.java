package com.lucene.filesearch.service;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

import java.io.IOException;

/**
 * Created by zhangzhende on 2018/1/30.
 *
 * IK分词器的类
 */
public class IKTokenizer6x extends Tokenizer {
    //ik分词器实现
    private IKSegmenter IKImplement;
    //词元文本属性
    private final CharTermAttribute termAttribute;
    //词元位移属性
    private final OffsetAttribute offsetAttribute;

    //词元分类属性
    private final TypeAttribute typeAttribute;
    //记录最后一个词元的结束位置
    private int endPosition;

    public IKTokenizer6x(boolean useSmart) {
        super();
        offsetAttribute = addAttribute(OffsetAttribute.class);
        termAttribute = addAttribute(CharTermAttribute.class);
        typeAttribute = addAttribute(TypeAttribute.class);
        IKImplement = new IKSegmenter(input, useSmart);
    }

    @Override
    public boolean incrementToken() throws IOException {
        clearAttributes();//清楚所有的词元属性
        Lexeme nextLexeme = IKImplement.next();
        if (nextLexeme != null) {
            //将Lexeme转换成Attribute
            termAttribute.append(nextLexeme.getLexemeText());//设置词元文本
            termAttribute.setLength(nextLexeme.getLength());//设置词元长度
            offsetAttribute.setOffset(nextLexeme.getBeginPosition(), nextLexeme.getEndPosition());//设置词元位移
            //记录最后分词位置
            endPosition = nextLexeme.getEndPosition();
            typeAttribute.setType(nextLexeme.getLexemeText());
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void reset() throws IOException {
        super.reset();
        IKImplement.reset(input);
    }

    @Override
    public void end() throws IOException {
        int finalOffset=correctOffset(this.endPosition);
        offsetAttribute.setOffset(finalOffset,finalOffset);
    }
}
