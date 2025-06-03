package com.sem.dto;

import lombok.*;

import java.sql.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PasswordHistoryDTO {
    private String password;
    private Date createdAt;
}
