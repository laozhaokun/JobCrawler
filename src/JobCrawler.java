package cn.zhf.test;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * 本程序暂时只保存了公司的名字，其他数据还没有加
 * 使用的时候，需要根据不同网站的情况，稍微修改下参数
 * @author zhf
 * @email zhao.thss@gmail.com
 *
 */
public class JobCrawler {

	public static void main(String[] args) throws IOException {
		Set<String> set = getFenzhi();
		for(String str : set)
			System.out.println(str);
//		FileUtil.write2File(set, "C:\\Users\\logic\\Desktop\\fenzhi.txt");
		System.out.println("写入文件成功。 ");
		System.out.println("保存了 " + set.size() + "条数据。");
	}

	/**
	 * 抓取 IT人 的数据，要指定 页面数 和 城市id，即cid
	 * 这里没什么职位，就是一个公司的列表
	 * @return
	 * @throws IOException
	 */
	public static Set<String> getIteer() throws IOException {
		Set<String> set = new HashSet<String>();
		String url = "http://www.iteer.net/modules/xdirectory/viewcat.php?cid=202&min=";
		for (int i = 1; i < 1267; i++) {
			Document doc = Jsoup
					.connect(url + i + "0")
					.timeout(10000)
					.userAgent(
							"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)")
					.post();
			Elements elements = doc.getElementsByClass("xdir_title");
			for (Element ele : elements) {
				set.add(ele.text());
			}
		}
		return set;
	}

	/**
	 * 抓取 水木社区 的数据，要指定 页面数
	 * 水木反应比较慢，这些数据是要从js返回的数据中提取出来的，在浏览器中看不到这些源码
	 * @return
	 * @throws IOException
	 */
	public static Set<String> getShuimu() throws IOException {
		Set<String> set = new HashSet<String>();
		String url = "http://www.newsmth.net/nForum/board/Career_Upgrade?ajax&p=";
		for (int i = 1; i <= 1000; i++) {
			Document doc = Jsoup
					.connect(url+i)
					.timeout(50000)
					.userAgent("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 "
							+ "(KHTML, like Gecko) Chrome/34.0.1847.131 Safari/537.36")
					.get();
			Elements elements = doc.getElementsByClass("title_9");
			for (Element ele : elements) {
				String str = ele.text();
				//过滤一些广告之类的数据
				if(str.contains("黑名单") || str.contains("更新") || str.contains("警告") || str.contains("主板") 
						||	str.contains("发布") || str.contains("发帖") || str.contains("转载") || str.contains("求职")
					|| str.contains("主题") || str.contains("专员") || str.contains("代招") || str.contains("猎头")){
					continue;
				}
				else
					set.add(ele.text());
			}
		}
		return set;
	}
	
	/**
	 * 抓取 中华英才网 的数据，需要指定 关键词 和 页面数
	 * 关键词的位置是/so/*
	 * 如果有多个关键词，可以放到一个Map里面，循环就好了
	 * @return
	 * @throws IOException
	 */
	public static Set<String> getChinahr() throws IOException {
		Set<String> set = new HashSet<String>();
		String url = "http://www.chinahr.com/so/c/0-0-0-0-0-0-0-0-0-0-0-0-0-0/p";
		for (int i = 1; i < 200; i++) {
			Document doc = Jsoup
					.connect(url + i * 2 + 0)
					.timeout(10000)
					.userAgent(
							"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)")
					.post();
			Elements elements = doc.getElementsByClass("joblist_a");
			for (Element ele : elements) {
				set.add(ele.text());
			}
		}
		return set;
	}

	/**
	 * 抓取 内推网 的数据，不指定关键词的话，就是全部都抓了，指定 页面数
	 * 如果有多个关键词，可以放到一个Map里面，循环就好了
	 * @return
	 * @throws IOException
	 */
	public static Set<String> getNeitui() throws IOException{
		Set<String> set = new HashSet<String>();
		String url = "http://www.neitui.me/neitui/type=all&page=";//200 pages
		for(int i=1;i<201;i++){
		Document doc = Jsoup
				.connect(url+i+".html")
				.timeout(30000)
				.userAgent(
						"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)")
				.post();
		Elements elements = doc.getElementsByClass("jobtitle");
		for (Element ele : elements) {
			set.add(ele.text());
		}
		}
		Set<String> _set = new HashSet<String>();
		//源数据格式是这样：
		//公司：上海梅花信息有限公司-TEC   •   地点：上海市长宁区中山西路1279弄6号国峰科技大厦6楼
		for(String se : set){
			se = se.substring(3);
			se = se.substring(0,se.indexOf("•")).trim();
			_set.add(se);//最后只要公司名
		}
		return _set;
	}
	
	/**
	 * 抓取 开源中国 的数据，如果不限城市什么的，就指定 页面数
	 * 如果有多个关键词，可以放到一个Map里面，循环就好了
	 * @return
	 * @throws IOException
	 */
	public static Set<String> getOSChina() throws IOException {
		Set<String> set = new HashSet<String>();
		String url = "http://www.oschina.net/job?q=&addr_city=&addr_prv=&type=0&rank=0&salary=&p=";
		for(int i=1;i<356;i++){
		Document doc = Jsoup
				.connect(url+i)
				.timeout(30000)
				.userAgent(
						"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)")
				.post();
		Elements elements = doc.getElementsByClass("CoPart");
		int j=1;
		//源数据格式是这样：
		//PHP工程师  - 爱卡汽车网
		for (Element ele : elements) {
			String co_name = ele.text();
			if(j++<7) continue;//只是职位名称，由于我暂时只要公司名，就先不理他了
			if(!co_name.contains("  - ")) continue;//没有分隔符，说明不是招聘信息，是广告！
			int index = co_name.indexOf("  - ")+"  - ".length();//去掉分隔符
			set.add(co_name.substring(index).trim());
		}
		}
		return set;
	}
	/**
	 * 抓取 拉勾网 的数据，需要页面数
	 * 这里只抓了公司列表里的公司名字
	 * @return
	 * @throws IOException
	 */
	public static Set<String> getLagou() throws IOException{
		Set<String> set = new HashSet<String>();
		String url = "http://www.lagou.com/jobs/list_?pn=";
		for(int i=1;i<21;i++){
		Document doc = Jsoup
				.connect(url+i)
				.timeout(3000)
				.userAgent(
						"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)")
				.post();
		Elements elements = doc.getElementsByClass("mb10");
		for (Element ele : elements) {
			set.add(ele.text());
		}
		}
		return set;
	}
	/**
	 * 抓取 职播网 的数据
	 * 这个网站也是抓取51job等的数据
	 * @return
	 * @throws IOException
	 */
	public static Set<String> getJobradio() throws IOException {
		Set<String> set = new HashSet<String>();
		String url = "http://jobradio.cn/channel/VADuF/p_1#?";
		Document doc = Jsoup
				.connect(url)
				.timeout(3000)
				.userAgent(
						"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)")
				.post();
		Elements elements = doc.getElementsByClass("companyName");
		for (Element ele : elements) {
			set.add(ele.text());
		}
		return set;
	}

	/**
	 * 抓取 智联招聘 的数据，需要指定关键词kw 和 页面数
	 * 如果有多个关键词，可以放到一个Map里面，循环就好了
	 * @return
	 * @throws IOException
	 */
	public static Set<String> getZhilian() throws IOException {
		Set<String> set = new HashSet<String>();
		String url = "http://sou.zhaopin.com/jobs/SearchResult.ashx?kw=java&p=";
		for (int i = 1; i < 91; i++) {
			Document doc = Jsoup
					.connect(url + i)
					.timeout(4000)
					.userAgent(
							"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)")
					.post();
			Elements elements = doc.getElementsByClass("Companyname");
			for (Element ele : elements) {
				set.add(ele.text());
			}
		}
		return set;
	}

	/**
	 * 抓取51job的数据，需要指定关键词keywords 和 页面数
	 * 如果有多个关键词，可以放到一个Map里面，循环就好了
	 * @return
	 * @throws IOException
	 */
	public static Set<String> get51job() throws IOException {
		Set<String> set = new HashSet<String>();
		String url = "http://search.51job.com/jobsearch/search_result.php?keyword=c&curr_page=";
		for (int i = 1; i < 1000; i++) {
			Document doc = Jsoup
					.connect(url + i)
					.timeout(4000)
					.userAgent(
							"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)")
					.post();
			Elements element = doc.getElementsByClass("coname");// 51job
			for (Element ele : element) {
				set.add(ele.text());
			}
		}
		return set;
	}

	/**
	 * 分智网 是我最想抓的，这里的数据，分类很清晰
	 * 但是一次只能抓10页左右吧，之后就直接被禁了，连浏览器都不能访问了！
	 * 不知道怎样可以连续的抓？
	 * @return
	 * @throws IOException
	 */
	public static Set<String> getFenzhi() throws IOException {
		Set<String> set = new HashSet<String>();
		String url = "http://www.fenzhi.com/xsc4p";
		for (int i = 1; i < 2; i++) {
			Document doc = Jsoup
					.connect(url + i + ".html")
					.timeout(40000)
					.userAgent(
							"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)")
					.post();
			Elements element = doc.getElementsByClass("tightTop");
			for (Element ele : element) {
				set.add(ele.text());
			}
		}
		return set;
	}
}
