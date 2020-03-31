##InfectStatistic-041701602


###使用方式

命令行（win+r cmd）cd到项目src下，之后输入命令： $ java InfectStatistic list -date 2020-01-22 -log D:/log/ -out D:/output.txt

会读取D:/log/下的所有日志，然后处理日志和命令，在D盘下生成ouput.txt文件列出2020-01-22全国和所有省的情

list命令 支持以下命令行参数：

 -    -log 指定日志目录的位置，该项必会附带，请直接使用传入的路径，而不是自己设置路径
 -    -out 指定输出文件路径和文件名，该项必会附带，请直接使用传入的路径，而不是自己设置路径
 -    -date 指定日期，不设置则默认为所提供日志最新的一天。你需要确保你处理了指定日期之前的所有log文件
 -    -type 可选择[ip： infection patients 感染患者，sp： suspected patients 疑似患者，cure：治愈 ，dead：死亡患者]，使用缩写选择，如 -type ip 表示只列出感染患者的情况，-type sp
   cure则会按顺序【sp, cure】列出疑似患者和治愈患者的情况，不指定该项默认会列出所有情况。
 -   -province 指定列出的省，如-province 福建，则只列出福建，-province 全国 浙江则只会列出全国、浙江