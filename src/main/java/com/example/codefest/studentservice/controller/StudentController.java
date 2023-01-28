package com.example.codefest.studentservice.controller;

import com.example.codefest.studentservice.model.Department;
import com.example.codefest.studentservice.model.Response;
import com.example.codefest.studentservice.model.ResponseCode;
import com.example.codefest.studentservice.model.Student;
import com.example.codefest.studentservice.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class StudentController {

    @Autowired
    StudentService studentService;

    @GetMapping("/students/detailsfor/{id}")
    public Response fetchStudentById(@PathVariable("id") Long id)
    {
        Response response =null;
        if(null != id) {
            Student student = studentService.getStudentDetails(id);
            if (null != student) {
                response = new Response(ResponseCode.SUCCESS.getCode(),
                        "Student details fetched successfully", student);
            } else {
                response = new Response(ResponseCode.NOT_FOUND.getCode(),
                        "Student details not found", student);
            }
        } else {
            response = new Response(ResponseCode.INTERNAL_ERROR.getCode(),
                    "Provide valid student id", null);
        }
        return response;
    }

    @GetMapping("/students/{deptId}")
    public Response getStudents(@PathVariable("deptId")Long deptId){
        List<Student> list =  studentService.fetchStudentList(deptId);
        Response response =null;
        response =  new Response(ResponseCode.SUCCESS.getCode(), "Student details fetched successfully",list);
        return response;

    }

    @PostMapping("/students")
    public Response saveStudent(
            @RequestBody Student student)
    {
        Response response = null;

        Student student1 = studentService.saveStudent(student);
        response =  new Response(ResponseCode.SUCCESS.getCode(), "Student details saved successfully",student1);
        return response;
    }


    // Update operation
    @PutMapping("/students/{id}")
    public Response
    updateDepartment(@RequestBody Student student,
                     @PathVariable("id") Long studentId)
    {
        Response response =null;
        Student student1 = studentService.updateStudent(
                student, studentId);
        response = new Response(ResponseCode.SUCCESS.getCode(), "Student details updated successfully",student1);
        return response;
    }

    // Delete operation
    @DeleteMapping("/students/{id}")
    public String deleteStudentById(@PathVariable("id")
                                       Long studentId)
    {
        studentService.deleteStudentById(
                studentId);
        return "Deleted Successfully";
    }

  }
