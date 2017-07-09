package de.roamingthings.devworkbench.category.resource

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import de.roamingthings.devworkbench.link.api.CategoryDto
import org.springframework.hateoas.ResourceSupport

/**
 *
 *
 * @author Alexander Sparkowsky [info@roamingthings.de]
 * @version 2017/07/01
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
data class CategoryResource
@JsonCreator
constructor(
        @JsonProperty("id") val _id: Long,
        @JsonProperty("code") val code: String,
        @JsonProperty("linkPattern") val linkPattern: String?,
        @JsonProperty("title") val title: String?) : ResourceSupport() {

    companion object {

        fun fromDto(dto: CategoryDto): CategoryResource =
                CategoryResource(
                        _id = dto.id,
                        code = dto.code,
                        linkPattern = dto.linkPattern,
                        title = dto.title
                )
    }
}