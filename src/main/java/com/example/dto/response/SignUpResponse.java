package com.example.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpResponse {

    @JsonProperty("register_data")
    private RegisterData registerData;
    private Info info;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RegisterData {
        private Integer id;
        private String login;
        private String pass;
        private List<Object> games;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Info {
        private String status;
        private String message;
    }
}