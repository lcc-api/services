package com.languagecomputer.services.spatial

import java.util.*

/**
 * Message used to send gazetteer information (or relative location info) over the wire.
 *
 * @param representation
 * @param id database id
 * @param name string name of the object e.g. "hawaii"
 * @param designation designation of the level of the object (PPL, PPCA, COUNTRY etc.)
 * @param latitude latitude in degrees
 * @param longitude longitude in degrees
 * @param population population of the location
 * @param importance importance of the location (more important locations are preferred)
 * @param parentId Parent id (only used to add a record to the database)
 */
class SimpleGazetteerRecord (
        val representation: String,
        val id: Int,
        val name: String,
        val designation: String,
        val latitude: Double,
        val longitude: Double,
        val population: Int,
        val importance: Int,
        val parentId: Int?
) {
  override fun toString(): String {
    return "SimpleGazetteerRecord{" +
            "representation='" + representation + '\'' +
            ", id=" + id +
            ", name='" + name + '\'' +
            ", designation='" + designation + '\'' +
            ", latitude=" + latitude +
            ", longitude=" + longitude +
            ", population=" + population +
            ", importance=" + importance +
            ", parentId=" + parentId +
            '}'
  }

  override fun equals(other : Any?) : Boolean {
    if (this === other) return true
    if (other !is SimpleGazetteerRecord) return false
    return id == other.id
  }

  override fun hashCode() : Int{
    return Objects.hashCode(id)
  }
}
