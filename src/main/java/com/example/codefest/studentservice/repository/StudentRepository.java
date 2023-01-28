package com.example.codefest.studentservice.repository;

import com.example.codefest.studentservice.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student,Long> {

    @Query(name = "select s from students s where s.departmentId=:departmentId",nativeQuery = true)
    List<Student> getAllByDepartmentId(@Param("departmentId") Long departmentId);
}
