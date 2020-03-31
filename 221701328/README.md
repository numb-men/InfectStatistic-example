# InfectStatistic-221701328
## 疫情统计
### 项目描述
最近新型冠状病毒疫情严重，全国人民都感到担忧，迫切希望能够及时了解到病毒最新的情况.本程序通过日志文件统计各地疫情信息。
### 运行方式
以Linux操作系统为例，使用java1.8及以上版本编译源代码，cd进入class文件目录，输入命令：

    java InfectStatistic <命令> -log <log文件目录> -out <输出文件目录> 
如：

    java InfectStatistic list -log /root/log -out /root/out/out.txt
### 功能简介
+ ##### list命令
    + -log 指定日志目录的位置，该项必需附带，请直接使用传入的路径。
    + -out 指定输出文件路径和文件名，该项必需附带，请直接使用传入的路径。
    + -date 指定日期，不设置则默认为所提供日志最新的一天。你需要确保你处理了指定日期之前的所有log文件
    + -type 可选择[ip： infection patients 感染患者，sp： suspected patients 疑似患者，cure：治愈 ，dead：死亡患者]，使用缩写选择，如 -type ip 表示只列出感染患者的情况，-type sp cure则会按顺序【sp, cure】列出疑似患者和治愈患者的情况，不指定该项默认会列出所有情况。
    + -province 指定列出的省，如-province 福建，则只列出福建，-province 全国 浙江则只会列出全国、浙江
+ ##### 其他命令
    + 开发中。。。。。。
### 本次作业相关链接
- ##### [课程链接](https://edu.cnblogs.com/campus/fzu/2020SpringW)
- ##### [作业要求](https://edu.cnblogs.com/campus/fzu/2020SpringW/homework/10281)
- ##### [作业正文](https://www.cnblogs.com/herokilito/p/12264891.html)
