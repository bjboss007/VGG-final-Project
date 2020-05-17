package com.vgg.fvp.common.security;

import com.vgg.fvp.common.data.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private UserPrincipalDetailsService userPrincipalDetailsService;
    private UserRepository repository;



    public SecurityConfiguration(UserPrincipalDetailsService userPrincipalDetailsService, UserRepository repository) {
        this.userPrincipalDetailsService = userPrincipalDetailsService;
        this.repository = repository;
    }
    @Override
    protected void configure(AuthenticationManagerBuilder auth){
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(new JwtAuthenticationFilter(authenticationManager()))
                .addFilter(new JwtAuthorizationFilter(authenticationManager(), this.repository))
                .authorizeRequests()
                .antMatchers("/api/fvp/vendors/**").permitAll()
                .antMatchers("/api/fvp/v1/vendors/**").permitAll()
                .antMatchers("/api/fvp/customers/**").permitAll()
//                .antMatchers("/api/fvp/v1/vendors/**").hasRole("VENDOR")
                .antMatchers("/api/fvp/v1/customers/**").hasRole("CUSTOMER")
                .anyRequest().authenticated();
    }


    @Bean
    DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userPrincipalDetailsService);
        return daoAuthenticationProvider;
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    public DefaultWebSecurityExpressionHandler defaultWebSecurityExpressionHandler() {
//        DefaultWebSecurityExpressionHandler defaultWebSecurityExpressionHandler = new DefaultWebSecurityExpressionHandler();
//        defaultWebSecurityExpressionHandler.setDefaultRolePrefix("");
//        return defaultWebSecurityExpressionHandler;
//    }
}
