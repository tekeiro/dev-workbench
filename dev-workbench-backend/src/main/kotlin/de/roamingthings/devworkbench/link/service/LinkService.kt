package de.roamingthings.devworkbench.link.service

import de.roamingthings.devworkbench.category.repository.CategoryRepository
import de.roamingthings.devworkbench.category.service.CategoryNotFoundException
import de.roamingthings.devworkbench.link.api.CreateLinkDto
import de.roamingthings.devworkbench.link.api.LinkDto
import de.roamingthings.devworkbench.link.api.UpdateLinkDto
import de.roamingthings.devworkbench.link.domain.Link
import de.roamingthings.devworkbench.link.repository.LinkRepository
import org.slf4j.Logger
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import javax.transaction.Transactional

interface LinkService {
    fun retrieveLinkById(id: Long): LinkDto?

    fun updateLink(id: Long, updateRequest: UpdateLinkDto): LinkDto?

    fun deleteById(id: Long)

    fun recordLinkAccess(id: Long): LinkDto?

    fun retrieveLinkListForCategoryByRelevance(categoryId: Long, limit: Int = 5): List<LinkDto>

    fun retrieveLinksForCategory(categoryId: Long): List<LinkDto>

    fun addLinkToCategory(createRequest: CreateLinkDto, categoryId: Long): LinkDto

//    fun retrieveByCode(code: String): LinkDto?

//    fun retrieveLinks(): List<LinkDto>

//    fun addLinkUniqueByCode(createRequest: CreateLinkDto): LinkDto

//    fun addAndTrackLinkUniqueByCode(createRequest: CreateLinkDto): LinkDto

//    fun promoteLinkByCode(code: String)

//    fun linkForCodeExists(code: String): Boolean
}


@Service
@Transactional
internal class LinkServiceImpl(val linkRepository: LinkRepository, val categoryRepository: CategoryRepository, val log: Logger) : LinkService {
    override fun retrieveLinkById(id: Long): LinkDto? {
        return linkRepository.findOne(id)?.toDto()
    }

    override fun retrieveLinksForCategory(categoryId: Long): List<LinkDto> {
        return linkRepository.findAllByCategoryId(categoryId).map { it.toDto() }
    }

    override fun addLinkToCategory(createRequest: CreateLinkDto, categoryId: Long): LinkDto {
        val category = categoryRepository.findOne(categoryId) ?: throw CategoryNotFoundException("No Category found for id $categoryId")

        val link = Link.fromDtoInCategory(createRequest, category)
        if (category.links != null) {
            category.links!!.add(link)
        } else {
            val links = mutableListOf(link)
            category.links = links
        }
        return linkRepository.save(link).toDto()
    }

    override fun updateLink(id: Long, updateRequest: UpdateLinkDto): LinkDto? {
        val currentLink = linkRepository.findOne(id)
        return if (currentLink != null) linkRepository.save(currentLink.updateFromDto(updateRequest)).toDto()
        else null
    }

    override fun retrieveLinkListForCategoryByRelevance(categoryId: Long, limit: Int): List<LinkDto> {
        val pageRequest = PageRequest(0, limit)
        return linkRepository.findAllByCategoryIdOrderByLastAccessedDesc(categoryId, pageRequest).map { it.toDto() }
    }

    override fun deleteById(id: Long) {
        linkRepository.delete(id)
    }

    override fun recordLinkAccess(id: Long): LinkDto? {
        val currentLink = linkRepository.findOne(id)

        return if (currentLink != null) {
            Link(
                    id = currentLink.id,
                    code = currentLink.code,
                    uri = currentLink.uri,
                    title = currentLink.title,
                    lastAccessed = LocalDateTime.now(),
                    accessCount = currentLink.accessCount + 1).toDto()
        } else {
            null
        }
    }

/*
    override fun linkForCodeExists(code: String): Boolean {
        return linkRepository.existsByCode(code)
    }
*/

/*
    override fun promoteLinkByCode(code: String) {
        val link = linkRepository.findOneByCode(code)

        if (link != null) {
            recordLinkAccess(link.id!!)
        }
    }
*/

/*
    override fun retrieveByCode(code: String): LinkDto? {
        log.debug("Retrieving traceable link: {}", code)

        return linkRepository.findOneByCode(code)?.toDto()
    }
*/

    /*
        override fun addLinkUniqueByCode(createRequest: CreateLinkDto): LinkDto {
            if (!linkRepository.existsByCode(createRequest.code)) {
                return linkRepository.save(Link.fromDto(createRequest)).toDto()
            } else {
                return linkRepository.findOneByCode(createRequest.code)!!.toDto()
            }
        }
    */

/*
    override fun addAndTrackLinkUniqueByCode(createRequest: CreateLinkDto): LinkDto {
        val linkDto: LinkDto

        if (!linkRepository.existsByCode(createRequest.code)) {
            linkDto = linkRepository.save(Link.fromDto(createRequest)).toDto()
        } else {
            linkDto = linkRepository.findOneByCode(createRequest.code)!!.toDto()
        }

        recordLinkAccess(linkDto.id)

        return linkDto
    }
*/
}
