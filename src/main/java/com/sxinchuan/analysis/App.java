package com.sxinchuan.analysis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.regex.Pattern;

import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.JiebaSegmenter.SegMode;
import com.huaban.analysis.jieba.SegToken;

/**
 * Hello world!
 *
 */
public class App {

	private static String input_file = "d:/contract.txt";
	private static String output_file = "d:/words-new.txt";

	public static void main(String[] args) {
		System.out.println("分词开始!");
		JiebaSegmenter segmenter = new JiebaSegmenter();
		StringBuilder content = new StringBuilder("");  
		// 读取数据
		try { // 防止文件建立或读取失败，用catch捕捉错误并打印，也可以throw
			String code = resolveCode(input_file);
			/* 读入TXT文件 */
			File filename = new File(input_file); // 要读取以上路径的input。txt文件
			InputStreamReader reader = new InputStreamReader(new FileInputStream(filename), code); // 建立一个输入流对象reader
			BufferedReader br = new BufferedReader(reader); // 建立一个对象，它把文件内容转成计算机能读懂的语言
			String line = "";
			while (null != (line = br.readLine())) {
				content.append(line);  
			}
			br.close();
			String str = content.toString().trim();
			str = str.replaceAll("\n|\r\\\"|“|”|\\\\d+|", "").replaceAll(" ", "").replaceAll("\\d+","");
			String regEx = "、|，|。|；|？|！|,|\\.|;|\\?|!|:|：|]";
			Pattern p =Pattern.compile(regEx);     
//			Matcher m = p.matcher(str); 
			
			String[] sentences = p.split(str) ;
			/* 写入Txt文件 */
			File writename = new File(output_file); // 相对路径，如果没有则要建立一个新的output。txt文件
			writename.createNewFile(); // 创建新文件
			BufferedWriter out = new BufferedWriter(new FileWriter(writename));
			StringBuilder sb =  new StringBuilder("");
			for (String sentence : sentences) {
				List<com.huaban.analysis.jieba.SegToken> seg =  segmenter.process(sentence, SegMode.SEARCH);
				for (SegToken segToken : seg) {
					sb.append(segToken.word+"\r\n");
				}
			}
			System.out.println(sb.toString());
			out.write(sb.toString());
			out.flush(); // 把缓存区内容压入文件
			out.close(); // 最后记得关闭文件
			System.out.println("分词结束！分词文件位于D:\\words-new.txt\r\n\r\n程序自动退出中。。。请勿强制关闭");
			
			Thread.sleep(1000);
			System.out.println("3。。。");
			Thread.sleep(1000);
			System.out.println("2。。。");
			Thread.sleep(1000);
			System.out.println("1。。。");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	public static String resolveCode(String path) throws Exception {  
//      String filePath = "D:/article.txt"; //[-76, -85, -71]  ANSI  
//      String filePath = "D:/article111.txt";  //[-2, -1, 79] unicode big endian  
//      String filePath = "D:/article222.txt";  //[-1, -2, 32]  unicode  
//      String filePath = "D:/article333.txt";  //[-17, -69, -65] UTF-8  
        InputStream inputStream = new FileInputStream(path);    
        byte[] head = new byte[3];    
        inputStream.read(head);      
        String code = "gb2312";  //或GBK  
        if (head[0] == -1 && head[1] == -2 )    
            code = "UTF-16";    
        else if (head[0] == -2 && head[1] == -1 )    
            code = "Unicode";    
        else if(head[0]==-17 && head[1]==-69 && head[2] ==-65)    
            code = "UTF-8";    
            
        inputStream.close();  
          
        System.out.println(code);   
        return code;  
    }  

}
