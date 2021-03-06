package de.roamingthings.devworkbench.link

import de.roamingthings.devworkbench.link.domain.Link
import de.roamingthings.devworkbench.link.repository.LinkRepository
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
@EnableJpaRepositories(basePackageClasses = arrayOf(LinkRepository::class))
@EntityScan(basePackageClasses = arrayOf(Link::class))
@EnableTransactionManagement
class InternalLinkModuleConfig {
}