package ru.kashtanov.myPracticeInSecurity.auth;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static ru.kashtanov.myPracticeInSecurity.security.UserRoles.*;

@Repository("some")
public class SomeApplicationUserDaoService implements ApplicationUserDao{

    private final PasswordEncoder passwordEncoder;

    public SomeApplicationUserDaoService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<ApplicationUser> selectApplicationUserByUserName(String username) {
        return getApplicationUsers()
                .stream()
                .filter(applicationUser -> username.equals(applicationUser.getUsername()))
                .findFirst();
    }
    private List<ApplicationUser> getApplicationUsers(){
        List<ApplicationUser> applicationUsers= Lists.newArrayList(
                new ApplicationUser(STUDENT.getGrantedAuthorities(),
                        passwordEncoder.encode("jacky"),
                        "jacky",
                        true,
                        true,
                        true,
                        true),

                new ApplicationUser(ADMIN.getGrantedAuthorities(),
                        passwordEncoder.encode("admin"),
                        "admin",
                        true,
                        true,
                        true,
                        true),

                new ApplicationUser(TRAINEE.getGrantedAuthorities(),
                        passwordEncoder.encode("tom"),
                        "tom",
                        true,
                        true,
                        true,
                        true)
        );
        return applicationUsers;
    }
}
