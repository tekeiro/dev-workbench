package de.roamingthings.devworkbench.config

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ComponentScan(basePackageClasses = arrayOf(ConfigModule::class))
class ConfigModule
