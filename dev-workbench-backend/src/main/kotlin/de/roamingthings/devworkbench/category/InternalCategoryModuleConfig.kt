package de.roamingthings.devworkbench.category

import de.roamingthings.devworkbench.category.domain.Category
import de.roamingthings.devworkbench.category.repository.CategoryRepository
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.transaction.annotation.EnableTransactionManagement

/**
 *
 *
 * @author Alexander Sparkowsky [info@roamingthings.de]
 * @version 2017/06/30
 */
@Configuration
@EnableJpaRepositories(basePackageClasses = arrayOf(CategoryRepository::class))
@EntityScan(basePackageClasses = arrayOf(Category::class))
@EnableTransactionManagement
class InternalCategoryModuleConfig {
}