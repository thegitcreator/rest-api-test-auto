package com.example.client;

import com.example.dto.request.LoginRequest;
import com.example.dto.request.SignUpRequest;
import io.qameta.allure.Step;
import io.restassured.response.Response;

public class AuthApiClient extends ApiClient {

    private static final String SIGNUP_PATH = "/signup";
    private static final String LOGIN_PATH = "/login";
    private static final String USER_PATH = "/user";

    @Step("Регистрация нового пользователя")
    public Response signUp(SignUpRequest request) {
        return given()
                .body(request)
                .when()
                .post(SIGNUP_PATH)
                .then()
                .extract()
                .response();
    }

    @Step("Получение JWT токена для пользователя")
    public Response login(LoginRequest request) {
        return given()
                .body(request)
                .when()
                .post(LOGIN_PATH)
                .then()
                .extract()
                .response();
    }

    @Step("Получение информации о пользователе")
    public Response getUserInfo(String token) {
        return given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get(USER_PATH)
                .then()
                .extract()
                .response();
    }

    @Step("Удаляет пользователя из Базы данных")
    public Response deleteUser(String token) {
        return given()
                .header("Authorization", "Bearer " + token)
                .when()
                .delete(USER_PATH)
                .then()
                .extract()
                .response();
    }
}