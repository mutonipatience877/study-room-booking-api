package com.studyroom.api.controller;

import com.studyroom.api.model.StudyRoom;
import com.studyroom.api.service.StudyRoomService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/study-rooms")
public class StudyRoomController {

    private final StudyRoomService studyRoomService;

    public StudyRoomController(StudyRoomService studyRoomService) {
        this.studyRoomService = studyRoomService;
    }

    @GetMapping
    public List<StudyRoom> getAll() {
        return studyRoomService.findAll();
    }

    @GetMapping("/{id}")
    public StudyRoom getById(@PathVariable Long id) {
        return studyRoomService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StudyRoom create(@Valid @RequestBody StudyRoom room) {
        return studyRoomService.create(room);
    }

    @PutMapping("/{id}")
    public StudyRoom update(@PathVariable Long id, @Valid @RequestBody StudyRoom room) {
        return studyRoomService.update(id, room);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        studyRoomService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
