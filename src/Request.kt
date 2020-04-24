class Request(
    val adress: Int,
    val appearanceTime: Int,
    expiresIn: Int?
) {
    // expirationTime will be equal to expiresIn + appearanceTime if the former is not null, and null otherwise.
    private val expirationTime: Int? = expiresIn?.plus(appearanceTime)

    override fun toString(): String {
        return "Request(adress=$adress, appearanceTime=$appearanceTime, expirationTime=$expirationTime)"
    }

    fun isExpired(timeElapsed: Int): Boolean = expirationTime == timeElapsed

    fun isAppearanceTime(timeElapsed: Int): Boolean = appearanceTime == timeElapsed
}