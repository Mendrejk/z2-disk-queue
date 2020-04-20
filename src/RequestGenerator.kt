import kotlin.random.Random

fun generateRequests(howMany: Int, maxAppearanceTime: Int, diskSize: Int, realTimePercentage: Double, maxExpirationTime: Int): List<Request> =
        List<Request>(howMany) {
            Request(
                    adress = (0..diskSize).random(),
                    appearanceTime = (0..maxAppearanceTime).random(),
                    expirationTime = if (Random.nextDouble() <= realTimePercentage) (0..maxExpirationTime).random() else null
            )
        }