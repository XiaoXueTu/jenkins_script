# jenkins_script
jenkins中常用的script

#clear_slave.groovy
背景：随着系统运行的时间推移，slave的空间会渐渐的不足<br/>
目的：避免机器出现硬盘空间不足<br/>
操作：<br/>
1. 定时清理不再需要监控的项目<br/>
2. 定时清理Slave的硬盘空间，删除已经废弃、超时的项目文件<br/>

#email_presend_scripts.groovy
背景：我们希望邮件每次发送的都是相对于上一次新增的错误<br/>
目的：每次发送邮件都是相对于上一次新增的错误<br/>

