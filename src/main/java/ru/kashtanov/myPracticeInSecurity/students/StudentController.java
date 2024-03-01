package ru.kashtanov.myPracticeInSecurity.students;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("api/v1/students")
public class StudentController {
    private static final List<Student> STUDENTS = Arrays.asList(
            new Student(1,"Jacky Chan"),
            new Student(2,"John Ceana"),
            new Student(3,"Mama Maria")
    );
    @GetMapping(path="/{studentId}") //                  that studentId  is
    public Student getStudent(@PathVariable("studentId") Integer student_Id){  //this studentId
        return STUDENTS.stream()
                .filter(student -> student_Id.equals(student.getStudentId())) // returns student obj from a list with such id
                .findFirst()
                .orElseThrow(()-> new IllegalStateException("student with id= "+ student_Id+"does not exist"));
    }
}
