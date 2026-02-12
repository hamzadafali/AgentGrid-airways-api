package io.agentgrid.domain.service;

import io.agentgrid.domain.enums.BookingStatus;
import io.agentgrid.domain.enums.PaymentMethod;
import io.agentgrid.domain.enums.PaymentStatus;
import io.agentgrid.infrastructure.persistence.entity.Booking;
import io.agentgrid.infrastructure.persistence.entity.Payment;
import io.agentgrid.infrastructure.repository.BookingRepository;
import io.agentgrid.infrastructure.repository.PaymentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    PaymentRepository paymentRepo;

    @Mock
    BookingRepository bookingRepo;

    @InjectMocks
    PaymentService paymentService;

    @Test
    void processPayment_updates_booking_and_saves_payment() {
        Booking booking = Booking.builder()
                .id(1L)
                .status(BookingStatus.PENDING)
                .build();

        when(paymentRepo.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(bookingRepo.save(any(Booking.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Payment payment = paymentService.processPayment(booking, 120.0, PaymentMethod.CREDIT_CARD);

        assertThat(booking.getStatus()).isEqualTo(BookingStatus.CONFIRMED);
        verify(bookingRepo).save(booking);

        ArgumentCaptor<Payment> paymentCaptor = ArgumentCaptor.forClass(Payment.class);
        verify(paymentRepo).save(paymentCaptor.capture());

        Payment saved = paymentCaptor.getValue();
        assertThat(saved.getAmount()).isEqualTo(120.0);
        assertThat(saved.getPaymentMethod()).isEqualTo(PaymentMethod.CREDIT_CARD);
        assertThat(saved.getStatus()).isEqualTo(PaymentStatus.COMPLETED);
        assertThat(saved.getBooking()).isEqualTo(booking);

        assertThat(payment).isEqualTo(saved);
    }
}
