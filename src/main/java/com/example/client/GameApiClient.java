package com.example.client;

import com.example.dto.request.GameRequest;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.util.List;

public class GameApiClient extends ApiClient {

    private static final String USER_GAMES_PATH = "/user/games";
    private static final String USER_GAME_DLC_PATH = "/user/games/{gameId}";

    @Step("Добавляет игру пользователю")
    public Response addGame(String token, GameRequest request) {
        return given()
                .header("Authorization", "Bearer " + token)
                .body(request)
                .when()
                .post(USER_GAMES_PATH)
                .then()
                .extract()
                .response();
    }

    @Step("Обновляет полностью список DLC у игры")
    public Response updateGameDLCs(String token, Integer gameId, List<GameRequest.DLCRequest> dlcs) {
        return given()
                .header("Authorization", "Bearer " + token)
                .pathParam("gameId", gameId)
                .body(dlcs)
                .when()
                .put(USER_GAME_DLC_PATH)
                .then()
                .extract()
                .response();
    }
}