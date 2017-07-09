package de.roamingthings.devworkbench.link.domain

import de.roamingthings.devworkbench.Application
import de.roamingthings.devworkbench.category.CategoryModule
import de.roamingthings.devworkbench.category.domain.Category
import de.roamingthings.devworkbench.config.JiraConfiguration
import de.roamingthings.devworkbench.config.WorkbenchConfiguration
import de.roamingthings.devworkbench.link.LinkModule
import org.assertj.core.api.JUnitSoftAssertions
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.boot.test.context.SpringBootTest
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
        CategoryModule::class,
        LinkModule::class,
        JiraConfiguration::class,
        WorkbenchConfiguration::class))
/*
@AutoConfigureDataJpa
@AutoConfigureTestDatabase
@AutoConfigureTestEntityManager
@Transactional
*/
class LinkJpaIT {

    @Autowired
    private lateinit var entityManager: TestEntityManager

    @get:Rule
    var softly = JUnitSoftAssertions()

    @Test
    fun `should add Link to category`() {
        val category = entityManager.persist(Category(code = "testCategory"))
        val link = entityManager.persistAndFlush(Link(code = "linkCode", uri = "http://test.de", category = category))
        entityManager.clear()

        val retrievedCategory = entityManager.find(Category::class.java, category.id)

        softly.assertThat(retrievedCategory.links?.size).isEqualTo(1)
        val retrievedLink = retrievedCategory.links!![0]
        softly.assertThat(retrievedLink.code).isEqualTo(link.code)
    }

}