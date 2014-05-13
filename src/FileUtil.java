package cn.zhf.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class FileUtil {
	/**
	 * 按行读取文件
	 * @param path
	 * @return
	 */
	public static Set<String> readFile(String path){
		Set<String> set = new HashSet<String>();
		 try {
			   BufferedReader br = new BufferedReader(new FileReader(path));
			   String line = null;
			   while((line = br.readLine()) != null){
					set.add(line.trim());
			   }
			   br.close();
			  } catch (IOException e) {
			   e.printStackTrace();
			  }
		return set;
	}
	
	/**
	 * 按行写入文件
	 * @param set
	 * @param path
	 */
	public static void write2File(Set<String> set,String path){
		File file = new File(path);
		 try {
			file.createNewFile();
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			for(String str : set){
				bw.write(str);
				bw.newLine();
			}
			bw.flush();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 看str中是否出现stop中的元素
	 * @param str
	 * @param stop
	 */
	public static boolean find(String str,Set<String> stop){
		for(String st : stop){
			if(str.contains(st))
				return true;
		}
		return false;
	}
	
	/**
	 * 预处理，删除包含停用词的字段
	 * @param targetPath
	 * @param stopPath
	 */
	public static Set<String> preprocess(String targetPath,String stopPath){
		Set<String> set = readFile(targetPath);
		Set<String> stop = readFile(stopPath);
		Set<String> _set = new HashSet<String>();
		//去除公司名包含字母的 或者 自己不想要什么的就写好了
		Pattern pattern = Pattern.compile("[a-zA-Z]+");	
		for(String s: set){
			if(find(s,stop))
				continue;
			else if(pattern.matcher(s).find())
				continue;
			else
				_set.add(s);
		}
		return _set;
	}
	//测试一下是否OK
	public static void main(String[] args) {
		String target = "C:\\Users\\logic\\Desktop\\neitui.txt";
		String stop = "..\\res\\stopwords.txt";
		String newfile = "C:\\Users\\logic\\Desktop\\newfile.txt";
		Set<String> set = preprocess(target,stop);
		write2File(set,newfile);
	}

}
