package de.roamingthings.devworkbench.link.repository

import de.roamingthings.devworkbench.link.domain.Link
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
internal interface LinkRepository : JpaRepository<Link, Long> {
    fun findAllByCategoryIdOrderByLastAccessedDesc(categoryId: Long, pageable: Pageable): List<Link>

    fun findAllByCategoryId(categoryId: Long): List<Link>

/*
    fun findOneByCode(code: String): Link?

    fun existsByCode(ticketId: String): Boolean

    fun findAllByCategoryCode(categoryCode: String): List<Link>

    fun findRelevantByCategoryCode(categoryCode: String): List<Link>
*/
}
