package de.roamingthings.devworkbench.link.repository

import de.roamingthings.devworkbench.Application
import de.roamingthings.devworkbench.category.CategoryModule
import de.roamingthings.devworkbench.category.domain.Category
import de.roamingthings.devworkbench.config.JiraConfiguration
import de.roamingthings.devworkbench.config.WorkbenchConfiguration
import de.roamingthings.devworkbench.link.LinkModule
import de.roamingthings.devworkbench.link.domain.Link
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
import org.springframework.data.domain.PageRequest
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner
import java.time.LocalDateTime

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
class LinkRepositoryIT {

    class Config {
        @Bean
        @Scope("prototype")
        fun logger(injectionPoint: InjectionPoint): Logger = Mockito.mock(Logger::class.java)
    }

    @Autowired
    private lateinit var entityManager: TestEntityManager

    @Autowired
    private lateinit var linkRepository: LinkRepository

    @get:Rule
    var softly = JUnitSoftAssertions()

    @Test
    fun `should find list of all Links for a given Category id`() {
        val category = entityManager.persist(Category(code = "testCategory"))
        arrayOf("link1", "link2", "link3")
                .map { Link(code = it, uri = "http://test.de/$it", category = category) }
                .forEach { entityManager.persist(it) }
        entityManager.clear()

        val linkList = linkRepository.findAllByCategoryId(category.id!!)

        softly.assertThat(linkList.size).isEqualTo(3)
        softly.assertThat(linkList[0].code).isEqualTo("link1")
        softly.assertThat(linkList[1].code).isEqualTo("link2")
        softly.assertThat(linkList[2].code).isEqualTo("link3")
    }

    @Test
    fun `should retrieve a list of links ordered by relevance for a given Category id`() {
        val category = entityManager.persist(Category(code = "testCategory"))
        arrayOf("link1", "link2", "link3", "link4", "link5", "link6")
                .map { Link(code = it, uri = "http://test.de/$it", lastAccessed = LocalDateTime.now(), category = category) }
                .forEach { entityManager.persist(it) }
        entityManager.clear()

        val linkList = linkRepository.findAllByCategoryIdOrderByLastAccessedDesc(category.id!!, PageRequest(0, 5))

        softly.assertThat(linkList.size).isEqualTo(5)
        softly.assertThat(linkList[0].code).isEqualTo("link6")
        softly.assertThat(linkList[1].code).isEqualTo("link5")
        softly.assertThat(linkList[2].code).isEqualTo("link4")

    }
}