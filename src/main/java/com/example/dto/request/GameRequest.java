package com.example.dto.request;

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
public class GameRequest {
    private String company;
    private String description;
    private List<DLCRequest> dlcs;

    @JsonProperty("gameId")
    private Integer gameId;

    private String genre;

    @JsonProperty("isFree")
    private Boolean isFree;

    private Double price;

    @JsonProperty("publish_date")
    private String publishDate;

    private Integer rating;

    @JsonProperty("requiredAge")
    private Boolean requiredAge;

    private Requirements requirements;
    private List<String> tags;
    private String title;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DLCRequest {
        private String description;
        private String dlcName;
        private Boolean isDlcFree;
        private Double price;
        private Integer rating;
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
        private Integer hardDrive;
        private String osName;
        private Integer ramGb;
        private String videoCard;
    }
}