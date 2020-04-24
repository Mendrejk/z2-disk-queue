import kotlin.random.Random

fun generateRequests(howMany: Int, maxAppearanceTime: Int, diskSize: Int, realTimePercentage: Double, maxExpirationTime: Int): List<Request> =
        List<Request>(howMany) {
            Request(
                    // until - range exclusive, .. - range inclusive
                    adress = (0 until diskSize).random(),
                    appearanceTime = (0..maxAppearanceTime).random(),
                    expiresIn = if (Random.nextDouble() <= realTimePercentage) (1..maxExpirationTime).random() else null
            )
        }