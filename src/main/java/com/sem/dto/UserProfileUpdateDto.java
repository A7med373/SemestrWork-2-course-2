package com.sem.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserProfileUpdateDto {
    private String firstName;
    private String lastName;
    private String description;
}
