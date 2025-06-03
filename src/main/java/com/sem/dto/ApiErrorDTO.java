package com.sem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiErrorDTO {
    private Date timestamp;
    private Integer status;
    private String error;
    private String message;
    private String path;
}