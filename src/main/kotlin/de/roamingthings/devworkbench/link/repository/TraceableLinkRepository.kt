package de.roamingthings.devworkbench.link.repository

import de.roamingthings.devworkbench.link.domain.TraceableLink
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import javax.transaction.Transactional

/**
 *
 *
 * @author Alexander Sparkowsky [info@roamingthings.de]
 * @version 2017/06/30
 */
@Repository
@Transactional(Transactional.TxType.MANDATORY)
internal interface TraceableLinkRepository : JpaRepository<TraceableLink, Long> {
    fun findOneByCode(code: String): TraceableLink?

    fun findAllByOrderByLastAccessedDesc(pageable: Pageable): List<TraceableLink>

    fun existsByCode(ticketId: String): Boolean
}
