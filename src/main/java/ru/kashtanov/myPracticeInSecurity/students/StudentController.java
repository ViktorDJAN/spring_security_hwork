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
            new Student(2,"John Cena"),
            new Student(3,"Mike Bro")
    );
    @GetMapping(path="/{studentId}")
    public Student getStudent(@PathVariable("studentId") Integer student_Id){
        return STUDENTS.stream()
                .filter(student -> student_Id.equals(student.getStudentId()))
                .findFirst()
                .orElseThrow(()-> new IllegalStateException("student with id= "+ student_Id+"does not exist"));
    }
}
