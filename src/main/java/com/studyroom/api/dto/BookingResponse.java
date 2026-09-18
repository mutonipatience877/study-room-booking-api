package com.studyroom.api.dto;

import com.studyroom.api.model.Booking;
import com.studyroom.api.model.BookingStatus;
import java.time.LocalDate;
import java.time.LocalTime;

public class BookingResponse {

    private Long id;
    private LocalDate bookingDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String purpose;
    private BookingStatus status;
    private Long studentId;
    private String studentName;
    private Long roomId;
    private String roomNumber;

    public static BookingResponse from(Booking booking) {
        BookingResponse response = new BookingResponse();
        response.id = booking.getId();
        response.bookingDate = booking.getBookingDate();
        response.startTime = booking.getStartTime();
        response.endTime = booking.getEndTime();
        response.purpose = booking.getPurpose();
        response.status = booking.getStatus();
        response.studentId = booking.getStudent().getId();
        response.studentName = booking.getStudent().getFullName();
        response.roomId = booking.getStudyRoom().getId();
        response.roomNumber = booking.getStudyRoom().getRoomNumber();
        return response;
    }

    public Long getId() {
        return id;
    }

    public LocalDate getBookingDate() {
        return bookingDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public String getPurpose() {
        return purpose;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public Long getStudentId() {
        return studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public Long getRoomId() {
        return roomId;
    }

    public String getRoomNumber() {
        return roomNumber;
    }
}
