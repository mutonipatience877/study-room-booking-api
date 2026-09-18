package com.studyroom.api.repository;

import com.studyroom.api.model.Booking;
import com.studyroom.api.model.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByStudentId(Long studentId);

    List<Booking> findByStudyRoomId(Long roomId);

    /**
     * Business rule support (no double-booking): finds any booking on the same
     * room and date whose time range overlaps [startTime, endTime), excluding
     * cancelled bookings and (on update) the booking being edited.
     */
    @Query("""
            select b from Booking b
            where b.studyRoom.id = :roomId
              and b.bookingDate = :bookingDate
              and b.status <> com.studyroom.api.model.BookingStatus.CANCELLED
              and (:excludeId is null or b.id <> :excludeId)
              and b.startTime < :endTime
              and b.endTime > :startTime
            """)
    List<Booking> findOverlapping(@Param("roomId") Long roomId,
                                   @Param("bookingDate") LocalDate bookingDate,
                                   @Param("startTime") LocalTime startTime,
                                   @Param("endTime") LocalTime endTime,
                                   @Param("excludeId") Long excludeId);

    List<Booking> findByStatus(BookingStatus status);
}
