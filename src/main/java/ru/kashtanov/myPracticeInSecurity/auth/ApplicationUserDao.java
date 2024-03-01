package ru.kashtanov.myPracticeInSecurity.auth;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Optional;

public interface ApplicationUserDao {

    Optional<ApplicationUser>selectApplicationUserByUserName(String username);
}
