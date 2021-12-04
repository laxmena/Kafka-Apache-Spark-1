package HelperUtils

class SparkUtil
object SparkUtil {
    def checkLogLevel(log: String, logLevel: String): Int = {
        val logSplit = log.split(" ")
        val logType = logSplit(2)
        if (logType == logLevel) {
            return 1
        }
        return 0
    }

    def isError = (log: String) => checkLogLevel(log, Constants.ERROR_LOG_LEVEL)
    def isWarning = (log: String) => checkLogLevel(log, Constants.WARN_LOG_LEVEL)
    def isDebug = (log: String) => checkLogLevel(log, Constants.DEBUG_LOG_LEVEL)
    def isInfo = (log: String) => checkLogLevel(log, Constants.INFO_LOG_LEVEL)
    def combinePartitions = (p1: Int, p2: Int) => p1 + p2

}