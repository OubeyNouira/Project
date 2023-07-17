package com.example.prototype.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class WebSecurity{
    @Bean
    public InMemoryUserDetailsManager userDetailsManager(){
        UserDetails user = User.withDefaultPasswordEncoder()
                .username("rieb")
                .password("password1")
                .roles("USER")
                .build();
        UserDetails admin = User.withDefaultPasswordEncoder()
                .username("oubey")
                .password("password2")
                .roles("ADMIN")
                .build();
        return new  InMemoryUserDetailsManager(user,admin);
    }
    @Bean
    public SecurityFilterChain configure(HttpSecurity http)throws Exception{
        return http
                .csrf(csrf ->csrf.disable() )
                .authorizeRequests(auth->{
                    auth.requestMatchers("/personnes").permitAll();
                    auth.requestMatchers("/personnes/employee").hasRole("USER");
                    auth.requestMatchers("/personnes/chefpole").hasRole("ADMIN");
                })
                .httpBasic(Customizer.withDefaults())
                .build();

    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }}

