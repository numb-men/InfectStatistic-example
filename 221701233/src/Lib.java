import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.Collator;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Lib
 * TODO
 *
 * @author 张一凡
 * @version 1.0
 * @since 1.0
 */
public class Lib {
}

/**
 * 命令参数解析
 */
class CommandLine {
    private String[] args;

    private Command command;

    CommandLine(String[] args, Command command) {
        this.args = args;
        this.command = command;
    }

    /**
     * 获取某个参数的一个值
     *
     * @param arg 选项名称
     * @return 参数 {@code arg} 的值，不存在返回 {@code null}
     */
    String getValue(String arg) {
        int index = indexOf(arg);

        if (index != -1) {
            if (index + 1 < args.length && args[index + 1].charAt(0) != '-') {
                return args[index + 1];
            }
        }

        return null;
    }

    /**
     * 获取某个参数的所有值
     *
     * @param arg 选项名称
     * @return 参数 {@code arg} 的值列表
     */
    List<String> getValues(String arg) {

        List<String> values = new ArrayList<>();

        int index = indexOf(arg);

        if (index != -1) {
            for (int i = index + 1; i < args.length && args[i].charAt(0) != '-'; i++) {
                values.add(args[i]);
            }
        }

        return values;
    }

    /**
     * 获取参数的索引
     *
     * @param arg 选项名称
     * @return 该选项的索引值；若不存在返回 -1
     */
    private int indexOf(String arg) {
        for (int i = 1; i < args.length; i++) {
            if (args[i].equalsIgnoreCase(arg)) {
                return i;
            }
        }

        return -1;
    }

    /**
     * list命令入口
     *
     * @throws Exception
     */
    public void execute() throws Exception {
        command.execute(this);
    }
}

/**
 * 命令工厂
 */
class CommandFactory {

    /**
     * 通过反射执行相应方法获取命令对象
     *
     * @param name     命令名
     * @param receiver 命令接受者
     * @return 相应方法执行后获取的命令对象
     * @throws Exception 不存在相应命令对象
     */
    public Command getCommand(String name, CommandReceiver receiver) throws Exception {
        try {
            return (Command) this.getClass().getMethod(name.toLowerCase(), Class.forName("CommandReceiver"))
                    .invoke(this, receiver);
        } catch (Exception e) {
            throw new NoSuchMethodException("未知命令 \'" + name + "\'");
        }

    }

    public Command list(CommandReceiver receiver) {
        return new ListCommand(receiver);
    }

    // 在此处扩展命令...
}

/**
 * 命令接口
 */
interface Command {

    void execute(CommandLine commandLine) throws Exception;

}

/**
 * list命令
 */
class ListCommand implements Command {

    private CommandReceiver receiver;

    private ListChecker checker;

    public ListCommand(CommandReceiver receiver) {
        checker = new ListChecker();
        this.receiver = receiver;
    }

    @Override
    public void execute(CommandLine commandLine) throws Exception {
        checker.check(commandLine);
        receiver.list(ListCommandEntity.Mapper(commandLine));
    }

}

/**
 * list命令检查器
 */
class ListChecker {

    public void check(CommandLine line) throws Exception {
        // 检查参数
        checkLog(line.getValue("-log"));
        checkOut(line.getValue("-out"));
        checkDate(line.getValue("-date"));
        checkType(line.getValues("-type"));
        checkProvince(line.getValues("-province"));
    }


    /**
     * 检查日志路径
     *
     * @param receiverPath
     * @throws Exception
     */
    public void checkLog(String receiverPath) throws Exception {

        if (receiverPath == null) {
            throw new Exception("\"-log\" 不能为空");
        }

        File receiverDir = new File(receiverPath);

        if (!receiverDir.isDirectory()) {
            throw new Exception("\"" + receiverPath + "\" 不是有效的日志目录");
        }
    }

    /**
     * 检查导出路径
     *
     * @param outPath
     * @throws Exception
     */
    public void checkOut(String outPath) throws Exception {

        if (outPath == null) {
            throw new Exception("\"-out\" 不能为空");
        }

        File outFile = new File(outPath);
        outFile.createNewFile();

        if (!outFile.isFile()) {
            throw new Exception("\"" + outPath + "\" 不是有效的输出文件");
        }
    }

    /**
     * 检查日期格式
     *
     * @param dateStr
     * @throws Exception
     */
    public void checkDate(String dateStr) throws Exception {
        if (dateStr == null) {
            return;
        }

        try {
            Util.DATE_FORMAT.parse(dateStr);
        } catch (Exception e) {
            throw new Exception("日期非法或格式错误");
        }
    }

    /**
     * 检查人员类型
     *
     * @param argTypes
     * @throws Exception
     */
    public void checkType(List<String> argTypes) throws Exception {
        List<String> defaultTypes = new ArrayList<>(Arrays.asList("ip", "sp", "cure", "dead"));

        if (argTypes.size() == 0) {
            return;
        }

        for (String type : argTypes) {
            if (!defaultTypes.contains(type)) {
                throw new Exception("无效 type 参数值：" + type);
            }
        }
    }

    /**
     * 检查地区
     *
     * @param provinces
     * @throws Exception
     */
    public void checkProvince(List<String> provinces) throws Exception {
        for (String prov : provinces) {
            if (!StatisticResult.contains(prov))
                throw new Exception("无效地区：" + prov);
        }
    }
}

/**
 * List命令实体
 */
class ListCommandEntity {

    public List<String> log;

    public File out;

    public Date date;

    public List<String> type = new ArrayList<>(Arrays.asList("ip", "sp", "cure", "dead"));

    public List<String> province;

    /**
     * 将CommandLine映射成ListCommandUtil
     *
     * @param line
     * @return
     */
    public static ListCommandEntity Mapper(CommandLine line) throws Exception {
        ListCommandEntity entity = new ListCommandEntity();

        entity.out = new File(line.getValue("-out"));

        try {
            entity.date = Util.DATE_FORMAT.parse(line.getValue("-date"));
        } catch (Exception ignored) {
        }

        entity.log = LogReader.readLog(entity.date, new File(line.getValue("-log")));

        List<String> types = line.getValues("-type");
        if (types.size() != 0) {
            entity.type = types;
        }

        entity.province = line.getValues("-province");

        return entity;
    }
}

/**
 * 日志读取器
 */
class LogReader {

    /**
     * 从日志文件名中提取日期
     *
     * @param file 日志文件
     * @return 日志日期
     */
    private static String getLogDate(File file) {
        String fileName = file.getName();
        return fileName.substring(0, fileName.lastIndexOf(".log.txt"));
    }

    /**
     * 获取指定日期之前的所有log
     *
     * @param requiredDate 指定日期
     * @param logDir       日志文件所在目录
     * @return 指定日期之前的所有log列表
     */
    public static List<String> readLog(Date requiredDate, File logDir) throws Exception {

        File[] fileList = logDir.listFiles();
        List<String> logLines = new ArrayList<>();

        Date latestDate = Util.DATE_FORMAT.parse("2000-01-01");

        for (File file : fileList) {
            if (file.isFile()) {
                Date logDate;

                // 过滤日期不规范的日志文件
                try {
                    logDate = Util.DATE_FORMAT.parse(getLogDate(file));
                } catch (Exception e) {
                    continue;
                }

                // 过滤指定日期之后的日志
                if (requiredDate == null || !logDate.after(requiredDate)) {

                    // 记录日志最新日期
                    if (logDate.after(latestDate)) {
                        latestDate = logDate;
                    }

                    try (InputStream fr = new FileInputStream(file.getAbsolutePath());
                         BufferedReader bf = new BufferedReader(new InputStreamReader(fr, StandardCharsets.UTF_8))) {
                        String line;
                        // 按行读取
                        while ((line = bf.readLine()) != null) {
                            // 过滤注解
                            if (!line.substring(0, 2).equals("//")) {
                                logLines.add(line);
                            }
                        }
                    } catch (IOException e) {
                        throw new Exception("日志文件读取失败: '" + file.getAbsolutePath() + "'");
                    }
                }
            }
        }

        if (requiredDate != null && requiredDate.after(latestDate)) {
            throw new Exception("日期超出范围");
        }

        return logLines;
    }
}

/**
 * 命令接收者
 */
class CommandReceiver {

    /**
     * list命令
     *
     * @param entity
     */
    public void list(ListCommandEntity entity) throws Exception {
        List<String> results = LogParser.parse(entity);
        LogWriter.write(entity.out.getAbsolutePath(), results);
        StatisticResult.reset();
    }

    // 扩展命令...
}

/**
 * 抽象动作类
 */
abstract class AbstractAction {

    public static int ADD_IP = 0;

    public static int FLOW_IN = 1;

    public static int DEAD_OR_CURE = 2;

    public static int DGS = 3;

    public static int EXC = 4;

    protected int typeNum;

    //责任链中的下一个动作
    protected AbstractAction nextAction;

    public void passOn(int typeNum, String logLine) {
        if (this.typeNum == typeNum) {
            doAction(logLine);
            return;
        }
        if (nextAction != null) {
            nextAction.passOn(typeNum, logLine);
        }
    }

    abstract protected void doAction(String logLine);
}

/**
 * 人员增加
 */
class Add extends AbstractAction {

    private String regex = "(\\S+) 新增 (\\S+) (\\d+)人";

    Add(AbstractAction nextAction) {
        this.typeNum = ADD_IP;
        this.nextAction = nextAction;
    }

    @Override
    protected void doAction(String logLine) {

        Pattern pattern = Pattern.compile(regex);
        Matcher addMatcher = pattern.matcher(logLine);

        if (addMatcher.find()) {
            String regionName = addMatcher.group(1);
            String peopleType = addMatcher.group(2);
            int peopleNum = Integer.parseInt(addMatcher.group(3));
            StatisticResult.get(regionName).increase(peopleNum, peopleType);
        }

    }
}

/**
 * 人员流动
 */
class FlowIn extends AbstractAction {

    private String regex = "(\\S+) (\\S+) 流入 (\\S+) (\\d+)人";

    FlowIn(AbstractAction nextAction) {
        this.typeNum = FLOW_IN;
        this.nextAction = nextAction;
    }

    @Override
    protected void doAction(String logLine) {
        Pattern pattern = Pattern.compile(regex);
        Matcher flowMatcher = pattern.matcher(logLine);

        if (flowMatcher.find()) {
            String regionFrom = flowMatcher.group(1);
            String peopleType = flowMatcher.group(2);
            String regionTo = flowMatcher.group(3);
            int peopleNum = Integer.parseInt(flowMatcher.group(4));


            StatisticResult.get(regionFrom).flowOut(peopleNum, peopleType);
            StatisticResult.get(regionTo).flowIn(peopleNum, peopleType);
        }
    }
}

/**
 * 人员死亡或治愈
 */
class DeadOrCure extends AbstractAction {

    private String regex = "(\\S+) (\\S+) (\\d+)人";

    DeadOrCure(AbstractAction nextAction) {
        this.typeNum = DEAD_OR_CURE;
        this.nextAction = nextAction;
    }

    @Override
    protected void doAction(String logLine) {
        Pattern pattern = Pattern.compile(regex);
        Matcher DOCMatcher = pattern.matcher(logLine);

        if (DOCMatcher.find()) {
            String regionName = DOCMatcher.group(1);
            String peopleType = DOCMatcher.group(2);
            int peopleNum = Integer.parseInt(DOCMatcher.group(3));
            StatisticResult.get(regionName).deadOrCure(peopleNum, peopleType);
        }
    }
}

/**
 * 疑似患者确诊
 */
class Diagnosis extends AbstractAction {

    String regex = "(\\S+) 疑似患者 确诊感染 (\\d+)人";

    Diagnosis(AbstractAction nextAction) {
        this.typeNum = DGS;
        this.nextAction = nextAction;
    }

    @Override
    protected void doAction(String logLine) {
        Pattern pattern = Pattern.compile(regex);
        Matcher DiaMatcher = pattern.matcher(logLine);

        if (DiaMatcher.find()) {
            String regionName = DiaMatcher.group(1);
            int peopleNum = Integer.parseInt(DiaMatcher.group(2));
            StatisticResult.get(regionName).diagnose(peopleNum);
        }
    }
}

/**
 * 排除疑似患者
 */
class Exclusive extends AbstractAction {

    String regex = "(\\S+) 排除 疑似患者 (\\d+)人";

    Exclusive(AbstractAction nextAction) {
        this.typeNum = EXC;
        this.nextAction = nextAction;
    }

    @Override
    protected void doAction(String logLine) {
        Pattern pattern = Pattern.compile(regex);
        Matcher ExcMatcher = pattern.matcher(logLine);

        if (ExcMatcher.find()) {
            String regionName = ExcMatcher.group(1);
            int peopleNum = Integer.parseInt(ExcMatcher.group(2));
            StatisticResult.get(regionName).exclusive(peopleNum);
        }
    }
}

/**
 * 工具类
 */
class Util {

    // 正则表达式集
    static final String[] REGEXS = {
            "(\\S+) 新增 (\\S+) (\\d+)人",
            "(\\S+) (\\S+) 流入 (\\S+) (\\d+)人",
            "(\\S+) (\\S+) (\\d+)人",
            "(\\S+) 疑似患者 确诊感染 (\\d+)人",
            "(\\S+) 排除 疑似患者 (\\d+)人",
    };

    // 日期格式标准
    static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    static {
        DATE_FORMAT.setLenient(false);
    }

    /**
     * 匹配日志类型
     *
     * @param logLine 日志的一条记录
     * @return 正则表达式的索引；匹配失败返回-1
     */
    public static int logType(String logLine) {
        for (int i = 0; i < REGEXS.length; i++) {
            if (logLine.matches(REGEXS[i])) {
                return i;
            }
        }
        return -1;
    }
}

/**
 * 地区实体类
 * 一个实例代表一个地区的情况
 */
class Region implements Comparable<Region> {

    static final Comparator<Object> CHINA_COMPARE = Collator.getInstance(Locale.CHINA);

    private String name;
    private int infected;
    private int suspected;
    private int cure;
    private int dead;
    private boolean isChecked;

    Region(String name) {
        this.infected = 0;
        this.suspected = 0;
        this.cure = 0;
        this.dead = 0;
        this.name = name;
        this.isChecked = false;
    }

    public int getCure() {
        return cure;
    }

    public int getDead() {
        return dead;
    }

    public int getInfected() {
        return infected;
    }

    public int getSuspected() {
        return suspected;
    }

    public String getName() {
        return name;
    }

    public void setInfected(int infected) {
        this.infected = infected;
    }

    public void setSuspected(int suspected) {
        this.suspected = suspected;
    }

    public void setDead(int dead) {
        this.dead = dead;
    }

    public void setCure(int cure) {
        this.cure = cure;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    /**
     * 增加人数
     *
     * @param num  人数
     * @param type 类型
     */
    public void increase(int num, String type) {
        if (type.equals("感染患者")) {
            this.infected += num;
        }
        if (type.equals("疑似患者")) {
            this.suspected += num;
        }
    }

    /**
     * 疑似患者确诊
     *
     * @param num 人数
     */
    public void diagnose(int num) {
        this.suspected -= num;
        this.infected += num;
    }

    /**
     * 增加死亡人数或治愈人数
     *
     * @param num  人数
     * @param type 类型
     */
    public void deadOrCure(int num, String type) {
        this.infected -= num;
        if (type.equals("死亡")) {
            this.dead += num;
        }
        if (type.equals("治愈")) {
            this.cure += num;
        }
    }

    /**
     * 排除疑似患者
     *
     * @param num 人数
     */
    public void exclusive(int num) {
        this.suspected -= num;
    }

    /**
     * 流入人员
     *
     * @param num  人数
     * @param type 类型
     */
    public void flowIn(int num, String type) {
        if (type.equals("感染患者")) {
            this.infected += num;
        }
        if (type.equals("疑似患者")) {
            this.suspected += num;
        }
    }

    /**
     * 流出人员
     *
     * @param num  人数
     * @param type 类型
     */
    public void flowOut(int num, String type) {
        if (type.equals("感染患者")) {
            this.infected -= num;
        }
        if (type.equals("疑似患者")) {
            this.suspected -= num;
        }
    }

    /**
     * 用名称判断是否相等
     *
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        return this.getName().equals(((Region) obj).getName());
    }

    /**
     * 统计指定类型的人员数量
     *
     * @param type 指定类型列表
     * @return 包含指定类型人员统计结果的字符串，格式“[省] [人员类型]x人 [人员类型]y人..”
     */
    public String toStringWithCertainType(List<String> type) {
        StringBuilder string = new StringBuilder(this.getName());
        for (String tp : type) {
            if (tp.equalsIgnoreCase("ip")) {
                string.append(String.format(" 感染患者%d人", this.getInfected()));
            }
            if (tp.equalsIgnoreCase("sp")) {
                string.append(String.format(" 疑似患者%d人", this.getSuspected()));
            }
            if (tp.equalsIgnoreCase("cure")) {
                string.append(String.format(" 治愈%d人", this.getCure()));
            }
            if (tp.equalsIgnoreCase("dead")) {
                string.append(String.format(" 死亡%d人", this.getDead()));
            }
        }
        return "" + string + "\n";
    }

    /**
     * 实现地区比较
     * <p>
     * 基于CHINA_COMPARE，”全国“优先，并解决”重庆“多音字的问题
     *
     * @param o
     * @return
     */
    @Override
    public int compareTo(Region o) {
        String rg1 = this.getName().replace("重庆", "冲庆");
        String rg2 = o.getName().replace("重庆", "冲庆");

        if (rg1.equals("全国") || rg2.equals("全国")) {
            return (rg1.equals("全国")) ? -1 : 1;
        }

        return CHINA_COMPARE.compare(rg1, rg2);
    }
}

/**
 * 统计结果集
 */
class StatisticResult {

    // 地区的有序列表
    private static final String[] REGIONS_LIST = new String[]{
            "全国", "安徽", "北京", "重庆", "福建", "甘肃", "广东", "广西", "贵州", "海南", "河北", "河南", "黑龙江", "湖北",
            "湖南", "吉林", "江苏", "江西", "辽宁", "内蒙古", "宁夏", "青海", "山东", "山西", "陕西", "上海", "四川", "天津",
            "西藏", "新疆", "云南", "浙江"
    };

    static List<Region> regions = new ArrayList<>();

    static {
        for (String rg : REGIONS_LIST) {
            regions.add(new Region(rg));
        }
    }

    public static void setChecked(String name) {
        get(name).setChecked(true);
    }

    /**
     * 列表是否包含某地区
     *
     * @param name
     * @return
     */
    static boolean contains(String name) {
        return Arrays.asList(REGIONS_LIST).contains(name);
    }

    /**
     * 按地区名二分查找地区
     * 如果当前集合{@code regions}中不存在此地区返回null
     *
     * @param name
     * @return
     */
    public static Region get(String name) {
        int begin = 0, end = regions.size() - 1;
        int mid;

        Region temp = new Region(name);

        while (begin <= end) {
            mid = (begin + end) >> 1;
            Region cur = regions.get(mid);
            if (name.equals(cur.getName())) {
                return regions.get(mid);
            }
            if (cur.compareTo(temp) < 0) {
                begin = mid;
            }
            if (cur.compareTo(temp) > 0) {
                end = mid;
            }
        }

        return null;
    }

    /**
     * 统计所有地区的疫情状况
     *
     * @param logLines 读取到的日志
     */
    public static void doStatistic(List<String> logLines) {
        Add add = new Add(null);
        FlowIn flowIn = new FlowIn(add);
        DeadOrCure deadOrCure = new DeadOrCure(flowIn);
        Diagnosis diagnosis = new Diagnosis(deadOrCure);
        Exclusive exclusive = new Exclusive(diagnosis);

        for (String logLine : logLines) {
            setChecked(logLine.substring(0, logLine.indexOf(" ")));
            exclusive.passOn(Util.logType(logLine), logLine);
        }
    }

    /**
     * 生成全国的统计
     *
     * @return
     */
    public static Region statisticAll() {
        Region allRegions = get("全国");
        for (Region rg : regions) {
            allRegions.setInfected(rg.getInfected() + allRegions.getInfected());
            allRegions.setSuspected(rg.getSuspected() + allRegions.getSuspected());
            allRegions.setCure(rg.getCure() + allRegions.getCure());
            allRegions.setDead(rg.getDead() + allRegions.getDead());
        }
        return allRegions;
    }

    public static List<String> filterTypeAndProvince(List<String> type, List<String> province) {
        List<String> result = new ArrayList<>();

        for (String prv : province) {
            if (!prv.equals("全国")) {
                result.add(Objects.requireNonNull(get(prv)).toStringWithCertainType(type));
            }
        }

        if (province.contains("全国") || province.size() == 0) {
            // 添加全国统计
            result.add(Objects.requireNonNull(statisticAll()).toStringWithCertainType(type));
        }

        if (province.size() == 0) {
            for (Region rg : regions) {
                if (rg.isChecked()) {
                    result.add(rg.toStringWithCertainType(type));
                }
            }
        }

        return result;
    }

    /**
     * 重置数据
     */
    public static void reset() {
        regions.clear();
    }
}

/**
 * 日志解析
 */
class LogParser {

    /**
     * 读取日志并解析
     *
     * @param entity
     * @return
     */
    public static List<String> parse(ListCommandEntity entity) {
        StatisticResult.doStatistic(entity.log);

        return StatisticResult.filterTypeAndProvince(entity.type, entity.province);
    }
}

/**
 * 统计结果导出到文件
 */
class LogWriter {
    /**
     * 将统计结果写入文件
     *
     * @param filePath
     * @param results
     */
    static void write(String filePath, List<String> results) throws Exception {
        try (FileOutputStream out = new FileOutputStream(filePath);
             BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8))) {
            for (String resultLine : results) {
                bw.write(resultLine);
            }
            bw.write("// 该文档并非真实数据，仅供测试使用\n");
        }
    }
}

