package com.studyroom.api.service;

import com.studyroom.api.exception.BusinessRuleException;
import com.studyroom.api.exception.ResourceNotFoundException;
import com.studyroom.api.model.StudyRoom;
import com.studyroom.api.repository.StudyRoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudyRoomService {

    private final StudyRoomRepository studyRoomRepository;

    public StudyRoomService(StudyRoomRepository studyRoomRepository) {
        this.studyRoomRepository = studyRoomRepository;
    }

    public List<StudyRoom> findAll() {
        return studyRoomRepository.findAll();
    }

    public StudyRoom findById(Long id) {
        return studyRoomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Study room not found with id " + id));
    }

    // Business rule: room number must be unique across the building.
    public StudyRoom create(StudyRoom room) {
        studyRoomRepository.findByRoomNumber(room.getRoomNumber()).ifPresent(existing -> {
            throw new BusinessRuleException("A study room with number " + room.getRoomNumber() + " already exists");
        });
        room.setId(null);
        return studyRoomRepository.save(room);
    }

    public StudyRoom update(Long id, StudyRoom updated) {
        StudyRoom existing = findById(id);
        existing.setRoomNumber(updated.getRoomNumber());
        existing.setBuilding(updated.getBuilding());
        existing.setFloor(updated.getFloor());
        existing.setCapacity(updated.getCapacity());
        existing.setHasProjector(updated.isHasProjector());
        existing.setHasWhiteboard(updated.isHasWhiteboard());
        existing.setStatus(updated.getStatus());
        return studyRoomRepository.save(existing);
    }

    public void delete(Long id) {
        StudyRoom existing = findById(id);
        studyRoomRepository.delete(existing);
    }
}
