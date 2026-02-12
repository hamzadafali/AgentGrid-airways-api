package io.agentgrid.domain.service;

import io.agentgrid.domain.enums.BookingStatus;
import io.agentgrid.domain.enums.PaymentMethod;
import io.agentgrid.domain.enums.PaymentStatus;
import io.agentgrid.infrastructure.persistence.entity.Booking;
import io.agentgrid.infrastructure.persistence.entity.Payment;
import io.agentgrid.infrastructure.repository.BookingRepository;
import io.agentgrid.infrastructure.repository.PaymentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepository paymentRepo;
    private final BookingRepository bookingRepo;

    @Transactional
    public Payment processPayment(Booking booking, Double amount, PaymentMethod method) {
        Payment payment = Payment.builder()
                .amount(amount)
                .paymentMethod(method)
                .paymentDate(LocalDateTime.now())
                .status(PaymentStatus.COMPLETED)
                .booking(booking)
                .build();

        booking.setStatus(BookingStatus.CONFIRMED);
        bookingRepo.save(booking);

        return paymentRepo.save(payment);
    }

    public Payment findById(Long id) {
        return paymentRepo.findById(id).orElseThrow(() -> new RuntimeException("Payment not found"));
    }
}
