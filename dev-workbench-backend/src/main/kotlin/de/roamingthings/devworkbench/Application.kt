package de.roamingthings.devworkbench

import com.fasterxml.jackson.annotation.JsonInclude.Include
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.InjectionPoint
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Scope

@SpringBootApplication
class Application {

    @Bean
    @Scope("prototype")
    fun logger(injectionPoint: InjectionPoint): Logger = LoggerFactory.getLogger(injectionPoint.methodParameter.containingClass)

    @Bean
    fun objectMapper(): ObjectMapper {
        val mapper = ObjectMapper()
                .registerModule(KotlinModule())
                .registerModule(ParameterNamesModule())
                .registerModule(Jdk8Module())
                .registerModule(JavaTimeModule())
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        mapper.setSerializationInclusion(Include.NON_NULL)
        return mapper
    }
}

fun main(args: Array<String>) {
    SpringApplication.run(Application::class.java, *args)
}
