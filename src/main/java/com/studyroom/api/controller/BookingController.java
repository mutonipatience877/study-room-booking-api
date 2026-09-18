package com.studyroom.api.controller;

import com.studyroom.api.dto.BookingRequest;
import com.studyroom.api.dto.BookingResponse;
import com.studyroom.api.model.Booking;
import com.studyroom.api.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping
    public List<BookingResponse> getAll() {
        return bookingService.findAll().stream().map(BookingResponse::from).toList();
    }

    @GetMapping("/{id}")
    public BookingResponse getById(@PathVariable Long id) {
        return BookingResponse.from(bookingService.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingResponse create(@Valid @RequestBody BookingRequest request) {
        Booking booking = bookingService.create(request);
        return BookingResponse.from(booking);
    }

    @PutMapping("/{id}")
    public BookingResponse update(@PathVariable Long id, @Valid @RequestBody BookingRequest request) {
        Booking booking = bookingService.update(id, request);
        return BookingResponse.from(booking);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        bookingService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
