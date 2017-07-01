package de.roamingthings.devworkbench.config

import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter

/**
 *
 *
 * @author Alexander Sparkowsky [info@roamingthings.de]
 * @version 2017/07/01
 */
@Configuration
@EnableWebSecurity
//open class WebSecurityConfig constructor(val userDetailsService : UserDetailsService) : WebSecurityConfigurerAdapter() {
class WebSecurityConfig : WebSecurityConfigurerAdapter() {

/*
    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService(userDetailsService)
    }
*/

    override fun configure(http: HttpSecurity) {
        http
                .authorizeRequests()
                .antMatchers("/assets/**", "/vendor/**", "/webjars/**", "/console/**").permitAll()
                .anyRequest().permitAll()
                .and()
                .csrf().disable()
                .headers().frameOptions().disable()

/*
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .permitAll()
                .and()
                .logout()
                .permitAll()
*/
    }
}