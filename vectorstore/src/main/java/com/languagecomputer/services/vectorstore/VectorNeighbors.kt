package com.languagecomputer.services.vectorstore

/**
 * The results of a neighbors query.
 */
data class VectorNeighbors(
    val neighbors: List<Neighbor> // the neighbor list in array
)

// A neighbor to a vector with its id and the distance
data class Neighbor (
    val id: String, // the id of the neighbor
    val distance: Double // the cosine distance in some vector space
) : Comparable<Neighbor> {
    override fun compareTo(other: Neighbor) = distance.compareTo(other.distance)
}

/**
 * A nearest neighbors query for a [VectorStore] or [VectorSpace].
 * Used to find the closest vectors to the given [vector] ranked by
 * cosine distance.
 * At most, [numNeighbors] neighbors should be returned.
 * Optionally, the returned values are limited to be within the given [maxDistance]
 */
data class VectorNearestNeighborsQuery @JvmOverloads constructor(
    val vector: DoubleArray, // the vector to search for neighbors
    val numNeighbors : Int = 10, // the number of neighbors to retrieve
    val maxDistance : Double? = null// the maximum distance threshold
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as VectorNearestNeighborsQuery
        return numNeighbors == other.numNeighbors &&
                maxDistance == other.maxDistance &&
                vector.contentEquals(other.vector)
    }

    override fun hashCode(): Int {
        var result = numNeighbors
        result = 31 * result + maxDistance.hashCode()
        return result
    }
}
