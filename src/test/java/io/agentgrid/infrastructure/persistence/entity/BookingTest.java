package io.agentgrid.infrastructure.persistence.entity;

import io.agentgrid.domain.enums.BookingStatus;
import io.agentgrid.infrastructure.persistence.entity.Booking;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class BookingTest {

    /**
     * Test to ensure onCreate sets the bookingDate to current time if not already set.
     */
    @Test
    void onCreate_shouldSetBookingDateToNow_whenBookingDateIsNull() {
        // Arrange
        Booking booking = Booking.builder()
                .bookingReference("REF123")
                .seatNumber("12A")
                .status(BookingStatus.PENDING)
                .build();

        // Act
        booking.onCreate();

        // Assert
        assertThat(booking.getBookingDate()).isNotNull();
        assertThat(booking.getBookingDate()).isBeforeOrEqualTo(LocalDateTime.now());
    }

    /**
     * Test to ensure onCreate does not override bookingDate if it is already set.
     */
    @Test
    void onCreate_shouldNotOverrideBookingDate_whenBookingDateIsNotNull() {
        // Arrange
        LocalDateTime existingDate = LocalDateTime.of(2026, 2, 14, 10, 0);
        Booking booking = Booking.builder()
                .bookingReference("REF123")
                .seatNumber("12A")
                .bookingDate(existingDate)
                .status(BookingStatus.PENDING)
                .build();

        // Act
        booking.onCreate();

        // Assert
        assertThat(booking.getBookingDate()).isEqualTo(existingDate);
    }

    /**
     * Test to ensure onCreate sets the status to PENDING if status is null.
     */
    @Test
    void onCreate_shouldSetStatusToPending_whenStatusIsNull() {
        // Arrange
        Booking booking = Booking.builder()
                .bookingReference("REF123")
                .seatNumber("12A")
                .bookingDate(LocalDateTime.now())
                .build();

        // Act
        booking.onCreate();

        // Assert
        assertThat(booking.getStatus()).isNotNull();
        assertThat(booking.getStatus()).isEqualTo(BookingStatus.PENDING);
    }

    /**
     * Test to ensure onCreate does not override the status if it is already set.
     */
    @Test
    void onCreate_shouldNotOverrideStatus_whenStatusIsNotNull() {
        // Arrange
        Booking booking = Booking.builder()
                .bookingReference("REF123")
                .seatNumber("12A")
                .bookingDate(LocalDateTime.now())
                .status(BookingStatus.CONFIRMED)
                .build();

        // Act
        booking.onCreate();

        // Assert
        assertThat(booking.getStatus()).isEqualTo(BookingStatus.CONFIRMED);
    }
}