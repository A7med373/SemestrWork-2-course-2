package com.sem.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRegDto {
    @NotBlank
    @Size(min=2, max=100)
    private String firstName;

    @NotBlank
    @Size(min=2, max=100)
    private String lastName;

    @NotBlank
    @Size(min = 12, max = 100)
    private String password;

    @Email
    private String email;

}
