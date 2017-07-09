package de.roamingthings.devworkbench.category.service

import de.roamingthings.devworkbench.Application
import de.roamingthings.devworkbench.category.CategoryModule
import de.roamingthings.devworkbench.config.ConfigModule
import de.roamingthings.devworkbench.link.LinkModule
import de.roamingthings.devworkbench.link.api.*
import de.roamingthings.devworkbench.link.service.LinkService
import de.roamingthings.devworkbench.link.service.LinkServiceIT
import de.roamingthings.devworkbench.persistence.UpdateField
import de.roamingthings.devworkbench.persistence.UpdateNullableField
import org.assertj.core.api.JUnitSoftAssertions
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.slf4j.Logger
import org.springframework.beans.factory.InjectionPoint
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Scope
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner

/**
 *
 *
 * @author Alexander Sparkowsky [info@roamingthings.de]
 * @version 2017/07/09
 */
@RunWith(SpringRunner::class)
@SpringBootTest(classes = arrayOf(Application::class))
@DataJpaTest
@ContextConfiguration(classes = arrayOf(
        LinkServiceIT.Config::class,
        LinkModule::class,
        CategoryModule::class,
        ConfigModule::class))
class CategoryServiceIT {
    class Config {
        @Bean
        @Scope("prototype")
        fun logger(injectionPoint: InjectionPoint): Logger = Mockito.mock(Logger::class.java)
    }

    @Autowired
    private lateinit var categoryService: CategoryService

    @Autowired
    private lateinit var linkService: LinkService

    @Autowired
    private lateinit var entityManager: TestEntityManager

    @get:Rule
    var softly = JUnitSoftAssertions()

    @Test
    fun `should create category`() {
        val createdCategoryDto = createCategoryForTesting()

        softly.assertThat(createdCategoryDto).isNotNull()
        softly.assertThat(createdCategoryDto.id).isNotNull()
        softly.assertThat(createdCategoryDto.code).isEqualTo("categoryCode")
        softly.assertThat(createdCategoryDto.linkPattern).isEqualTo("categoryLinkPattern")
        softly.assertThat(createdCategoryDto.title).isEqualTo("categoryTitle")
        softly.assertThat(createdCategoryDto.linkCount).isEqualTo(0)
    }

    @Test
    fun `should have link count of 0 for created category`() {
        val createdCategoryDto = createCategoryForTesting()

        val updatedCategory = categoryService.retrieveCategory(createdCategoryDto.id)
        softly.assertThat(updatedCategory?.linkCount).isEqualTo(0)
    }

    @Test
    fun `should have link count set on category`() {
        val createdCategoryDto = createCategoryForTesting()

        linkService.addLinkToCategory(CreateLinkDto("TST-1234", "http://test", "title"), createdCategoryDto.id)
        linkService.addLinkToCategory(CreateLinkDto("TST-5678", "http://test2", "title2"), createdCategoryDto.id)
        linkService.addLinkToCategory(CreateLinkDto("TST-9012", "http://test3", "title3"), createdCategoryDto.id)

        val updatedCategory = categoryService.retrieveCategory(createdCategoryDto.id)
        softly.assertThat(updatedCategory?.linkCount).isEqualTo(3)
    }

    @Test
    fun `should delete category and links`() {
        val createdCategoryDto = createCategoryForTesting()

        linkService.addLinkToCategory(CreateLinkDto("TST-1234", "http://test", "title"), createdCategoryDto.id)
        linkService.addLinkToCategory(CreateLinkDto("TST-5678", "http://test2", "title2"), createdCategoryDto.id)
        linkService.addLinkToCategory(CreateLinkDto("TST-9012", "http://test3", "title3"), createdCategoryDto.id)
        entityManager.flush()
        entityManager.clear()

        val linkCountBeforeDelete: Long = entityManager.entityManager.createQuery("SELECT count(l) FROM Link l").singleResult as Long

        categoryService.deleteCategory(createdCategoryDto.id)
        entityManager.flush()
        entityManager.clear()

        val retrievedCategory = categoryService.retrieveCategory(createdCategoryDto.id)

        val linkCountAfterDelete: Long = entityManager.entityManager.createQuery("SELECT count(l) FROM Link l").singleResult as Long
        softly.assertThat(retrievedCategory).isNull()
        softly.assertThat(linkCountBeforeDelete).isEqualTo(3L)
        softly.assertThat(linkCountAfterDelete).isEqualTo(0)
    }

    @Test
    fun `should update all fields of category`() {
        val createdCategoryDto = createCategoryForTesting()

        val updatedCategory = categoryService.updateCategory(
                createdCategoryDto.id,
                UpdateCategoryDto(
                        code = UpdateField.of("updatedCategory"),
                        title = UpdateNullableField.of("title2"),
                        linkPattern = UpdateNullableField.of("updatedLinkPattern")
                )
        )

        softly.assertThat(updatedCategory).isNotNull
        softly.assertThat(updatedCategory!!.code).isEqualTo("updatedCategory")
        softly.assertThat(updatedCategory.title).isEqualTo("title2")
        softly.assertThat(updatedCategory.linkPattern).isEqualTo("updatedLinkPattern")
    }

    @Test
    fun `should update fields of category to null`() {
        val createdCategoryDto = createCategoryForTesting()

        val updatedCategory = categoryService.updateCategory(
                createdCategoryDto.id,
                UpdateCategoryDto(
                        code = UpdateField.ignore(),
                        title = UpdateNullableField.setNull(),
                        linkPattern = UpdateNullableField.setNull()
                )
        )

        softly.assertThat(updatedCategory).isNotNull
        softly.assertThat(updatedCategory!!.code).isEqualTo("categoryCode")
        softly.assertThat(updatedCategory.title).isNull()
        softly.assertThat(updatedCategory.linkPattern).isNull()
    }

    @Test
    fun `should not update ignored fields of category`() {
        val createdCategoryDto = createCategoryForTesting()

        val updatedCategory = categoryService.updateCategory(
                createdCategoryDto.id,
                UpdateCategoryDto(
                        code = UpdateField.ignore(),
                        title = UpdateNullableField.ignore(),
                        linkPattern = UpdateNullableField.ignore()
                )
        )

        softly.assertThat(updatedCategory).isNotNull
        softly.assertThat(updatedCategory!!.code).isEqualTo("categoryCode")
        softly.assertThat(updatedCategory.title).isEqualTo("categoryTitle")
        softly.assertThat(updatedCategory.linkPattern).isEqualTo("categoryLinkPattern")
    }

    @Test
    fun `should retrieve all categories`() {
        categoryService.createCategory(CreateCategoryDto("categoryCode1", "categoryLinkPattern1", "categoryTitle1"))
        categoryService.createCategory(CreateCategoryDto("categoryCode2", "categoryLinkPattern2", "categoryTitle2"))

        val allCategoriesList = categoryService.retrieveAllCategories()

        softly.assertThat(allCategoriesList).isNotNull
        softly.assertThat(allCategoriesList.size).isEqualTo(2)
    }

    @Test
    fun `should retrieve single category`() {
        val createdCategory = categoryService.createCategory(CreateCategoryDto("categoryCode1", "categoryLinkPattern1", "categoryTitle1"))
        categoryService.createCategory(CreateCategoryDto("categoryCode2", "categoryLinkPattern2", "categoryTitle2"))

        val retrieveCategory = categoryService.retrieveCategory(createdCategory.id)

        softly.assertThat(retrieveCategory).isNotNull
        softly.assertThat(retrieveCategory!!.code).isEqualTo("categoryCode1")
    }

    @Test
    fun `should retrieve single category by code`() {
        categoryService.createCategory(CreateCategoryDto("categoryCode1", "categoryLinkPattern1", "categoryTitle1"))
        categoryService.createCategory(CreateCategoryDto("categoryCode2", "categoryLinkPattern2", "categoryTitle2"))

        val retrieveCategory = categoryService.retrieveCategoryByCode("categoryCode1")

        softly.assertThat(retrieveCategory).isNotNull
        softly.assertThat(retrieveCategory!!.code).isEqualTo("categoryCode1")
    }

    private fun createCategoryForTesting(): CategoryDto {
        val category = categoryService.createCategory(CreateCategoryDto("categoryCode", "categoryLinkPattern", "categoryTitle"))
        return category
    }
}