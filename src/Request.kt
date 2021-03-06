import kotlin.math.abs

class Request(
    val adress: Int,
    val appearanceTime: Int,
    expiresIn: Int?
) {
    // expirationTime will be equal to expiresIn + appearanceTime if the former is not null, and null otherwise.
    val expirationTime: Int? = expiresIn?.plus(appearanceTime)

    override fun toString(): String {
        return "Request(adress=$adress, appearanceTime=$appearanceTime, expirationTime=$expirationTime)"
    }

    fun isExpired(timeElapsed: Int): Boolean = expirationTime == timeElapsed

    fun isAppearanceTime(timeElapsed: Int): Boolean = appearanceTime == timeElapsed

    fun isFeasible(headLocation: Int, timeElapsed: Int):Boolean = if (expirationTime != null) {
        abs(adress - headLocation) <= abs(expirationTime - timeElapsed)
    } else {
        false
    }

}