package ru.kashtanov.myPracticeInSecurity.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import ru.kashtanov.myPracticeInSecurity.auth.ApplicationUserService;

import java.util.concurrent.TimeUnit;

import static ru.kashtanov.myPracticeInSecurity.security.ApplicationUserPermission.COURSE_WRITE;
import static ru.kashtanov.myPracticeInSecurity.security.UserRoles.*;


@Configuration
@EnableWebSecurity
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {
    private final PasswordEncoder passwordEncoder;
    private final ApplicationUserService applicationUserService;
    @Autowired
    public ApplicationSecurityConfig(PasswordEncoder passwordEncoder,
                                     ApplicationUserService applicationUserService) {
        this.passwordEncoder = passwordEncoder;
        this.applicationUserService = applicationUserService;
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
               .formLogin()
                   .loginPage("/login")
                   .permitAll()// !!! don't forget to add 'permitAll' when you're shaping a login or any page
                   .defaultSuccessUrl("/courses",true)  // here we set default URL address after a successful entry through the login page
                   .passwordParameter("password")                             // true forces 'redirect' to courses page
                   .usernameParameter("username") // it makes a program more secured
               .and()
               .rememberMe()  // set time for TWO weeks !! cookie contains of  -username expiration time md5 hash of these values
                   .tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(21))
                   .key("somethingVerySecured") // Here we extend our session from 2 weeks to 21 days
               .and()
               .logout()
                   .logoutUrl("/logout")
                   .logoutRequestMatcher(new AntPathRequestMatcher("/logout","GET")) // because we use .csrf().disable() and GET method
                   .clearAuthentication(true)
                   .invalidateHttpSession(true) // annul or void cookies
                   .deleteCookies("JSESSIONID","remember-me") // i took it from 'browser-> inspect'
                   .logoutSuccessUrl("/login");
        // .httpBasic();// for basic authentication
    }



    /**UserDetails предоставляет необходимую информацию для построения объекта Authentication из DAO объектов
     * приложения или других источников данных системы безопасности. Объект UserDetailsсодержит имя пользователя,
     * пароль, флаги: isAccountNonExpired, isAccountNonLocked, isCredentialsNonExpired, isEnabled и Collection — прав
     * (ролей) пользователя.*/

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(applicationUserService);
        return provider;
    }






    // UserDetailsService, используется чтобы создать UserDetails объект
    // путем реализации единственного метода этого интерфейса
    // Позволяет получить из источника данных объект пользователя и сформировать из него объект UserDetails
    // который будет использоваться контекстом Spring Security.
    //UserDetailsService fetches stuff from DataBase

//    @Bean
//    @Override
//    protected UserDetailsService userDetailsService() {
//        UserDetails jackyUser = User.builder()
//                .username("jacky")
//                .password(passwordEncoder.encode("jacky"))
//               // .roles(UserRoles.STUDENT.name())  // ROLE_STUDENT
//                .authorities(STUDENT.getGrantedAuthorities())
//                .build();
//
//        UserDetails adminUser = User.builder()
//                .username("admin")
//                .password(passwordEncoder.encode("admin"))
//               // .roles(UserRoles.ADMIN.name()) //ROLE_ADMIN
//                .authorities(ADMIN.getGrantedAuthorities())
//                .build();
//
//        UserDetails tomUser = User.builder()
//                .username("tom")
//                .password(passwordEncoder.encode("tom"))
//              //  .roles(UserRoles.TRAINEE.name()) //ROLE_TRAINEE
//                .authorities(TRAINEE.getGrantedAuthorities())
//                .build();
//    //!!! Be careful since 3 UserDetails are stored in MemoryUserDetailsManager NOT in real Data Base
//
//    // поддерживает запросы информации о паролях пользователя, хранящейся в памяти, а также обеспечивает управление
//    // UserDetails кучей // also it is used instead of real database
//        return new InMemoryUserDetailsManager(
//                jackyUser,
//                tomUser,
//                adminUser
//        );
//    }


}
