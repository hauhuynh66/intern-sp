package com.intern.config;

import com.intern.security.CustomAuthenticationFailureHandler;
import com.intern.security.CustomAuthenticationSuccessHandler;
import com.intern.security.CustomUserDetailService;
import com.intern.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private CustomUserDetailService userDetailService;
    @Autowired
    private Utils utils;
    @Autowired
    private CustomAuthenticationSuccessHandler successHandler;
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/resources/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/manage/**").hasAnyRole("ADMIN","OWNER")
                .antMatchers("/register/**").hasRole("OWNER")
                .anyRequest().permitAll()
                .and()
                .formLogin().loginPage("/login").failureUrl("/login?error").successHandler(successHandler).permitAll()
                .and()
                .exceptionHandling().accessDeniedPage("/forbidden")
                .and()
                .logout().logoutUrl("/logout").logoutSuccessUrl("/login?logout").invalidateHttpSession(true).deleteCookies("JSESSIONID")
                .and()
                .rememberMe().key("uniqueandsecret").userDetailsService(userDetailService)
                .and()
                .csrf().disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailService)
                .passwordEncoder(utils.encoder());
    }
}
