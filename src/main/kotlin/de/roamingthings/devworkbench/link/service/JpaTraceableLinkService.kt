package de.roamingthings.devworkbench.link.service

import de.roamingthings.devworkbench.link.api.CreateTraceableLinkDto
import de.roamingthings.devworkbench.link.api.TraceableLinkDto
import de.roamingthings.devworkbench.link.api.UpdateTraceableLinkDto
import de.roamingthings.devworkbench.link.domain.TraceableLink
import de.roamingthings.devworkbench.link.repository.TraceableLinkRepository
import org.slf4j.Logger
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import javax.transaction.Transactional

@Service
@Transactional
internal class JpaTraceableLinkService(val traceableLinkRepository: TraceableLinkRepository, val log: Logger) : TraceableLinkService {

    override fun retrieveTraceableLinkById(id: Long): TraceableLinkDto? {
        log.debug("Retrieving traceable link {}", id)

        return traceableLinkRepository.findOne(id)?.toDto()
    }

    override fun retrieveByCode(code: String): TraceableLinkDto? {
        log.debug("Retrieving traceable link: {}", code)

        return traceableLinkRepository.findOneByCode(code)?.toDto()
    }

    override fun retrieveTraceableLinks(): List<TraceableLinkDto> {
        return traceableLinkRepository.findAll().map { it.toDto() }
    }

    override fun addTraceableLinkUniqueById(createRequest: CreateTraceableLinkDto): TraceableLinkDto {
        if (!traceableLinkRepository.existsByCode(createRequest.code)) {
            return traceableLinkRepository.save(TraceableLink.fromDto(createRequest)).toDto()
        } else {
            return traceableLinkRepository.findOneByCode(createRequest.code)!!.toDto()
        }
    }

    override fun updateTraceableLink(id: Long, updateRequest: UpdateTraceableLinkDto): TraceableLinkDto? {
        val currentTraceableLink = traceableLinkRepository.findOne(id)
        return if (currentTraceableLink != null) traceableLinkRepository.save(currentTraceableLink.updateFromDto(updateRequest)).toDto()
        else null
    }

    override fun retrieveListByRelevance(): List<TraceableLinkDto> {
        return traceableLinkRepository.findAllByOrderByLastAccessedDesc().map { it.toDto() }
    }

    override fun deleteById(id: Long) {
        traceableLinkRepository.delete(id)
    }

    override fun recordLinkAccess(id: Long): TraceableLinkDto {
        val currentTraceableLink = traceableLinkRepository.findOne(id)

        val updatedTraceableLink = TraceableLink(
                id = currentTraceableLink.id,
                code = currentTraceableLink.code,
                uri = currentTraceableLink.uri,
                lastAccessed = LocalDateTime.now(),
                accessCount = currentTraceableLink.accessCount + 1)
        return traceableLinkRepository.save(updatedTraceableLink).toDto()
    }

    override fun promoteTraceableLinkByCode(code: String) {
        val traceableLink = traceableLinkRepository.findOneByCode(code)

        if (traceableLink != null) {
            recordLinkAccess(traceableLink.id!!)
        }
    }

    override fun traceableLinkForCodeExists(code: String): Boolean {
        return traceableLinkRepository.existsByCode(code)
    }
}
