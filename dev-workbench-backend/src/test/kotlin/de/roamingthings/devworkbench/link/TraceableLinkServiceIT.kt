package de.roamingthings.devworkbench.link


import de.roamingthings.devworkbench.Application
import de.roamingthings.devworkbench.link.api.CreateTraceableLinkDto
import de.roamingthings.devworkbench.link.api.UpdateTraceableLinkDto
import de.roamingthings.devworkbench.link.domain.TraceableLink
import de.roamingthings.devworkbench.link.repository.TraceableLinkRepository
import de.roamingthings.devworkbench.link.service.TraceableLinkService
import de.roamingthings.devworkbench.persistence.UpdateField
import org.assertj.core.api.Assertions
import org.assertj.core.api.JUnitSoftAssertions
import org.junit.Rule
import org.junit.Test
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
        TraceableLinkServiceIT.Config::class,
        LinkModule::class))
class TraceableLinkServiceIT {

    class Config {

        @Bean
        @Scope("prototype")
        fun logger(injectionPoint: InjectionPoint): Logger = Mockito.mock(Logger::class.java)
    }

    @Autowired
    private lateinit var service: TraceableLinkService

    @Autowired
    private lateinit var repository: TraceableLinkRepository

    @get:Rule
    var softly = JUnitSoftAssertions()

    @Test
    fun `'retrieveTraceableLinks' should retrieve empty list if repository doesn't contain entities`() {
        Assertions.assertThat(service.retrieveTraceableLinks()).isEmpty()
    }

    @Test
    fun `'retrieveByCode' should return null if traceable link with traceableLinkId doesn't exist`() {
        Assertions.assertThat(service.retrieveByCode("invalid")).isNull()
    }

    @Test
    fun `'addTraceableLinkUniqueById' should return created entity`() {
        val (id, code, uri, title, lastAccessed, accessCount) = service.addTraceableLinkUniqueById(CreateTraceableLinkDto("TST-1234", "http://test.de", "title"))
        softly.assertThat(id).isNotNull()
        softly.assertThat(code).isEqualTo("TST-1234")
        softly.assertThat(uri).isEqualTo("http://test.de")
        softly.assertThat(title).isEqualTo("title")
        softly.assertThat(lastAccessed).isNull()
        softly.assertThat(lastAccessed).isNull()
        softly.assertThat(accessCount).isEqualTo(0)
    }

    @Test
    fun `'addTraceableLinkUniqueById' should created entity only once ignoring modified values`() {
        service.addTraceableLinkUniqueById(CreateTraceableLinkDto("TST-1234", "http://test.de", "title"))
        service.addTraceableLinkUniqueById(CreateTraceableLinkDto("TST-1234", "http://test2.de", "title2"))

        val allTraceableLinks = service.retrieveTraceableLinks()
        softly.assertThat(allTraceableLinks.size).isEqualTo(1)
        softly.assertThat(allTraceableLinks[0].code).isEqualTo("TST-1234")
        softly.assertThat(allTraceableLinks[0].title).isEqualTo("title")
        softly.assertThat(allTraceableLinks[0].uri).isEqualTo("http://test.de")
    }

    @Test
    fun `'updateTraceableLink' should not update ignored fields`() {
        val createdTraceableLink = repository.save(TraceableLink(code = "TST-1234", uri = "http://test.de", title = "title"))

        val expectedLastAccessed = LocalDateTime.now()
        val updatedLink = service.updateTraceableLink(
                createdTraceableLink.id!!,
                UpdateTraceableLinkDto(
                        lastAccessed = UpdateField.of(expectedLastAccessed)
                )
        )

        softly.assertThat(updatedLink).isNotNull
        softly.assertThat(updatedLink?.code).isEqualTo("TST-1234")
        softly.assertThat(updatedLink?.title).isEqualTo("title")
        softly.assertThat(updatedLink?.uri).isEqualTo("http://test.de")
        softly.assertThat(updatedLink?.lastAccessed).isEqualTo(expectedLastAccessed)
        softly.assertThat(updatedLink?.accessCount).isEqualTo(0)
    }

    @Test
    fun `'updateTraceableLink' should update all fields`() {
        val createdTraceableLink = repository.save(TraceableLink(code = "TST-1234", uri = "http://test.de", title = "title"))

        val expectedLastAccessed = LocalDateTime.now()
        val updatedLink = service.updateTraceableLink(
                createdTraceableLink.id!!,
                UpdateTraceableLinkDto(
                        code = UpdateField.of("TST-4321"),
                        title = UpdateField.of("title2"),
                        lastAccessed = UpdateField.of(expectedLastAccessed),
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
    fun `'updateTraceableLink' should update fields to null`() {
        val createdTraceableLink = repository.save(TraceableLink(code = "TST-1234", uri = "http://test.de", title = "title", lastAccessed = LocalDateTime.now()))

        val updatedLink = service.updateTraceableLink(
                createdTraceableLink.id!!,
                UpdateTraceableLinkDto(
                        title = UpdateField.setNull(),
                        lastAccessed = UpdateField.setNull())
        )

        softly.assertThat(updatedLink).isNotNull
        softly.assertThat(updatedLink?.code).isEqualTo("TST-1234")
        softly.assertThat(updatedLink?.title).isNull()
        softly.assertThat(updatedLink?.lastAccessed).isNull()
        softly.assertThat(updatedLink?.accessCount).isEqualTo(0)
    }

    @Test
    fun `'delete' should delete entity`() {
        val createdTraceableLink = repository.save(TraceableLink(code = "TST-1234", uri = "http://test.de"))

        val createdEntityId = createdTraceableLink.id!!
        service.deleteById(createdEntityId)

        Assertions.assertThat(repository.exists(createdEntityId)).isFalse()
    }

    @Test
    fun `should record access`() {
        val createdTraceableLink = repository.save(TraceableLink(code = "TST-1234", uri = "http://test.de", title = "title"))

        val createdEntityId = createdTraceableLink.id!!
        val (_, code, uri, title, lastAccessed, accessCount) = service.recordLinkAccess(createdEntityId)

        softly.assertThat(code).isEqualTo("TST-1234")
        softly.assertThat(uri).isEqualTo("http://test.de")
        softly.assertThat(title).isEqualTo("title")
        softly.assertThat(lastAccessed).isNotNull()
        softly.assertThat(accessCount).isEqualTo(1)
    }

    @Test
    fun `should add traceable link when promoting unknown traceable link`() {
        service.addAndPromoteTraceableLinkUniqueById(CreateTraceableLinkDto("TST-1234", "http://test.de"))

        Assertions.assertThat(service.retrieveByCode("TST-1234")).isNotNull()
    }

    @Test
    fun `should not add traceable link again when promoting known traceable link`() {
        service.addTraceableLinkUniqueById(CreateTraceableLinkDto(code = "TST-1234", uri = "http://test.de"))
        service.promoteTraceableLinkByCode("TST-1234")

        Assertions.assertThat(service.retrieveTraceableLinks().size).isEqualTo(1)
    }

    @Test
    fun `should retrieve all traceable links according to relevance`() {
        service.addAndPromoteTraceableLinkUniqueById(CreateTraceableLinkDto("TST-1234", "http://test.de"))
        service.addAndPromoteTraceableLinkUniqueById(CreateTraceableLinkDto("TST-5678", "http://test.de"))
        service.addAndPromoteTraceableLinkUniqueById(CreateTraceableLinkDto("TST-9012", "http://test.de"))
        service.addAndPromoteTraceableLinkUniqueById(CreateTraceableLinkDto("TST-1234", "http://test.de"))

        val allTraceableLinks = service.retrieveListByRelevanceWithLimit()
        softly.assertThat(allTraceableLinks.size).isEqualTo(3)
        softly.assertThat(allTraceableLinks[0].code).isEqualTo("TST-1234")
        softly.assertThat(allTraceableLinks[1].code).isEqualTo("TST-9012")
        softly.assertThat(allTraceableLinks[2].code).isEqualTo("TST-5678")
    }

    @Test
    fun `'retrieveListByRelevanceWithLimit' should limit number of results by default`() {
        service.addAndPromoteTraceableLinkUniqueById(CreateTraceableLinkDto("TST-1", "http://test.de"))
        service.addAndPromoteTraceableLinkUniqueById(CreateTraceableLinkDto("TST-2", "http://test.de"))
        service.addAndPromoteTraceableLinkUniqueById(CreateTraceableLinkDto("TST-3", "http://test.de"))
        service.addAndPromoteTraceableLinkUniqueById(CreateTraceableLinkDto("TST-4", "http://test.de"))
        service.addAndPromoteTraceableLinkUniqueById(CreateTraceableLinkDto("TST-5", "http://test.de"))
        service.addAndPromoteTraceableLinkUniqueById(CreateTraceableLinkDto("TST-6", "http://test.de"))

        val relevantLinks = service.retrieveListByRelevanceWithLimit()
        softly.assertThat(relevantLinks.size).isEqualTo(5)
        softly.assertThat(relevantLinks[0].code).isEqualTo("TST-6")
        softly.assertThat(relevantLinks[1].code).isEqualTo("TST-5")
        softly.assertThat(relevantLinks[2].code).isEqualTo("TST-4")
        softly.assertThat(relevantLinks[3].code).isEqualTo("TST-3")
        softly.assertThat(relevantLinks[4].code).isEqualTo("TST-2")
    }
}
