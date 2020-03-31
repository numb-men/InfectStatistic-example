import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.Collator;
import java.util.Arrays;

/**
 * InfectStatistic
 * TODO
 *
 * @author ������
 * @version 7.3
 * 
 */
public class InfectStatistic {
	static int count=0;//�����������ݵ�����
	static int selcount=0;//��ɸѡָ��ʡ�ݵĸ���
	static int seltypecount=0;////��ɸѡָ�����͵ĸ���
	static line[] all=new line[34];//��ʼ�����
	static line[] result=new line[34];//�ܵ��������
    static line[] proresult=new line[34];//ɸѡʡ�ݺ���������
    static String topath=new String();//����ĵ�·��
    static String frompath=new String();//log�ļ�·��
    static int index=0;//�����Ƿ��������ڱ���־����һ�컹�磬������ֵΪ-2
    static int isWrong=0;//���������Ƿ�����������ڱ����µ���־����
        	
    public static void main(String[] args) throws IOException {
    	if(args.length==0) {
    		System.out.println("����������Ϊ�գ����������룡");
    		return;
    	}
    	if(!args[0].equals("list")) {//�������
    		System.out.println("δ�������list�����򲻿��Դ����������������룡");
    		return;
    	}
	    for(int j=0;j<34;j++) {
	    	all[j]=new line();
	    	result[j]=new line();
	    	proresult[j]=new line();
	    }
    	cmdArgs cmd=new cmdArgs(args);
    	int hasDate=cmd.hasParam("-date");//�����������
    	int hasPro=cmd.hasParam("-province");//����Ƿ���province����
    	int hasType=cmd.hasParam("-type");//����Ƿ�����������
    	int hasPath=cmd.hasParam("-out");//��ȡ���·������
    	int hasLog=cmd.hasParam("-log");//��ȡlog·������
    	getTopath(args,hasPath);
    	getFrompath(args,hasLog);
    	if(!isCorformpath(frompath)) {//������־�����ļ����д�
    		return;
    	}
    	if(hasDate!=-1) {//��ָ������
    		readLog(args[hasDate+1],true);  
    		if(index!=-2&&isWrong==0&&hasPro!=-1) {//��ָ��ʡ��
    			String[] province=selectPro(args,hasPro);    			
    			line[] a=selectMes(province);   			 			
    			line[] b=sortline1(a,selcount);
    			printSel(b);
    			if(hasType!=-1) {//��ָ������
        			String[] type=selectType(args,hasType);
        			printSelpart(proresult,type,selcount);
    			}
    		}
    		else if(index!=-2&&isWrong==0&&hasType!=-1) {//��ָ������δָ��ʡ��
    			String[] type=selectType(args,hasType);
    			addAll();
    			printSelpart(result,type,count+1);
			}    		
    	}
    	else {//δָ������
    		readLog(args[hasDate+1],false);   		
    		if(hasPro!=-1) {//��ָ��ʡ��   			
    			String[] province=selectPro(args,hasPro);    			
    			line[] a=selectMes(province);  			
    			line[] b=sortline1(a,selcount);
    			printSel(b);
    			if(hasType!=-1) {//��ָ�����ͺ�ʡ��
        			String[] type=selectType(args,hasType);
        			printSelpart(proresult,type,selcount);
    			}
    		}
    		else if(hasType!=-1) {//��ָ������δָ��ʡ��
    			String[] type=selectType(args,hasType);
    			addAll();
    			printSelpart(result,type,count+1);
			}
    	}    	
    }    

	static class line{//ͳ��֮��Ĳ���ÿ���Ľṹ
		String location;//����λ��
		int grhz;//��Ⱦ��������
		int yshz;//���ƻ�������
		int recover;//��������
		int dead;//��������
		
		line(String plocation,int pgrhz,int pyshz,int precover,int pdead){
			location=plocation;
			grhz=pgrhz;
			yshz=pyshz;
			recover=precover;
			dead=pdead;
		}
		
		line(){		
		}
		/*
	              * ���ܣ���ӡһ��ͳ��������Ϣ
	              * �����������
	              *����ֵ����Ϣ�ַ���
	    */
		String printline() {
			return(location+" ��Ⱦ����"+grhz+"�� ���ƻ���"+yshz+"�� ����"+recover+"�� ����"+dead+"��");
		}
	}

	static class cmdArgs {//��ȡʹ������
	    String[] args;
	    
	    cmdArgs(String[] passargs) {
	        args=passargs;
	    }
	    
	    /*
	                     * ���ܣ��ж��Ƿ����ĳ����
	                     * �����������Ҫ���������
	                     *����ֵ�������������intֵ�����û�и������򷵻�-1
	    */
	    int hasParam(String key) {    	
	        for(int i=0;i<args.length;i++) {
	        	if(args[i].equals(key)) {
	        		return i;
	        	}
	        }
	        return -1;
	    }    
	}

    /*
              * ���ܣ��ж����ڵĺϷ���
              * ������������¸�����־��ʱ�䣬����֤�����ַ���
              *����ֵ��true,false
    */
    static boolean isCorrectdate(String lastdate,String date) {
    	if(isBefore(lastdate,date)) {
    		return false;
    	}
    	else {
    		return true;
    	}
    }
    
    /*
     * ���ܣ�������־�ļ������ĵ���·������ȷ��
     * ���������String��־�ļ���·��
     *����ֵ��boolean
    */
    static boolean isCorformpath(String path) {
    	//System.out.println(path.matches("^[A-z]:\\\\(.+?\\\\)*$"));
    	if(path.matches("^[A-z]:\\\\(.+?\\\\)*$")){//��ʽ��ȷ
    		File file = new File(path);
    		if(!file.exists()) {//�����ļ��в�����
    			System.out.println("�������־�ļ�·�������ڣ��������������");
    			return false; 
    		}
    		else {
    			String[] filename = file.list();//��ȡ������־�ļ���     	
    			if(filename.length==0) {//�ļ�����û��־
    				System.out.println("�������־�ļ����������ݣ��������������");
    				return false;
    			}
    			else {
    				return true;
    			}
    		}
    	}
    	else {
    		System.out.println("�������־�ļ�·����ʽ�����������������");
    		return false;
    	}
    }
    
    /*
     * ���ܣ���ȡ����ĵ�λ��
     * ���������������String���飬-out������������
     *����ֵ����
    */
    static void getTopath(String[] args,int pos) {
    	int i=pos+1;//���·����������   
		topath=args[i];
		//System.out.print(topath);
    }    
    
    /*
     * ���ܣ���ȡ��־�ĵ�λ��
     * ���������������String���飬-log������������
     *����ֵ����
    */
    static void getFrompath(String[] args,int pos) {
    	int i=pos+1;//���·����������   
		frompath=args[i];
    }    
    
    /*
     * ���ܣ��Ƚ����ڴ�С
     * ���������������Ҫ�Ƚϵ������ַ���
     *����ֵ��ǰ<�󷵻�true��ǰ>�󷵻�false
    */
    static boolean isBefore(String date1,String date2) {
    	if(date1.compareTo(date2)>=0) {
    		return false;
    	}
    	else {
    		return true;
    	}
    }
    
    /*
              * ���ܣ���ȡ������־��ʱ��
              * �����������
              *����ֵ�������ַ���
    */
    static String getLastdate() {
    	String date="";
    	File file = new File(frompath);
        String[] filename = file.list();//��ȡ������־�ļ���    
    	date=filename[filename.length-1].substring(0,10);   
    	//System.out.print(date);
    	return date;
    }
    
    /*
     * ���ܣ���ȡָ�������ļ���������־�е�����
     * ���������ָ�������ַ���
     *����ֵ������intֵ
    */
    static int findPot(String date) {
    	File file = new File(frompath);
        String[] filename = file.list();//��ȡ������־�ļ���      
        int mid=-1;//�м�洢�������ݴ淵��ֵ
        if(isBefore(date,filename[0].substring(0,10))) {//�������ڱ���־���绹��
        	return -2;
        }
    	for(int i=0;i<filename.length-1;i++) {
    		String datecut1=filename[i].substring(0,10);//ֻ��ȡ�ļ���ǰ������
    		String datecut2=filename[i+1].substring(0,10);//ǰ����������
    		if(date.equals(datecut1)) {   	   			
    			mid=i;
    			return mid;
    		}
    		else if(date.equals(datecut2)) {
    			mid=i+1;
    			return mid;
    		}
    		else if(isBefore(datecut1,date)&&isBefore(date,datecut2)) {//���������������м�¼����־֮��
    			mid=i;
    			return mid;
    		}   		
    	}    	
    	return -1;   	
    }
        
    /*
     * ���ܣ���ȡlog�ļ�
     * ���������ָ����������ڣ��Ƿ�ָ���������
     *����ֵ����"d:/log/"
    */
    static void readLog(String date,boolean hasDate) throws IOException {
    	//System.out.print("1");
    	if(hasDate==true) {    			
    		if(isCorrectdate(getLastdate(),date)) {//��������������ȷ��
    			int i=0;//������־��ȡ����
    			File file = new File(frompath);
    			String[] filename = file.list();//��ȡ������־�ļ���     	
    			index=findPot(date);
    			if(index==-2) {//�����µ����ڻ���
    				File f = new File(topath);
    		        BufferedWriter output = new BufferedWriter(new FileWriter(f,false));
    		        output.write("��");  
    		        output.close();
    			}
    			else {
	    			while(i<=index) { 
	    				//System.out.print(findPot(date));			
						FileInputStream fs=new FileInputStream(frompath+filename[i]);
					    InputStreamReader is=new InputStreamReader(fs,"UTF-8");
					    BufferedReader br=new BufferedReader(is);
					    String s="";				    
					    while((s=br.readLine())!=null){//һ��һ�ж�
					    	if(s.charAt(0)=='/'&&s.charAt(1)=='/') {//�ų�ע�͵�������
					    		continue;
					    	}
					    	else {
					    		String[] sp =s.split(" ");//�ָ������ַ���
					    		statistics(sp,all);
					    	}
					    	//System.out.print(s+"\n");
			    	    }
					    br.close();
					    i++;
			    	}
    			}
    		}
    		else {//���ڲ���ȷ
    			isWrong=1;
    			System.out.print("��������ڳ�����Χ�����������");
    		}
    	}
    	else {//û����ָ������
    		int i=0;//������־��ȡ����
			File file = new File(frompath);
			String[] filename = file.list();//��ȡ������־�ļ���  
			while(i<filename.length) {   			
				FileInputStream fs=new FileInputStream(frompath+filename[i]);
			    InputStreamReader is=new InputStreamReader(fs,"UTF-8");
			    BufferedReader br=new BufferedReader(is);
			    String s="";				    
			    while((s=br.readLine())!=null){//һ��һ�ж�
			    	if(s.charAt(0)=='/'&&s.charAt(1)=='/') {//�ų�ע�͵�������
			    		continue;
			    	}
			    	else {
			    		String[] sp =s.split(" ");//�ָ������ַ���
					    statistics(sp,all);			    		
			    	}
	    	    }
			    br.close();
			    i++;
	    	}   		
    	}
    	if(index!=-2&&isWrong!=1) {
    		printtxt(sortline(all,count));
    	}
    }
       
     /*
	     * ���ܣ�ͳ�Ƹ��ص����
	     * �����������־��ÿ�е��ַ������飬�ܵļ�¼����
	     *����ֵ����
    */
    static void statistics(String[] sp,line[] all) {   	
    	String location="";    	
    	location=sp[0];
    	line line1;
    	if(!isExistlocation(location,all)) {//�����ڶ�Ӧ��ʡ�ļ�¼
    		line1=new line(location,0,0,0,0);//�½�������   		
    		all[count]=line1;
    		count++;
    	}
    	else {
    		line1=getLine(location,all);//���ԭ�е�������
    	}
    	if(sp[1].equals("����")) {
    		if(sp[2].equals("��Ⱦ����")) {//��ø�Ⱦ����
    			line1.grhz+=Integer.valueOf(sp[3].substring(0,sp[3].length()-1));
    			
    		}
    		else {//���ƻ���
    			line1.yshz+=Integer.valueOf(sp[3].substring(0,sp[3].length()-1));
    		}
    	}
    	else if(sp[1].equals("����")) {
    		line1.dead+=Integer.valueOf(sp[2].substring(0,sp[2].length()-1));
    		line1.grhz-=Integer.valueOf(sp[2].substring(0,sp[2].length()-1));
    	}
    	else if(sp[1].equals("����")) {
    		line1.recover+=Integer.valueOf(sp[2].substring(0,sp[2].length()-1));
    		line1.grhz-=Integer.valueOf(sp[2].substring(0,sp[2].length()-1));
    	}
    	else if(sp[1].equals("���ƻ���")) {
    		if(sp[2].equals("ȷ���Ⱦ")){
    			int change=Integer.valueOf(sp[3].substring(0,sp[3].length()-1));//�ı�����
    			line1.grhz+=change;
    			line1.yshz-=change; 			
    		}
    		else {//�������
    			String tolocation=sp[3];//����ʡ
    			int change=Integer.valueOf(sp[4].substring(0,sp[4].length()-1));//�ı�����
    			line line2;
    	    	if(!isExistlocation(tolocation,all)) {//�����ڶ�Ӧ��ʡ�ļ�¼
    	    		line2=new line(tolocation,0,0,0,0);//�½�������
    	    		all[count]=line2;
    	    		count++;
    	    	}
    	    	else {
    	    		line2=getLine(tolocation,all);//���ԭ�е�������
    	    	}
    			line1.yshz-=change;
    			line2.yshz+=change;
    		}
    	}
    	else if(sp[1].equals("�ų�")) {
    		line1.yshz-=Integer.valueOf(sp[3].substring(0,sp[3].length()-1));   		
    	}
    	else {//��Ⱦ�����������
    		String tolocation=sp[3];//����ʡ
    		//System.out.print(sp[0]);
			int change=Integer.valueOf(sp[4].substring(0,sp[4].length()-1));//�ı�����
			line line2;
	    	if(!isExistlocation(tolocation,all)) {//�����ڶ�Ӧ��ʡ�ļ�¼
	    		line2=new line(tolocation,0,0,0,0);//�½�������
	    		all[count]=line2;
	    		count++;
	    	}
	    	else {
	    		line2=getLine(tolocation,all);//���ԭ�е�������
	    	}
			line1.grhz-=change;
			line2.grhz+=change;   		
    	}
    }
    
     /*
              * ���ܣ��ҳ�ָ����ַ�Ƿ��Ѿ����ڼ�¼
              * ���������ʡ�����֣��ܵļ�¼����
              *����ֵ��true,false
    */
    static boolean isExistlocation(String location,line[] all) {
    	for(int i=0;i<count;i++) {
    		if(location.equals(all[i].location)) {
    			return true;
    		}
    	}
    	return false;    	
    }
    
     /*
              * ���ܣ��ҳ�ָ����ַ�ļ�¼
              * ���������ʡ�����֣��ܵļ�¼����
              *����ֵ��һ����¼
    */  
    static line getLine(String location,line[] all) {
    	for(int i=0;i<count;i++) {
    		if(location.equals(all[i].location)) {
    			return all[i];
    		}
    	}
    	return null;//�����õ�
    }
    
    /*
     * ���ܣ���ȫ����Ϣ����result��������
     * �����������
     *����ֵ����
    */
    static void addAll() {
    	line[] mid=new line[count+1];//�ݴ���Ϣ
    	mid[0]=calAll(result,count);
    	for(int i=1;i<count+1;i++) {
    		mid[i]=result[i-1];
    	}
    	result=mid;
    }
    
     /*
     * ���ܣ������м�¼�����txt�ļ�
     * ����������ܵļ�¼����all
     *����ֵ����
    */
    static void printtxt(line[] result) throws IOException {
    	File f = new File(topath);
        BufferedWriter output = new BufferedWriter(new FileWriter(f,false));
        output.write(calAll(result,count).printline()+"\n");
        for(int i=0;i<count;i++) {//д��ͳ������
        	output.write(result[i].printline()+"\n");
        }
        output.write("//���ĵ�������ʵ���ݣ���������ʹ��");
    	output.close();
    }
    
    /*
     * ���ܣ�ƴ��˳������line����
     * �����������¼����,�����������
     *����ֵ������õ�����
    */
    static line[] sortline(line[] wannasort,int num) {
    	String[] location=new String[num];
    	for(int i=0;i<num;i++) {
    		location[i]=wannasort[i].location;    		
    	}    	
        Collator cmp = Collator.getInstance(java.util.Locale.CHINA);
        Arrays.sort(location, cmp);
        int i=0; 
        int j=0;//����ʡ��ƴ��˳������
        while(j<num) {       	
        	while(i<num) {
	        	if(wannasort[i].location.equals(location[j])) {
	        		result[j]=wannasort[i];
	        		j++;
	        		if(j>=num) {
	        			break;
	        		}
	        	}
	        	i++;
        	}
        } 
        return result;
    }
    
    /*
     * ���ܣ�ƴ��˳������line���飨��ѡʡ�ݺ�ģ�
     * �����������¼����,�����������
     *����ֵ������õ�����
    */
    static line[] sortline1(line[] wannasort,int num) {
    	String[] location=new String[num];
    	int i=0; 
    	line[] aa=new line[num];
    	for(i=0;i<num;i++) {
    		aa[i]=new line();
    	}
    	for(i=0;i<num;i++) {
    		location[i]=wannasort[i].location;   		
    	}    	
        Collator cmp = Collator.getInstance(java.util.Locale.CHINA);
        Arrays.sort(location, cmp);
        i=0;
        int j=0;//����ʡ��ƴ��˳������        	
    	while(i<num) {
        	if(wannasort[i].location.equals(location[j])) {
        		aa[j]=wannasort[i];
        		j++;       		
        		if(j>=num) {
        			break;
        		}
        		i=-1;//���¿�ʼѭ��
        	}
        	i++;
    	}    
        return aa;
    }
    
    /*
     * ���ܣ���ѡʡ������Ϣ
     * ����������ַ���ʡ������,Ҫ������ʡ�ĸ���
     * ����ֵ��ɸѡ�����Ϣ����
    */
    static line[] selectMes(String[] pro) {
    	int flag=0;//�Ƿ��ҵ��Ѳ��ʡ
        int j=0;//����ɸѡ����Ϣ����
        int i=0;//����������Ϣ������
        line[] aftersel=new line[selcount];//ɸѡ֮�����Ϣ���� 
        for(i=0;i<selcount;i++) {
        	aftersel[i]=new line();
        }
        i=0;
        while(j<selcount) {
        while(i<count) {
        	flag=0;
        	if(result[i].location.equals(pro[j])) {
        		aftersel[j]=result[i];
        		j++;
        		i=-1;//��ͷ��ʼѭ������
        		flag=1;
        		if(j==selcount) {
        			break;
        		}
        	}
        	else if(i==count-1&&flag==0) {//ѭ����һȦû����Ӧ��Ϣ
        		if(pro[j].equals("ȫ��")) {//ȫ�����
	    			aftersel[j]=calAll(result,count);
	    			j++;
	    			flag=1;	    			
	    		}
	    		else {
		    		aftersel[j]=new line(pro[j],0,0,0,0);
		    		j++;
		    		flag=1;		    		
	    		}
        		if(j==selcount) {
	        			break;
	        		}
        	}
        	i++;
        }
        i=0;
        }
        return aftersel;
    }
    
    /*
     * ���ܣ��ж�����ѡ��ʡ������Щ
     * ��������������ַ�������args��province������������
     * ����ֵ��ʡ���ַ�������
    */
    static String[] selectPro(String[] args,int pos) {
    	String[] province=new String[34];   
    	int i=pos+1;   
		while(i<args.length&&args[i].charAt(0)!='-') {//��������
			province[selcount]=args[i];
			selcount++;
			i++;
		}    	  
		/*for(i=0;i<selcount;i++) {
			System.out.print(province[i]+"\n");
		}*/
    	return province;
    }
    
    /*
     * ���ܣ��ж�����ѡ����������Щ
     * ��������������ַ�������args��type������������
     * ����ֵ�������ַ�������
    */
    static String[] selectType(String[] args,int pos) {
    	String[] type=new String[4];   
    	int i=pos+1;    	
		while(i<args.length&&args[i].charAt(0)!='-') {//��������
			type[seltypecount]=args[i];
			seltypecount++;
			i++;
		}
		
    	return type;
    }
      
    /*
     * ���ܣ�����ȫ���������
     * ���������������Ϣ֮��line���飬��Ϣ����
     * ����ֵ��ȫ����line����
    */ 
    static line calAll(line[] all,int num) {
    	int sumg=0;//ȫ����Ⱦ��������
        int sumy=0;//ȫ�����ƻ�������
        int sumd=0;//ȫ����������
        int sumr=0;//ȫ����������
        for(int i=0;i<num;i++) {
        	sumg+=all[i].grhz;
        	sumy+=all[i].yshz;
        	sumd+=all[i].dead;
        	sumr+=all[i].recover;
        }
        return new line("ȫ��",sumg,sumy,sumr,sumd);
    }
    
    /*
	     * ���ܣ����ɸѡʡ�ݺ�Ľ��
	     * ���������Ҫ�������������line
	     * ����ֵ����
    */
    static void printSel(line[] selresult) throws IOException {
    	int flag=-1;//ȫ����Ϣ������λ��
    	File f = new File(topath);
        BufferedWriter output = new BufferedWriter(new FileWriter(f,false));       
        //output.write("���������"+"\n");
        for(int i=0;i<selcount;i++) {//����ȫ����Ϣ������λ
        	if(selresult[i].location.equals("ȫ��")) {
        		output.write(calAll(result,count).printline()+"\n");
        		proresult[0]=calAll(result,count);
        		flag=i;
        		break;
        	}
        }        
        for(int i=0;i<selcount;i++) {//д��ͳ������
        	if(selresult[i].location.equals("ȫ��")) {        
        			i=i+1;//����ȫ��       		
        	}        	
        	if(i<selcount) {
        		output.write(selresult[i].printline()+"\n");
        	}
        }
        output.write("//���ĵ�������ʵ���ݣ���������ʹ��");
    	output.close();
    	if(flag!=-1) {//��ȫ����Ϣ����ǰ
	    	for(int i=1;i<=flag;i++) {
	    		proresult[i]=selresult[i-1];
	    	}
	    	for(int i=flag+1;i<selcount;i++) {
	    		proresult[i]=selresult[i];
	    	}
    	}
    	else {
    		for(int i=0;i<selcount;i++) {
        		proresult[i]=selresult[i];
        	}	
    	}
    }
    
	/*
	     * ���ܣ����ɸѡ���ͺ�Ľ��
	     * ���������Ҫ�������������line��ָ�����͵�String���飬�����line���鳤��
	     * ����ֵ����
	*/
    //location+" ��Ⱦ����"+grhz+"�� ���ƻ���"+yshz+"�� ����"+recover+"�� ����"+dead+"��"
	static void printSelpart(line[] selresult,String[] type,int len) throws IOException {
		File f = new File(topath);
	    BufferedWriter output = new BufferedWriter(new FileWriter(f,false));
	    int j=0;//�������line������
	    int flag=0;//��ע�������Ƿ��ǵ�һ�������Ǵ�ʡ�ݵ�����
	    //output.write("���������"+"\n");
	    while(j<len) {
	    	flag=0;
		    for(int i=0;i<seltypecount;i++) {
		    	if(type[i].equals("ip")) {
		    		if(flag==0) {//�������ǵ�һ��
		    			output.write(selresult[j].location+" ");
		    		}
			    	output.write("��Ⱦ����"+selresult[j].grhz+"�� ");
			    	flag=1;			    		
		    	}
		    	if(type[i].equals("sp")) {
		    		if(flag==0) {//�������ǵ�һ��
		    			output.write(selresult[j].location+" ");
		    		}
			    	output.write("���ƻ���"+selresult[j].yshz+"�� ");
			    	flag=1;			    		
		    	}
		    	if(type[i].equals("cure")) {
		    		if(flag==0) {//�������ǵ�һ��
		    			output.write(selresult[j].location+" ");
		    		}
			    	output.write("����"+selresult[j].recover+"�� ");
			    	flag=1;			    		
		    	}
		    	if(type[i].equals("dead")) {
		    		if(flag==0) {//�������ǵ�һ��
		    			output.write(selresult[j].location+" ");
		    		}
			    	output.write("����"+selresult[j].dead+"�� ");
			    	flag=1;			    		
		    	}
		    }
		    j++;
		    output.write("\n");
	    }
	    output.write("//���ĵ�������ʵ���ݣ���������ʹ��");
		output.close();
	}    
}
