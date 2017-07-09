package de.roamingthings.devworkbench.category

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ComponentScan(basePackageClasses = arrayOf(InternalCategoryModuleConfig::class))
class CategoryModule
