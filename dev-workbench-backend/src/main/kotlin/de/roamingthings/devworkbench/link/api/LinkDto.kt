package de.roamingthings.devworkbench.link.api

import de.roamingthings.devworkbench.persistence.UpdateField
import de.roamingthings.devworkbench.persistence.UpdateNullableField
import java.time.LocalDateTime

data class LinkDto(
        val id: Long,
        val code: String,
        val uri: String,
        val title: String?,
        val lastAccessed: LocalDateTime?,
        val accessCount: Int)

data class CreateLinkDto(
        val code: String,
        val uri: String,
        val title: String? = null
)

data class UpdateLinkDto(val code: UpdateField<String> = UpdateField.ignore(),
                         val uri: UpdateField<String> = UpdateField.ignore(),
                         val title: UpdateNullableField<String> = UpdateNullableField.ignore(),
                         val lastAccessed: UpdateNullableField<LocalDateTime> = UpdateNullableField.ignore(),
                         val accessCount: UpdateField<Int> = UpdateField.ignore())
