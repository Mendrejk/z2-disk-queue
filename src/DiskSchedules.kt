import kotlin.collections.ArrayDeque
import kotlin.math.abs

fun firstComeFirstServe(diskSize: Int, incomingRequests: MutableList<Request>): Int {
    // ArrayDeque is part of kotlin's experimentalStdLibApi, it's use has to be enabled in build settings
    val ongoingRequests: ArrayDeque<Request> = ArrayDeque()
    var elapsedTime: Int = 0
    var headLocation: Int = 0
    var goingForward: Boolean = true

    while (incomingRequests.isNotEmpty() || ongoingRequests.isNotEmpty()) {
        ongoingRequests.addAll(retrievePendingRequests(incomingRequests, elapsedTime))
        handleRequests(ongoingRequests, headLocation)

        // when the head reaches the disk's end, it starts going the other way
        goingForward = findHeadDirectionChange(headLocation, goingForward, diskSize)

        // the head goes back if the first request in queue is behind it
        // when there are no requests in queue, the head goes forward
        val headLocationChange: Int = when {
            ongoingRequests.isNotEmpty() && ongoingRequests[0].adress < headLocation -> -1
            ongoingRequests.isNotEmpty() && ongoingRequests[0].adress > headLocation -> 1
            goingForward -> 1
            else -> -1
        }

        headLocation += headLocationChange
        elapsedTime++
    }
    return elapsedTime
}

fun shortestSeekTimeFirst(diskSize: Int, incomingRequests: MutableList<Request>): Int {
    val ongoingRequests: ArrayDeque<Request> = ArrayDeque()
    var elapsedTime: Int = 0
    var headLocation: Int = 0
    var goingForward: Boolean = true

    while (incomingRequests.isNotEmpty() || ongoingRequests.isNotEmpty()) {
        ongoingRequests.addAll(retrievePendingRequests(incomingRequests, elapsedTime))
        // this leads to non-instant handling of requests that appear in between handles and have the same adress
        // as nearest, but it's very rare and the overhead is just 2 ticks (it goes one forward, then goes back and
        // handles the request), so I left it be as to not complicate handleRequests function and keep sorting to the
        // bare minimum
        if (handleRequests(ongoingRequests, headLocation)) {
            // only sort when the nearest request is handled
            ongoingRequests.sortBy { findHeadDistance(headLocation, it) }
        }

        goingForward = findHeadDirectionChange(headLocation, goingForward, diskSize)

        val headLocationChange: Int = when {
            ongoingRequests.isNotEmpty() && ongoingRequests[0].adress < headLocation -> -1
            ongoingRequests.isNotEmpty() && ongoingRequests[0].adress > headLocation -> 1
            goingForward -> 1
            else -> -1
        }

        headLocation += headLocationChange
        elapsedTime++
    }
    return elapsedTime
}

fun cScan(diskSize: Int, incomingRequests: MutableList<Request>): Int {
    var ongoingRequests: ArrayDeque<Request> = ArrayDeque()
    var elapsedTime: Int = 0
    var headLocation: Int = 0

    while (incomingRequests.isNotEmpty() || ongoingRequests.isNotEmpty()) {
        val ongoingRequestsPreviousSize = ongoingRequests.size
        ongoingRequests.addAll(retrievePendingRequests(incomingRequests, elapsedTime))
        if (ongoingRequests.size > ongoingRequestsPreviousSize) {
            // sort when there are new requests in queue
            // had to introduce a temp variable, not the most elegenant solution
            // an alternative would be to make retrievePendingRequests return a Pair with a boolean value,
            // but I don't think it's good either
            // here, I don't know if it's beeter for sortedCScan to return new ArrayDeque and make ongoingRequests var
            // or to make (sortCScan) change the elements of ongoingRequests and keep them as val
            ongoingRequests = sortedScan(ongoingRequests, headLocation)
        }
        handleRequests(ongoingRequests, headLocation)

        // moves head to disk beggining if it reaches it's end
        headLocation = if (headLocation < diskSize) headLocation + 1 else 0

        // TODO might want to put those in if as well
        elapsedTime++
    }
    return elapsedTime
}

fun scan(diskSize: Int, incomingRequests: MutableList<Request>): Int {
    var ongoingRequests: ArrayDeque<Request> = ArrayDeque()
    var elapsedTime: Int = 0
    var headLocation: Int = 0
    var goingForward: Boolean = true

    while (incomingRequests.isNotEmpty() || ongoingRequests.isNotEmpty()) {
        val ongoingRequestsPreviousSize = ongoingRequests.size
        ongoingRequests.addAll(retrievePendingRequests(incomingRequests, elapsedTime))
        if (ongoingRequests.size > ongoingRequestsPreviousSize) {
            ongoingRequests = sortedScan(ongoingRequests, headLocation, goingForward)
        }
        handleRequests(ongoingRequests, headLocation)

        goingForward = findHeadDirectionChange(headLocation, goingForward, diskSize)

        val headLocationChange: Int = when {
            goingForward -> 1
            else -> -1
        }

        headLocation += headLocationChange
        elapsedTime++
    }
    return elapsedTime
}

fun retrievePendingRequests(incomingRequests: MutableList<Request>, elaspedTime: Int): List<Request> {
    val pendingRequests: MutableList<Request> = mutableListOf()
    while (incomingRequests.isNotEmpty() && incomingRequests[0].isAppearanceTime(elaspedTime)) {
        pendingRequests.add(incomingRequests.removeAt(0))
    }
    return pendingRequests
}

fun handleRequests(ongoingRequests: ArrayDeque<Request>, headLocation: Int): Boolean {
    var isRemoved: Boolean = false
    while (ongoingRequests.isNotEmpty() && ongoingRequests[0].adress == headLocation) {
        ongoingRequests.removeFirst()
        isRemoved = true
    }
    return isRemoved
}

fun findHeadDirectionChange(headLocation: Int, goingForward: Boolean, diskSize: Int): Boolean =
    when (headLocation) {
        0 -> true
        diskSize - 1 -> false
        else -> goingForward
    }

fun findHeadDistance(headLocation: Int, request: Request): Int = abs(request.adress - headLocation)

// this is probably a better way  of sorting in SCAN overall, but the one below is fancier so I went with it :(
fun findHeadDistanceCScan(headLocation: Int, request: Request, diskSize: Int): Int =
        when {
            request.adress >= headLocation -> request.adress - headLocation
            else -> diskSize - headLocation + request.adress
        }

fun sortedScan(ongoingRequests: ArrayDeque<Request>, headLocation: Int, goingForward: Boolean = true): ArrayDeque<Request> =
        if (goingForward) {
            ArrayDeque(
                    ongoingRequests.groupByTo(sortedMapOf()) { it.adress >= headLocation }
                            .values
                            .map { x -> x.sortedBy { it.adress } }
                            .reduce { before, ahead -> ahead + before })
        } else {
            ArrayDeque(
                    ongoingRequests.groupByTo(sortedMapOf()) { it.adress >= headLocation }
                            .values
                            .map { x -> x.sortedByDescending { it.adress } }
                            .reduce { before, ahead -> before + ahead })
        }