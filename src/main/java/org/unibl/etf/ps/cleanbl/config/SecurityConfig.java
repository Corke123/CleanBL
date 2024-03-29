package org.unibl.etf.ps.cleanbl.config;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.unibl.etf.ps.cleanbl.security.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Override
    public void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.cors()
                .and()
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/api/v1/auth/**")
                .permitAll()
                .antMatchers("/v2/api-docs",
                        "/swagger-resources/**",
                        "/swagger-ui/**",
                        "/webjars/**",
                        "favicon.ico")
                .permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/reports/**")
                .permitAll()
                .antMatchers(HttpMethod.POST, "/api/v1/reports/")
                .hasAuthority("Report_Create")
                .antMatchers(HttpMethod.DELETE, "/api/v1/reports/")
                .hasAnyAuthority("Report_Delete", "Role_DepartmentOfficer")
                .antMatchers(HttpMethod.PUT, "/api/v1/reports/")
                .hasAnyAuthority("Report_Update", "Role_DepartmentOfficer")
                .antMatchers(HttpMethod.GET, "/api/v1/departments")
                .permitAll()
                .antMatchers(HttpMethod.POST, "/api/v1/contact-us-messages")
                .permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/report-statuses")
                .permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/statistics/**")
                .permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/department-services/names")
                .permitAll()
                .anyRequest()
                .authenticated();
        httpSecurity.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }
}
