import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

import org.junit.Test;


/**
 * InfectStatistic
 * TODO
 *
 * @author xxx
 * @version xxx
 * @since xxx
 */

class InfectStatistic { //主类有内部类FileDispose(文件处理类)，CmdArgsParse(命令行参数解析类)
	

	//以下为命令行参数解析以及文件中所用到的一些属性
	
	public String logPath;  //日志文件所在地址
	public String outPath;  //输出文件所在地址
	
	//因为-date不设置则指定日期默认为所提供日志最新的一天，需要确保你处理了指定日期之前的所有log文件，所以提前设置时间为当前时间
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); 
	Date d = new Date(System.currentTimeMillis());
	public String date = formatter.format(d);//当前时间设置
	
	//设置-type下的信息
	public int[] type = new int [5]; //类型输出判断数组，初始默认为0，序号1代表感染患者，序号2代表疑似患者，序号3代表治愈，序号4代表死亡
	public int  isTypeExist = 0;  //判断-Type是否有指定
	public String[] typeStr = {"感染患者", "疑似患者", "治愈", "死亡"};  //类型顺序排列
	
	//设置-province下的信息
	public int[] province = new int [35];  //若有指定省份，用来判断省份是否输出数组,初始默认为false
	public int[] provinceExist = new int [35] ;  //若没有指定省份，用来判断有哪些省份有记录，初始默认为false
	public int  isProvinceExist = 0;  //判断-Province是否有指定 
	public String[] provinceStr = {"全国", "安徽", "澳门" ,"北京", "重庆", "福建","甘肃","广东", "广西", "贵州", "海南", "河北", "河南", "黑龙江", "湖北", "湖南", 
			"吉林","江苏", "江西", "辽宁", "内蒙古", "宁夏", "青海", "山东", "山西", "陕西", "上海","四川", "台湾", "天津", "西藏", "香港", "新疆", "云南", "浙江"};
	//全国以及省份顺序排列
	
	public  int[][] peopleNumber = new int [35][4];  //记录全国以及每个省份每个类型的人数，初始默认为0，按照上面类型和省份顺序排列
	
	
	
	public static  void main(String[] args) {  //主函数入口
		
		InfectStatistic infectStatistic = new InfectStatistic();
    	InfectStatistic.CmdArgsParse CmdArgs = infectStatistic.new CmdArgsParse();
    	 CmdArgs.isCurrentCmdArgs(args);
    	
    	 	InfectStatistic.FileDispose filehandle = infectStatistic.new FileDispose();
        	filehandle.readFileList();
        	filehandle.writeOutTxt();
		
	}
		
	
	class CmdArgsParse {  //命令行解析类，主要对命令行参数进行解析
		
		CmdArgsParse() {}  //空构造函数
		public  boolean isCurrentCmdArgs(String[] args) {  //判断该命令行是否为有效的命令行并且解析出每个部分相应的参数
		
			boolean isLogExist = false;  //因为-log为必须附带，所以需要进行是否有-log存在的判断
			boolean isOutExist = false;  //因为-out为必须附带，所以需要进行是否有-out存在的判断
		
			if(!args[0].equals("list")) {  //判断命令行开头是否为list，如果不是则输出命令行有误
		        System.out.println("命令行有误");
		        return false;
		    }
			for (int i=1; i<args.length; i++) {  //将各部分的内容一一判断并且解析参数,解析有误则停止解析并输出命令行有误
			
			if(args[i].equals("-log")) {  //-log部分的解析
				i++;  //将命令行参数移动到准备解析的部分
				isLogExist = true ;  //-log存在的判断
				i = getLogPath(i, args);
				if(i == -1) {  //获得日志文件所在地址存在错误
					System.out.println("命令行有误");
					return false;
				}
			}
			
			else if(args[i].equals("-out")) {  //-out部分的解析
				i++;  //将命令行参数移动到准备解析的部分
				isOutExist = true; 
				i = getOutPath(i, args);
				if(i == -1) {  //获得日志文件所在地址存在错误
					System.out.println("命令行有误");
					return false;
				}
			}
			
			else if(args[i].equals("-date")) {  //-date部分的解析
				i++;  //将命令行参数移动到准备解析的部分
				i = getDate(i, args);
				if(i == -1) {  //获得日志文件所在地址存在错误
					System.out.println("命令行有误");
					return false;
				}
			}
			
			else if(args[i].equals("-type")) {  //-type部分的解析
				isTypeExist = 1;  //-type部分有指定
				i++;  //将命令行参数移动到准备解析的部分
				i = getType(i, args);
				if(i == -1) {  //获得日志文件所在地址存在错误
					System.out.println("命令行有误");
					return false;
				}
			}
			
			else if(args[i].equals("-province")) {  //-province部分的解析
				isProvinceExist = 1;  //-province部分有指定
				i++;  //将命令行参数移动到准备解析的部分
				i = getProvince(i, args);
				if(i == -1) {  //获得日志文件所在地址存在错误
					System.out.println("命令行有误");
					return false;
				}
			}
			
		}
		if(isLogExist == false || isOutExist == false) {  //因为-log和-out至少有一个不存在有误
			System.out.println("命令行有误");
			return false;
		} 
		
		return true;
	}


	
		public int getLogPath(int i, String[] args) {  //获得日志文件所在地址
	        if(args[i].matches("^[A-z]:\\\\(.+?\\\\)*$"))  //通过正则表达式来判断字符串是不是文件目录路径
	        	logPath = args[i];
	        
	        else  
	          return -1;
	        return i;  
		}
	
		public int getOutPath(int i, String[] args){  //获得输出文件所在地址
			if(args[i].matches("^[A-z]:\\\\(\\S+)+(\\.txt)$"))  //通过正则表达式来判断字符串是不是txt文件路径
				outPath = args[i];
			else  
	         return -1;
			return i;  		
		}
	
		public  int getDate(int i, String[] args) {  //获得指定日期
			if(date.compareTo(args[i]) >= 0)  //判断指定日期是否超过当前日期
				date = args[i];   //若不超过，将日期更改为指定日期
			else 
			  return -1;
			return i;  
		}
	
		public int getType(int i, String[] args) {  //获得指定类型
			int j;
			for(j = 0; j < 4; j++)  //类型判断输出数组置0
				type[j] = 0;
			j=0;	 
			while(i < args.length){  //开始判断指定类型并且获得输出顺序
    			if(args[i].equals("ip")){
    				type[j] = 1;
    				j++;
    				i++;
    			} 
    			else if(args[i].equals("sp")){
    				type[j] = 2;
    				j++;
    				i++;
    			} 
    			else if(args[i].equals("cure")){
    				type[j] = 3;
    				j++;
    				i++;
    			} 
    			else if(args[i].equals("dead")){
    				type[j] = 4;
    				j++;
    				i++;
    			} 
    		}
			return i-1;  //避免解析过程中漏掉部分信息  
		}
    
		public int getProvince(int i, String[] args) {  //获得指定省份
			int j;
    		for(j = 0; j < 35; j++)  //判断省份输出数组置0
    			province[j] = 0;
    		while(i < args.length){
    			for(j = 0; j < 35; j++){
    				if(args[i].equals(provinceStr[j])){
    					province[j] = 1;
    					i++;
    					break;
    				 }
    			 }
    		}
    			return i-1;  //避免解析过程中漏掉部分信息 	
		}
    
	}
	
	
	class FileDispose{  //文件处理类，用来输入日志文件，通过解析后的命令行参数对输入的日志进行处理，然后输出到输出文件
		
		FileDispose(){};  //空构造函数
		
		public void readFileList() {  //读取指定路径下的文件名
			date = date + ".log.txt";  //把date加上后缀
			File file = new File(logPath);
			  String[] files = file.list();  //list()方法是返回某个目录下的所有文件和目录的文件名，返回的是String数组
			 for(int j = 0; j < files.length; j++){
				if(files[j].compareTo(date) <= 0) {  //判断该文件时间是否小于指定时间
					readLogTxt(logPath + files[j]);  //开始读取日志文件内容
				}
			}
		}
		
		public void readLogTxt(String filePath){ //读取日志文件内容
			try {
				BufferedReader bfr = new BufferedReader
			    (new InputStreamReader(new FileInputStream(new File(filePath)),"UTF-8")); //通过普通的缓存方式文本读取，编码为UTF-8
			    String readLine = null;
			    while ((readLine = bfr.readLine()) != null) {  //按行读取文本内容
			    	if(! readLine.startsWith("//"))  //遇到“//”不读取
			    		handleReadLine(readLine);  //开始进行文本内容的处理
			        }
			    bfr.close();  //关闭文件
			 	}  	catch (Exception e) {  //异常处理
			 			e.printStackTrace();
			    }
			
		} 
		
		
		public  void  handleReadLine(String readLine){  //处理文本内容的信息
			
			//总共有以下八种可能出现的文本信息，运用正则表达式来表示,并且用正则表达式判断是哪种出现的情况
			String readLine1 = "(\\S+) 新增 感染患者 (\\d+)人";
		    String readLine2 = "(\\S+) 新增 疑似患者 (\\d+)人";
		    String readLine3 = "(\\S+) 治愈 (\\d+)人";
		    String readLine4 = "(\\S+) 死亡 (\\d+)人";
		    String readLine5 = "(\\S+) 感染患者 流入 (\\S+) (\\d+)人";
		    String readLine6 = "(\\S+) 疑似患者 流入 (\\S+) (\\d+)人";
		    String readLine7 = "(\\S+) 疑似患者 确诊感染 (\\d+)人";
		    String readLine8 = "(\\S+) 排除 疑似患者 (\\d+)人";
		    
		    
		    if(Pattern.matches(readLine1, readLine))  //判断正则表达式是否一致，下同
		    	addToIP(readLine);
		    
		    if(Pattern.matches(readLine2, readLine))
		    	addToSP(readLine);
			    
		    if(Pattern.matches(readLine3, readLine)) 
		    	addToCure(readLine);
		    
		    if(Pattern.matches(readLine4, readLine))
		    	addToDead(readLine);
		    
		    if(Pattern.matches(readLine5, readLine))
		    	flowIntoIP(readLine);
		    
		    if(Pattern.matches(readLine6, readLine))  
		    	flowIntoSP(readLine);
		    	
		    if(Pattern.matches(readLine7, readLine))
		    	makeSPtoIP(readLine);
		    	
		    if(Pattern.matches(readLine8, readLine))
		    	excludeSP(readLine);
		    	
		}
		
		//以下为每种情况的处理方法
		
		public void addToIP(String readLine){  //添加全国和各省的感染患者
			 String[] str = readLine.split(" ");  //将字符串以空格分割为多个字符串(运用了新学的split方法),下同
			 int number = Integer.valueOf(str[3].replace("人", ""));  //将人前的数字从字符串类型转化为整数类型，下同       
			    for(int i = 0; i < 35; i++) {
			        if(str[0].equals(provinceStr[i])) {  //第一个字符串为省份，下同
			            peopleNumber[0][0] += number;  //全国感染患者人数增加
			            peopleNumber[i][0] += number;  //该省份感染患者人数增加
			            provinceExist[i] = 1;
			            break;
			        }
			    }
			
		}
		
		public void addToSP(String readLine){  //添加全国和各省的疑似患者
			 String[] str = readLine.split(" "); 
			 int number = Integer.valueOf(str[3].replace("人", ""));        
			    for(int i = 0; i < 35; i++) {
			        if(str[0].equals(provinceStr[i])) {  
			            peopleNumber[0][1] += number;  //全国疑似患者人数增加
			            peopleNumber[i][1] += number;  //该省份疑似患者人数增加
			            provinceExist[i] = 1;
			            break;
			        }
			    }
		}
		
		public void addToCure(String readLine){  //添加全国和各省的治愈患者
			 String[] str = readLine.split(" ");  
			 int number = Integer.valueOf(str[2].replace("人", ""));     
			    for(int i = 0; i < 35; i++) {
			        if(str[0].equals(provinceStr[i])) {  
			            peopleNumber[0][0] -= number;  //全国感染患者人数减少
			            peopleNumber[i][0] -= number;  //该省份感染患者人数减少
			            peopleNumber[0][2] += number;  //全国治愈患者人数增加
			            peopleNumber[i][2] += number;  //该省份治愈患者人数增加
			            provinceExist[i] = 1;
			            break;
			        }
			    }
		}
		
		public void addToDead(String readLine){  //添加全国和各省的死亡患者
			 String[] str = readLine.split(" ");  
			 int number = Integer.valueOf(str[2].replace("人", ""));       
			    for(int i = 0; i < 35; i++) {
			        if(str[0].equals(provinceStr[i])) {  
			        	peopleNumber[0][0] -= number;  //全国感染患者人数减少
			            peopleNumber[i][0] -= number;  //该省份感染患者人数减少
			            peopleNumber[0][3] += number;  //全国死亡患者人数增加
			            peopleNumber[i][3] += number;  //该省份死亡患者人数增加
			            provinceExist[i] = 1;
			            break;
			        }
			    }
		}
		
		public void flowIntoIP(String readLine){  //流入感染患者的处理
			 String[] str = readLine.split(" ");  
			 int number = Integer.valueOf(str[4].replace("人", ""));  //将人前的数字从字符串类型转化为整数类型，下同       
			    for(int i = 0; i < 35; i++) {
			        if(str[0].equals(provinceStr[i])) {  //第一个字符串为流出省份
			            peopleNumber[i][0] -= number;  //该省份感染患者人数减少
			            provinceExist[i] = 1;
			            break;
			        }
			    }
			    for(int i = 0; i < 35; i++) {
			        if(str[3].equals(provinceStr[i])) {  //第四个字符串为流入省份
			            peopleNumber[i][0] += number;  //该省份感染患者人数增加
			            provinceExist[i] = 1;
			            break;
			        }
			    }
		}
		
		public void flowIntoSP(String readLine){  //流入疑似患者的处理
			 String[] str = readLine.split(" "); 
			 int number = Integer.valueOf(str[4].replace("人", ""));       
			    for(int i = 0; i < 35; i++) {
			        if(str[0].equals(provinceStr[i])) {  //第一个字符串为流出省份
			            peopleNumber[i][1] -= number;  //该省份疑似患者人数增加
			            provinceExist[i] = 1;
			            break;
			        }
			    }
			    for(int i = 0; i < 35; i++) {
			        if(str[3].equals(provinceStr[i])) {  //第四个字符串为流入省份
			            peopleNumber[i][1] += number;  //该省份疑似患者人数增加
			            provinceExist[i] = 1;
			            break;
			        }
			    }
		}
		
		public void makeSPtoIP(String readLine){  //疑似患者确诊感染的处理
			 String[] str = readLine.split(" ");  
			 int number = Integer.valueOf(str[3].replace("人", ""));     
			    for(int i = 0; i < 35; i++) {
			        if(str[0].equals(provinceStr[i])) {  //第一个字符串为省份
			            peopleNumber[0][0] += number;  //全国感染患者人数增加
			            peopleNumber[i][0] += number;  //该省份感染患者人数增加
			            peopleNumber[0][1] -= number;  //全国疑似患者人数减少
			            peopleNumber[i][1] -= number;  //该省份疑似患者人数减少
			            provinceExist[i] = 1;
			            break;
			        }
			    }
		}
		
		public void excludeSP(String readLine){  //排除疑似患者的处理
			 String[] str = readLine.split(" ");  
			 int number = Integer.valueOf(str[3].replace("人", ""));    
			    for(int i = 0; i < 35; i++) {
			        if(str[0].equals(provinceStr[i])) {  //第一个字符串为省份
			            peopleNumber[0][1] -= number;  //全国疑似患者人数增加
			            peopleNumber[i][1] -= number;  //该省份疑似患者人数增加
			            provinceExist[i] = 1;
			            break;
			        }
			    }
		}
		
		
		public void writeOutTxt() {  //输出文件内容
			FileWriter fwriter = null;
			int i, j;	
			provinceExist[0] = 1; 
			try {
				fwriter = new FileWriter(outPath);  
				if(isProvinceExist == 0){  //若-province未指定
					for(i = 0; i < 35; i++){
						if(provinceExist[i] == 1){
							fwriter.write(provinceStr[i] + " ");
							if(isTypeExist == 0){  //若-type未指定
								for( j = 0; j < 4; j++)
									fwriter.write(typeStr[j] + peopleNumber[i][j] + "人 ");}
							
						
							else{  //若-type指定
								for( j = 0; j < 4; j++)
									if(type[j] != 0)
									fwriter.write(typeStr[type[j]-1] + peopleNumber[i][type[j]-1] + "人 ");
							}
							fwriter.write("\n");
						}
					}
				}
				else {  //若-province有指定
					for(i = 0; i < 35; i++){
						if(province[i] == 1){
							fwriter.write(provinceStr[i] + " ");
							if(isTypeExist == 0){  //若-type未指定
								for( j = 0; j < 4; j++)
									fwriter.write(typeStr[j] + peopleNumber[i][j] + "人 ");
							}
							else{  //若-type指定
								for( j = 0; j < 4; j++)
									if(type[j] != 0)
										fwriter.write(typeStr[type[j]-1] + peopleNumber[i][type[j]-1] + "人 ");
							} 
							fwriter.write("\n");
						}
							
					}
				}
				        
				fwriter.write("// 该文档并非真实数据，仅供测试使用");
				 }
				 catch (Exception e) {
				        e.printStackTrace();
				    				 } finally {
				     try {
				         fwriter.flush();
				         fwriter.close();
				         }	catch (IOException e1) {
				            		e1.printStackTrace();
				         	}
				    				 	}
			
		}
	}

	
	}
//以下为测试样本
/*import org.junit.Test;

public class InfectStatisticTest {

	@Test  //测试1只有-log和-out两个命令，其他命令未指定
	public void test1() {
		String[] testStr = {"list", "-log", "D:\\InfectStatistic-test\\log\\", 
				"-out", "D:\\InfectStatistic-test\\result\\testOut1.txt"};
		InfectStatistic.main(testStr);
	}
	
	@Test  //测试2在测试1的基础上添加-date命令,日期为第一篇日志发布的时间之前
	public void test2() {
		String[] testStr = {"list", "-log", "D:\\InfectStatistic-test\\log\\", 
				"-out", "D:\\InfectStatistic-test\\result\\testOut2.txt", "-date", "2020-01-01"} ;
		InfectStatistic.main(testStr);
	}
	
	@Test  //测试3在测试2的基础上修改了日期的值，日期为2020-01-24，即没有对应当天的日志文件但是有之前日期的对应日志文件
	public void test3() {
		String[] testStr = {"list", "-log", "D:\\InfectStatistic-test\\log\\", 
				"-out", "D:\\InfectStatistic-test\\result\\testOut3.txt", "-date", "2020-01-24"} ;
		InfectStatistic.main(testStr);
	}
	
	@Test   //测试4在测试3的基础上添加了-province命令，值为一个出现过的省份福建
	public void test4() {
		String[] testStr = {"list", "-log", "D:\\InfectStatistic-test\\log\\", 
				"-out", "D:\\InfectStatistic-test\\result\\testOut4.txt", "-date", "2020-01-24"
				, "-province", "福建"} ;
		InfectStatistic.main(testStr);
	}
	
	@Test  //测试5在测试4的基础上修改了省份的值，值为全国和一个未出现的省份浙江
	public void test5() {
		String[] testStr = {"list", "-log", "D:\\InfectStatistic-test\\log\\", 
				"-out", "D:\\InfectStatistic-test\\result\\testOut5.txt", "-date", "2020-01-24",
				 "-province", "全国", "浙江"} ;
		InfectStatistic.main(testStr);
	}
	
	@Test  //测试6在测试4的基础上添加了-type命令，值为sp和ip
	public void test6() {   
		String[] testStr = {"list", "-log", "D:\\InfectStatistic-test\\log\\", 
				"-out", "D:\\InfectStatistic-test\\result\\testOut6.txt", "-date", "2020-01-24",
				 "-type", "sp", "ip"} ;
		InfectStatistic.main(testStr);
	}
	
	@Test  //测试7在测试6的基础上替换了sp和ip的位置
	public void test7() {  
		String[] testStr = {"list", "-log", "D:\\InfectStatistic-test\\log\\", 
				"-out", "D:\\InfectStatistic-test\\result\\testOut7.txt", "-date", "2020-01-24",
				"-type", "ip" , "sp"} ;
		InfectStatistic.main(testStr);
	}
	
	@Test  //测试8运用加入了所有的-type的值，且以cure，ip，dead，sp的顺序输入
	public void test8() {
		String[] testStr = {"list", "-log", "D:\\InfectStatistic-test\\log\\", 
				"-out", "D:\\InfectStatistic-test\\result\\testOut8.txt", "-date", "2020-01-24",
				"-type", "cure", "ip", "dead", "sp"} ;
		InfectStatistic.main(testStr);
	}
	
	@Test   //测试9在测试8的基础上添加了-province的命令，值为全国，福建，浙江  
	public void test9() {  
		String[] testStr = {"list", "-log", "D:\\InfectStatistic-test\\log\\", 
				"-out", "D:\\InfectStatistic-test\\result\\testOut9.txt",
				"-province", "全国", "福建", "浙江","-type", "cure", "ip", "dead", "sp"} ;
		InfectStatistic.main(testStr);
	}

	@Test  //测试10在测试1的基础上更改了-log和-out命令的输入顺序
	public void test10() {
		String[] testStr = {"list", "-out", "D:\\InfectStatistic-test\\result\\testOut10.txt" 
				, "-log", "D:\\InfectStatistic-test\\log\\"} ;
		InfectStatistic.main(testStr);
	}
}*/

