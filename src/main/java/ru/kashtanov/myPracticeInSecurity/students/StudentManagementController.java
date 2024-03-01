package ru.kashtanov.myPracticeInSecurity.students;

import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("management/api/v1/students")
public class StudentManagementController {
    private static final List<Student> STUDENTS = Arrays.asList(
            new Student(1,"Jacky Chan"),
            new Student(2,"John Cena"),
            new Student(3,"Mike Bro")
    );

    @GetMapping
    public List<Student>getAllStudents(){
        System.out.println("getAllStudents");
        return STUDENTS;
    }

    @PostMapping
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
