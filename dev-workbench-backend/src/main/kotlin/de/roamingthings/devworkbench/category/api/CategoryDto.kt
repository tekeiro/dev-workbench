package de.roamingthings.devworkbench.link.api

import de.roamingthings.devworkbench.persistence.UpdateField
import de.roamingthings.devworkbench.persistence.UpdateNullableField

data class CategoryDto(
        val id: Long,
        val code: String,
        val linkPattern: String?,
        val title: String?,
        val linkCount: Int)

data class CreateCategoryDto(
        val code: String,
        val linkPattern: String? = null,
        val title: String? = null
)

data class UpdateCategoryDto(
        val code: UpdateField<String> = UpdateField.ignore(),
        val linkPattern: UpdateNullableField<String> = UpdateNullableField.ignore(),
        val title: UpdateNullableField<String> = UpdateNullableField.ignore())
