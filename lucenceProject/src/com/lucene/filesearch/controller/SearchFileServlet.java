package com.lucene.filesearch.controller;

import com.lucene.filesearch.model.FileModel;
import com.lucene.filesearch.service.IKAnalyzer6x;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangzhende on 2018/3/3.
 */
public class SearchFileServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //索引路径
        String indexpathstr=request.getServletContext().getRealPath("/indexdir");
        //接受查询字符串
        String query=request.getParameter("query");
        //编码格式转换
//        query =new String(query.getBytes("iso8859-1"),"UTF-8");
        if (query==null ||query.equals("")){
            System.out.println("参数错误");
            request.getRequestDispatcher("error.jsp").forward(request,response);
        }else {
            List<FileModel> list=getTopDoc(query,indexpathstr,100);
            System.out.println("共搜到"+list.size()+"条数据");
            request.setAttribute("resultList",list);
            request.setAttribute("queryback",query);
            request.getRequestDispatcher("result.jsp").forward(request,response);
        }
        System.out.println("SearchFIleServlet");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    /**
     * 搜索文档的方法实现
     * @param key
     * @param indexpathStr
     * @param N
     * @return
     */
    public static List<FileModel> getTopDoc(String key, String indexpathStr, int N) {
        List<FileModel> hitsList = new ArrayList<>();
        //检索域
        String[] fields = {"title", "content"};
        Path indexpath = Paths.get(indexpathStr);
        Directory directory;
        try {
            directory = FSDirectory.open(indexpath);
            IndexReader indexReader = DirectoryReader.open(directory);
            IndexSearcher searcher = new IndexSearcher(indexReader);
            Analyzer analyzer = new IKAnalyzer6x();
            MultiFieldQueryParser parser = new MultiFieldQueryParser(fields, analyzer);
            //查询字符串
            Query query = parser.parse(key);
            TopDocs topDocs = searcher.search(query, N);
            //定制高亮标签
            SimpleHTMLFormatter simpleHTMLFormatter = new SimpleHTMLFormatter("<span style=\"color:red;\">", "</span>");
            QueryScorer scoretitle = new QueryScorer(query, fields[0]);
            Highlighter highlightertitle = new Highlighter(simpleHTMLFormatter, scoretitle);
            QueryScorer scorecontent = new QueryScorer(query, fields[0]);
            Highlighter highlighterContent = new Highlighter(simpleHTMLFormatter, scorecontent);
            TopDocs hits = searcher.search(query, 100);
            for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
                Document doc = searcher.doc(scoreDoc.doc);
                String title = doc.get("title");
                String content = doc.get("content");
                TokenStream tokenStream = TokenSources.getAnyTokenStream(searcher.getIndexReader(), scoreDoc.doc, fields[0], new IKAnalyzer6x());
                Fragmenter fragmenter = new SimpleSpanFragmenter(scoretitle);
                highlightertitle.setTextFragmenter(fragmenter);
                String h_title = highlightertitle.getBestFragment(tokenStream, title);
                //获取高亮片段，可以对其数量进行限制
                tokenStream = TokenSources.getAnyTokenStream(searcher.getIndexReader(), scoreDoc.doc, fields[1], new IKAnalyzer6x());
                fragmenter = new SimpleSpanFragmenter(scorecontent);
                highlighterContent.setTextFragmenter(fragmenter);
                String h_content = highlighterContent.getBestFragment(tokenStream, content);
                FileModel fileModel = new FileModel(h_title != null ? h_title : title, h_content != null ? h_content : content);
                hitsList.add(fileModel);
            }
            directory.close();
            indexReader.close();

        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidTokenOffsetsException e) {
            e.printStackTrace();
        }
        return hitsList;
    }
}
