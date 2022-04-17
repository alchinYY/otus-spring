package ru.otus.spring.library.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    public void configure( WebSecurity web ) {
        web.ignoring().antMatchers(  "/static/**" )
                .antMatchers("/swagger-ui.html")
                .antMatchers("/swagger-resources/**");
    }

    @Override
    public void configure( HttpSecurity http ) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                    .antMatchers("/login*").permitAll()
                    .antMatchers( "/**").authenticated()
                .and()
                    .formLogin()
                    .defaultSuccessUrl("/", true)
                .and()
                    .logout()
                    .permitAll();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}