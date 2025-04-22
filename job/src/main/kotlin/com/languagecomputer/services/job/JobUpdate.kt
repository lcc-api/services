package com.languagecomputer.services.job

/**
 * Represents an arbitrary update for a job that can be sent to a message queue.
 *
 * The most common would be a completion status update, but error reporting and general progress are also possible.
 * @author smonahan
 */
data class JobUpdate(
    val id: Long, 
    val field: String, 
    val newValue: Object
);
