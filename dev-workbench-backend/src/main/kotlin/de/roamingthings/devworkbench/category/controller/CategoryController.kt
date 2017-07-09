package de.roamingthings.devworkbench.category.controller

import de.roamingthings.devworkbench.category.resource.CategoryResource
import de.roamingthings.devworkbench.category.service.CategoryService
import de.roamingthings.devworkbench.link.api.*
import de.roamingthings.devworkbench.link.controller.LinkController
import de.roamingthings.devworkbench.link.resource.LinkResource
import de.roamingthings.devworkbench.link.service.LinkService
import de.rpr.mycity.web.PATH_CATEGORIES
import de.rpr.mycity.web.PATH_LINKS
import org.slf4j.Logger
import org.springframework.hateoas.Resources
import org.springframework.hateoas.mvc.ControllerLinkBuilder
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder
import javax.validation.Valid
import javax.validation.constraints.NotNull

/**
 *
 *
 * @author Alexander Sparkowsky [info@roamingthings.de]
 * @version 2017/07/08
 */
@RestController
@RequestMapping(
        value = PATH_CATEGORIES,
        produces = arrayOf(
                MediaType.APPLICATION_JSON_VALUE,
                MediaType.TEXT_XML_VALUE,
                MediaType.APPLICATION_XML_VALUE),
        consumes = arrayOf(
                MediaType.APPLICATION_JSON_VALUE,
                MediaType.TEXT_XML_VALUE,
                MediaType.APPLICATION_XML_VALUE))
class CategoryController constructor(val categoryService: CategoryService, val linkService: LinkService, val log: Logger) {
    @PostMapping
    fun createCategory(@Valid @RequestBody createRequest: CreateCategoryDto, uriBuilder: UriComponentsBuilder): ResponseEntity<Void> {
        val createdCategory = categoryService.createCategory(createRequest)

        return ResponseEntity
                .created(uriBuilder.path("$PATH_CATEGORIES/{id}")
                        .buildAndExpand(createdCategory.id).toUri())
                .build()
    }

    @PutMapping("{id}")
    fun updateCategory(@NotNull @PathVariable("id") id: Long, updateRequest: UpdateCategoryDto, uriBuilder: UriComponentsBuilder): ResponseEntity<Void> {
        val updatedCategory = categoryService.updateCategory(id, updateRequest)

        return if (updatedCategory != null) {
            ResponseEntity
                    .created(uriBuilder.path("$PATH_CATEGORIES/{id}")
                            .buildAndExpand(id).toUri())
                    .build()
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @DeleteMapping("{id}")
    fun deleteCategory(@NotNull @PathVariable("id") id: Long): ResponseEntity<Void> {
        categoryService.deleteCategory(id)

        return ResponseEntity.noContent().build()
    }

    @GetMapping
    fun retrieveAll(uriBuilder: UriComponentsBuilder): ResponseEntity<Resources<CategoryResource>> {
        val categoryList = categoryService.retrieveAllCategories()
        return mapCategoryListToResponse(categoryList)
    }

    @GetMapping("{id}")
    fun retrieveCategory(@NotNull @PathVariable("id") id: Long): ResponseEntity<CategoryResource> {
        val category = categoryService.retrieveCategory(id)

        return if (category != null) mapSingleCategoryToResponse(category) else ResponseEntity.status(HttpStatus.NOT_FOUND).build()
    }

    @GetMapping("codes/{code}")
    fun retrieveCategoryCode(@NotNull @PathVariable("code") code: String): ResponseEntity<CategoryResource> {
        val category = categoryService.retrieveCategoryByCode(code)

        return if (category != null) mapSingleCategoryToResponse(category) else ResponseEntity.status(HttpStatus.NOT_FOUND).build()
    }

    @GetMapping("{categoryId}/links")
    fun retrieveLinkList(@NotNull @PathVariable("categoryId") categoryId: Long, @RequestParam("mode") mode: String?, uriBuilder: UriComponentsBuilder): ResponseEntity<Resources<LinkResource>> {

        val categoryList =
                if (mode == "relevance") {
                    linkService.retrieveLinkListForCategoryByRelevance(categoryId)
                } else {
                    linkService.retrieveLinksForCategory(categoryId)
                }

        return LinkController.mapLinkListToResponse(categoryList)
    }

    @PostMapping("{categoryId}/links")
    fun createLinkInCategory(@NotNull @PathVariable("categoryId") categoryId: Long, @RequestBody createRequest: CreateLinkDto, uriBuilder: UriComponentsBuilder): ResponseEntity<LinkResource> {
        val category = categoryService.retrieveCategory(categoryId) ?: return ResponseEntity.notFound().build()

        val createdLink = linkService.addLinkToCategory(createRequest, category.id)

        val resource = LinkResource.fromDto(createdLink)
        resource.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(LinkController::class.java).retrieveLink(createdLink.id)).withSelfRel())
        return ResponseEntity
                .created(buildLinkResourceLocationUri(uriBuilder, createdLink))
                .body(resource)
    }

    private fun buildLinkResourceLocationUri(uriBuilder: UriComponentsBuilder, createdLink: LinkDto) = uriBuilder.path("${PATH_LINKS}/{id}").buildAndExpand(createdLink.id).toUri()

    companion object {
        fun mapCategoryListToResponse(result: List<CategoryDto>): ResponseEntity<Resources<CategoryResource>> {
            return ResponseEntity.ok(Resources(result.map {
                val resource = CategoryResource.fromDto(it)
                resource.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(CategoryController::class.java).retrieveCategory(it.id)).withSelfRel())
                resource
            }))
        }

        fun mapSingleCategoryToResponse(result: CategoryDto): ResponseEntity<CategoryResource> {
            val resource = CategoryResource.fromDto(result)
            resource.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(CategoryController::class.java).retrieveCategory(result.id)).withSelfRel())
            return ResponseEntity.ok(resource)
        }
    }

/*
    Retrieve a (weighted) list of links for a given category:: `GET /api/categories/{id}/links
    Create a link (inside a category):: `POST /api/categories/{id}/links`
*/

}