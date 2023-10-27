package com.languagecomputer.services.job

/**
 * Class holding a map from JobState (as a String) to its count
 */
class JobStatusCounts(val statusMap: Map<String, Int>) {
  override fun toString(): String {
    return "JobStatusCounts(statusMap=$statusMap)"
  }
}
