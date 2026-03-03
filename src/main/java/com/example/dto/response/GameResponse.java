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
public class GameResponse {

    @JsonProperty("register_data")
    private RegisterData registerData;
    private Info info;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RegisterData {
        @JsonProperty("gameId")
        private Integer gameId;

        private String title;
        private String genre;
        private Boolean requiredAge;
        private Boolean isFree;
        private Double price;
        private String company;

        @JsonProperty("publish_date")
        private String publishDate;

        private Integer rating;
        private String description;
        private List<String> tags;
        private List<DLC> dlcs;
        private Requirements requirements;

        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class DLC {
            private Boolean isDlcFree;
            private String dlcName;
            private Integer rating;
            private String description;
            private Double price;
            private SimilarDlc similarDlc;

            @Data
            @Builder
            @NoArgsConstructor
            @AllArgsConstructor
            public static class SimilarDlc {
                private String dlcNameFromAnotherGame;
                private Boolean isFree;
            }
        }

        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Requirements {
            private String osName;
            private Integer ramGb;
            private Integer hardDrive;
            private String videoCard;
        }
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