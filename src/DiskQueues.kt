fun firstComeFirstServe(diskSize: Int, incomingRequests: MutableList<Request>): Unit {
    // ArrayDeque is part of kotlin's experimentalStdLibApi, it's use has to be enabled in build settings
    val ongoingRequests: ArrayDeque<Request> = ArrayDeque()
    var elaspedTime: Int = 0
    var headLocation: Int = 0
    var goingForward: Boolean = true

    while (incomingRequests.isNotEmpty() || ongoingRequests.isNotEmpty()) {
        ongoingRequests.addAll(retrievePendingRequests(incomingRequests, elaspedTime))
        handleRequests(ongoingRequests, headLocation)

        // when the head reaches the disk's end, it starts going the other way
        when (headLocation) {
            0 -> goingForward = true
            diskSize -> goingForward = false
        }

        // the head goes back if the first request in queue is behind it
        // when there are no requests in queue, the head goes forward
        val headLocationChange: Int = when {
            ongoingRequests.isNotEmpty() && ongoingRequests[0].adress < headLocation -> -1
            goingForward -> 1
            else -> -1
        }
        headLocation += headLocationChange
        elaspedTime++
    }
}

fun retrievePendingRequests(incomingRequests: MutableList<Request>, elaspedTime: Int): List<Request> {
    val pendingRequests: MutableList<Request> = mutableListOf()
    while (incomingRequests.isNotEmpty() && incomingRequests[0].isAppearanceTime(elaspedTime)) {
        pendingRequests.add(incomingRequests.removeAt(0))
    }
    return pendingRequests
}

fun handleRequests(ongoingRequests: ArrayDeque<Request>, headLocation: Int): Unit {
    while (ongoingRequests.isNotEmpty() && ongoingRequests[0].adress == headLocation) {
        ongoingRequests.removeFirst()
    }
}