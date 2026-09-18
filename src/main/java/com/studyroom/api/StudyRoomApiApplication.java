package com.studyroom.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import com.studyroom.api.model.RoomStatus;
import com.studyroom.api.model.Student;
import com.studyroom.api.model.StudyRoom;
import com.studyroom.api.repository.StudentRepository;
import com.studyroom.api.repository.StudyRoomRepository;

@SpringBootApplication
public class StudyRoomApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudyRoomApiApplication.class, args);
    }

    /** Seeds a few students and rooms on first startup so Postman has data to work with immediately. */
    @Bean
    CommandLineRunner seedData(StudentRepository studentRepository, StudyRoomRepository studyRoomRepository) {
        return args -> {
            if (studentRepository.count() == 0) {
                studentRepository.save(new Student("Alice Uwase", "alice.uwase@example.com", "23001", "0788000001"));
                studentRepository.save(new Student("Brian Niyonzima", "brian.niyonzima@example.com", "23002", "0788000002"));
                studentRepository.save(new Student("Clarisse Mutesi", "clarisse.mutesi@example.com", "23003", "0788000003"));
            }
            if (studyRoomRepository.count() == 0) {
                studyRoomRepository.save(new StudyRoom("A101", "Main Library", 1, 4, true, true, RoomStatus.AVAILABLE));
                studyRoomRepository.save(new StudyRoom("A102", "Main Library", 1, 6, false, true, RoomStatus.AVAILABLE));
                studyRoomRepository.save(new StudyRoom("B201", "Science Block", 2, 10, true, true, RoomStatus.UNDER_MAINTENANCE));
            }
        };
    }
}
