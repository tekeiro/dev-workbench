package de.roamingthings.devworkbench.link.controller

import de.roamingthings.devworkbench.link.api.CreateTraceableLinkDto
import de.roamingthings.devworkbench.link.api.TraceableLinkDto
import de.roamingthings.devworkbench.link.api.UpdateTraceableLinkDto
import de.roamingthings.devworkbench.link.resource.TraceableLinkResource
import de.roamingthings.devworkbench.link.service.TraceableLinkService
import de.rpr.mycity.web.PATH_TRACEABLE_LINKS
import org.slf4j.Logger
import org.springframework.hateoas.Resources
import org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo
import org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn
import org.springframework.http.HttpEntity
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder


/**
 *
 *
 * @author Alexander Sparkowsky [info@roamingthings.de]
 * @version 2017/06/30
 */
@RestController
@RequestMapping(
        value = PATH_TRACEABLE_LINKS,
        produces = arrayOf(
                MediaType.APPLICATION_JSON_VALUE,
                MediaType.TEXT_XML_VALUE,
                MediaType.APPLICATION_XML_VALUE))
class TraceableLinkController constructor(val traceableLinkService: TraceableLinkService, val log: Logger) {

    @GetMapping
    fun retrieveTraceableLinks(): HttpEntity<Resources<TraceableLinkResource>> {
        log.debug("Retrieving traceable links")

        val result = traceableLinkService.retrieveTraceableLinks()
        return ResponseEntity.ok(Resources(result.map {
            val resource = TraceableLinkResource.fromDto(it)
            resource.add(linkTo(methodOn(this::class.java).retrieveTraceableLink(it.id)).withSelfRel())
            resource
        }))
    }

    @GetMapping(value = "{id}")
    fun retrieveTraceableLink(@PathVariable("id") traceableLinkId: Long): HttpEntity<TraceableLinkResource> {
        log.debug("Retrieving traceableLink: {}", traceableLinkId)

        val result = traceableLinkService.retrieveTraceableLinkById(traceableLinkId)
        if (result != null) {
            val resource = TraceableLinkResource.fromDto(result)
            resource.add(linkTo(methodOn(this::class.java).retrieveTraceableLink(result.id)).withSelfRel())
            return ResponseEntity.ok(resource)
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
    }

    @GetMapping(value = "code/{code}")
    fun retrieveTraceableLinkByCode(@PathVariable("code") code: String): TraceableLinkDto? {
        return traceableLinkService.retrieveByCode(code)
    }

    @PostMapping(consumes = arrayOf(
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.TEXT_XML_VALUE,
            MediaType.APPLICATION_XML_VALUE))
    fun addTraceableLink(@RequestBody traceableLink: CreateTraceableLinkDto, uriBuilder: UriComponentsBuilder): HttpEntity<TraceableLinkResource> {
        log.debug("Request to add a city")

        val result = traceableLinkService.addTraceableLinkUniqueById(traceableLink)
        val resource = TraceableLinkResource.fromDto(result)
        resource.add(linkTo(methodOn(this::class.java).retrieveTraceableLink(result.id)).withSelfRel())
        return ResponseEntity
                .created(uriBuilder.path("$PATH_TRACEABLE_LINKS/{id}").buildAndExpand(result.id).toUri())
                .body(resource)
    }

    @DeleteMapping(value = "{id}")
    fun deleteTraceableLink(@PathVariable("id") id: Long): HttpEntity<Void> {
        traceableLinkService.deleteById(id)
        return ResponseEntity.noContent().build()
    }

    @PutMapping("{id}")
    fun updateTraceableLink(@PathVariable("id") traceableLinkId: Long, @RequestBody traceableLink: UpdateTraceableLinkDto): HttpEntity<TraceableLinkResource> {
        val result = traceableLinkService.updateTraceableLink(traceableLinkId, traceableLink)
        if (result != null) {
            val resource = TraceableLinkResource.fromDto(result)
            resource.add(linkTo(methodOn(this::class.java).retrieveTraceableLink(result.id)).withSelfRel())
            return ResponseEntity.ok(resource)
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build()
        }
    }

}