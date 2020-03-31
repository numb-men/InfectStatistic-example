# InfectStatistic-221701434
疫情统计

## 如何运行
1、命令行（win+r cmd）cd到项目src下<br>
2、输入命令：javac Infectstatistic.java<br>
3、输入命令：java Infectstatistic list （+各个参数）<br>
## 功能介绍
会读取-log参数后的路径下的日志文件，然后根据-date参数后的日期，提取该日期前的日志信息，列出-province指定省份以及-type参数后的各患者类型的情况，最后在-out参数后的路径生成txt文件。
list命令 支持以下命令行参数：<br>
**-log** 指定日志目录的位置，该项必会附带，请直接使用传入的路径，而不是自己设置路径<br>
**-out** 指定输出文件路径和文件名，该项必会附带，请直接使用传入的路径，而不是自己设置路径<br>
**-date** 指定日期，不设置则默认为所提供日志最新的一天。你需要确保你处理了指定日期之前的所有log文件<br>
**-type** 可选择[ip： infection patients 感染患者，sp： suspected patients 疑似患者，cure：治愈 ，dead：死亡患者]，使用缩写选择，如 -type ip 表示只列出感染患者的情况，-type sp cure则会按顺序【sp, cure】列出疑似患者和治愈患者的情况，不指定该项默认会列出所有情况。
-province 指定列出的省，如-province 福建，则只列出福建，-province 全国 浙江则只会列出全国、浙江
### 作业链接:<[寒假作业2/2](https://edu.cnblogs.com/campus/fzu/2020SpringW/homework/10281)>
### 博客链接：<[寒假作业(2/2)——疫情统计](https://www.cnblogs.com/An1ess/p/12319708.html)>
