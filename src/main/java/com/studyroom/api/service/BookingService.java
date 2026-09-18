package com.studyroom.api.service;

import com.studyroom.api.dto.BookingRequest;
import com.studyroom.api.exception.BusinessRuleException;
import com.studyroom.api.exception.ResourceNotFoundException;
import com.studyroom.api.model.Booking;
import com.studyroom.api.model.RoomStatus;
import com.studyroom.api.model.Student;
import com.studyroom.api.model.StudyRoom;
import com.studyroom.api.repository.BookingRepository;
import com.studyroom.api.repository.StudentRepository;
import com.studyroom.api.repository.StudyRoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final StudentRepository studentRepository;
    private final StudyRoomRepository studyRoomRepository;

    public BookingService(BookingRepository bookingRepository,
                           StudentRepository studentRepository,
                           StudyRoomRepository studyRoomRepository) {
        this.bookingRepository = bookingRepository;
        this.studentRepository = studentRepository;
        this.studyRoomRepository = studyRoomRepository;
    }

    public List<Booking> findAll() {
        return bookingRepository.findAll();
    }

    public Booking findById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id " + id));
    }

    public Booking create(BookingRequest request) {
        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id " + request.getStudentId()));
        StudyRoom room = studyRoomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new ResourceNotFoundException("Study room not found with id " + request.getRoomId()));

        applyBusinessRules(request, room, null);

        Booking booking = new Booking();
        booking.setStudent(student);
        booking.setStudyRoom(room);
        mapRequestToBooking(request, booking);
        return bookingRepository.save(booking);
    }

    public Booking update(Long id, BookingRequest request) {
        Booking booking = findById(id);
        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id " + request.getStudentId()));
        StudyRoom room = studyRoomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new ResourceNotFoundException("Study room not found with id " + request.getRoomId()));

        applyBusinessRules(request, room, id);

        booking.setStudent(student);
        booking.setStudyRoom(room);
        mapRequestToBooking(request, booking);
        return bookingRepository.save(booking);
    }

    public void delete(Long id) {
        Booking booking = findById(id);
        bookingRepository.delete(booking);
    }

    private void applyBusinessRules(BookingRequest request, StudyRoom room, Long excludeBookingId) {
        // Business rule: cannot book a room that is not AVAILABLE.
        if (room.getStatus() != RoomStatus.AVAILABLE) {
            throw new BusinessRuleException("Room " + room.getRoomNumber() + " is not available (" + room.getStatus() + ")");
        }

        // Business rule: end time must be after start time.
        if (!request.getEndTime().isAfter(request.getStartTime())) {
            throw new BusinessRuleException("endTime must be after startTime");
        }

        // Business rule: no double-booking for the same room and overlapping time range.
        boolean hasConflict = !bookingRepository.findOverlapping(
                room.getId(), request.getBookingDate(), request.getStartTime(), request.getEndTime(), excludeBookingId
        ).isEmpty();
        if (hasConflict) {
            throw new BusinessRuleException("Room " + room.getRoomNumber()
                    + " is already booked for an overlapping time slot on " + request.getBookingDate());
        }
    }

    private void mapRequestToBooking(BookingRequest request, Booking booking) {
        booking.setBookingDate(request.getBookingDate());
        booking.setStartTime(request.getStartTime());
        booking.setEndTime(request.getEndTime());
        booking.setPurpose(request.getPurpose());
        booking.setStatus(request.getStatus());
    }
}
