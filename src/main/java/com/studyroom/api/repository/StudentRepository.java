package com.studyroom.api.repository;

import com.studyroom.api.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByEmail(String email);
    boolean existsByStudentNumber(String studentNumber);
}
