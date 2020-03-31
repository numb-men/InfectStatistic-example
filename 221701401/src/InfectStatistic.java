import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.*;

/**
 * InfectStatistic
 * TODO
 *
 * @author 221701401
 * @version 1.0
 * @since 2.10
 */
class InfectStatistic {
    public static void main(String[] args) throws Exception{
    	CmdHandle cmdr=new CmdHandle();
    	cmdr.setCmdArg(args);
    	cmdr.isCmd();
    }
}

//命令行管理类
class CmdHandle {
	private String[] args=new String[] {};
	private Param p=new Param();
	
	void setCmdArg(String[] arg) {
		args=arg;
	}

	//是否命令
	public void isCmd() throws Exception {
		switch (args[0]) {
		case "list":
			Param pm=CmdAnalyse();
			CmdList c=new CmdList();
			c.doCmd(pm);
			break;
		default:
			throw new Exception("输入不合法命令");
		}
	}
	
	//参数分析，返回分解好的参数
	public Param CmdAnalyse() throws Exception {
		int len=args.length;
		for (int i=1;i<len;i++) {
			switch (args[i]) {
			case "-date":
				String arg=args[i+1];
				if (isCorrectDate(arg)) {
					p.setDate(true);
					p.setDateValue(arg);	
				}
				else throw new Exception("日期非法");
				break;
			case "-out":
				p.setOut(true);
				p.setOutValue(args[i+1]);
				break;
			case "-log":
				File f=new File(args[i+1]);
				if(!f.exists()) throw new Exception("目录不存在");	
				p.setLog(true);
				p.setLogValue(args[i+1]);
				break;
			case "-type":
				p.setType(true);
				List<String> typeList=new ArrayList<>();
				for (int j=i+1;j<args.length;j++) {
					if(args[j].substring(0,1).equals("-")) break;
					typeList.add(args[j]);
				}
				p.setTypeValue(typeList);
				break;
			case "-province":
				p.setProvince(true);
				int begin=i+1,end=i+1;
				for (;end<args.length;end++) {
					if(args[end].substring(0,1).equals("-")) break;
				}
				String[] tmp=Arrays.copyOfRange(args,begin,end);
				p.setProvinceValue(tmp);
				break;
			default:
				break;
			}
		}
		return p;
	}
	
	//判断传入的字符串是否合法日期，false为不合法
	public boolean isCorrectDate(String date) {
		SimpleDateFormat sd=new SimpleDateFormat("yyyy-MM-dd");
		try {
			sd.setLenient(false);
			sd.parse(date);
		}
		catch (Exception e) {
			return false;
		}
		return true;
	}
}

//命令接口
interface Cmd {
	//执行命令函数
	public abstract void doCmd(Param pm) throws IOException, Exception;
}

//list命令类
class CmdList implements Cmd {
	//继承至命令接口
	public void doCmd(Param pm) throws Exception {
		FileHandle l=new FileHandle();
		String[] require=null;
		if (!pm.isLog()||(!pm.isLog())) throw new Exception("-log -out为必需项");
    	if (!pm.isType()) pm.setTypeValue();
		if (!pm.isDate()) pm.setDateValue("");
    	if (pm.isProvince()) require=pm.getProvinceValue();
    	l.dealFile(pm.getLogValue(),pm.getDateValue(),require);
    	l.writeFile(pm.getOutValue(),pm.getTypeValue());
	}
}

//-type参数值结构类
class TypeStruct {
	private int index;//顺序
	private String name;//名称
	private boolean isExist;//命令中是否有这个参数值
	
	TypeStruct(int i,String n,boolean e) {
		index=i;
		name=n;
		isExist=e;
	}
	
	void setIndex(int i) {
		index=i;
	}
	
	void setExist(boolean b) {
		isExist=b;
	}
	
	int getIndex() {
		return index;
	}
	
	boolean getIsExist() {
		return isExist;
	}
	
	String getName() {
		return name;
	}
}

//-type参数值类
class TypeValue {
	private TypeStruct ip;
	private TypeStruct sp;
	private TypeStruct cure;
	private TypeStruct dead;
	private TypeStruct[] set;//上述四种参数值的集合
	int maxIndex;//有几个备选参数值
	
	TypeValue() {
		ip=new TypeStruct(1,"ip",false);
		sp=new TypeStruct(2,"sp",false);
		cure=new TypeStruct(3,"cure",false);
		dead=new TypeStruct(4,"dead",false);
		set= new TypeStruct[]{ip,sp,cure,dead};
		maxIndex=4;
	}
	
	TypeStruct[] getSet() {
		return set;
	}

	TypeStruct getIp() {
		return ip;
	}
	
	TypeStruct getSp() {
		return sp;
	}
	
	TypeStruct getCure() {
		return cure;
	}
	
	TypeStruct getDead() {
		return dead;
	}
	
	//给参数值排序
	public void sortSet() {
		List<TypeStruct> list=new ArrayList<>();
		list.addAll(Arrays.asList(set).subList(0, maxIndex));
		Collections.sort(list,new Comparator<TypeStruct>() {
			public int compare(TypeStruct r1,TypeStruct r2) {
				return r1.getIndex()-r2.getIndex();
			}
		});
		for (int i=0;i<maxIndex;i++) {
			set[i]=list.get(i);
		}
	}
}

//参数及参数值类
class Param {
	
	private boolean out;
	private boolean log;
	private boolean date;
	private boolean type;
	private boolean province;
	private String logValue;
	private String outValue;
	private String dateValue;
	private TypeValue typeValue;
	private String[] provinceValue;
	
	Param() {
		date=false;
		type=false;
		province=false;
		out=false;
		log=false;
		typeValue=new TypeValue();
		logValue=outValue=dateValue="";
		provinceValue=new String[] {};
	}
	
	void setOut(boolean o) {
		out=o;
	}

	boolean isOut() {
		return out;
	}

	void setLog(boolean g) {
		log=g;
	}

	boolean isLog() {
		return log;
	}

	String getOutValue() {
		return outValue;
	}
	
	void setOutValue(String o) {
		outValue=o;
	}
	
	String getLogValue() {
		return logValue;
	}
	
	void setLogValue(String g) {
		logValue=g;
	}
	
	void setDate(boolean d) {
		date=d;
	}

	String getDateValue() {
		return dateValue;
	}
	
	void setDateValue(String d) {
		if (d.equals("")) {
			SimpleDateFormat df=new SimpleDateFormat("yyyy-MM-dd");
			dateValue=df.format(new Date());
	    }
		else dateValue=d;
	}
	
	void setProvince(boolean p) {
		province=p;
	}
	
	boolean isProvince() {
		return province;
	}
	
	String[] getProvinceValue() {
		return provinceValue;
	}
	
	void setProvinceValue(String[] p) {
		provinceValue=p;
	}
	
	void setType(boolean t) {
		type=t;
	}
	
	boolean isType() {
		return type;
	}

	public boolean isDate() { 
		return date; 
	}
	
	TypeValue getTypeValue() {
		return typeValue;
	}
	
	void setTypeValue() {
		typeValue.getIp().setExist(true);
		typeValue.getSp().setExist(true);
		typeValue.getCure().setExist(true);
		typeValue.getDead().setExist(true);
	}
	
	void setTypeValue(List<String> list) throws Exception {
		if (list.isEmpty()) {
			setTypeValue();
		}
		else {
			for (int i=0;i<list.size();i++) {
				switch (list.get(i)) {
				case "ip":
					typeValue.getIp().setExist(true);
					typeValue.getIp().setIndex(i+1);
					break;
				case "sp":
					typeValue.getSp().setExist(true);
					typeValue.getSp().setIndex(i+1);
					break;
				case "cure":
					typeValue.getCure().setExist(true);
					typeValue.getCure().setIndex(i+1);
					break;
				case "dead":
					typeValue.getDead().setExist(true);
					typeValue.getDead().setIndex(i+1);
					break;
				default:
					throw new Exception("参数值非法"); 
				}
			}
		}
		typeValue.sortSet();
	}
}

//文件处理类
class FileHandle {
	private List<LogResult> all=new ArrayList<>();//存放所有文件的处理结果
	
	//读文件（输入参数：路径）
	public void readFile(String path) throws IOException {
		FileInputStream fin=new FileInputStream(path);
        InputStreamReader reader=new InputStreamReader(fin,"UTF-8");
        BufferedReader buffReader=new BufferedReader(reader);
        String strTmp="";
        DataHandle dh=new DataHandle();
		while ((strTmp=buffReader.readLine())!=null) {
			//win10无法建立无BOM,用此去掉开头
        	if (strTmp.startsWith("\uFEFF")) strTmp=strTmp.replace("\uFEFF","");
        	//结果统计
        	dh.calResult(strTmp);
        }
		List<LogResult> res=dh.calNationData();
		all.addAll(res);
		buffReader.close();
	}		
	
	//找到日期对应文件进行处理(输入参数：路径，日期，要求的省份)
	public void dealFile(String path,String date,String[] require) throws Exception {
		String[] list=new File(path).list();
		boolean isEnd=false;
		//按日期排序
		List<String> fileList=Arrays.asList(list);
	    Collections.sort(fileList,new Comparator<String>() {
	    	@Override
	    	public int compare(String o1,String o2) {
	    		return o1.compareTo(o2);
	    	}
	    });
	    //找文件
		StringBuffer dt=new StringBuffer(date);
		dt.append(".log.txt");
		String targetDate=dt.toString();
		String lastDate=fileList.get(fileList.size()-1);
		String earlyDate=fileList.get(0);
		if (targetDate.compareTo(lastDate)>0&&(!targetDate.contentEquals(dt))) throw new Exception("日期超出范围");
		if (targetDate.compareTo(earlyDate)<0) {
			LogResult lr=new LogResult("全国");
			all.add(lr);
		}
		else {
			for (String file:fileList) {
				if (file.compareTo(targetDate)>0&&(!file.contentEquals(dt))) break;
				if (file.contentEquals(dt)) isEnd=true;
				StringBuffer ph=new StringBuffer(path);
				ph.append("/");
				ph.append(file);
				readFile(ph.toString());
				if (isEnd) break;
			}
		}
		DataHandle dh=new DataHandle();
		//转化成要求的省份
		if (!(require==null)) all=dh.toFormatPro(require,all);
		List<LogResult> res=dh.mergeResult(all);
		all=res;
	}
	
	//写文件(输入参数：路径，类型)
	public void writeFile(String path,TypeValue type) throws IOException {
		File file=new File(path);
		if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
		if (!file.exists()) file.createNewFile();
		FileOutputStream fout=new FileOutputStream(file,true);
		OutputStreamWriter writer=new OutputStreamWriter(fout,"UTF-8");
		//这里写入内容	
		for (LogResult lr:all) {
			String tmp=lr.toString(type);
			writer.write(tmp);
		}
		writer.write("//该文档并非真实数据，仅供测试使用\n");
        writer.flush();
        writer.close();
	}	
}

//日志结果类
class LogResult {
	private String province;//省份
	private int ip;//感染
	private int sp;//疑似
	private int cure;//治愈
	private int dead;//死亡
	private String[] order={"全国","安徽","北京","重庆","福建","甘肃","广东","广西",
			   "贵州","海南","河北","河南","黑龙江","湖北","湖南","吉林","江苏","江西",
			   "辽宁","内蒙古","宁夏","青海","山东","山西","陕西","上海","四川","天津",
			   "西藏","新疆","云南","浙江"};//指定的排序顺序
	
	LogResult() {
		ip=sp=cure=dead=0;
		province="";
	}
	
	LogResult(String pro) {
		ip=sp=cure=dead=0;
		province=pro;
	}
	
	void setProvince(String pro) {
		province=pro;
	}
	
	String getProvince() {
		return province;
	}
		
	void setIp(int i) {
		ip=i;
	}
	
	int getIp() {
		return ip;
	}
	
	void setSp(int s) {
		sp=s;
	}
	
	int getSp() {
		return sp;
	}
	
	void setCure(int c) {
		cure=c;
	}
	
	int getCure() {
		return cure;
	}
	
	void setDead(int d) {
		dead=d;
	}
	
	int getDead() {
		return dead;
	}
	
	//增加ip,sp,cure,dead
	void sum(int i,int s,int c,int d) {
		ip+=i;
		sp+=s;
		cure+=c;
		dead+=d;
	}
	
	//得到省份在指定顺序中的次序
	int getIndex (String str) {
		for (int i=0;i<order.length;i++) {
			if (str.equals(order[i])) return i;
		}
		return -1;
	}
	
	//按指定顺序输出信息（输入参数：type的参数值及顺序）
	public String toString(TypeValue type) {
		String res=province;
		TypeStruct[] set=type.getSet();
		for (TypeStruct typeStruct:set) {
			switch (typeStruct.getName()) {
				case "ip":
					if (typeStruct.getIsExist()) res+=" 感染患者"+ip+"人";
					break;
				case "sp":
					if (typeStruct.getIsExist()) res+=" 疑似患者"+sp+"人";
					break;
				case "cure":
					if (typeStruct.getIsExist()) res+=" 治愈"+cure+"人";
					break;
				case "dead":
					if (typeStruct.getIsExist()) res+=" 死亡"+dead+"人";
					break;
			}
		}
		res+="\n";
		return res;
	}
}

//数据处理类
class DataHandle {
	private Pattern addIp=Pattern.compile("([\\u4e00-\\u9fa5]{0,}+) 新增 感染患者 (\\d+)人");
	private Pattern addSp=Pattern.compile("([\\u4e00-\\u9fa5]{0,}+) 新增 疑似患者 (\\d+)人");
	private Pattern addAndSubIp=Pattern.compile("([\\u4e00-\\u9fa5]{0,}+) 感染患者 流入 ([\\u4e00-\\u9fa5]{0,}+) (\\d+)人");
	private Pattern addAndSubSp=Pattern.compile("([\\u4e00-\\u9fa5]{0,}+) 疑似患者 流入 ([\\u4e00-\\u9fa5]{0,}+) (\\d+)人");
	private Pattern addDead=Pattern.compile("([\\u4e00-\\u9fa5]{0,}+) 死亡 (\\d+)人");
	private Pattern addCure=Pattern.compile("([\\u4e00-\\u9fa5]{0,}+) 治愈 (\\d+)人");
	private Pattern spToIp=Pattern.compile("([\\u4e00-\\u9fa5]{0,}+) 疑似患者 确诊感染 (\\d+)人");
	private Pattern subSp=Pattern.compile("([\\u4e00-\\u9fa5]{0,}+) 排除 疑似患者 (\\d+)人");
	private List<LogResult> list;//存放处理好的数据
	
	DataHandle () {
		list=new ArrayList<>();
	}

	//将单行文本转换成LogResult并加入集合
	public void calResult(String str) {
		Handler addIpHandler=new AddIp(addIp);
		addIpHandler.getData(str);
	}
	
	//计算全国的数据并加在处理好的数据后合并
	public List<LogResult> calNationData() {
		LogResult all=new LogResult("全国");
		List<LogResult> res=new ArrayList<>();
		for (LogResult lr:list) {
			all.sum(lr.getIp(),lr.getSp(),lr.getCure(),lr.getDead());
		}
		list.add(all);
		res=mergeResult(list);
		return res;
	}
	
	//将传入的list中的内容改为仅含传入字符串数组的省份的list
	public List<LogResult> toFormatPro(String[] pro, List<LogResult> all) {
		HashMap<String,LogResult> target=new HashMap<>();
		for (String s : pro) {
			LogResult lr = new LogResult(s);
			target.put(s, lr);
		}
		for (LogResult lr:all) {
			String tmp=lr.getProvince();
			if (target.containsKey(tmp)) {
				LogResult temp=new LogResult(tmp);
                temp.setIp(target.get(tmp).getIp()+lr.getIp());
                temp.setSp(target.get(tmp).getSp()+lr.getSp());
                temp.setCure(target.get(tmp).getCure()+lr.getCure());
                temp.setDead(target.get(tmp).getDead()+lr.getDead());
                target.put(tmp,temp);
			}
		}
		List<LogResult> newList=new ArrayList<>();
        for (String tmp:target.keySet()) {
            newList.add(target.get(tmp));
        }
        return newList;
	}
	
	//合并list中数据并去重（参考）
	public List<LogResult> mergeResult(List<LogResult> list) {
		HashMap<String,LogResult> tempMap=new HashMap<String,LogResult>();
        for (LogResult lr:list) {
            String tmpStr=lr.getProvince();
            if (tempMap.containsKey(tmpStr)) {
                LogResult temp=new LogResult(tmpStr);
                temp.setIp(tempMap.get(tmpStr).getIp()+lr.getIp());
                temp.setSp(tempMap.get(tmpStr).getSp()+lr.getSp());
                temp.setCure(tempMap.get(tmpStr).getCure()+lr.getCure());
                temp.setDead(tempMap.get(tmpStr).getDead()+lr.getDead());
                //HashMap不允许key重复，当有key重复时，前面key对应的value值会被覆盖
                tempMap.put(tmpStr,temp);
            } else {
                tempMap.put(tmpStr,lr);
            }    
        }
        //去重
        List<LogResult> newList=new ArrayList<>();
        for (String temp:tempMap.keySet()) {
            newList.add(tempMap.get(temp));
        }
        List<LogResult> sortList=ListSort(newList);
        return sortList;
	}
	
	//将传入的list按照指定的省份顺序排序
	public List<LogResult> ListSort (List<LogResult> list) {
		Collections.sort(list,new Comparator<LogResult>() {
			public int compare(LogResult r1,LogResult r2) {
				return r1.getIndex(r1.getProvince())-r2.getIndex(r2.getProvince());
			}
		});
		return list;
	}
	
	//尝试使用责任链模式，抽象类下方的八个类分别代表八种模式及对应的处理方法
	//因为没有找到减少类的方法，所以就这么按照网上的例子写了，不是很懂
	//总觉得代码重复率有点高
	abstract class Handler {
		protected Pattern ptn;//正则表达式
	    protected Handler nextHandler;//下一个处理者
	    
	    //提取数据
	    public abstract LogResult getData(String str);
	}

	//新增感染
	class AddIp extends Handler {

		public AddIp(Pattern p) {
			ptn=p;
			nextHandler=new AddSp(addSp);
		}

		@Override
	    public LogResult getData(String str) {
			Matcher m=ptn.matcher(str);
			boolean rs=m.find();
			if (rs) {
				LogResult lr=new LogResult();
				lr.setProvince(m.group(1));
				lr.setIp(Integer.parseInt(m.group(2)));
				list.add(lr);
				return lr;
			}
			else return nextHandler.getData(str);
		}
		
	}

	//新增疑似
	class AddSp extends Handler {

		public AddSp(Pattern p) {
			ptn=p;
			nextHandler=new AddAndSubIp(addAndSubIp);;
		}

		@Override
		public LogResult getData(String str) {
			Matcher m=ptn.matcher(str);
			boolean rs=m.find();
			if (rs) {
				LogResult lr=new LogResult();
				lr.setProvince(m.group(1));
				lr.setSp(Integer.parseInt(m.group(2)));
				list.add(lr);
				return lr;
			}
			else return nextHandler.getData(str);
		}
	}
	
	//感染流入
	class AddAndSubIp extends Handler {
		public AddAndSubIp(Pattern p) {
			ptn=p;
			nextHandler=new AddAndSubSp(addAndSubSp);
		}

		@Override
		public LogResult getData(String str) {
			Matcher m=ptn.matcher(str);
			boolean rs=m.find();
			if (rs) {
				LogResult lr=new LogResult();
				LogResult lr2=new LogResult();
				lr.setProvince(m.group(1));
				int ip=Integer.parseInt(m.group(3));
				lr.setIp(-ip);
				lr2.setProvince(m.group(2));
				lr2.setIp(ip);
				list.add(lr);
				list.add(lr2);
				return lr;
			}
			else return nextHandler.getData(str);
		}
	}
	
	//疑似流入
	class AddAndSubSp extends Handler {
		public AddAndSubSp(Pattern p) {
			ptn=p;
			nextHandler=new AddDead(addDead);
		}

		@Override
		public LogResult getData(String str) {
			Matcher m=ptn.matcher(str);
			boolean rs=m.find();
			if (rs) {
				LogResult lr=new LogResult();
				LogResult lr2=new LogResult();
				lr.setProvince(m.group(1));
				int sp=Integer.parseInt(m.group(3));
				lr.setSp(-sp);
				lr2.setProvince(m.group(2));
				lr2.setSp(sp);
				list.add(lr);
				list.add(lr2);
				return lr;
			}
			else return nextHandler.getData(str);
		}
	}
	
	//死亡
	class AddDead extends Handler {

		public AddDead(Pattern p) {
			ptn=p;
			nextHandler=new AddCure(addCure);
		}

		@Override
	    public LogResult getData(String str) {
			Matcher m=ptn.matcher(str);
			boolean rs=m.find();
			if (rs) {
				LogResult lr=new LogResult(),lr2=new LogResult();
				int dead=Integer.parseInt(m.group(2));
				String pro=m.group(1);
				lr.setProvince(pro);
				lr.setDead(dead);
				lr2.setProvince(pro);
				lr2.setIp(-dead);
				list.add(lr);
				list.add(lr2);
				return lr;
			}
			else return nextHandler.getData(str);
		}	
	}

	//治愈
	class AddCure extends Handler {

		public AddCure(Pattern p) {
			ptn=p;
			nextHandler=new SpToIp(spToIp);
		}

		@Override
	    public LogResult getData(String str) {
			Matcher m=ptn.matcher(str);
			boolean rs=m.find();
			if (rs) {
				LogResult lr=new LogResult(),lr2=new LogResult();
				int cure=Integer.parseInt(m.group(2));
				String pro=m.group(1);
				lr.setProvince(pro);
				lr.setCure(cure);
				lr2.setProvince(pro);
				lr2.setIp(-cure);
				list.add(lr);
				list.add(lr2);
				return lr;
			}
			else 
				return nextHandler.getData(str);
		}		
	}

	//疑似确诊
	class SpToIp extends Handler {

		public SpToIp(Pattern p) {
			ptn=p;
			nextHandler=new SubSp(subSp);
		}

		@Override
	    public LogResult getData(String str) {
			Matcher m=ptn.matcher(str);
			boolean rs=m.find();
			if (rs) {
				LogResult lr=new LogResult(),lr2=new LogResult();
				int ip=Integer.parseInt(m.group(2));
				String pro=m.group(1);
				lr.setProvince(pro);
				lr.setSp(-ip);
				lr2.setProvince(pro);
				lr2.setIp(ip);
				list.add(lr);
				list.add(lr2);
				return lr;
			}
			else return nextHandler.getData(str);
		}		
	}

	//排除疑似
	class SubSp extends Handler {

		public SubSp(Pattern p) {
			ptn=p;
			nextHandler=null;
		}

		@Override
	    public LogResult getData(String str) {
			Matcher m=ptn.matcher(str);
			boolean rs=m.find();
			if (rs) {
				LogResult lr=new LogResult();
				int sp=Integer.parseInt(m.group(2));
				String pro=m.group(1);
				lr.setProvince(pro);
				lr.setSp(-sp);
				list.add(lr);
				return lr;
			}
			else return null;
		}		
	}
}