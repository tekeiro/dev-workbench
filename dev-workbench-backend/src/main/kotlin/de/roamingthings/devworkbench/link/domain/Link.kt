package de.roamingthings.devworkbench.link.domain

import de.roamingthings.devworkbench.category.domain.Category
import de.roamingthings.devworkbench.link.api.CreateLinkDto
import de.roamingthings.devworkbench.link.api.LinkDto
import de.roamingthings.devworkbench.link.api.UpdateLinkDto
import java.time.LocalDateTime
import javax.persistence.*
import javax.validation.constraints.NotNull

/**
 *
 *
 * @author Alexander Sparkowsky [info@roamingthings.de]
 * @version 2017/06/30
 */
@Entity
@Table(name = "link")
@NamedQueries(
        NamedQuery(name = "Link.findAllByCategoryCode", query = "SELECT l FROM Link l WHERE l.category.code=?1)"),
        NamedQuery(name = "Link.findRelevantByCategoryCode", query = "SELECT l FROM Link l WHERE l.category.code=?1)")
)
internal data class Link(
        @Id @GeneratedValue
        val id: Long? = null,
        val code: String,
        val uri: String,
        val title: String? = null,
        val lastAccessed: LocalDateTime? = null,
        val accessCount: Int = 0,
        @ManyToOne
        @NotNull
        val category: Category? = null) {

/*
    @Suppress("unused")
    private constructor() : this(code = "", title = null, lastAccessed = null, category = null)
*/

    fun toDto(): LinkDto = LinkDto(
            id = this.id!!,
            code = this.code,
            uri = this.uri,
            title = this.title,
            lastAccessed = this.lastAccessed,
            accessCount = this.accessCount
    )

    fun updateFromDto(dto: UpdateLinkDto) = Link(
            id = id!!,
            code = dto.code.getOrDefault(code),
            uri = dto.uri.getOrDefault(uri),
            title = dto.title.getOrNullOrDefault(title),
            lastAccessed = dto.lastAccessed.getOrNullOrDefault(lastAccessed),
            accessCount = dto.accessCount.getOrDefault(accessCount))

    override fun toString(): String {
        return "Link(id=$id, code='$code', uri='$uri', title='$title', lastAccessed=$lastAccessed, accessCount=$accessCount, category=${category?.code})"
    }

    companion object {

        fun fromDto(dto: LinkDto) = Link(
                id = dto.id,
                code = dto.code,
                uri = dto.uri,
                title = dto.title,
                lastAccessed = dto.lastAccessed)

        fun fromDtoInCategory(dto: CreateLinkDto, category: Category) = Link(
                id = null,
                code = dto.code,
                uri = dto.uri,
                title = dto.title,
                category = category)
    }
}
