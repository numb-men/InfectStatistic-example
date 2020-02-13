/**
 * InfectStatistic
 *
 * @author 欧振贵
 * @version 1.0
 * @since 2020.2.11
 */

import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import org.junit.Test;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.System.exit;

class CommandErrorException extends Exception {

    public CommandErrorException() {
    }

    public CommandErrorException(String message) {
        super(message);
    }
}


public class InfectStatistic {
    static class ListCommand {
        private Map<String, List<String>> listMap = new LinkedHashMap<>();
        static List<String> command = new ArrayList<>();

        static {
            command.add("-log");
            command.add("-out");
            command.add("-date");
            command.add("-type");
            command.add("-province");
        }
    }

    private ListCommand listCommand = new ListCommand();
    static private List<String> commands = new ArrayList<>();
    static private Map<String, Map<String, Integer>> province = new LinkedHashMap<>(32);
    static private String[] provinces = {"全国", "安徽", "北京", "重庆", "福建", "甘肃", "广东", "广西", "贵州", "海南", "河北"
            , "河南", "黑龙江", "湖北", "湖南", "吉林", "江苏", "江西", "辽宁", "内蒙古", "宁夏"
            , "青海", "山东", "山西", "陕西", "上海", "四川", "天津", "西藏", "新疆", "云南", "浙江"};
    static private List<String> result = new ArrayList<>();

    static {
        commands.add("list");
        for (int i = 0; i < 32; i++) {
            province.put(provinces[i], new LinkedHashMap<>());
            Map<String, Integer> map = province.get(provinces[i]);
            map.put("感染患者", 0);
            map.put("疑似患者", 0);
            map.put("治愈", 0);
            map.put("死亡", 0);
            map.put("flag", -1);
        }
        province.get("全国").put("flag", 0);
    }

    public static String getTheLatestDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(new Date());
    }

    public void commandAnalyze(String[] commands) throws CommandErrorException {
        if (!InfectStatistic.commands.contains(commands[0])) {
            throw new CommandErrorException();
        }
        if ("list".equals(commands[0])) {
            String command = "";
            List<String> params = new ArrayList<>();
            int i;
            int count = 0;
            for (i = 1; i <= commands.length; i++) {
                if (i == commands.length) {
                    if (params.isEmpty()) {
                        throw new CommandErrorException();
                    }
                    listCommand.listMap.put(command, params);
                    break;
                }
                if (commands[i].charAt(0) == '-' && !ListCommand.command.contains(commands[i])) {
                    throw new CommandErrorException();
                }
                if (!commands[i].equals(command) && !"".equals(command) && ListCommand.command.contains(commands[i])) {
                    if (params.isEmpty()) {
                        throw new CommandErrorException();
                    }
                    listCommand.listMap.put(command, params);
                    params = new ArrayList<>();
                }
                if (ListCommand.command.contains(commands[i])) {
                    command = commands[i];
                } else {
                    params.add(commands[i]);
                    count++;
                }

            }
        }
    }

    public void fileRead(String[] commands) throws IOException {
        String input = listCommand.listMap.get("-log").get(0);
        String output = listCommand.listMap.get("-out").get(0);
        String tempString = "";
        String command = "";
        if (listCommand.listMap.containsKey("-date")) {
            String date = listCommand.listMap.get("-date").get(0);
            input += "\\" + date + ".log.txt";

        } else {
            input += "\\" + getTheLatestDate() + ".log.txt";
        }
        System.out.println(input);
        for (String ch : commands) {
            command += ch + " ";
        }
        System.out.println(command);
        BufferedReader reader = new BufferedReader(new FileReader(input));
        while ((tempString = reader.readLine()) != null) {
            stringProcessing(tempString);
        }
        updateCountry();
        if (!listCommand.listMap.containsKey("-type") && !listCommand.listMap.containsKey("-province")) {
            stringSynthesis(new ArrayList<>(),new ArrayList<>());
        }else if(listCommand.listMap.containsKey("-type") && !listCommand.listMap.containsKey("-province"))
        {

            List<String> typeParams = listCommand.listMap.get("-type");
            stringSynthesis(typeParams,new ArrayList<>());
        }else if(!listCommand.listMap.containsKey("-type") && listCommand.listMap.containsKey("-province"))
        {
            List<String> provinceParams = listCommand.listMap.get("-province");
            stringSynthesis(new ArrayList<>(),provinceParams);
        }
        else {
            List<String> typeParams = listCommand.listMap.get("-type");
            List<String> provinceParams = listCommand.listMap.get("-province");
            stringSynthesis(typeParams,provinceParams);
        }
        logOutput();
        reset();
        reader.close();

    }


    public void stringProcessing(String ch) {
        String pro = "";
        String pro1 = "";
        String state = "";
        int temp = 0;
        int num = 0;
        if (ch.matches("(\\S+) 新增 (\\S\\S\\S\\S) (\\d+)人")) {
            pro = getPro(ch, 0);
            num = getNum(ch);
            state = getState(ch, 2);
            Map<String, Integer> tempMap = province.get(pro);

            tempMap.put("flag", 0);
            temp = tempMap.get(state) + num;
            tempMap.put(state, temp);
        } else if (ch.matches("(\\S+) (\\S+) 流入 (\\S+) (\\d+)人")) {
            pro = getPro(ch, 0);
            pro1 = getPro(ch, 3);
            num = getNum(ch);
            state = getState(ch, 1);
            Map<String, Integer> tempMap = province.get(pro);

            tempMap.put("flag", 0);
            temp = tempMap.get(state) - num;
            tempMap.put(state, temp);

            tempMap = province.get(pro1);
            tempMap.put("flag", 0);
            temp = tempMap.get(state) + num;
            tempMap.put(state, temp);
        } else if (ch.matches("(\\S+) (\\S+) (\\d+)人")) {
            pro = getPro(ch, 0);
            state = getState(ch, 1);
            num = getNum(ch);
            Map<String, Integer> tempMap = province.get(pro);

            tempMap.put("flag", 0);
            temp = tempMap.get(state) + num;
            tempMap.put(state, temp);
            temp = tempMap.get("感染患者") - num;
            tempMap.put("感染患者", temp);
        } else if (ch.matches("(\\S+) (\\S+) 确诊感染 (\\d+)人")) {
            pro = getPro(ch, 0);
            state = getState(ch, 1);
            num = getNum(ch);
            Map<String, Integer> tempMap = province.get(pro);

            tempMap.put("flag", 0);
            temp = tempMap.get(state) - num;
            tempMap.put(state, temp);
            temp = tempMap.get("感染患者") + num;
            tempMap.put("感染患者", temp);
        } else if (ch.matches("(\\S+) 排除 (\\S+) (\\d+)人")) {
            pro = getPro(ch, 0);
            state = getState(ch, 2);
            num = getNum(ch);
            Map<String, Integer> tempMap = province.get(pro);

            tempMap.put("flag", 0);
            temp = tempMap.get(state) - num;
            tempMap.put(state, temp);
        }
    }

    public void stringSynthesis(List<String> typeParams,List<String> countryParams)
    {
        if(!countryParams.isEmpty())
        {
            for (int i = 0; i < 32; i++) {
                province.get(provinces[i]).put("flag", -1);
            }
            for(String param:countryParams){
                province.get(param).put("flag",0);
            }
        }
        if(typeParams.isEmpty()) {
            for (String key : province.keySet()) {
                StringBuffer tempString = new StringBuffer();
                Map<String, Integer> map = province.get(key);
                if (map.get("flag") == -1) {
                    continue;
                } else {
                    tempString.append(key + " ");
                    for (String ch : map.keySet()) {
                        if (!"flag".equals(ch)) {
                            tempString.append(ch);
                            tempString.append(map.get(ch) + "人 ");
                        }
                    }
                    result.add(tempString.toString().trim());
                    System.out.println(tempString.toString().trim());
                }
            }
        }
        else {
            for (String key : province.keySet()) {
                StringBuffer tempString = new StringBuffer();
                Map<String, Integer> map = province.get(key);
                if (map.get("flag") == -1) {
                    continue;
                } else {
                    tempString.append(key + " ");
                    for (String param : typeParams) {
                        if("ip".equals(param)){
                            tempString.append("感染患者");
                            tempString.append(map.get("感染患者") + "人 ");
                        }else if("sp".equals(param)){
                            tempString.append("疑似患者");
                            tempString.append(map.get("疑似患者") + "人 ");
                        }else if("cure".equals(param)){
                            tempString.append("治愈");
                            tempString.append(map.get("治愈") + "人 ");
                        }else if("dead".equals(param)){
                            tempString.append("死亡");
                            tempString.append(map.get("死亡") + "人 ");
                        }
                    }
                    result.add(tempString.toString().trim());
                    System.out.println(tempString.toString().trim());
                }
            }
        }
        result.add("// 该文档并非真实数据，仅供测试使用");
    }

    public void logOutput()
    {
        String out = listCommand.listMap.get("-out").get(0);
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(out));
            for(String ch:result)
            {
                writer.write(ch);
                writer.write("\n");
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public String getPro(String ch, int index) {
        String pro = "";
        String[] temp = ch.split(" ");
        pro = temp[index];
        return pro;
    }


    public String getState(String ch, int index) {
        String state = "";
        String[] temp = ch.split(" ");
        state = temp[index];
        return state;
    }

    public Integer getNum(String ch) {
        String regex = "\\d+";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(ch);
        matcher.find();
        Integer num = Integer.parseInt(matcher.group());
        return num;
    }

    public void updateCountry()
    {
        Map<String, Integer> country = province.get("全国");
        int temp = 0;
        for (String key : province.keySet()) {
            if ("全国".equals(key)) {
                continue;
            }
            Map<String, Integer> map = province.get(key);
            for (String str : map.keySet()) {
                if ("flag".equals(str)) {
                    continue;
                }
                temp = country.get(str) + map.get(str);
                country.put(str, temp);
            }
        }
    }
    public void reset()
    {
        for (int i = 0; i < 32; i++) {
            province.put(provinces[i], new LinkedHashMap<>());
            Map<String, Integer> map = province.get(provinces[i]);
            map.put("感染患者", 0);
            map.put("疑似患者", 0);
            map.put("治愈", 0);
            map.put("死亡", 0);
            map.put("flag", -1);
        }
        province.get("全国").put("flag", 0);
    }

    @Test
    public void unitTest() {
        List<String> list = new ArrayList<>();
        //list.add("list -date 2020-01-22 -log D:\\java\\InfectStatistic-main\\221701227\\log -out D:\\java\\InfectStatistic-main\\221701227\\result\\output.txt");
        //list.add("list -log D:\\java\\InfectStatistic-main\\221701227\\log -out D:\\java\\InfectStatistic-main\\221701227\\result\\output2.txt -date 2020-01-23 -type cure dead ip");
        list.add("list -log D:\\java\\InfectStatistic-main\\221701227\\log -out D:\\java\\InfectStatistic-main\\221701227\\result\\output2.txt -date 2020-01-22 -type cure dead ip -province 福建 河北");
        //list.add("list -date 2020-01-22 -log D:\\java\\InfectStatistic-main\\221701227\\log -out D:/output.txt");
        //list.add("list -type sp cure -province 福建 -log D:/log/ -out D:/output.txt");
        //list.add("change -date 2020-01-22 -log D:/log/ -out D:/output.txt");
        //list.add("list -typesp cure -log D:/log/ -out D:/output.txt");
        //list.add("list -type sp cure -province -log D:/log/ -out D:/output.txt");
        for (String command : list) {
            String[] commands = command.split(" ");
//            for (String ch : commands) {
//                System.out.println(ch);
//            }
//            System.out.println("-------------------------------");
            try {
                commandAnalyze(commands);
//                for (String key : listCommand.listMap.keySet()) {
//                    System.out.print(key + ":");
//                    for (String ch : listCommand.listMap.get(key)) {
//                        System.out.print(ch + " ");
//                    }
//                    System.out.println("");
//                }
                System.out.println("-------------------------------");
                fileRead(commands);
            } catch (CommandErrorException e) {
                System.out.println("您输入的命令出现错误，请重新输入!");
                exit(-1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static void main(String[] args) {

    }
}
