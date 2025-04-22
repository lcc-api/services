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
): Timed {
  init {
    check(endTimeMillis > startTimeMillis) { "start time >= end time (start=$startTimeMillis, end=$endTimeMillis) text=$text"}
  }
}

data class CreateTranscript(
  val utterances: List<Utterance>,
  val source: String,
  val metaData: Map<String, String> = emptyMap(),
  val parentDocID: String? = null,
)

data class Transcript(
    val source: String,
    override val id: String,
    val utterances: List<Utterance>,
    override val metaData: Map<String, String> = mutableMapOf(),
    override val parents: List<String>
): CommonDocProperties, AtomicDocProps {
    override val title: String
        get() = metaData["TITLE"] ?: id
}
