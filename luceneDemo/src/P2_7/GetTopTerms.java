package P2_7;

import org.apache.lucene.index.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

/**
 * Created by zhangzhende on 2018/2/27.
 */
public class GetTopTerms {
    public static void main(String args[]) throws IOException{
        Directory directory= FSDirectory.open(Paths.get("indexdir"));
        IndexReader indexReader= DirectoryReader.open(directory);
        //因为只索引了一个文档，所以DOCid=0
        //通过getTermVector 获取Content字段的词项
        Terms terms=indexReader.getTermVector(0,"content");
        //遍历词项
        TermsEnum termsEnum=terms.iterator();
        Map<String ,Integer> map =new HashMap<>();
        BytesRef thisTerm;
        while((thisTerm=termsEnum.next())!=null){
            //词项
            String termtext=thisTerm.utf8ToString();
            //通过totalTermFreq()方法获取词项频率
            map.put(termtext,(int)(termsEnum.totalTermFreq()));
        }
        //按照value排序
        List<Map.Entry<String,Integer>> sortedMap=new ArrayList<Map.Entry<String, Integer>>(map.entrySet());
        Collections.sort(sortedMap, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return (o2.getValue()-o1.getValue());
            }
        });
            getTopN(sortedMap,10);
    }
    public static void getTopN(List<Map.Entry<String,Integer>> sortedMap,int N){
        for(int i=0;i<N;i++){
            System.out.println(sortedMap.get(i).getKey()+":"+sortedMap.get(i).getValue());
        }
    }
}
