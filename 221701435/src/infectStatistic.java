import java.io.*;
import java.text.Collator;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * InfectStatistic TODO
 *
 * @author moli
 * @version 1.0.0
 * @since 2020-02-17
 */

public class infectStatistic {

	/**
	 * CommandLine TODO
	 * 
	 * @description ͨ���ڲ�Command�������list���Record��������������ŵ�һЩ����
	 * @author moli
	 * @version m 1.0.0
	 * @since 2020-02-17
	 */

	static class CommandLine {

		private Command command;

		private Record record;

		public Command getCommand() {
			return command;
		}

		public void setCommand(Command command) {
			this.command = command;
		}

		public Record getRecord() {
			return record;
		}

		public void setRecord(Record record) {
			this.record = record;
		}

		public String toString() {
			return command + " -" + record.isLog() + " " + record.getLog_content() + " -" + record.isOut() + " "
					+ record.getOut_content() + " -" + record.isDate() + " " + record.getDate_content() + " -"
					+ record.isType() + " " + record.getType_content() + " -" + record.isProvince() + " "
					+ record.getProvince_content();
		}

		/**
		 * Command TODO
		 * 
		 * @description ���岼���ͱ������ж������Ƿ�Ϊlist
		 * @author moli
		 * @version m 1.0.0
		 * @since 2020-02-17
		 */

		static class Command {
			private boolean list;

			public boolean isList() {
				return list;
			}

			public void setList(boolean list) {
				this.list = list;
			}
		}

		/**
		 * Record TODO
		 * 
		 * @description ����������log��out,date,type,province����
		 * @author moli
		 * @version m 1.0.0
		 * @since 2020-02-17
		 */

		static class Record {
			private boolean log;

			private String log_content;

			private boolean out;

			private String out_content;

			private boolean date;

			private String date_content;

			private boolean type;

			private ArrayList<TypeOption> type_content;

			private boolean province;

			private ArrayList<String> province_content;

			public boolean isLog() {
				return log;
			}

			public void setLog(boolean log) {
				this.log = log;
			}

			public String getLog_content() {
				return log_content;
			}

			public void setLog_content(String log_content) {
				this.log_content = log_content;
			}

			public boolean isOut() {
				return out;
			}

			public void setOut(boolean out) {
				this.out = out;
			}

			public String getOut_content() {
				return out_content;
			}

			public void setOut_content(String out_content) {
				this.out_content = out_content;
			}

			public boolean isDate() {
				return date;
			}

			public void setDate(boolean date) {
				this.date = date;
			}

			public String getDate_content() {
				return date_content;
			}

			public void setDate_content(String date_content) {
				this.date_content = date_content;
			}

			public boolean isType() {
				return type;
			}

			public void setType(boolean type) {
				this.type = type;
			}

			public ArrayList<TypeOption> getType_content() {
				return type_content;
			}

			public void setType_content(ArrayList<TypeOption> type_content) {
				this.type_content = type_content;
			}

			public boolean isProvince() {
				return province;
			}

			public void setProvince(boolean province) {
				this.province = province;
			}

			public ArrayList<String> getProvince_content() {
				return province_content;
			}

			public void setProvince_content(ArrayList<String> province_content) {
				this.province_content = province_content;
			}
		}

		/**
		 * TypeOptionEnum TODO
		 * 
		 * @description ����type���������IP,SP,CURE,DEAD
		 * @author moli
		 * @version m 1.0.0
		 * @since 2020-02-17
		 */

		enum TypeOptionEnum {
			IP("ip", 1, false), SP("sp", 2, false), CURE("cure", 3, false), DEAD("dead", 4, false);

			private String name;

			private int index;

			private boolean status;

			private TypeOptionEnum(String name, int index, boolean status) {
				this.name = name;
				this.index = index;
				this.status = status;
			}

			public String getName() {
				return name;
			}

			public void setName(String name) {
				this.name = name;
			}

			public int getIndex() {
				return index;
			}

			public void setIndex(int index) {
				this.index = index;
			}

			public boolean isStatus() {
				return status;
			}

			public void setStatus(boolean status) {
				this.status = status;
			}
		}

		/**
		 * TypeOption TODO
		 * 
		 * @description ��ö�ٶ�����������ͷ�װ�����д洢
		 * @author moli
		 * @version m 1.0.0
		 * @since 2020-02-17
		 */

		static class TypeOption {
			private String name;

			private int index;

			private boolean status;

			public String getName() {
				return name;
			}

			public void setName(String name) {
				this.name = name;
			}

			public int getIndex() {
				return index;
			}

			public void setIndex(int index) {
				this.index = index;
			}

			public boolean isStatus() {
				return status;
			}

			public void setStatus(boolean status) {
				this.status = status;
			}

			public TypeOption setTypeOptionIp() {
				CommandLine.TypeOption typeoption = new CommandLine.TypeOption();

				typeoption.setName(TypeOptionEnum.IP.getName());
				typeoption.setIndex(TypeOptionEnum.IP.getIndex());
				TypeOptionEnum.IP.setStatus(true);
				typeoption.setStatus(TypeOptionEnum.IP.isStatus());

				return typeoption;
			}

			public TypeOption setTypeOptionSp() {
				CommandLine.TypeOption typeoption = new CommandLine.TypeOption();

				typeoption.setName(TypeOptionEnum.SP.getName());
				typeoption.setIndex(TypeOptionEnum.SP.getIndex());
				TypeOptionEnum.SP.setStatus(true);
				typeoption.setStatus(TypeOptionEnum.SP.isStatus());

				return typeoption;
			}

			public TypeOption setTypeOptionCure() {
				CommandLine.TypeOption typeoption = new CommandLine.TypeOption();

				typeoption.setName(TypeOptionEnum.CURE.getName());
				typeoption.setIndex(TypeOptionEnum.CURE.getIndex());
				TypeOptionEnum.CURE.setStatus(true);
				typeoption.setStatus(TypeOptionEnum.CURE.isStatus());

				return typeoption;
			}

			public TypeOption setTypeOptionDead() {
				CommandLine.TypeOption typeoption = new CommandLine.TypeOption();

				typeoption.setName(TypeOptionEnum.DEAD.getName());
				typeoption.setIndex(TypeOptionEnum.DEAD.getIndex());
				TypeOptionEnum.DEAD.setStatus(true);
				typeoption.setStatus(TypeOptionEnum.DEAD.isStatus());

				return typeoption;
			}
		}
	}

	/**
	 * CommandLineAnalytic TODO
	 * 
	 * @description ����main�����õ���list���������ݽ��н��������洢��CommandLine��
	 * @author moli
	 * @version m 1.0.0
	 * @since 2020-02-17
	 */

	static class CommandLineAnalytic {

		/**
		 * @description �������ݣ��������CommandLine��
		 * @param list
		 * @return CommandLine
		 */
		public CommandLine Analytic(List<String> list) {
			CommandLine commandline = new CommandLine();
			CommandLine.Command command = new CommandLine.Command();
			CommandLine.Record record = new CommandLine.Record();

			for (int i = 0; i < list.size(); i++) {
				String temp = list.get(i);
				switch (temp) {
				case "list":
					command.setList(true);
					break;
				case "-log":
					record.setLog(true);
					record.setLog_content(list.get(i + 1));
					break;
				case "-out":
					record.setOut(true);
					record.setOut_content(list.get(i + 1));
					break;
				case "-date":
					record.setDate(true);
					record.setDate_content(list.get(i + 1));
					break;
				case "-type":
					record.setType(true);
					ArrayList<CommandLine.TypeOption> arraylist = new ArrayList<CommandLine.TypeOption>();
					for (int j = i + 1; j < list.size(); j++) {
						if (!list.get(j).substring(0, 1).equals("-")) {
							if (list.get(j).equals("ip")) {
								CommandLine.TypeOption typeoption = new CommandLine.TypeOption();
								arraylist.add(typeoption.setTypeOptionIp());
							}
							if (list.get(j).equals("sp")) {
								CommandLine.TypeOption typeoption = new CommandLine.TypeOption();
								arraylist.add(typeoption.setTypeOptionSp());
							}
							if (list.get(j).equals("cure")) {
								CommandLine.TypeOption typeoption = new CommandLine.TypeOption();
								arraylist.add(typeoption.setTypeOptionCure());
							}
							if (list.get(j).equals("dead")) {
								CommandLine.TypeOption typeoption = new CommandLine.TypeOption();
								arraylist.add(typeoption.setTypeOptionDead());
							}
						}
					}
					record.setType_content(arraylist);
					break;
				case "-province":
					record.setProvince(true);
					ArrayList<String> list1 = new ArrayList<String>();
					for (int j = i + 1; j < list.size(); j++) {
						if (!list.get(j).substring(0, 1).equals("-")) {
							list1.add(list.get(j));
						}
					}
					record.setProvince_content(list1);
					break;
				}
			}
			commandline.setCommand(command);
			commandline.setRecord(record);

			return commandline;
		}
	}

	/**
	 * RegexUtil TODO
	 *
	 * @description ����8��string��Ӧ��8�����ݵ����ͣ�ʹ��������ʽ���ֱ�õ���Ӧ�����ݣ��ڴ���RegexParameter��
	 * @author moli
	 * @version m 1.0.0
	 * @since 2020-02-17
	 */

	public static class RegexUtil {
		String type1 = "\\W+ ���� ��Ⱦ���� \\d+��";
		String type2 = "\\W+ ���� ���ƻ��� \\d+��";
		String type3 = "\\W+ ��Ⱦ���� ���� \\W+ \\d+��";
		String type4 = "\\W+ ���ƻ��� ���� \\W+ \\d+��";
		String type5 = "\\W+ ���� \\d+��";
		String type6 = "\\W+ ���� \\d+��";
		String type7 = "\\W+ ���ƻ��� ȷ���Ⱦ \\d+��";
		String type8 = "\\W+ �ų� ���ƻ��� \\d+��";

		public String getType1() {
			return type1;

		}

		public void setType1(String type1) {
			this.type1 = type1;
		}

		public String getType2() {
			return type2;
		}

		public void setType2(String type2) {
			this.type2 = type2;
		}

		public String getType3() {
			return type3;
		}

		public void setType3(String type3) {
			this.type3 = type3;
		}

		public String getType4() {
			return type4;
		}

		public void setType4(String type4) {
			this.type4 = type4;
		}

		public String getType5() {
			return type5;
		}

		public void setType5(String type5) {
			this.type5 = type5;
		}

		public String getType6() {
			return type6;
		}

		public void setType6(String type6) {
			this.type6 = type6;
		}

		public String getType7() {
			return type7;
		}

		public void setType7(String type7) {
			this.type7 = type7;
		}

		public String getType8() {
			return type8;
		}

		public void setType8(String type8) {
			this.type8 = type8;
		}

		/**
		 * RegexParameter TODO
		 * 
		 * @description ���ͨ��������ʽƥ���õ�������
		 * @author moli
		 * @version m 1.0.0
		 * @since 2020.02.17
		 */

		static class RegexParameter {
			String pattern1;
			String pattern2;
			int pattern3;

			public String getPattern1() {
				return pattern1;
			}

			public void setPattern1(String pattern1) {
				this.pattern1 = pattern1;
			}

			public String getPattern2() {
				return pattern2;
			}

			public void setPattern2(String pattern2) {
				this.pattern2 = pattern2;
			}

			public int getPattern3() {
				return pattern3;
			}

			public void setPattern3(int pattern3) {
				this.pattern3 = pattern3;
			}
		}

		public static RegexParameter getAddIp(String text) {
			Pattern pattern1 = Pattern.compile("(.*) ����");
			Pattern pattern2 = Pattern.compile("��Ⱦ���� (.*)��");

			return getString(text, pattern1, pattern2);
		}

		public static RegexParameter getAddSp(String text) {
			Pattern pattern1 = Pattern.compile("(.*) ����");
			Pattern pattern2 = Pattern.compile("���ƻ��� (.*)��");

			return getString(text, pattern1, pattern2);
		}

		public static RegexParameter getEmptiesIp(String text) {
			Pattern pattern1 = Pattern.compile("(.*) ��Ⱦ����");
			Pattern pattern2 = Pattern.compile("���� (.*) \\d+��");
			Pattern pattern3 = Pattern.compile("\\W+ (.*)��");

			return getString(text, pattern1, pattern2, pattern3);
		}

		public static RegexParameter getEmptiesSp(String text) {
			Pattern pattern1 = Pattern.compile("(.*) ���ƻ���");
			Pattern pattern2 = Pattern.compile("���� (.*) \\d+��");
			Pattern pattern3 = Pattern.compile("\\W+ (.*)��");

			return getString(text, pattern1, pattern2, pattern3);
		}

		public static RegexParameter getDead(String text) {
			Pattern pattern1 = Pattern.compile("(.*) ����");
			Pattern pattern2 = Pattern.compile("���� (.*)��");

			return getString(text, pattern1, pattern2);
		}

		public static RegexParameter getCure(String text) {
			Pattern pattern1 = Pattern.compile("(.*) ����");
			Pattern pattern2 = Pattern.compile("���� (.*)��");

			return getString(text, pattern1, pattern2);
		}

		public static RegexParameter getSpToIp(String text) {
			Pattern pattern1 = Pattern.compile("(.*) ���ƻ���");
			Pattern pattern2 = Pattern.compile("ȷ���Ⱦ (.*)��");

			return getString(text, pattern1, pattern2);
		}

		public static RegexParameter getRemoveSp(String text) {
			Pattern pattern1 = Pattern.compile("(.*) �ų�");
			Pattern pattern2 = Pattern.compile("���ƻ��� (.*)��");

			return getString(text, pattern1, pattern2);
		}

		/**
		 * 
		 * @description ��������ʽƥ��������matcher����Ȼ���õ�һ���Ӳ��֣�����RegexParameter
		 * @param text pattern1 pattern2
		 * @return RegexParameter
		 */

		private static RegexParameter getString(String text, Pattern pattern1, Pattern pattern2) {
			RegexParameter regexParameter = new RegexParameter();
			Matcher matcher1 = pattern1.matcher(text);
			Matcher matcher2 = pattern2.matcher(text);

			if (matcher1.find()) {
				regexParameter.setPattern1(matcher1.group(1));
			}
			if (matcher2.find()) {
				regexParameter.setPattern3(Integer.parseInt(matcher2.group(1)));
			}

			return regexParameter;
		}

		/**
		 * 
		 * @description ��������ʽƥ��������matcher����Ȼ���õ�һ���Ӳ��֣�����RegexParameter
		 * @param text pattern1 pattern2 pattern3
		 * @return RegexParameter
		 */

		private static RegexParameter getString(String text, Pattern pattern1, Pattern pattern2, Pattern pattern3) {
			RegexParameter regexParameter = new RegexParameter();
			Matcher matcher1 = pattern1.matcher(text);
			Matcher matcher2 = pattern2.matcher(text);
			Matcher matcher3 = pattern3.matcher(text);

			if (matcher1.find()) {
				regexParameter.setPattern1(matcher1.group(1));
			}
			if (matcher2.find()) {
				regexParameter.setPattern2(matcher2.group(1));
			}
			if (matcher3.find()) {
				regexParameter.setPattern3(Integer.parseInt(matcher3.group(1)));
			}

			return regexParameter;
		}
	}

	/**
	 * LogUtil TODO
	 * 
	 * @description ���ļ���ÿ�д��������ݽ���һ���洢
	 * @author moli
	 * @version m 1.0.0
	 * @since 2020-02-17
	 */

	static class LogUtil {

		/**
		 * InfectResult TODO
		 * 
		 * @description ����һ���������ڴ��������ʽ����������
		 * @author moli
		 * @version m 1.0.0
		 * @since 2020.02.17
		 */

		static class InfectResult {
			String province;
			int ip;
			int sp;
			int cure;
			int dead;

			public InfectResult() {
				super();
			}

			public InfectResult(String province, int ip, int sp, int cure, int dead) {
				super();
				this.province = province;
				this.ip = ip;
				this.sp = sp;
				this.cure = cure;
				this.dead = dead;
			}

			public String getProvince() {
				return province;
			}

			public void setProvince(String province) {
				this.province = province;
			}

			public int getIp() {
				return ip;
			}

			public void setIp(int ip) {
				this.ip = ip;
			}

			public int getSp() {
				return sp;
			}

			public void setSp(int sp) {
				this.sp = sp;
			}

			public int getCure() {
				return cure;
			}

			public void setCure(int cure) {
				this.cure = cure;
			}

			public int getDead() {
				return dead;
			}

			public void setDead(int dead) {
				this.dead = dead;
			}

			public String toResultString() {
				return province + " " + "��Ⱦ����" + ip + "��" + " " + "���ƻ���" + sp + "��" + " " + "����" + cure + "��" + " "
						+ "����" + dead + "��";
			}

			public String toString() {
				return province + " " + ip + " " + sp + " " + cure + " " + dead;
			}
		}

		/**
		 * ResultUtil TODO
		 * 
		 * @description ��������ʽ�õ���RegexParameter����ת���洢��InfectResult��
		 * @author moli
		 * @version m 1.0.0
		 * @since 2020.02.17
		 */

		static class ResultUtil {
			public static InfectResult getIpResult(String text) {
				InfectResult infectResult = new InfectResult();
				RegexUtil.RegexParameter regexParameter = new RegexUtil.RegexParameter();

				regexParameter = RegexUtil.getAddIp(text);
				infectResult.setProvince(regexParameter.getPattern1());
				infectResult.setIp(regexParameter.getPattern3());

				return infectResult;
			}

			public static InfectResult getSpResult(String text) {
				InfectResult infectResult = new InfectResult();
				RegexUtil.RegexParameter regexParameter = new RegexUtil.RegexParameter();

				regexParameter = RegexUtil.getAddSp(text);
				infectResult.setProvince(regexParameter.getPattern1());
				infectResult.setSp(regexParameter.getPattern3());

				return infectResult;
			}

			public static List<InfectResult> getEmptiesIpResult(String text) {
				List<InfectResult> list = new ArrayList<InfectResult>();
				RegexUtil.RegexParameter regexParameter = new RegexUtil.RegexParameter();
				LogUtil.InfectResult infectResult1 = new LogUtil.InfectResult();
				LogUtil.InfectResult infectResult2 = new LogUtil.InfectResult();

				regexParameter = RegexUtil.getEmptiesIp(text);
				infectResult1.setProvince(regexParameter.getPattern1());
				infectResult1.setIp(-regexParameter.getPattern3());
				infectResult2.setProvince(regexParameter.getPattern2());
				infectResult2.setIp(regexParameter.getPattern3());
				list.add(infectResult1);
				list.add(infectResult2);

				return list;
			}

			public static List<InfectResult> getEmptiesSpResult(String text) {
				List<InfectResult> list = new ArrayList<InfectResult>();
				RegexUtil.RegexParameter regexParameter = new RegexUtil.RegexParameter();
				LogUtil.InfectResult infectResult1 = new LogUtil.InfectResult();
				LogUtil.InfectResult infectResult2 = new LogUtil.InfectResult();

				regexParameter = RegexUtil.getEmptiesSp(text);
				infectResult1.setProvince(regexParameter.getPattern1());
				infectResult1.setSp(-regexParameter.getPattern3());
				infectResult2.setProvince(regexParameter.getPattern2());
				infectResult2.setSp(regexParameter.getPattern3());
				list.add(infectResult1);
				list.add(infectResult2);

				return list;
			}

			public static InfectResult getDeadResult(String text) {
				InfectResult infectResult = new InfectResult();
				RegexUtil.RegexParameter regexParameter = new RegexUtil.RegexParameter();

				regexParameter = RegexUtil.getDead(text);
				infectResult.setProvince(regexParameter.getPattern1());
				infectResult.setIp(-regexParameter.getPattern3());
				infectResult.setDead(regexParameter.getPattern3());

				return infectResult;
			}

			public static InfectResult getCureResult(String text) {
				InfectResult infectResult = new InfectResult();
				RegexUtil.RegexParameter regexParameter = new RegexUtil.RegexParameter();

				regexParameter = RegexUtil.getCure(text);
				infectResult.setProvince(regexParameter.getPattern1());
				infectResult.setIp(-regexParameter.getPattern3());
				infectResult.setCure(regexParameter.getPattern3());

				return infectResult;
			}

			public static InfectResult getSpToIpResult(String text) {
				InfectResult infectResult = new InfectResult();
				RegexUtil.RegexParameter regexParameter = new RegexUtil.RegexParameter();

				regexParameter = RegexUtil.getSpToIp(text);
				infectResult.setProvince(regexParameter.getPattern1());
				infectResult.setSp(-regexParameter.getPattern3());
				infectResult.setIp(regexParameter.getPattern3());

				return infectResult;
			}

			public static InfectResult getRemoveSpResult(String text) {
				InfectResult infectResult = new InfectResult();
				RegexUtil.RegexParameter regexParameter = new RegexUtil.RegexParameter();

				regexParameter = RegexUtil.getRemoveSp(text);
				infectResult.setProvince(regexParameter.getPattern1());
				infectResult.setSp(-regexParameter.getPattern3());

				return infectResult;
			}
		}

		/**
		 * ListUtil TODO
		 *
		 * @description ���õ���InfectResult���飬��ͬʡ�µĺ�Ϊһ��InfectResult,����дcompareTor�ӿڣ�ʹ�䰴��ʡ��ƴ����������
		 * @author ASUS
		 * @version m 1.0.0
		 * @since 2020-02-17
		 */

		static class ListUtil {

			/**
			 * 
			 * @description ���õ���InfectResult���飬��ͬʡ�µĽ���һ���ϲ�������flag������Ŀ¼���ļ�֮��ϲ��Ĳ�ͬ��
			 * @param a flag
			 * @return List<InfectResult>
			 */

			public static List<InfectResult> mergeList(List<InfectResult> a, boolean flag) {
				List<InfectResult> list = new ArrayList<InfectResult>();
				InfectResult r1 = new InfectResult();
				InfectResult r2 = new InfectResult();
				InfectResult r3 = new InfectResult();

				r3.setProvince("ȫ��");
				if (a.size() > 0) {
					r1 = a.get(0);
					for (InfectResult aa : a) {
						r3.setIp(r3.getIp() + aa.getIp());
						r3.setSp(r3.getSp() + aa.getSp());
						r3.setCure(r3.getCure() + aa.getCure());
						r3.setDead(r3.getDead() + aa.getDead());
						if (compare(aa, r1)) {
							if (r2.getProvince() != null) {
								r2.setIp(r2.getIp() + aa.getIp());
								r2.setSp(r2.getSp() + aa.getSp());
								r2.setCure(r2.getCure() + aa.getCure());
								r2.setDead(r2.getDead() + aa.getDead());
							} else {
								r2 = r1;
							}
						} else {
							list.add(r2);
							r1 = aa;
							r2 = r1;
						}
					}
					if (!list.contains(r2)) {
						list.add(r2);
					}
					if (flag != true) {
						r3.setIp(r3.getIp() / 2);
						r3.setSp(r3.getSp() / 2);
						r3.setDead(r3.getDead() / 2);
						r3.setCure(r3.getCure() / 2);
					}
					list.add(r3);
				} else {
					r3.setIp(0);
					r3.setSp(0);
					r3.setCure(0);
					r3.setDead(0);
					list.add(r3);
				}

				return list;
			}

			public static boolean compare(Object o1, Object o2) {
				InfectResult a = (InfectResult) o1;
				InfectResult a2 = (InfectResult) o2;

				if (a.getProvince().equals(a2.getProvince())) {
					return true;
				}

				return false;
			}

			public static enum EnumTest {
				ȫ��(1), ����(2), ����(3), ����(4), ����(5), ����(6), �㶫(7), ����(8), ����(9), ����(10), �ӱ�(11), ����(12), ������(13), ����(14),
				����(15), ����(16), ����(17), ����(18), ����(19), ���ɹ�(20), ����(21), �ຣ(22), ɽ��(23), ɽ��(24), ����(25), �Ϻ�(26), �Ĵ�(27),
				���(28), ����(29), �½�(30), ����(31), �㽭(32);

				private int value;

				private EnumTest(int value) {
					this.value = value;
				}

				public int getValue() {
					return value;
				}

				public void setValue(int value) {
					this.value = value;
				}
			}

			@SuppressWarnings("rawtypes")
			static class comparator implements Comparator {
				public int compare(Object object, Object object2) {
					InfectResult a = (InfectResult) object;
					InfectResult a2 = (InfectResult) object2;

					return ((Integer) EnumTest.valueOf(a.getProvince()).getValue())
							.compareTo(EnumTest.valueOf(a2.getProvince()).getValue());
				}
			}
		}
	}

	/**
	 * LogDao TODO
	 * 
	 * @description �ļ����ݵ����������
	 * @author moli
	 * @version m 1.0.0
	 * @since 2020.02.17
	 */

	static class LogDao {
		@SuppressWarnings("unchecked")
		/**
		 * 
		 * @description �����ַ�з����������ļ�
		 * @param fileName date
		 * @return List<LogUtil.InfectResult>
		 */

		public static List<LogUtil.InfectResult> initLog(String fileName, String date) {
			RegexUtil regexUtil = new RegexUtil();
			@SuppressWarnings("unused")
			RegexUtil.RegexParameter regexParameter = new RegexUtil.RegexParameter();
			List<LogUtil.InfectResult> list = new ArrayList<LogUtil.InfectResult>();
			List<LogUtil.InfectResult> last = new ArrayList<LogUtil.InfectResult>();
			List<LogUtil.InfectResult> merge = new ArrayList<LogUtil.InfectResult>();
			File file = new File(fileName);

			if (file.isFile()) {
				BufferedReader reader = null;
				try {
					reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
					String tempString = null;
					while ((tempString = reader.readLine()) != null) {
						if (tempString.matches(regexUtil.getType1())) {
							list.add(LogUtil.ResultUtil.getIpResult(tempString));
						} else if (tempString.matches(regexUtil.getType2())) {
							list.add(LogUtil.ResultUtil.getSpResult(tempString));
						} else if (tempString.matches(regexUtil.getType3())) {
							list.addAll(LogUtil.ResultUtil.getEmptiesIpResult(tempString));
						} else if (tempString.matches(regexUtil.getType4())) {
							list.addAll(LogUtil.ResultUtil.getEmptiesSpResult(tempString));
						} else if (tempString.matches(regexUtil.getType5())) {
							list.add(LogUtil.ResultUtil.getDeadResult(tempString));
						} else if (tempString.matches(regexUtil.getType6())) {
							list.add(LogUtil.ResultUtil.getCureResult(tempString));
						} else if (tempString.matches(regexUtil.getType7())) {
							list.add(LogUtil.ResultUtil.getSpToIpResult(tempString));
						} else if (tempString.matches(regexUtil.getType8())) {
							list.add(LogUtil.ResultUtil.getRemoveSpResult(tempString));
						}
					}
					reader.close();
					Collections.sort(list, new LogUtil.ListUtil.comparator());
					last = LogUtil.ListUtil.mergeList(list, true);
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if (reader != null) {
						try {
							reader.close();
						} catch (IOException e1) {

						}
					}
				}
			} else if (file.isDirectory()) {
				List<String> paths = getFiles(fileName);
				for (String file_path : paths) {
					int firstIndex = -1;
					int secondIndex = -1;
					int index = -1;
					do {
						firstIndex = index + 1;
						index = file_path.indexOf("\\", index + 1);
					} while (index != -1);
					String result = file_path.substring(firstIndex);
					do {
						secondIndex = index;
						index = result.indexOf(".", index + 1);
					} while (result.indexOf(".", index + 1) != -1);
					String reresult = result.substring(0, secondIndex);
					int res = reresult.compareTo(date);

					if (date.equals("default"))
						res = 0;

					if (res > 0)
						continue;
					merge.addAll(initLog(file_path, null));
				}
				Collections.sort(merge, new LogUtil.ListUtil.comparator());
				last = LogUtil.ListUtil.mergeList(merge, false);
			}

			return last;
		}

		/**
		 * 
		 * @description ���·��Ŀ¼�µ������ļ�
		 * @param path
		 * @return List<String>
		 */

		public static List<String> getFiles(String path) {
			List<String> files = new ArrayList<String>();
			File file = new File(path);
			File[] tempList = file.listFiles();

			for (int i = 0; i < tempList.length; i++) {
				if (tempList[i].isFile()) {
					files.add(tempList[i].toString());
				}
				if (tempList[i].isDirectory()) {
					files.addAll(getFiles(tempList[i].toString()));
				}
			}

			return files;
		}

		/**
		 * 
		 * @description ��ͨ��initlog���������ݣ�����ȫ�����ڵ�һλ��ԭ���ٴν�������
		 * @param fileName date
		 * @return List<LogUtil.infectResult>
		 * @throws IOException
		 */

		public static List<LogUtil.InfectResult> sortResultByProvince(String fileName, String date) throws IOException {
			List<LogUtil.InfectResult> list = initLog(fileName, date);
			List<LogUtil.InfectResult> sort_list = new ArrayList<LogUtil.InfectResult>();
			String[] newArray = new String[list.size()];
			String temp = null;

			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getProvince().equals("ȫ��")) {
					temp = list.get(i).toString();
				}
				newArray[i] = list.get(i).toString();
			}

			LogUtil.InfectResult temp_infectResult = new LogUtil.InfectResult();
			String[] temp_arr = temp.split(" ");

			temp_infectResult.setProvince(temp_arr[0]);
			temp_infectResult.setIp(Integer.parseInt(temp_arr[1]));
			temp_infectResult.setSp(Integer.parseInt(temp_arr[2]));
			temp_infectResult.setCure(Integer.parseInt(temp_arr[3]));
			temp_infectResult.setDead(Integer.parseInt(temp_arr[4]));
			sort_list.add(temp_infectResult);
			for (String i : newArray) {
				LogUtil.InfectResult infectResult = new LogUtil.InfectResult();
				String[] arr = i.split(" ");
				if (arr.length > 4) {
					if (arr[0].equals("ȫ��"))
						continue;
					infectResult.setProvince(arr[0]);
					infectResult.setIp(Integer.parseInt(arr[1]));
					infectResult.setSp(Integer.parseInt(arr[2]));
					infectResult.setCure(Integer.parseInt(arr[3]));
					infectResult.setDead(Integer.parseInt(arr[4]));
					sort_list.add(infectResult);
					// ����ط��Ļ���ȫ���ٴ�ȥ���ˣ���Ϊ����ȫ����Ҫ���ڵ�һ��λ�õ�
				}
			}

			return sort_list;
		}

		/**
		 * 
		 * @description ��type��province��Ҫ��Դ���õ��ļ����ݽ������
		 * @param fileName out_path province type date
		 * @throws IOException
		 */

		public static void outLog(String fileName, String out_path, List<String> province, List<String> type,
				String date) throws IOException {
			List<LogUtil.InfectResult> list = LogDao.sortResultByProvince(fileName, date);
			List<String> sort_province = LogDao.newSort(province);
			BufferedWriter out = null;
			String type_temp = "";

			try {
				out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(out_path, true)));
				if (sort_province != null) {
					for (String s : sort_province) {
						boolean find = false;
						for (int j = 0; j < list.size(); j++) {
							if (s.equals(list.get(j).getProvince())) {
								find = true;
								if (type != null) {
									for (String str : type) {
										if (str.equals("ip")) {
											type_temp += " ��Ⱦ����" + list.get(j).getIp() + "��";
										} else if (str.equals("sp")) {
											type_temp += " ���ƻ���" + list.get(j).getSp() + "��";
										} else if (str.equals("cure")) {
											type_temp += " ����" + list.get(j).getCure() + "��";
										} else if (str.equals("dead")) {
											type_temp += " ����" + list.get(j).getDead() + "��";
										}
									}
									out.write(list.get(j).getProvince() + type_temp + "\n");
									type_temp = "";
									break;
								} else {
									out.write(list.get(j).toResultString() + "\n");
									break;
								}
							}
						}
						if (find == false) {
							if (type != null) {
								for (String str : type) {
									if (str.equals("ip")) {
										type_temp += " ��Ⱦ����0��";
									} else if (str.equals("sp")) {
										type_temp += " ���ƻ���0��";
									} else if (str.equals("cure")) {
										type_temp += " ��������0��";
									} else if (str.equals("dead")) {
										type_temp += " ��������0��";
									}
								}
								out.write(s + type_temp + "\n");
								type_temp = "";
							} else {
								out.write(s + " ��Ⱦ����0�� ��������0�� ��������0�� ��������0��" + "\n");
							}
						}
					}

				} else {
					// ��ô����Ҫ���ľ��ǰ���������ѡ��ȫ�����������������ס���ʱ�����������Ѿ����ź������
					for (int i = 0; i < list.size(); i++) {
						if (type != null) {
							for (String str : type) {
								if (str.equals("ip")) {
									type_temp += " ��Ⱦ����" + list.get(i).getIp() + "��";
								} else if (str.equals("sp")) {
									type_temp += " ���ƻ���" + list.get(i).getSp() + "��";
								} else if (str.equals("cure")) {
									type_temp += " ����" + list.get(i).getCure() + "��";
								} else if (str.equals("dead")) {
									type_temp += " ����" + list.get(i).getDead() + "��";
								}
							}
							out.write(list.get(i).getProvince() + type_temp + "\n");
							type_temp = "";
						} else {
							out.write(list.get(i).toResultString() + "\n");
						}
					}
				}
			} finally {
				if (out != null) {
					try {
						out.close();
					} catch (IOException e1) {

					}
				}
			}
		}

		/**
		 * 
		 * @description �Դ����province����һ�����򣬿���ʹ���ļ���û�г��ֵ����ݸ����׵ı�����
		 * @param province
		 * @return List<String>
		 */

		public static List<String> newSort(List<String> province) {
			String[] final_province = { "ȫ��", "����", "����", "����", "����", "����", "�㶫", "����", "����", "����", "�ӱ�", "����", "������",
					"����", "����", "����", "����", "����", "����", "���ɹ�", "����", "�ຣ", "ɽ��", "ɽ��", "����", "�Ϻ�", "�Ĵ�", "���", "����",
					"�½�", "����", "�㽭" };
			List<String> sort_province = new ArrayList<String>();
			/*
			 * if (province == null) { for (String str : final_province) {
			 * sort_province.add(str); } return sort_province; }
			 */
			if (province == null) {
				return null;
			}
			for (String str : final_province) {
				for (String st : province) {
					if (str.equals(st)) {
						sort_province.add(st);
						break;
					}
				}
			}

			return sort_province;
		}
	}

	/**
	 * CommandLineApplication TODO
	 * 
	 * @description ����ָ��
	 * @author moli
	 * @version m 1.0.0
	 * @since 2020.02.17
	 */

	static class CommandLineApplication {
		public void Application(List<String> list) throws IOException {
			CommandLine commandLine = new CommandLine();
			CommandLineAnalytic commandLineAnalytic = new CommandLineAnalytic();
			commandLine = commandLineAnalytic.Analytic(list);
			String in_path = null;
			String out_path = null;
			String date = null;
			List<String> province = new ArrayList<String>();
			List<String> type = new ArrayList<String>();

			if (commandLine.getCommand().isList()) {
				if (commandLine.getRecord().isLog()) {
					if (commandLine.getRecord().isDate()) {
						if (commandLine.getRecord().getDate_content() != null) {
							in_path = commandLine.getRecord().getLog_content();
							date = commandLine.getRecord().getDate_content();
						}
					} else {
						in_path = commandLine.getRecord().getLog_content();
					}
				}
				if (commandLine.getRecord().isOut()) {
					out_path = commandLine.getRecord().getOut_content();
				}
				if (commandLine.getRecord().isType()) {
					for (int i = 0; i < commandLine.getRecord().getType_content().size(); i++) {
						type.add(commandLine.getRecord().getType_content().get(i).name);
					}
				} else {
					type = null;
				}
				if (commandLine.getRecord().isProvince()) {
					province.addAll(commandLine.getRecord().getProvince_content());
				} else {
					province = null;
				}
			}
			LogDao.outLog(in_path, out_path, province, type, date);
		}
	}

	public static void main(String[] args) throws IOException {
		List<String> list = new ArrayList<String>();

		for (String temp : args) {
			list.add(temp);
		}

		if (list.get(list.size() - 1).equals("-date")) {
			list.add("default");
		} else {
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).equals("-date")) {
					if (list.get(i + 1).substring(0, 1).equals("-")) {
						list.add(i + 1, "default");
						break;
					}
				}
			}
		}
		CommandLineApplication commandLineApplication = new CommandLineApplication();
		commandLineApplication.Application(list);
	}
}
