fun main() {
    val howMany: Int = 1000000;
    val maxAppearanceTime: Int = 10000000;
    val diskSize: Int = 50;
    val realTimePercentage: Double = 0.35;
    val maxExpirationTime: Int = 10;
    val requests = generateRequests(howMany, maxAppearanceTime, diskSize, realTimePercentage, maxExpirationTime)

    firstComeFirstServe(diskSize, requests.sortedBy { it.appearanceTime }.toMutableList())
}