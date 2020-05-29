package com.vgg.fvp.common.security;

import com.vgg.fvp.common.data.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
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
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/v2/api-docs",
                "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/security",
                "/swagger-ui.html",
                "/webjars/**");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth){
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    @Bean
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors().and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(new JwtAuthorizationFilter(authenticationManager(), this.repository))
                .authorizeRequests()
                .antMatchers("/api/fvp/v1/auth","/api/fvp/v1/all-users","/"
                        "/api/fvp/v1/customers/**/set-password",
                        "/api/fvp/v1/vendors/**/set-password",
                        "/api/fvp/v1/customers/register",
                        "/api/fvp/v1/vendors/register",
                        "/api/fvp/v1/notifications/**").permitAll()

                .antMatchers(HttpMethod.GET,"/api/fvp/v1/customers/**", "/api/fvp/v1/vendors/**").permitAll()

                .antMatchers(HttpMethod.POST,"/api/fvp/v1/customers/**").hasRole("CUSTOMER")
                .antMatchers(HttpMethod.DELETE,"/api/fvp/v1/customers/**").hasRole("CUSTOMER")
                .antMatchers(HttpMethod.PUT,"/api/fvp/v1/customers/**").hasRole("CUSTOMER")

                .antMatchers(HttpMethod.POST,"/api/fvp/v1/vendors/**").hasRole("VENDOR")
                .antMatchers(HttpMethod.DELETE,"/api/fvp/v1/vendors/**").hasRole("VENDOR")
                .antMatchers(HttpMethod.PUT,"/api/fvp/v1/vendors/**").hasRole("VENDOR")

                .antMatchers("/api/fvp/v1/sendmail").hasAnyRole("VENDOR","CUSTOMER")

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
