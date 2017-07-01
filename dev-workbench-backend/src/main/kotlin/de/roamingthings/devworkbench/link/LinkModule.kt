package de.roamingthings.devworkbench.link

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ComponentScan(basePackageClasses = arrayOf(InternalLinkModuleConfig::class))
class LinkModule
