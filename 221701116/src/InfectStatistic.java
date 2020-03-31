/**
 * InfectStatistic
 * TODO
 *
 * @author ĺЦ
 * @version 1.0
 * @since 2020-02-06
 */

import java.io.File;
import java.io.*;
import java.util.regex.*;
import java.util.Date;
import java.text.SimpleDateFormat;

class InfectStatistic {
	public String log_path; //��־�ļ�λ��
	public String out_path; //����ļ�λ��
	/*
	 * ָ������
	 * ��ָ������Ĭ������Ϊ��ǰ���ڣ�����������ĸ������ڣ�
	 */
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	Date d = new Date(System.currentTimeMillis());
	public String date = formatter.format(d); //ָ������
	
	/*
	 * ָ������(��˳��ֱ�Ϊip,sp,cure,dead)
	 * ����ֵ��Ϊ0ʱ��ʾ��Ҫ�г���Ϊ0ʱ�����г�
	 * Ĭ��˳��Ϊ1,2,3,4����ȫ���г�����������˳��
	 */
	public int[] type = {1,2,3,4};
	/*
	 * �����б�(��˳��ֱ�Ϊip,sp,cure,dead)
	 */
	public String[] type_str = {"��Ⱦ����", "���ƻ���", "����", "����"};
	
	/*
	 * ָ��ʡ��(��ʡ���������򣨵�һλΪȫ����)
	 * ����ֵΪ1ʱ��ʾ��Ҫ�г���Ϊ0ʱ�����г�
	 * Ĭ�ϵ�һλΪ-1,����ȫΪ0,����δָ��ʡ��
	 */
	public int[] province = new int[35];
	
	/*
	 * ʡ���б�(��ʡ������ƴ�����򣨵�һλΪȫ����)
	 */
	public String[] province_str = {"ȫ��", "����", "����" ,"����", "����", "����","����",
			"�㶫", "����", "����", "����", "�ӱ�", "����", "������", "����", "����", "����",
			"����", "����", "����", "���ɹ�", "����", "�ຣ", "ɽ��", "ɽ��", "����", "�Ϻ�",
			"�Ĵ�", "̨��", "���", "����", "���", "�½�", "����", "�㽭"};
	/*
	 * ����ͳ��(һά����ʡ�����򣬶�ά��ʾ��������)
	 * ��ʡ������ƴ�����򣨵�һλΪȫ����
	 * ���Ͱ�˳��ֱ�Ϊip,sp,cure,dead
	 */
	public int[][] people = new int[35][4]; 
	
	/*
	 * ���������в���
	 */
	class CmdArgs{
		String[] args; //���洫���������
		
		CmdArgs(String[] args_str){
			args = args_str;
			province[0] = -1; //��ȫ��ָ��λ��Ϊ-1
		}
		
		/*
		 * ��ȡ�������еĲ���
		 * @return boolean
		 */
		public boolean extractCmd() {
			if(!args[0].equals("list")) {//�ж������ʽ��ͷ�Ƿ���ȷ
				System.out.println("�����и�ʽ���󡪡���ͷ��list����");
				return false;
			}
			
			int i;
			
			for(i = 1; i < args.length; i++) {
				if(args[i].equals("-log")) { //��ȡ��-log����
					i = getLogPath(++i);
					if(i == -1) { //˵�����������з��������г���
						System.out.println("�����и�ʽ���󡪡���־·����������");
						return false;
					}
				} else if(args[i].equals("-out")) { //��ȡ��-out����
					i= getOutPath(++i);
					if(i == -1) { //˵�����������з��������г���
						System.out.println("�����и�ʽ���󡪡����·����������");
						return false;
					}
				} else if(args[i].equals("-date")) { //��ȡ��-date����
					i= getDate(++i);
					if(i == -1) { //˵�����������з��������г���
						System.out.println("�����и�ʽ���󡪡����ڲ�������");
						return false;
					}
				} else if(args[i].equals("-type")) { //��ȡ��-type����
					i = getType(++i); 
					if(i == -1) { //˵�����������з��������г���
						System.out.println("�����и�ʽ���󡪡�ָ�����Ͳ�������");
						return false;
					}
				} else if(args[i].equals("-province")) { //��ȡ��-province����
					i = getProvince(++i);
					if(i == -1) { //˵�����������з��������г���
						System.out.println("�����и�ʽ���󡪡�ָ��ʡ�ݲ�������");
						return false;
					}
				} else { //δ��ȡ�����ϲ���
					System.out.println("�����и�ʽ���󡪡���������ϲ�������");
					return false;
				}
			}
			return true;
		}
		
		/*
		 * �õ���־�ļ�λ��
		 * @param i
		 * @return int
		 */
		public int getLogPath(int i) {
			if(i < args.length) { //���±�δԽ��
				if(args[i].matches("^[A-z]:\\\\(.+?\\\\)*$")) //�ж��ַ����ǲ����ļ�Ŀ¼·��
					log_path = args[i];
				else
					return -1;
			} else
				return -1;
			return i;
		}
		
		/*
		 * �õ���־�ļ�λ��
		 * @param i
		 * @return int
		 */
		public int getOutPath(int i) {
			if(i < args.length) { //���±�δԽ��
				if(args[i].matches("^[A-z]:\\\\(\\S+)+(\\.txt)$")) //�ж��ַ����ǲ���txt�ļ�·��
					out_path = args[i];
				else
					return -1;
			} else
				return -1;
			return i;
		}
		
		/*
		 * �õ�ָ������
		 * @param i
		 * @return int
		 */
		public int getDate(int i) {
			if(i < args.length) { //���±�δԽ��
				if(isValidDate(args[i])) { //�ж��Ƿ��ǺϷ����ڸ�ʽ��yyyy-MM-dd
					if(date.compareTo(args[i]) >= 0) //�ж������Ƿ񳬹���ǰ����
						date = args[i] + ".log.txt";
					else
						return -1;
				} else
					return -1;
			} else
				return -1;
			return i;
		}
		
		/*
		 * �ж��Ƿ��ǺϷ����ڸ�ʽ��yyyy-MM-dd
		 * @param strDate
		 */
		public boolean isValidDate(String strDate) {
	        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	        try {
	            //����lenientΪfalse. ����SimpleDateFormat��ȽϿ��ɵ���֤����
	        	//����2018-02-29�ᱻ���ܣ���ת����2018-03-01 
	            format.setLenient(false);
	            format.parse(strDate);
	            
	            //�жϴ����yyyy��-MM��-dd�� �ַ����Ƿ�Ϊ����
	            String[] sArray = strDate.split("-");
	            for (String s : sArray) {
	                boolean isNum = s.matches("[0-9]+");
	                if (!isNum)
	                    return false;
	            }
	        } catch (Exception e) {
	            return false;
	        }
	        return true;
	    }
		
		/*
		 * �õ�ָ������(type���������ж��)
		 * @param i
		 * @return int
		 */
		public int getType(int i) {
			int j, m = i; 

			if(i < args.length) { //���±�δԽ��
				for(j = 0; j < 4; j++) //��Ĭ��ָ��ȫ��Ϊ0
					type[j] = 0;
				j = 1; //j����ָ�����͵�˳�����
				while(i<args.length) {
					if(args[i].equals("ip")) {
						type[0] = j;
						i++;
						j++;
					} else if(args[i].equals("sp")) {
						type[1] = j;
						i++;
						j++;
					} else if(args[i].equals("cure")) {
						type[2] = j;
						i++;
						j++;
					} else if(args[i].equals("dead")) {
						type[3] = j;
						i++;
						j++;
					} else
						break;
				}
			}
			if(m == i) //˵��-type������ȷ����
				return -1;
			return (i - 1); //��������Ϊ-type�Ĳ�������Խ��
		}
		
		/*
		 * �õ�ָ��ʡ��(province���������ж��)
		 * @param i
		 * @return int
		 */
		public int getProvince(int i) {
			int j, m = i;

			if(i < args.length){
				province[0] = 0; //ȡ��δָ��״̬���
				while(i<args.length) {
					for(j = 0; j < province_str.length; j++) {
						if(args[i].equals(province_str[j])) { //��������ҵ���Ӧʡ��
							province[j] = 1; //ָ����ʡ����Ҫ���
							i++;
							break;
						}
					}
				}
			}
			if(m == i) //˵��-province������ȷ����
				return -1;
			return (i - 1); //��������Ϊprovince�Ĳ�������Խ��
		}
		
	}
	
	/*
	 * �����ļ�����ȡĿ¼�µ��ļ������ļ�����ȡ���ݣ�������ݵ��ļ���
	 */
	class FileHandle{
		
		
		FileHandle() {} //�չ��캯��
		
		/*
		 * ��ȡָ��·���µ��ļ�����Ŀ¼��
		 */
		public void getFileList() {
			File file = new File(log_path);
			File[] fileList = file.listFiles();
			String fileName;
	 
			for (int i = 0; i < fileList.length; i++) {
				fileName = fileList[i].getName();
				if (fileName.compareTo(date) <= 0) { //������ļ�������С��ָ������
					readTxt(log_path + fileName);
				}
			}
		}
		
		
		/*
		 * ��ȡ�ļ�����
		 * @param filePath
		 */
	    public void readTxt(String filePath) {
	        try {
	            BufferedReader bfr = new BufferedReader(new InputStreamReader(
	            		new FileInputStream(new File(filePath)), "UTF-8"));
	            String lineTxt = null;
	            
	            while ((lineTxt = bfr.readLine()) != null) { //���ж�ȡ�ı�����
	            	if(!lineTxt.startsWith("//")) //������//������ȡ
	            		textProcessing(lineTxt);
	            }
	            bfr.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	    
	    /*
	     * �ı�����
	     * @param string
	     */
	    public void textProcessing(String string) {
	    	String pattern1 = "(\\S+) ���� ��Ⱦ���� (\\d+)��";
	    	String pattern2 = "(\\S+) ���� ���ƻ��� (\\d+)��";
	    	String pattern3 = "(\\S+) ���� (\\d+)��";
	    	String pattern4 = "(\\S+) ���� (\\d+)��";
	    	String pattern5 = "(\\S+) ��Ⱦ���� ���� (\\S+) (\\d+)��";
	    	String pattern6 = "(\\S+) ���ƻ��� ���� (\\S+) (\\d+)��";
	    	String pattern7 = "(\\S+) ���ƻ��� ȷ���Ⱦ (\\d+)��";
	    	String pattern8 = "(\\S+) �ų� ���ƻ��� (\\d+)��";
	    	boolean isMatch1 = Pattern.matches(pattern1, string);
	    	boolean isMatch2 = Pattern.matches(pattern2, string);
	    	boolean isMatch3 = Pattern.matches(pattern3, string);
	    	boolean isMatch4 = Pattern.matches(pattern4, string);
	    	boolean isMatch5 = Pattern.matches(pattern5, string);
	    	boolean isMatch6 = Pattern.matches(pattern6, string);
	    	boolean isMatch7 = Pattern.matches(pattern7, string);
	    	boolean isMatch8 = Pattern.matches(pattern8, string);
	    	
	    	if(isMatch1) //���� ��Ⱦ���ߴ���
	    		addIP(string);
	    	else if(isMatch2) //���� ���ƻ��ߴ���
	    		addSP(string);
	    	else if(isMatch3) //���� �������ߴ���
	    		addCure(string);
	    	else if(isMatch4) //���� �������ߴ���
	    		addDead(string);
	    	else if(isMatch5) //��Ⱦ���� ���봦��
	    		flowIP(string);
	    	else if(isMatch6) //���ƻ��� ���봦��
	    		flowSP(string);
	    	else if(isMatch7) //���ƻ��� ȷ���Ⱦ����
	    		diagnosisSP(string);
	    	else if(isMatch8) //�ų� ���ƻ��ߴ���
	    		removeSP(string);
	    }
	    
	    /*
	     * ���� ��Ⱦ���ߴ���
	     * @param string
	     */
	    public void addIP(String string) {
	    	String[] str_arr = string.split(" "); //���ַ����Կո�ָ�Ϊ����ַ���
	    	int i;
	    	int n = Integer.valueOf(str_arr[3].replace("��", ""));//����ǰ�����ִ��ַ�������ת��Ϊint����
	    	
	    	for(i = 0; i < province_str.length; i++) {
	    		if(str_arr[0].equals(province_str[i])) { //��һ���ַ���Ϊʡ��
	    			people[0][0] += n; //ȫ����Ⱦ������������
	    			people[i][0] += n; //��ʡ�ݸ�Ⱦ������������
	    			if(province[0] == -1) //ʡ�ݴ���δָ��״̬
	    				province[i] = 1; //��Ҫ����ʡ���г�
	    			break;
	    		}
	    	}
	    }
	    
	    /*
	     * ���� ���ƻ��ߴ���
	     * @param string
	     */
	    public void addSP(String string) {
	    	String[] str_arr = string.split(" "); //���ַ����Կո�ָ�Ϊ����ַ���
	    	int i;
	    	int n = Integer.valueOf(str_arr[3].replace("��", ""));//����ǰ�����ִ��ַ�������ת��Ϊint����
	    	
	    	for(i = 0; i < province_str.length; i++) {
	    		if(str_arr[0].equals(province_str[i])) { //��һ���ַ���Ϊʡ��
	    			people[0][1] += n; //ȫ�����ƻ�����������
	    			people[i][1] += n; //��ʡ�����ƻ�����������
	    			if(province[0] == -1) //ʡ�ݴ���δָ��״̬
	    				province[i] = 1; //��Ҫ����ʡ���г�
	    			break;
	    		}
	    	}
	    }
	    
	    /*
	     * ���� �������ߴ���
	     * @param string
	     */
	    public void addCure(String string) {
	    	String[] str_arr = string.split(" "); //���ַ����Կո�ָ�Ϊ����ַ���
	    	int i;
	    	int n = Integer.valueOf(str_arr[2].replace("��", ""));//����ǰ�����ִ��ַ�������ת��Ϊint����
	    	
	    	for(i = 0; i < province_str.length; i++) { 
	    		if(str_arr[0].equals(province_str[i])) { //��һ���ַ���Ϊʡ��
	    			people[0][2] += n; //ȫ��������������
	    			people[0][0] -= n; //ȫ����Ⱦ������������
	    			people[i][2] += n; //��ʡ��������������
	    			people[i][0] -= n; //��ʡ�ݸ�Ⱦ������������
	    			if(province[0] == -1) //ʡ�ݴ���δָ��״̬
	    				province[i] = 1; //��Ҫ����ʡ���г�
	    			break;
	    		}
	    	}
	    }
	    
	    /*
	     * ���� �������ߴ���
	     * @param string
	     */
	    public void addDead(String string) {
	    	String[] str_arr = string.split(" "); //���ַ����Կո�ָ�Ϊ����ַ���
	    	int i;
	    	int n = Integer.valueOf(str_arr[2].replace("��", ""));//����ǰ�����ִ��ַ�������ת��Ϊint����
	    	
	    	for(i = 0; i < province_str.length; i++) {
	    		if(str_arr[0].equals(province_str[i])) { //��һ���ַ���Ϊʡ��
	    			people[0][3] += n; //ȫ��������������
	    			people[0][0] -= n; //ȫ����Ⱦ������������
	    			people[i][3] += n; //��ʡ��������������
	    			people[i][0] -= n; //��ʡ�ݸ�Ⱦ������������
	    			if(province[0] == -1) //ʡ�ݴ���δָ��״̬
	    				province[i] = 1; //��Ҫ����ʡ���г�
	    			break;
	    		}
	    	}
	    }
	    
	    /*
	     * ��Ⱦ���� ���봦��
	     * @param string
	     */
	    public void flowIP(String string) {
	    	String[] str_arr = string.split(" "); //���ַ����Կո�ָ�Ϊ����ַ���
	    	int i;
	    	int n = Integer.valueOf(str_arr[4].replace("��", ""));//����ǰ�����ִ��ַ�������ת��Ϊint����
	    	
	    	for(i = 0; i < province_str.length; i++) {
	    		if(str_arr[0].equals(province_str[i])) { //��һ���ַ���Ϊ����ʡ��
	    			people[i][0] -= n; //��ʡ�ݸ�Ⱦ������������
	    			if(province[0] == -1) //ʡ�ݴ���δָ��״̬
	    				province[i] = 1; //��Ҫ����ʡ���г�
	    		}
	    		if(str_arr[3].equals(province_str[i])) { //���ĸ��ַ���Ϊ����ʡ��
	    			people[i][0] += n; //��ʡ�ݸ�Ⱦ������������
	    			if(province[0] == -1) //ʡ�ݴ���δָ��״̬
	    				province[i] = 1; //��Ҫ����ʡ���г�
	    		}
	    	}
	    }
	    
	    /*
	     * ���ƻ��� ���봦��
	     * @param string
	     */
	    public void flowSP(String string) {
	    	String[] str_arr = string.split(" "); //���ַ����Կո�ָ�Ϊ����ַ���
	    	int i;
	    	int n = Integer.valueOf(str_arr[4].replace("��", ""));//����ǰ�����ִ��ַ�������ת��Ϊint����
	    	
	    	for(i = 0; i < province_str.length; i++) {
	    		if(str_arr[0].equals(province_str[i])) { //��һ���ַ���Ϊ����ʡ��
	    			people[i][1] -= n; //��ʡ�����ƻ��߼���
	    			if(province[0] == -1) //ʡ�ݴ���δָ��״̬
	    				province[i] = 1; //��Ҫ����ʡ���г�
	    		}
	    		if(str_arr[3].equals(province_str[i])) { //���ĸ��ַ���Ϊ����ʡ��
	    			people[i][1] += n; //��ʡ�����ƻ�������
	    			if(province[0] == -1) //ʡ�ݴ���δָ��״̬
	    				province[i] = 1; //��Ҫ����ʡ���г�
	    		}
	    	}
	    	
	    }
	    
	    /*
	     * ���ƻ��� ȷ���Ⱦ����
	     * @param string
	     */
	    public void diagnosisSP(String string) {
	    	String[] str_arr = string.split(" "); //���ַ����Կո�ָ�Ϊ����ַ���
	    	int i;
	    	int n = Integer.valueOf(str_arr[3].replace("��", ""));//����ǰ�����ִ��ַ�������ת��Ϊint����
	    	
	    	for(i = 0; i < province_str.length; i++) {
	    		if(str_arr[0].equals(province_str[i])) { //��һ���ַ���Ϊʡ��
	    			people[0][1] -= n; //ȫ�����ƻ�����������
	    			people[0][0] += n; //ȫ����Ⱦ������������
	    			people[i][1] -= n; //��ʡ�����ƻ�����������
	    			people[i][0] += n; //��ʡ�ݸ�Ⱦ������������
	    			if(province[0] == -1) //ʡ�ݴ���δָ��״̬
	    				province[i] = 1; //��Ҫ����ʡ���г�
	    			break;
	    		}
	    	}
	    }
	    
	    /*
	     * �ų� ���ƻ��ߴ���
	     * @param string
	     */
	    public void removeSP(String string) {
	    	String[] str_arr = string.split(" "); //���ַ����Կո�ָ�Ϊ����ַ���
	    	int i;
	    	int n = Integer.valueOf(str_arr[3].replace("��", ""));//����ǰ�����ִ��ַ�������ת��Ϊint����
	    	
	    	for(i = 0; i < province_str.length; i++) {
	    		if(str_arr[0].equals(province_str[i])) { //��һ���ַ���Ϊʡ��
	    			people[i][1] -= n; //��ʡ�����ƻ�����������
	    			people[0][1] -= n; //ȫ�����ƻ�����������
	    			if(province[0] == -1) //ʡ�ݴ���δָ��״̬
	    				province[i] = 1; //��Ҫ����ʡ���г�
	    			break;
	    		}
	    	}
	    }
	    
	    
	    /*
		 * ����ļ�����
		 */
	    public void writeTxt() {
	    	FileWriter fwriter = null;
	    	int i, j, k;
	        try {
	        	fwriter = new FileWriter(out_path);
	        	if(province[0] == -1) //ʡ�ݴ���δָ��״̬
					province[0] = 1; //��ȫ���Լ�ʡ�ݸ�Ϊָ��״̬�����г���־�ļ��г��ֵ�ʡ�ݣ�
	        	for(i = 0; i < province_str.length; i++) { //����ʡ�ݲ�����Ҫ�г���ʡ��
	        		if(province[i] == 1) { //��ʡ����Ҫ�г�
	        			fwriter.write(province_str[i] + " ");
	        			for(j = 0; j < type.length; j++) {
	        				for(k = 0; k < type.length; k++) {
	        					if(type[k] == j+1) { //����ʡ����Ҫ�г��Ұ��ղ���˳��
	        						fwriter.write(type_str[k] + people[i][k] + "�� ");
	        						break;
	        					}
	        				}
	        			}
	        			fwriter.write("\n");
	        		}
	        	}
	        	fwriter.write("// ���ĵ�������ʵ���ݣ���������ʹ��");
	        } catch (Exception e) {
	            e.printStackTrace();
	        } finally {
	            try {
	                fwriter.flush();
	                fwriter.close();
	            } catch (IOException ex) {
	                ex.printStackTrace();
	            }
	        }
	    }
	}
	
    public static void main(String[] args) {
    	if (args.length == 0) {  
            System.out.println("�����и�ʽ���󡪡�δ�������");  
            return;
        }
    	InfectStatistic infectStatistic = new InfectStatistic();
    	InfectStatistic.CmdArgs cmdargs = infectStatistic.new CmdArgs(args);
    	boolean b = cmdargs.extractCmd();
    	if(b == false) {
    		return;
    	}
    	InfectStatistic.FileHandle filehandle = infectStatistic.new FileHandle();
    	filehandle.getFileList();
    	filehandle.writeTxt();
    }
}