package com.example.codefest.studentservice;

import com.example.codefest.studentservice.model.ResponseCode;
import com.example.codefest.studentservice.model.Student;
import com.example.codefest.studentservice.service.StudentService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.autoconfigure.RefreshAutoConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest
@ImportAutoConfiguration(RefreshAutoConfiguration.class)
public class StudentControllerTest {
    @Test
    void contextLoads() {
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private StudentService studentService;

    @Test
    public void givenStudentObject_whenCreateStudent_thenReturnSavedStudent() throws Exception {
        Student student = new Student(100L, "Laura", "John", "Laura@cc.com",101L);
        given(studentService.saveStudent(any(Student.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));

        ResultActions response = mockMvc.perform(post("/api/v1/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(student)));

        JsonNode node = objectMapper.readTree(response.andReturn().getResponse().getContentAsString());
        Assert.assertEquals(Optional.ofNullable(ResponseCode.SUCCESS.getCode()),Optional.ofNullable(node.get("status").asInt()));

    }

    @Test
    public void givenListOfStudents_whenGetAllStudents_thenReturnStudentList() throws Exception {
        // given - precondition or setup
        List<Student> students = new ArrayList<>();
        students.add(new Student(100L, "Laura", "John", "Laura@cc.com",101L));
        students.add(new Student(101L, "John", "Alex", "alex@cc.com",101L));
        given(studentService.fetchStudentList(101L)).willReturn(students);

        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(get("/api/v1/students/101"));

        JsonNode node = objectMapper.readTree(response.andReturn().getResponse().getContentAsString());
        Assert.assertEquals(Optional.ofNullable(ResponseCode.SUCCESS.getCode()),Optional.ofNullable(node.get("status").asInt()));
        Assert.assertEquals(2,node.get("data").size());

    }


    @Test
    public void givenStudentId_whenGetStudentById_thenReturnStudentObject() throws Exception {
        // given - precondition or setup
        long studentId = 101L;
        Student student = new Student(100L, "Laura", "John", "Laura@cc.com",101L);
        given(studentService.getStudentDetails(studentId)).willReturn(Optional.of(student).get());

        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(get("/api/v1/students/detailsfor/{id}", studentId));
        JsonNode node = objectMapper.readTree(response.andReturn().getResponse().getContentAsString());
        Assert.assertEquals(Optional.ofNullable(ResponseCode.SUCCESS.getCode()),Optional.ofNullable(node.get("status").asInt()));
        Assert.assertEquals(100L,node.get("data").get("studentId").asInt());
    }

    @Test
    public void givenInvalidStudentId_whenGetStudentById_thenReturnEmpty() throws Exception {
        // given - precondition or setup
        long studentId = 1L;
        Student student = new Student(100L, "Laura", "John", "Laura@cc.com",101L);

        Student findStu = null;
        given(studentService.getStudentDetails(studentId)).willReturn(findStu);

        // when -  action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(get("/api/students/detailsfor/{id}", studentId));
        JsonNode node = objectMapper.readTree(response.andReturn().getResponse().getContentAsString());
        Assert.assertEquals(ResponseCode.NOT_FOUND.getCode(),
                node.isEmpty() ? ResponseCode.NOT_FOUND.getCode() : ResponseCode.SUCCESS.getCode());


    }

    @Test
    public void givenUpdatedStudent_whenUpdateStudent_thenReturnUpdateStudentObject() throws Exception {

        long studentId = 101L;
        Student updatedStudent = new Student(100L, "Laura", "John", "laura.john@gmail.com",101L);
        given(studentService.getStudentDetails(studentId)).willReturn(updatedStudent);
        ResultActions response = mockMvc.perform(put("/api/v1/students/{id}", studentId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedStudent)));
        JsonNode node = objectMapper.readTree(response.andReturn().getResponse().getContentAsString());

        Assert.assertEquals(Optional.ofNullable(ResponseCode.SUCCESS.getCode()),Optional.ofNullable(node.get("status").asInt()));
    }

    @Test
    public void givenStudentId_whenDeleteStudent_thenReturn200() throws Exception {
        long studentId = 1L;
        willDoNothing().given(studentService).deleteStudentById(studentId);
        ResultActions response = mockMvc.perform(delete("/api/v1/students/{id}", studentId));

        Assert.assertEquals("Deleted Successfully",response.andReturn().getResponse().getContentAsString());
    }
}
