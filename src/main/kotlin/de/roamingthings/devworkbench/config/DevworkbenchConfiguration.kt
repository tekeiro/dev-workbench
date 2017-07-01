package de.roamingthings.devworkbench.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import javax.validation.constraints.NotNull

/**
 *
 *
 * @author Alexander Sparkowsky [info@roamingthings.de]
 * @version 2017/07/01
 */
@Configuration
@ConfigurationProperties(prefix = "devworkbench")
open class WorkbenchConfiguration {
    var optionalProp: String? = null
}

@Configuration
@ConfigurationProperties(prefix = "devworkbench.jira")
open class JiraConfiguration {
    @NotNull lateinit var baseUri: String
}
