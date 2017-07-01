package de.roamingthings.devworkbench.link.api

import de.roamingthings.devworkbench.persistence.UpdateField
import java.time.LocalDateTime

data class TraceableLinkDto(
        val id: Long,
        val code: String,
        val uri: String,
        val title: String?,
        val lastAccessed: LocalDateTime?,
        val accessCount: Int)

data class CreateTraceableLinkDto(
        val code: String,
        val uri: String,
        val title: String? = null

)

data class UpdateTraceableLinkDto(val code: UpdateField<String> = UpdateField.ignore(),
                                  val uri: UpdateField<String> = UpdateField.ignore(),
                                  val title: UpdateField<String> = UpdateField.ignore(),
                                  val lastAccessed: UpdateField<LocalDateTime> = UpdateField.ignore(),
                                  val accessCount: UpdateField<Int> = UpdateField.ignore())
