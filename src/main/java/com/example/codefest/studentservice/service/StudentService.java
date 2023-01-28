package com.example.codefest.studentservice.service;

import com.example.codefest.studentservice.model.Department;
import com.example.codefest.studentservice.model.Student;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;

import java.util.List;

public interface StudentService {

    Flux<Department> getDepartments();

    Student saveStudent(Student student);

    List<Student> fetchStudentList(Long departmentId);

    Student updateStudent(Student student,Long studentId);

    void deleteStudentById(Long studentId);

    Student getStudentDetails(Long id);
}
