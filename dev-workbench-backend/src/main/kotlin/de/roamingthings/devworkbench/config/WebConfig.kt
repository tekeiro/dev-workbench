package de.roamingthings.devworkbench.config

/**
 *
 *
 * @author Alexander Sparkowsky [info@roamingthings.de]
 * @version 2017/07/01
 */

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter

@Configuration
//@EnableWebMvc
class WebConfig : WebMvcConfigurerAdapter() {

    override fun configurePathMatch(configurer: PathMatchConfigurer?) {
        super.configurePathMatch(configurer)
        configurer!!.isUseRegisteredSuffixPatternMatch = false
        configurer.isUseSuffixPatternMatch = false
    }

    override fun addResourceHandlers(registry: ResourceHandlerRegistry?) {
        super.addResourceHandlers(registry)
        registry!!.addResourceHandler("/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/dev-workbench-frontend/0.0.1/")
    }

}