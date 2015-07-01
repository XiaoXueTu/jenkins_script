import hudson.model.*;
import hudson.util.*;
import jenkins.model.*;
import hudson.FilePath.FileCallable;
import hudson.slaves.OfflineCause;
import hudson.node_monitors.*;

// 获取废弃的Job名字
def deprecatedJobs = []

for (job in Jenkins.instance.items) {
	jobName = job.getFullDisplayName()

    if(jobName.contains("Deprecated_")) {
        deprecatedJobs.add(jobName)
    }
}

println("find deprecated jobs, the size is : " + deprecatedJobs.size())
deprecatedJobs.each{jobName->
  println(jobName)
}

// 清理 超过90天没有运行的jobs
println("\n\n============================================================")
println("Now going to delete the jobs which are deprecated over 30 days...")

def now= new Date()   
println(now)
for (job in Jenkins.instance.items) {
    def jobName = job.getFullDisplayName();
    if(jobName.contains("System_")) {
        continue;
    }
  
	def lastBuildTime = job.lastBuild.time;
  
    if( (now-lastBuildTime) > 30) {
        if (jobName.contains("CheckCode")) {
           if ((now-lastBuildTime) < 90) {
               continue; 
           }
        }
        println(jobName + " have deprecated over " + (now-lastBuildTime) + " days");
        job.delete();
    }
}

// 清理Slave的workspcae
println("\n\n============================================================")
println("Now going to delete the unuseful files in slave machines...")
for (slave in Jenkins.instance.nodes) {
	println("clear workspace for: '$slave.nodeName'")

	def workspaceRoot = slave.workspaceRoot

	if(workspaceRoot == null) {
	    println("Slave '$slave.nodeName' has a <null> workspaceRoot - skip workspace cleanup");
	    continue;
    }

    // 判断Slave是否在线
    if(!slave.computer.online) {
	    println("Slave '$slave.nodeName' is currently offline - skip workspace cleanup");
	    continue;
    }

    println("Slave '$slave.nodeName' is online - perform workspace cleanup:");
    
    // 开始删文件
    def subdirs = workspaceRoot.list();

	if(subdirs.size() == 0) {
	    println("  (workspace is empty)");
	    continue;
	}

	for(dir in subdirs) {
		// println(dir.name)

		dirName = dir.name
		def isExists = deprecatedJobs.contains(dirName) || deprecatedJobs.contains("Deprecated_" + dirName)
		
		if (isExists) {
			println("delete dir : Deprecated_" + dirName)
			dir.deleteRecursive()
        } else {
            def isExists2 = false
        	for (job in Jenkins.instance.items) {
                jobName = job.getFullDisplayName()
            
                if(jobName.equals(dirName)) {
                    isExists2 = true
                    break
                }
            }
          
            if (isExists2) {
                continue
            }
            println("delete dir : " + dirName)
            dir.deleteRecursive()
        }
	}
    println("------------------------------------------------------------")
}