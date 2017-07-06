package de.roamingthings.devworkbench.link.domain

import de.roamingthings.devworkbench.link.api.CreateTraceableLinkDto
import de.roamingthings.devworkbench.link.api.TraceableLinkDto
import de.roamingthings.devworkbench.link.api.UpdateTraceableLinkDto
import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table

/**
 *
 *
 * @author Alexander Sparkowsky [info@roamingthings.de]
 * @version 2017/06/30
 */
@Entity
@Table(name = "traceable_link")
internal data class TraceableLink(
        @Id @GeneratedValue val id: Long? = null,
        val code: String,
        val uri: String? = null,
        val title: String? = null,
        val lastAccessed: LocalDateTime? = null,
        val accessCount: Int = 0) {

    @Suppress("unused")
    private constructor() : this(code = "", title = null, lastAccessed = null)

    fun toDto(): TraceableLinkDto = TraceableLinkDto(
            id = this.id!!,
            code = this.code,
            uri = this.uri,
            title = this.title,
            lastAccessed = this.lastAccessed,
            accessCount = this.accessCount
    )

    fun updateFromDto(dto: UpdateTraceableLinkDto) = TraceableLink(
            id = id!!,
            code = dto.code.getOrDefault(code),
            uri = dto.uri.getOrNullOrDefault(uri),
            title = dto.title.getOrNullOrDefault(title),
            lastAccessed = dto.lastAccessed.getOrNullOrDefault(lastAccessed),
            accessCount = dto.accessCount.getOrDefault(accessCount))

    companion object {

        fun fromDto(dto: TraceableLinkDto) = TraceableLink(
                id = dto.id,
                code = dto.code,
                uri = dto.uri,
                title = dto.title,
                lastAccessed = dto.lastAccessed)

        fun fromDto(dto: CreateTraceableLinkDto) = TraceableLink(
                id = null,
                code = dto.code,
                uri = dto.uri,
                title = dto.title)
    }
}
