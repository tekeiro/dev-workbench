package de.roamingthings.devworkbench.link.controller

import de.roamingthings.devworkbench.config.JiraConfiguration
import de.roamingthings.devworkbench.link.api.LinkDto
import de.roamingthings.devworkbench.link.api.UpdateLinkDto
import de.roamingthings.devworkbench.link.resource.LinkResource
import de.roamingthings.devworkbench.link.service.LinkService
import de.rpr.mycity.web.PATH_LINKS
import org.slf4j.Logger
import org.springframework.hateoas.Resources
import org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo
import org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI


/**
 *
 *
 * @author Alexander Sparkowsky [info@roamingthings.de]
 * @version 2017/06/30
 */
@RestController
@RequestMapping(
        value = PATH_LINKS,
        produces = arrayOf(
                MediaType.APPLICATION_JSON_VALUE,
                MediaType.TEXT_XML_VALUE,
                MediaType.APPLICATION_XML_VALUE),
        consumes = arrayOf(
                MediaType.APPLICATION_JSON_VALUE,
                MediaType.TEXT_XML_VALUE,
                MediaType.APPLICATION_XML_VALUE))
class LinkController constructor(val jiraConfiguration: JiraConfiguration, val linkService: LinkService, val log: Logger) {

    @GetMapping(value = "{id}")
    fun retrieveLink(@PathVariable("id") linkId: Long): ResponseEntity<LinkResource> {
        val result = linkService.retrieveLinkById(linkId)
        return if (result != null) mapSingleLinkToResponse(result) else ResponseEntity.status(HttpStatus.NOT_FOUND).build()
    }

/*
    @GetMapping(value = "code/{code}")
    fun retrieveLinkByCode(@PathVariable("code") code: String): LinkDto? {
        return linkService.retrieveByCode(code)
    }
*/

/*
    @PostMapping
    fun addLink(@RequestBody link: CreateLinkDto, uriBuilder: UriComponentsBuilder): ResponseEntity<LinkResource> {
        val result = linkService.addLinkUniqueByCode(link)
        val resource = LinkResource.fromDto(result)
        resource.add(linkTo(methodOn(this::class.java).retrieveLink(result.id)).withSelfRel())
        return ResponseEntity
                .created(uriBuilder.path("$PATH_LINKS/{id}").buildAndExpand(result.id).toUri())
                .body(resource)
    }
*/

/*
    @GetMapping
    fun retrieveLinks(): ResponseEntity<Resources<LinkResource>> {
        val result = linkService.retrieveLinks()
        return mapLinkListToResponse(result)
    }
*/

/*
    @GetMapping("/relevance")
    fun retrieveLinksByRelevance(): ResponseEntity<Resources<LinkResource>> {
        val result = linkService.retrieveListByRelevanceWithLimit(5)
        return mapLinkListToResponse(result)
    }
*/

    @DeleteMapping(value = "{id}")
    fun deleteLink(@PathVariable("id") id: Long): ResponseEntity<Void> {
        linkService.deleteById(id)
        return ResponseEntity.noContent().build()
    }

    @PutMapping("{id}")
    fun updateLink(@PathVariable("id") linkId: Long, @RequestBody link: UpdateLinkDto): ResponseEntity<LinkResource> {
        val result = linkService.updateLink(linkId, link)
        if (result != null) {
            return mapSingleLinkToResponse(result)
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
    }

    @PostMapping("{id}/redirects")
    fun followLink(@PathVariable("id") linkId: Long): ResponseEntity<Void> {
        val link = linkService.recordLinkAccess(linkId)

        if (link != null) {
            return ResponseEntity.status(HttpStatus.FOUND).location(URI(link.uri)).build()
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
    }

/*
    @GetMapping("/code/{code}/following")
    fun addAndFollowLink(@PathVariable("code") code: String): String {

        if (!code.isEmpty()) {
            log.debug("Promoting link with code {}", code)

            val uri = "${jiraConfiguration.baseUri}/${code}"
            val title = null

            val link = linkService.addAndTrackLinkUniqueByCode(CreateLinkDto(code, uri, title, categoryCode))

            // TODO get the uri from either the link or the category
            return link.uri
        }

        return ""
    }
*/

    companion object {
        fun mapLinkListToResponse(result: List<LinkDto>): ResponseEntity<Resources<LinkResource>> {
            return ResponseEntity.ok(Resources(result.map {
                val resource = LinkResource.fromDto(it)
                resource.add(linkTo(methodOn(LinkController::class.java).retrieveLink(it.id)).withSelfRel())
                resource
            }))
        }

        fun mapSingleLinkToResponse(result: LinkDto): ResponseEntity<LinkResource> {
            val resource = LinkResource.fromDto(result)
            resource.add(linkTo(methodOn(LinkController::class.java).retrieveLink(result.id)).withSelfRel())
            return ResponseEntity.ok(resource)
        }
    }
}