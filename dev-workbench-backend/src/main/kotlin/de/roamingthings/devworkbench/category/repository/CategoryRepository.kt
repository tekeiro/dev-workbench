package de.roamingthings.devworkbench.category.repository

import de.roamingthings.devworkbench.category.domain.Category
import de.roamingthings.devworkbench.link.api.CategoryDto
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import javax.transaction.Transactional

/**
 *
 *
 * @author Alexander Sparkowsky [info@roamingthings.de]
 * @version 2017/07/08
 */
@Repository
@Transactional(Transactional.TxType.MANDATORY)
internal interface CategoryRepository : JpaRepository<Category, Long> {
    fun findOneByCode(code: String): Category?
}