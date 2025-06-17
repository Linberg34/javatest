package com.carrental.booking.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CanBookResponse {
    private boolean available;
    private String message;
}
