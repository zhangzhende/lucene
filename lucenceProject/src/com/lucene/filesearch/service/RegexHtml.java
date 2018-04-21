package com.lucene.filesearch.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zhangzhende on 2018/3/3.
 * 去除HTML中的HTML标签
 */
public class RegexHtml {
    public  String delHtmlTag(String line){
        String regEX_html="<[^>]+>";
        //创建Pattern 对象
        Pattern pattern=Pattern.compile(regEX_html);
        //创建matcher对象
        Matcher matcher=pattern.matcher(line);
        line=matcher.replaceAll("");
        return line;
    }
}
