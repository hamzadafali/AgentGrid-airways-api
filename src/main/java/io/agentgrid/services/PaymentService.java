package io.agentgrid.services;

import io.agentgrid.entities.Booking;
import io.agentgrid.entities.Payment;
import io.agentgrid.enums.BookingStatus;
import io.agentgrid.enums.PaymentMethod;
import io.agentgrid.enums.PaymentStatus;
import io.agentgrid.repositories.BookingRepository;
import io.agentgrid.repositories.PaymentRepository;
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
