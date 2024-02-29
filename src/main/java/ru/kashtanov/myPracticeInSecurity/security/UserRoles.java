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

    /**GrantedAuthority отражает разрешения выданные пользователю в масштабе всего приложения,
    такие разрешения (как правило называются «роли»), например ROLE_ANONYMOUS, ROLE_USER, ROLE_ADMIN.*/

    public Set<SimpleGrantedAuthority> getGrantedAuthorities(){
       Set<SimpleGrantedAuthority>permissions = getPermissions().stream()
                .map(permission->new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
       permissions.add(new SimpleGrantedAuthority("ROLE_"+this.name())); // this.name - it is a name of the ROLE
        System.out.println(permissions);
       return permissions;
       /** Используются для сохранения какой-то разрешение для пользователя по его ролью.
        * Тут на сроке 39 ты возвращаешь список разрешений пользователя.
        * Допустим у тебя две роли : админ и обычный пользователь (клиент) .
        * У них будут разные разрешения , админ может , например, добавить клиент или удалить его ,
        * а клиент может добавить какую-то запись ... все это зависят от их ролях.
        * При создании клиента сначала spring получает список разрешений пользователя,
        * который отправил запрос и проверяет если он разрешён выполнить эту операцию.
        Ты на строке 44 просто добавил ещё другое разрешение по названию ROLE_ADMIN или ROLE_CLIENT ,
        если у них роли называются ADMIN и CLIENT. Это тоже часть ответа второго вопроса ,
        добавляется не только знак подчёркнутая , но все String ROLE_ , а знак, чтобы разделить эту строку при проверке.
        Прочитав ROLE_ADMIN с суффиксом ROLE , спринг узнает какое роль для текущего пользователя.

        2. Другая часть ты стрим уже все создал и преображал на список, теперь работаешь с списком*/



    }
}



