class Request(
    val adress: Int,
    val appearanceTime: Int,
    val expirationTime: Int?
) {
    override fun toString(): String {
        return "Request(adress=$adress, appearanceTime=$appearanceTime, expirationTime=$expirationTime)"
    }

    fun isExpired(timeElapsed: Int): Boolean = expirationTime == timeElapsed

    fun isAppearanceTime(timeElapsed: Int): Boolean = appearanceTime == timeElapsed
}