fun main() {
    val howMany: Int = 100000
    val maxAppearanceTime: Int = 500000
    val diskSize: Int = 100
    val realTimePercentage: Double = 0.35
    val maxExpirationTime: Int = 10
    val requests = generateRequests(howMany, maxAppearanceTime, diskSize, realTimePercentage, maxExpirationTime)

    println(firstComeFirstServe(diskSize, requests.sortedBy { it.appearanceTime }.toMutableList()))
    println(shortestSeekTimeFirst(diskSize, requests.sortedBy { it.appearanceTime }.toMutableList()))
    println(cScan(diskSize, requests.sortedBy { it.appearanceTime }.toMutableList()))
    println(scan(diskSize, requests.sortedBy { it.appearanceTime }.toMutableList()))
}