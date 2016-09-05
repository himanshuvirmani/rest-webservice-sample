package com.sample.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Created by himanshu.virmani on 05/09/16.
 */

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.inMemoryAuthentication().//
                withUser("user").password("user").roles("USER").and().//
                withUser("admin").password("admin").roles("USER", "ADMIN");

    }

    // Though not needed as product repository is also protected for write by admin only.
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.httpBasic().and().authorizeRequests().//
                antMatchers(HttpMethod.POST, "/product").hasRole("ADMIN").//
                antMatchers(HttpMethod.PUT, "/product/**").hasRole("ADMIN").and().//
                csrf().disable();
    }
}
