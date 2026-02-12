-- Data for Flights
INSERT INTO flights (flight_number, airline, origin, destination, departure_date_time, arrival_date_time, total_seats, available_seats, price, status)
VALUES
    ('AF102', 'Air France', 'CDG', 'JFK', '2024-06-01 14:00:00', '2024-06-01 16:30:00', 200, 185, 450.00, 'SCHEDULED'),
    ('BA305', 'British Airways', 'LHR', 'DXB', '2024-06-02 08:30:00', '2024-06-02 19:45:00', 180, 120, 550.00, 'SCHEDULED'),
    ('LH450', 'Lufthansa', 'FRA', 'SIN', '2024-06-03 22:00:00', '2024-06-04 16:15:00', 250, 245, 890.00, 'SCHEDULED'),
    ('EK201', 'Emirates', 'DXB', 'JFK', '2024-06-05 02:45:00', '2024-06-05 08:50:00', 350, 310, 1200.00, 'SCHEDULED');

-- Data for Passengers
INSERT INTO passengers (first_name, last_name, email, phone_number, passport_number, date_of_birth)
VALUES
    ('Jean', 'Dupont', 'jean.dupont@email.com', '+33612345678', 'FRA987654321', '1985-05-15'),
    ('Alice', 'Smith', 'alice.smith@email.com', '+44791234567', 'GBR123456789', '1992-11-22'),
    ('Marco', 'Rossi', 'marco.rossi@email.com', '+39021234567', 'ITA456789123', '1978-02-10');

-- Data for Bookings
INSERT INTO bookings (booking_reference, seat_number, booking_date, status, flight_id, passenger_id)
VALUES
    ('REF-AF102-001', '12A', '2024-05-10 10:30:00', 'CONFIRMED', 1, 1),
    ('REF-BA305-002', '05C', '2024-05-12 15:45:00', 'PENDING', 2, 2),
    ('REF-EK201-003', '22F', '2024-05-14 09:20:00', 'CONFIRMED', 4, 3);

-- Data for Payments
INSERT INTO payments (amount, payment_method, payment_date, status, booking_id)
VALUES
    (450.00, 'CREDIT_CARD', '2024-05-10 10:35:00', 'COMPLETED', 1),
    (1200.00, 'PAYPAL', '2024-05-14 09:25:00', 'COMPLETED', 3);