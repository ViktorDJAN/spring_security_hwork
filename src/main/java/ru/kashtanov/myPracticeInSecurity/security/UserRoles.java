package ru.kashtanov.myPracticeInSecurity.security;

import com.google.common.collect.Sets;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

public enum UserRoles {
    STUDENT(Sets.newHashSet()),
    ADMIN(Sets.newHashSet(
            ApplicationUserPermission.COURSE_READ,
            ApplicationUserPermission.COURSE_WRITE,
            ApplicationUserPermission.STUDENT_READ,
            ApplicationUserPermission.STUDENT_WRITE)
            ),
    TRAINEE(Sets.newHashSet(
            ApplicationUserPermission.COURSE_READ,
            ApplicationUserPermission.STUDENT_READ)
    );
    private final Set<ApplicationUserPermission> permissions;

    UserRoles(Set<ApplicationUserPermission> permissions) {
        this.permissions = permissions;
    }

    public Set<ApplicationUserPermission> getPermissions() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getGrantedAuthorities(){
       Set<SimpleGrantedAuthority>permissions = getPermissions().stream()
                .map(permission->new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
       permissions.add(new SimpleGrantedAuthority("ROLE_"+this.name())); // this.name - it is a name of the ROLE
        System.out.println(permissions);
       return permissions;

    }
}



