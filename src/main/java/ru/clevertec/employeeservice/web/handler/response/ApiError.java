package ru.clevertec.employeeservice.web.handler.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiError {

    @JsonProperty(value = "message")
    private String message;

    @JsonProperty(value = "errorUrl")
    private String errorUrl;

    @JsonProperty(value = "errorTime")
    private Instant errorTime;

}
