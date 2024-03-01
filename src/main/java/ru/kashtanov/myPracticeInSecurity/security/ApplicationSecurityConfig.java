package ru.kashtanov.myPracticeInSecurity.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


import static ru.kashtanov.myPracticeInSecurity.security.ApplicationUserPermission.COURSE_WRITE;
import static ru.kashtanov.myPracticeInSecurity.security.UserRoles.*;


@Configuration
@EnableWebSecurity
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ApplicationSecurityConfig(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
       http
               .csrf().disable()
               .authorizeRequests()
               .antMatchers("/","index","/css/*","/js/*")
               .permitAll()
               .antMatchers("/api/**").hasRole(UserRoles.STUDENT.name())
               .antMatchers(HttpMethod.DELETE,"/management/api/**").hasAuthority(COURSE_WRITE.getPermission())
               .antMatchers(HttpMethod.POST,"/management/api/**").hasAuthority(COURSE_WRITE.getPermission())
               .antMatchers(HttpMethod.PUT,"/management/api/**").hasAuthority(COURSE_WRITE.getPermission())
               .antMatchers(HttpMethod.GET,"/management/api/**").hasAnyRole(ADMIN.name(),TRAINEE.name())
               .anyRequest()
               .authenticated()
               .and()
               .formLogin()
                   .loginPage("/login")
                   .permitAll()
                   .defaultSuccessUrl("/info",true)
                   .passwordParameter("password")
                   .usernameParameter("username")
               .and()
               .logout()
                   .logoutUrl("/logout")
                   .logoutRequestMatcher(new AntPathRequestMatcher("/logout","GET"))
                   .clearAuthentication(true)
                   .invalidateHttpSession(true)
                   .deleteCookies("JSESSIONID")
                   .logoutSuccessUrl("/login");
        // .httpBasic();// for basic authentication
    }

    @Bean
    @Override
    protected UserDetailsService userDetailsService() {
        UserDetails jackyUser = User.builder()
                .username("student")
                .password(passwordEncoder.encode("student"))
                .authorities(STUDENT.getGrantedAuthorities())
                .build();

        UserDetails adminUser = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("admin"))
                .authorities(ADMIN.getGrantedAuthorities())
                .build();

        UserDetails tomUser = User.builder()
                .username("trainee")
                .password(passwordEncoder.encode("trainee"))
                .authorities(TRAINEE.getGrantedAuthorities())
                .build();

        return new InMemoryUserDetailsManager(
                jackyUser,
                tomUser,
                adminUser
        );
    }


}
