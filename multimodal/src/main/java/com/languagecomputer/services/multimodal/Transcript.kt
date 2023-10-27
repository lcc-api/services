package com.languagecomputer.services.multimodal

interface Timed {
  val startTimeMillis: Int
  val endTimeMillis: Int

  fun overlapsTime(other: Timed): Boolean {
    return containsTime(other.startTimeMillis) || containsTime(other.endTimeMillis) ||
        other.containsTime(startTimeMillis) || other.containsTime(endTimeMillis)
  }

  fun containsTime(timeInMillis: Int) = timeInMillis in startTimeMillis until endTimeMillis
}

data class Utterance(
    val speaker: String,
    val text: String,
    override val startTimeMillis: Int,
    override val endTimeMillis: Int,
): Timed

data class Transcript(
    val source: String,
    val docId: String,
    val utterances: List<Utterance>,
)
