package de.roamingthings.devworkbench.category.domain

import de.roamingthings.devworkbench.link.api.CategoryDto
import de.roamingthings.devworkbench.link.api.CreateCategoryDto
import de.roamingthings.devworkbench.link.api.UpdateCategoryDto
import de.roamingthings.devworkbench.link.domain.Link
import javax.persistence.*

/**
 *
 *
 * @author Alexander Sparkowsky [info@roamingthings.de]
 * @version 2017/07/06
 */
@Entity @Table(name = "category")
internal data class Category(
        @Id @GeneratedValue val id: Long? = null,
        val code: String,
        val linkPattern: String? = null,
        val title: String? = null,
        @OneToMany(mappedBy = "category", cascade = arrayOf(CascadeType.REMOVE), orphanRemoval = true)
        var links: MutableList<Link>? = null
        ) {

/*
    @Suppress("unused")
    private constructor() : this(code = "")
*/

    fun toDto(): CategoryDto = CategoryDto(
            id = this.id!!,
            code = this.code,
            linkPattern = this.linkPattern,
            title = this.title,
            linkCount = if (this.links != null) this.links!!.size else 0
    )

    fun updateFromDto(dto: UpdateCategoryDto) = Category(
            id = id!!,
            code = dto.code.getOrDefault(code),
            linkPattern = dto.linkPattern.getOrNullOrDefault(linkPattern),
            title = dto.title.getOrNullOrDefault(title))

    companion object {

        fun fromDto(dto: CategoryDto) = Category(
                id = dto.id,
                code = dto.code,
                linkPattern = dto.linkPattern,
                title = dto.title)

        fun fromDto(dto: CreateCategoryDto) = Category(
                id = null,
                code = dto.code,
                linkPattern = dto.linkPattern,
                title = dto.title)
    }

}