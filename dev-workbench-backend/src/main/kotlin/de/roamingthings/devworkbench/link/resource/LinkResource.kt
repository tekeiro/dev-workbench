package de.roamingthings.devworkbench.link.resource

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import de.roamingthings.devworkbench.link.api.LinkDto
import org.springframework.hateoas.ResourceSupport
import java.time.LocalDateTime

/**
 *
 *
 * @author Alexander Sparkowsky [info@roamingthings.de]
 * @version 2017/07/01
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
data class LinkResource
@JsonCreator
constructor(
        @JsonProperty("id") val _id: Long,
        @JsonProperty("code") val code: String,
        @JsonProperty("uri") val uri: String?,
        @JsonProperty("title") val title: String?,
        @JsonProperty("lastAccessed") val lastAccessed: LocalDateTime?,
        @JsonProperty("accessCount") val accessCount: Int) : ResourceSupport() {

    companion object {

        fun fromDto(dto: LinkDto): LinkResource =
                LinkResource(
                        _id = dto.id,
                        code = dto.code,
                        uri = dto.uri,
                        title = dto.title,
                        lastAccessed = dto.lastAccessed,
                        accessCount = dto.accessCount
                )
    }
}