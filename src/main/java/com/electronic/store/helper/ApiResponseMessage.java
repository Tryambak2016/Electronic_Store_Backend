package com.electronic.store.helper;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@Builder
public class ApiResponseMessage {
    private String Message;
    private HttpStatus status;
    private boolean Success;
}
