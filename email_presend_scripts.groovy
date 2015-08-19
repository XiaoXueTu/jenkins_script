logger.println "===================="
logger.println build.result
notSend = true

if (build.result == Result.SUCCESS){
        def analysisCoreActions = []
        analysisCoreActions.addAll(build.actions.findAll{it.class.name =~ "hudson.plugins.pmd.PmdResultAction"})

    for (action in analysisCoreActions) {
        def numberOfNewWarnings = action.result.numberOfNewWarnings
        logger.println "number of new warngings: " + numberOfNewWarnings

        def numberOfNewWarningsTemp = 0
        def changeSets = build.changeSet
        def newWarnings = action.result.newWarnings

        for (newWarning in newWarnings) {
            def is_appear = false

            for (changeItem in changeSets) {
                if (is_appear) {
                    break
                }   
                
                def logs = changeSets.logs
                for (log in logs) {
                    if (is_appear) {
                        break
                    }
                    def affectedFiles = log.affectedFiles

                    for (affectedFile in affectedFiles) {
                        newWarningFileName = newWarning.shortFileName
                        affectedPath = affectedFile.path
                        
                        if (affectedPath.contains(newWarningFileName)) {
                            numberOfNewWarningsTemp = numberOfNewWarningsTemp + 1
                            is_appear = true
                            break
                        }
                    }
                }
            }
        }

        logger.println "numberOfNewWarningsTemp : " + numberOfNewWarningsTemp
        //if (numberOfNewWarnings > 0) {
        if (numberOfNewWarningsTemp > 0) {
            notSend = false
            break
        }
    }
} else {
    notSend = false
}
logger.println "notSend = " + notSend
logger.println "===================="
cancel = notSend