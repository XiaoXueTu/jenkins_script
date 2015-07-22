# jenkins_script
jenkins中常用的script

#clear_slave
背景：随着系统运行的时间推移，slave的空间会渐渐的不足
目的：避免机器出现硬盘空间不足
操作：
1. 定时清理不再需要监控的项目
2. 定时清理Slave的硬盘空间，删除已经废弃、超时的项目文件
