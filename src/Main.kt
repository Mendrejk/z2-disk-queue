fun main() {
    val howMany: Int = 100000
    val maxAppearanceTime: Int = 500000
    val diskSize: Int = 100
    val realTimePercentage: Double = 0.25
    val maxExpirationTime: Int = 100
    val requests = generateRequests(howMany, maxAppearanceTime, diskSize, realTimePercentage, maxExpirationTime)

    println("fcfs total path: ${firstComeFirstServe(diskSize, requests.sortedBy { it.appearanceTime }.toMutableList())}")
    println("sstf total path: ${shortestSeekTimeFirst(diskSize, requests.sortedBy { it.appearanceTime }.toMutableList())}")
    println("c-scan total path: ${cScan(diskSize, requests.sortedBy { it.appearanceTime }.toMutableList())}")
    println("scan total path: ${scan(diskSize, requests.sortedBy { it.appearanceTime }.toMutableList())}")
    val edfResult: Pair<Int, Double> = earliestDeadlineFirst(diskSize, requests.sortedBy { it.appearanceTime }.toMutableList())
    println("edf total path: ${edfResult.first}, real-time task fail percentage: ${edfResult.second * 100}%")
    val fdScanResult: Pair<Int, Double> = feasibleDeadlineScan(diskSize, requests.sortedBy { it.appearanceTime }.toMutableList())
    println("fd-scan total path: ${fdScanResult.first}, real-time task fail percentage: ${fdScanResult.second * 100}%")
}