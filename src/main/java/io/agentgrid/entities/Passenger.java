package io.agentgrid.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;


@Entity
@Table(name = "passengers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Passenger {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @Column(unique = true, nullable = false)
    @Email
    private String email;

    private String phoneNumber;

    @Column(unique = true, nullable = false)
    @NotBlank
    private String passportNumber;

    @NotNull
    private LocalDate dateOfBirth;
}
