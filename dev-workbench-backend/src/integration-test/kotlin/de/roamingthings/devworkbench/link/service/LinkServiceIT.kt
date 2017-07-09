package de.roamingthings.devworkbench.link.service


import de.roamingthings.devworkbench.Application
import de.roamingthings.devworkbench.category.CategoryModule
import de.roamingthings.devworkbench.category.service.CategoryNotFoundException
import de.roamingthings.devworkbench.category.service.CategoryService
import de.roamingthings.devworkbench.config.ConfigModule
import de.roamingthings.devworkbench.link.LinkModule
import de.roamingthings.devworkbench.link.api.*
import de.roamingthings.devworkbench.persistence.UpdateField
import de.roamingthings.devworkbench.persistence.UpdateNullableField
import org.assertj.core.api.JUnitSoftAssertions
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.slf4j.Logger
import org.springframework.beans.factory.InjectionPoint
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Scope
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner
import java.time.LocalDateTime

@RunWith(SpringRunner::class)
@SpringBootTest(classes = arrayOf(Application::class))
@DataJpaTest
@ContextConfiguration(classes = arrayOf(
        LinkServiceIT.Config::class,
        LinkModule::class,
        CategoryModule::class,
        ConfigModule::class))
class LinkServiceIT {

    class Config {
        @Bean
        @Scope("prototype")
        fun logger(injectionPoint: InjectionPoint): Logger = Mockito.mock(Logger::class.java)
    }

    @Autowired
    private lateinit var categoryService: CategoryService

    @Autowired
    private lateinit var linkService: LinkService

    @get:Rule
    var softly = JUnitSoftAssertions()

    @get:Rule
    var thrown = ExpectedException.none()

    @Test
    fun `should retrieve a link by id`() {
        val createdLinkDto = createLinkForTesting()

        val retrievedLink = linkService.retrieveLinkById(createdLinkDto.id)
        softly.assertThat(retrievedLink).isNotNull
        softly.assertThat(retrievedLink!!.code).isEqualTo("TST-1234")
    }

    @Test
    fun `should add link to a given category`() {
        val categoryDto = createCategoryForTesting()
        val createdLinkDto = linkService.addLinkToCategory(CreateLinkDto("TST-1234", "http://test", "title"), categoryDto.id)

        val retrievedLinks = linkService.retrieveLinksForCategory(categoryDto.id)
        softly.assertThat(retrievedLinks.size).isEqualTo(1)
        softly.assertThat(retrievedLinks[0].id).isEqualTo(createdLinkDto.id)
        softly.assertThat(retrievedLinks[0].code).isEqualTo("TST-1234")
    }

    @Test
    fun `should throw exception when adding a link to an unknown category`() {
        thrown.expect(CategoryNotFoundException::class.java)
        linkService.addLinkToCategory(CreateLinkDto("linkCode", "http://test", "title"), 9999)
    }

    @Test
    fun `should update all fields`() {
        val linkDto = createLinkForTesting()

        val expectedLastAccessed = LocalDateTime.now()
        val updatedLink = linkService.updateLink(
                linkDto.id,
                UpdateLinkDto(
                        code = UpdateField.of("TST-4321"),
                        title = UpdateNullableField.of("title2"),
                        lastAccessed = UpdateNullableField.of(expectedLastAccessed),
                        accessCount = UpdateField.of(1)
                )
        )

        softly.assertThat(updatedLink).isNotNull
        softly.assertThat(updatedLink?.code).isEqualTo("TST-4321")
        softly.assertThat(updatedLink?.title).isEqualTo("title2")
        softly.assertThat(updatedLink?.lastAccessed).isEqualTo(expectedLastAccessed)
        softly.assertThat(updatedLink?.accessCount).isEqualTo(1)
    }

    @Test
    fun `should update fields to null`() {
        val linkDto = createLinkForTesting()

        val updatedLink = linkService.updateLink(
                linkDto.id,
                UpdateLinkDto(
                        title = UpdateNullableField.setNull(),
                        lastAccessed = UpdateNullableField.setNull()
                )
        )

        softly.assertThat(updatedLink).isNotNull
        softly.assertThat(updatedLink?.code).isEqualTo("TST-1234")
        softly.assertThat(updatedLink?.title).isNull()
        softly.assertThat(updatedLink?.lastAccessed).isNull()
        softly.assertThat(updatedLink?.accessCount).isEqualTo(0)
    }

    @Test
    fun `should not update ignored fields`() {
        val linkDto = createLinkForTesting()

        val updatedLink = linkService.updateLink(
                linkDto.id,
                UpdateLinkDto(
                        code = UpdateField.ignore(),
                        uri = UpdateField.ignore(),
                        title = UpdateNullableField.ignore(),
                        lastAccessed = UpdateNullableField.ignore(),
                        accessCount = UpdateField.ignore()
                )
        )

        softly.assertThat(updatedLink).isNotNull
        softly.assertThat(updatedLink?.code).isEqualTo(linkDto.code)
        softly.assertThat(updatedLink?.title).isEqualTo(linkDto.title)
        softly.assertThat(updatedLink?.lastAccessed).isEqualTo(linkDto.lastAccessed)
        softly.assertThat(updatedLink?.accessCount).isEqualTo(linkDto.accessCount)
    }

    @Test
    fun `should delete link`() {
        val linkDto = createLinkForTesting()

        linkService.deleteById(linkDto.id)

        val retrievedLink = linkService.retrieveLinkById(linkDto.id)
        softly.assertThat(retrievedLink).isNull()
    }

    @Test
    fun `should record access to link`() {
        val linkDto = createLinkForTesting()

        val updatedLinkDto = linkService.recordLinkAccess(linkDto.id)

        softly.assertThat(updatedLinkDto?.accessCount).isEqualTo(linkDto.accessCount + 1)
        softly.assertThat(updatedLinkDto?.lastAccessed).isNotNull()
    }

    private fun createCategoryForTesting(): CategoryDto {
        val category = categoryService.createCategory(CreateCategoryDto("categoryCode"))
        return category
    }

    private fun createLinkForTesting(): LinkDto {
        val category = createCategoryForTesting()

        val createdLinkDto = linkService.addLinkToCategory(CreateLinkDto("TST-1234", "http://test", "title"), category.id)
        return createdLinkDto
    }
}
