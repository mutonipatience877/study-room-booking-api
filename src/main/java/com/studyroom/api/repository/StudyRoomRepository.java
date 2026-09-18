package com.studyroom.api.repository;

import com.studyroom.api.model.RoomStatus;
import com.studyroom.api.model.StudyRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudyRoomRepository extends JpaRepository<StudyRoom, Long> {
    Optional<StudyRoom> findByRoomNumber(String roomNumber);
    List<StudyRoom> findByStatus(RoomStatus status);
}
