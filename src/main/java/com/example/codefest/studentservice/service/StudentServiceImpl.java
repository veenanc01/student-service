package com.example.codefest.studentservice.service;

import com.example.codefest.studentservice.model.Department;
import com.example.codefest.studentservice.model.Student;
import com.example.codefest.studentservice.repository.StudentRepository;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class StudentServiceImpl implements StudentService{
    @Autowired
    private WebClient.Builder webClientBuiler;

    @Autowired
    private StudentRepository studentRepository;

    @Override
    public Flux<Department> getDepartments() {
      return (webClientBuiler.build()
              .get()
              .uri("http://DEPARTMENT-SERVICE/api/v1/departments")
              .retrieve()
              .bodyToFlux(Department.class));
    }

    @Override
    public Student saveStudent(Student student) {

       if(null != student.getDepartmentId()) {
           student.setDepartmentId(student.getDepartmentId());
       }
        Mono<Long> departmentMono = (webClientBuiler.build()
                .post()
                .uri("http://DEPARTMENT-SERVICE/api/v1/departments/"+student.getDeptCode())
                .retrieve()
                .bodyToMono(Long.class));
       student.setDepartmentId(departmentMono.block().longValue());
       Student savedStu = (Student) studentRepository.save(student);
       return savedStu;
    }

    @Override
    public List<Student> fetchStudentList(Long departmentId) {
         List<Student> list = (List<Student>) studentRepository.getAllByDepartmentId(departmentId);
        Mono<String> deptCode = (webClientBuiler.build()
                .get()
                .uri("http://DEPARTMENT-SERVICE/api/v1/departments/" + departmentId)
                .retrieve()
                .bodyToMono(String.class));
         list.stream().forEach(s-> {
                     s.setDeptCode(deptCode.block().toString());
                 }
                 );
         return list;
    }

    @Override
    public Student updateStudent(Student student,Long studentId) {

        Student stdDb
                = studentRepository.findById(studentId)
                .get();
        if(StringUtils.isNotEmpty(student.getEmailId())){
            stdDb.setEmailId(student.getEmailId());
        }
        if(StringUtils.isNotEmpty(student.getFirstName())){
            stdDb.setFirstName(student.getFirstName());
        }
        if(StringUtils.isNotEmpty(student.getLastName())){
            stdDb.setLastName(student.getLastName());
        }
        Student savedStu = (Student) studentRepository.save(stdDb);
        return savedStu;
    }

    @Override
    public void deleteStudentById(Long studentId) {
        studentRepository.deleteById(studentId);
    }

    @Override
    public Student getStudentDetails(Long id) {
        return studentRepository.findById(id).get();
    }
}
