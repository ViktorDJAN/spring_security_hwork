package ru.kashtanov.myPracticeInSecurity.students;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("management/api/v1/students")
public class StudentManagementController {
    private static final List<Student> STUDENTS = Arrays.asList(
            new Student(1,"Jacky Chan"),
            new Student(2,"John Ceana"),
            new Student(3,"Mama Maria")
    );

    //@PreAuthorize can be used instead of    andMatchers
    // hasRole('ROLE') hasAnyRole('ROLE') hasAuthority('permission) hasAnyAuthority('permission')

    @GetMapping
 //   @PreAuthorize("hasAnyRole('ROLE_ADMIN, ROLE_TRAINEE')") ////    8080:management/api/v1/students
    public List<Student>getAllStudents(){
        System.out.println("getAllStudents");
        return STUDENTS;
    }

    @PostMapping               //   @RequestBody binds params from request to student's fields
    public void registerNewStudent(@RequestBody Student student){
        System.out.println("posting-method");
        System.out.println(student);
    }
    @DeleteMapping(path = "/{studentId}")
    public void deleteStudent(@PathVariable("studentId") Integer studentId){
        System.out.println("deleting-method");
        System.out.println(studentId);
    }
    @PutMapping(path = "/{studentId}")
    public void updateStudent(@PathVariable("studentId") Integer studentId,Student student){
        System.out.println("putting-method");
        System.out.println(studentId + "  UPDATED_STUDENT_IS_HERE  " + student.toString());
    }
}
