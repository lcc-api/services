package com.languagecomputer.services.spatial

/**
 * Queries into the gazetteer
 */
enum class GazetteerQueryType {
  /**
   * requires one or more of name, designation, or id
   */
  SIMPLE,
  CONTAINER_SEARCH,  //requires
  WITHIN_BBOX,
  /**
   * Requires one of (name, id) and max or min distance
   */
  RELATIVE,
  /**
   * Requires lat, lang, and max or min distance
   */
  RELATIVE_LAT_LONG
}

open class SimpleGazetteerQuery(
    val queryType: GazetteerQueryType?,
    val parentID: Int?,
    val parentName: String?,
    val id: Int?,
    val name: String?,
    val type: String?,
    val latitude: Double?,
    val longitude: Double?,
    val relativeID: Int?,
    val relativeName: String?,
    val relativeLatitude: Double?,
    val relativeLongitude: Double?,
    val minLatitude: Double?,
    val maxLatitude: Double?,
    val minLongitude: Double?,
    val maxLongitude: Double?,
    val maxDistanceInKilometers: Double,
    val minDistanceInKilometers: Double,
    val isUseBorderDistance: Boolean,
    val resultLimit: Int
)

/**
 * Builder class for creating queries, use one of the XXXXQuery constructors
 * @param <SimpleGazetteerQueryBuilder>
</SimpleGazetteerQueryBuilder> */
open class SimpleGazetteerQueryBuilder {
  var queryType: GazetteerQueryType? = null
  var parentID: Int? = null
  var parentName: String? = null
  var id: Int? = null
  var name: String? = null
  var type: String? = null
  var latitude: Double? = null
  var longitude: Double? = null
  var relativeID: Int? = null
  var relativeName: String? = null
  var relativeLatitude: Double? = null
  var relativeLongitude: Double? = null
  var minLatitude: Double? = null
  var maxLatitude: Double? = null
  var minLongitude: Double? = null
  var maxLongitude: Double? = null
  var maxDistanceInKilometers = -1.0
  var minDistanceInKilometers = -1.0
  var useBorderDistance = false
  var orientationInDegrees = -1.0
  var orientationGranularity: Int? = null
  var resultLimit = Int.MAX_VALUE

  fun queryType(queryType: GazetteerQueryType?): SimpleGazetteerQueryBuilder {
    this.queryType = queryType
    return this
  }

  fun parentID(parentID: Int?): SimpleGazetteerQueryBuilder {
    this.parentID = parentID
    return this
  }

  fun parentName(parentName: String?): SimpleGazetteerQueryBuilder {
    this.parentName = parentName
    return this
  }

  fun iD(id: Int?): SimpleGazetteerQueryBuilder {
    this.id = id
    return this
  }

  fun name(name: String?): SimpleGazetteerQueryBuilder {
    this.name = name
    return this
  }

  fun type(type: String?): SimpleGazetteerQueryBuilder {
    this.type = type
    return this
  }

  fun relativeID(relativeID: Int?): SimpleGazetteerQueryBuilder {
    this.relativeID = relativeID
    return this
  }

  fun relativeName(relativeName: String?): SimpleGazetteerQueryBuilder {
    this.relativeName = relativeName
    return this
  }

  /**
   * Used for WITHIN_BBOX query type.
   * @param minLongitude
   * @param maxLongitude
   * @param minLatitude
   * @param maxLatitude
   * @return
   */
  fun boundingBox(minLongitude: Double, maxLongitude: Double, minLatitude: Double, maxLatitude: Double): SimpleGazetteerQueryBuilder {
    this.maxLongitude = maxLongitude
    this.minLongitude = minLongitude
    this.maxLatitude = maxLatitude
    this.minLatitude = minLatitude
    return this
  }

  fun clearRelativeCoordinates(): SimpleGazetteerQueryBuilder {
    queryType = null
    return this
  }

  fun relativeCoordinates(relativeLatitude: Double?, relativeLongitude: Double?): SimpleGazetteerQueryBuilder {
    this.relativeLatitude = relativeLatitude
    this.relativeLongitude = relativeLongitude
    return this
  }

  fun maxDistanceFromRelative(distanceInKilometers: Double): SimpleGazetteerQueryBuilder {
    maxDistanceInKilometers = distanceInKilometers
    return this
  }

  fun minDistanceFromRelative(distanceInKilometers: Double): SimpleGazetteerQueryBuilder {
    minDistanceInKilometers = distanceInKilometers
    return this
  }

  open fun orientationGranularity(granularity: Int?): SimpleGazetteerQueryBuilder? {
    orientationGranularity = granularity
    return this
  }

  open fun useBorderDistance(): SimpleGazetteerQueryBuilder? {
    useBorderDistance = true
    return this
  }

  open fun useMidpointDistance(): SimpleGazetteerQueryBuilder? {
    useBorderDistance = false
    return this
  }

  /**
   * maximum number to return
   * @param resultLimit
   * @return
   */
  fun resultLimit(resultLimit: Int): SimpleGazetteerQueryBuilder {
    this.resultLimit = resultLimit
    return this
  }

  /**
   * Sets result limit to integer.max_value
   * @return
   */
  fun unlimited(): SimpleGazetteerQueryBuilder {
    resultLimit = Int.MAX_VALUE
    return this
  }

  /**
   *
   * @return Builds a gazetteer query
   */
  open fun build(): SimpleGazetteerQuery {
    return SimpleGazetteerQuery(
            queryType,
            parentID,
            parentName,
            id,
            name,
            type,
            latitude,
            longitude,
            relativeID,
            relativeName,
            relativeLatitude,
            relativeLongitude,
            minLatitude,
            maxLatitude,
            minLongitude,
            maxLongitude,
            maxDistanceInKilometers,
            minDistanceInKilometers,
            useBorderDistance,
            resultLimit
    )
  }

  /**
   * Creates a new builder from a query object with the same parameters
   * @param esQuery
   * @return
   */
  fun copy(esQuery: SimpleGazetteerQuery): SimpleGazetteerQueryBuilder {
    queryType = esQuery.queryType
    parentID = esQuery.parentID
    parentName = esQuery.parentName
    id = esQuery.id
    name = esQuery.name
    type = esQuery.type
    latitude = esQuery.latitude
    longitude = esQuery.longitude
    relativeID = esQuery.relativeID
    relativeName = esQuery.relativeName
    relativeLatitude = esQuery.relativeLatitude
    relativeLongitude = esQuery.relativeLongitude
    minLatitude = esQuery.minLatitude
    maxLatitude = esQuery.maxLatitude
    minLongitude = esQuery.minLongitude
    maxLongitude = esQuery.maxLongitude
    maxDistanceInKilometers = esQuery.maxDistanceInKilometers
    minDistanceInKilometers = esQuery.minDistanceInKilometers
    useBorderDistance = esQuery.isUseBorderDistance
    resultLimit = esQuery.resultLimit
    return this
  }

  companion object {
    protected fun create(): SimpleGazetteerQueryBuilder {
      return SimpleGazetteerQueryBuilder()
    }

    /**
     * Simple Query to return a record with the matching id, can set resultlimit or build directly
     * @param id
     * @return
     */
    fun simpleQuery(id: Int): SimpleGazetteerQueryBuilder {
      return create().queryType(GazetteerQueryType.SIMPLE).iD(id)
    }

    /**
     * Simple Query to return a record with the matching id, can set resultlimit or build directly
     * @param name name of place
     * @return
     */
    @JvmStatic
    fun simpleQuery(name: String?): SimpleGazetteerQueryBuilder {
      return create().queryType(GazetteerQueryType.SIMPLE).name(name)
    }

    /**
     * Simple Query to return a record with the matching id, can set result limit or build directly
     * @param name name of place
     * @param designation Common designations are COUNTRY, PPL (populated place), PPLC (capital), ADM1 (province/state)
     * @return
     */
    fun simpleQuery(name: String?, designation: String?): SimpleGazetteerQueryBuilder {
      return create().queryType(GazetteerQueryType.SIMPLE).type(designation).name(name)
    }

    /**
     * queryBuilder for getting points within a relative distance
     * @param id
     * @param maxDistanceInKilometers
     * @return
     */
    fun relativeQuery(id: Int, maxDistanceInKilometers: Double): SimpleGazetteerQueryBuilder {
      return create().queryType(GazetteerQueryType.RELATIVE).relativeID(id).maxDistanceFromRelative(maxDistanceInKilometers)
    }

    /**
     * queryBuilder for getting points within a relative distance
     * @param name
     * @param maxDistanceInKilometers
     * @return
     */
    fun relativeQuery(name: String?, maxDistanceInKilometers: Double): SimpleGazetteerQueryBuilder {
      return create().queryType(GazetteerQueryType.RELATIVE).relativeName(name).maxDistanceFromRelative(maxDistanceInKilometers)
    }

    /**
     * queryBuilder for getting points within a relative distance
     * @param id
     * @param maxDistanceInKilometers
     * @param designation Common designations are COUNTRY, PPL (populated place), PPLC (capital), ADM1 (province/state)
     * @return
     */
    fun relativeQuery(id: Int, maxDistanceInKilometers: Double, designation: String?): SimpleGazetteerQueryBuilder {
      return create().queryType(GazetteerQueryType.RELATIVE).relativeID(id).type(designation).maxDistanceFromRelative(maxDistanceInKilometers)
    }

    /**
     * queryBuilder for getting points within a relative distance
     * @param name
     * @param maxDistanceInKilometers
     * @param designation Common designations are COUNTRY, PPL (populated place), PPLC (capital), ADM1 (province/state)
     * @return
     */
    fun relativeQuery(name: String?, maxDistanceInKilometers: Double, designation: String?): SimpleGazetteerQueryBuilder {
      return create().queryType(GazetteerQueryType.RELATIVE).relativeName(name).type(designation).maxDistanceFromRelative(maxDistanceInKilometers)
    }

    /**
     * queryBuilder for points relative to a lat/long
     * @param lat latitude
     * @param lng longitude
     * @param maxDistanceInKilometers distance in kilometers
     * @return
     */
    fun relativeQuery(lat: Double, lng: Double, maxDistanceInKilometers: Double): SimpleGazetteerQueryBuilder {
      return create().queryType(GazetteerQueryType.RELATIVE_LAT_LONG).relativeCoordinates(lat, lng).maxDistanceFromRelative(maxDistanceInKilometers)
    }

    /**
     * queryBuilder for points relative to a lat/long
     * @param lat latitude
     * @param lng longitude
     * @param maxDistanceInKilometers distance in kilometers
     * @param designation Common designations are COUNTRY, PPL (populated place), PPLC (capital), ADM1 (province/state)
     * @return
     */
    fun relativeQuery(lat: Double, lng: Double, maxDistanceInKilometers: Double, designation: String?): SimpleGazetteerQueryBuilder {
      return create().queryType(GazetteerQueryType.RELATIVE_LAT_LONG).relativeCoordinates(lat, lng).type(designation).maxDistanceFromRelative(maxDistanceInKilometers)
    }

    /**
     * queryBuilder for containers of lat/lng
     * @param lat
     * @param lng
     * @return
     */
    fun containerQuery(lat: Double, lng: Double): SimpleGazetteerQuery {
      return create().queryType(GazetteerQueryType.CONTAINER_SEARCH).relativeCoordinates(lat, lng).build()
    }

    /**
     * queryBuilder for points within a bounding box
     * @param minLatitude
     * @param maxLatitude
     * @param minLongitude
     * @param maxLongitude
     * @return
     */
    fun bboxQuery(minLatitude: Double, maxLatitude: Double, minLongitude: Double, maxLongitude: Double): SimpleGazetteerQueryBuilder {
      return create().queryType(GazetteerQueryType.WITHIN_BBOX)
              .boundingBox(minLongitude, maxLongitude, minLatitude, maxLatitude)
    }
  }
}

