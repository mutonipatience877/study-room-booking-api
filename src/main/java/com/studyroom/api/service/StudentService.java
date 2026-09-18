package com.studyroom.api.service;

import com.studyroom.api.exception.BusinessRuleException;
import com.studyroom.api.exception.ResourceNotFoundException;
import com.studyroom.api.model.Student;
import com.studyroom.api.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    public Student findById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id " + id));
    }

    // Business rule: student number must be unique.
    public Student create(Student student) {
        if (studentRepository.existsByStudentNumber(student.getStudentNumber())) {
            throw new BusinessRuleException("A student with number " + student.getStudentNumber() + " already exists");
        }
        student.setId(null);
        return studentRepository.save(student);
    }

    public Student update(Long id, Student updated) {
        Student existing = findById(id);
        existing.setFullName(updated.getFullName());
        existing.setEmail(updated.getEmail());
        existing.setStudentNumber(updated.getStudentNumber());
        existing.setPhone(updated.getPhone());
        return studentRepository.save(existing);
    }

    public void delete(Long id) {
        Student existing = findById(id);
        studentRepository.delete(existing);
    }
}
