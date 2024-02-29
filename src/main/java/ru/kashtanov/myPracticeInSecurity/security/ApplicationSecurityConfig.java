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
               .csrf().disable()  //!!!IMPORTANT to know
               .authorizeRequests()// настраивает значение авторизации для URL, например, если он требует аутентификации
                                   // или только определенные роли могут получить к нему доступ и т.д.
               .antMatchers("/","index","/css/*","/js/*")// настраивает, какой URL будет обрабатываться этим
               .permitAll()
               // ORDER IS REALLY MATTER cause !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

               /**antMatcher() is a method of HttpSecurity, it doesn't have anything to do with authorizeRequests().
                * Basically, http.antMatcher() tells Spring to only configure HttpSecurity if the path matches this pattern.
                The authorizeRequests().antMatchers() is then used to apply authorization to one or more paths you specify
                in antMatchers(). Such as permitAll() or hasRole('USER3'). These only get applied if the
                first http.antMatcher() is matched.*/

                // how to look through these rows below ? it is easy just ask each one while you're reading!!
               .antMatchers("/api/**").hasRole(UserRoles.STUDENT.name())
               .antMatchers(HttpMethod.DELETE,"/management/api/**").hasAuthority(COURSE_WRITE.getPermission())
               .antMatchers(HttpMethod.POST,"/management/api/**").hasAuthority(COURSE_WRITE.getPermission())
               .antMatchers(HttpMethod.PUT,"/management/api/**").hasAuthority(COURSE_WRITE.getPermission())
               .antMatchers(HttpMethod.GET,"/management/api/**").hasAnyRole(ADMIN.name(),TRAINEE.name())
               .anyRequest()
               .authenticated()
               .and()
               .formLogin();
               //.httpBasic(); for basic authentication
    }

    /**UserDetails предоставляет необходимую информацию для построения объекта Authentication из DAO объектов
     * приложения или других источников данных системы безопасности. Объект UserDetailsсодержит имя пользователя,
     * пароль, флаги: isAccountNonExpired, isAccountNonLocked, isCredentialsNonExpired, isEnabled и Collection — прав
     * (ролей) пользователя.*/

    // UserDetailsService, используется чтобы создать UserDetails объект
    // путем реализации единственного метода этого интерфейса
    // Позволяет получить из источника данных объект пользователя и сформировать из него объект UserDetails
    // который будет использоваться контекстом Spring Security.

    @Bean
    @Override
    protected UserDetailsService userDetailsService() {
        UserDetails jackyUser = User.builder()
                .username("jacky")
                .password(passwordEncoder.encode("jacky"))
               // .roles(UserRoles.STUDENT.name())  // ROLE_STUDENT
                .authorities(STUDENT.getGrantedAuthorities())
                .build();

        UserDetails adminUser = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("admin"))
               // .roles(UserRoles.ADMIN.name()) //ROLE_ADMIN
                .authorities(ADMIN.getGrantedAuthorities())
                .build();

        UserDetails tomUser = User.builder()
                .username("tom")
                .password(passwordEncoder.encode("tom"))
              //  .roles(UserRoles.TRAINEE.name()) //ROLE_TRAINEE
                .authorities(TRAINEE.getGrantedAuthorities())
                .build();

        return new InMemoryUserDetailsManager(
                jackyUser,
                tomUser,
                adminUser
        );
    }
}
