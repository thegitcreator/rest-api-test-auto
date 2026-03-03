package com.example.tests;

import com.example.client.AuthApiClient;
import com.example.client.GameApiClient;
import com.example.dto.request.*;
import com.example.dto.response.*;
import com.example.extensions.CleanupUser;
import com.example.utils.TestDataGenerator;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("QA Automation Train App for Rest Assured")
@DisplayName("Путь пользователя")
@Execution(ExecutionMode.CONCURRENT)
public class UserGameFlowTest {

    private AuthApiClient authApiClient;
    private GameApiClient gameApiClient;
    private String token;
    private Integer userId;
    private Integer gameId;
    private String login;
    private String password;

    @BeforeEach
    void setUp() {
        authApiClient = new AuthApiClient();
        gameApiClient = new GameApiClient();
    }

    @Test
    @Story("Полный путь пользователя")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Кейс: Регистрация -> Авторизация -> Добавление игры -> Обновление DLC")
    @CleanupUser(tokenField = "token")
    void completeUserFlowTest() {
        Allure.step("1. Регистрация нового пользователя");
        login = TestDataGenerator.generateLogin();
        password = TestDataGenerator.generatePassword();

        SignUpRequest signUpRequest = SignUpRequest.builder()
                .login(login)
                .pass(password)
                .build();

        Response signUpResponse = authApiClient.signUp(signUpRequest);

        assertThat(signUpResponse.getStatusCode())
                .as("Регистрация должна вернуть 201 Created")
                .isEqualTo(201);

        SignUpResponse signUpData = signUpResponse.as(SignUpResponse.class);
        userId = signUpData.getRegisterData().getId();

        assertThat(signUpData.getInfo().getStatus()).isEqualTo("success");
        assertThat(signUpData.getInfo().getMessage()).isEqualTo("User created");
        assertThat(userId).isNotNull();
        Allure.step("Пользователь создан с ID: " + userId);

        Allure.step("2. Авторизация пользователя");
        LoginRequest loginRequest = LoginRequest.builder()
                .username(login)
                .password(password)
                .build();

        Response loginResponse = authApiClient.login(loginRequest);

        assertThat(loginResponse.getStatusCode())
                .as("Авторизация должна вернуть 200 OK")
                .isEqualTo(200);

        LoginResponse loginData = loginResponse.as(LoginResponse.class);
        token = loginData.getToken();

        assertThat(token).isNotNull();
        assertThat(token).isNotEmpty();
        Allure.step("Токен получен");

        Allure.step("3. Добавление игры");
        GameRequest gameRequest = createTestGame();

        Response addGameResponse = gameApiClient.addGame(token, gameRequest);

        assertThat(addGameResponse.getStatusCode())
                .as("Добавление игры должно вернуть 201 Created")
                .isEqualTo(201);

        GameResponse gameData = addGameResponse.as(GameResponse.class);
        gameId = gameData.getRegisterData().getGameId();

        assertThat(gameData.getInfo().getStatus()).isEqualTo("success");
        assertThat(gameData.getInfo().getMessage()).isEqualTo("Game created");
        assertThat(gameId).isNotNull();
        Allure.step("Игра создана с ID: " + gameId);

        Allure.step("4. Обновление DLC к игре");
        List<GameRequest.DLCRequest> dlcs = createTestDLCs();

        Response updateDlcResponse = gameApiClient.updateGameDLCs(token, gameId, dlcs);

        assertThat(updateDlcResponse.getStatusCode())
                .as("Обновление DLC должно вернуть 200 OK")
                .isEqualTo(200);

        InfoResponse infoResponse = updateDlcResponse.as(InfoResponse.class);

        assertThat(infoResponse.getInfo().getStatus()).isEqualTo("success");
        assertThat(infoResponse.getInfo().getMessage()).isEqualTo("DlC successfully changed");
        Allure.step("DLC успешно обновлено");

        Allure.step("Тест пользовательского пути завершен без ошибок");
    }

    @Test
    @Story("Получение информации о пользователе")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Получение информации о пользователе")
    @CleanupUser(tokenField = "token")
    void getUserInfoTest() {
        Allure.step("1. Регистрация нового пользователя");
        login = TestDataGenerator.generateLogin();
        password = TestDataGenerator.generatePassword();

        SignUpRequest signUpRequest = SignUpRequest.builder()
                .login(login)
                .pass(password)
                .build();

        Response signUpResponse = authApiClient.signUp(signUpRequest);

        assertThat(signUpResponse.getStatusCode())
                .as("Регистрация должна вернуть 201 Created")
                .isEqualTo(201);

        SignUpResponse signUpData = signUpResponse.as(SignUpResponse.class);
        userId = signUpData.getRegisterData().getId();

        assertThat(signUpData.getInfo().getStatus()).isEqualTo("success");
        assertThat(signUpData.getInfo().getMessage()).isEqualTo("User created");
        assertThat(userId).isNotNull();
        Allure.step("Пользователь создан с ID: " + userId);

        Allure.step("2. Авторизация пользователя");
        LoginRequest loginRequest = LoginRequest.builder()
                .username(login)
                .password(password)
                .build();

        Response loginResponse = authApiClient.login(loginRequest);

        assertThat(loginResponse.getStatusCode())
                .as("Авторизация должна вернуть 200 OK")
                .isEqualTo(200);

        LoginResponse loginData = loginResponse.as(LoginResponse.class);
        token = loginData.getToken();

        assertThat(token).isNotNull();
        assertThat(token).isNotEmpty();
        Allure.step("Токен получен");

        Allure.step("3. Получение информации о пользователе");

        Response userInfoResponse = authApiClient.getUserInfo(token);

        assertThat(userInfoResponse.getStatusCode())
                .as("Получение информации должно вернуть 200 OK")
                .isEqualTo(200);

        UserInfoResponse userInfo = userInfoResponse.as(UserInfoResponse.class);

        assertThat(userInfo.getId()).isEqualTo(userId);
        assertThat(userInfo.getLogin()).isEqualTo(login);
        assertThat(userInfo.getPass()).isEqualTo(password);
        assertThat(userInfo.getGames()).isEmpty();

        Allure.step("Информация о пользователе получена: ID=" + userInfo.getId() +
                ", Login=" + userInfo.getLogin());

        Allure.step("Тест получения информации завершен без ошибок");
    }

    private GameRequest createTestGame() {
        GameRequest.Requirements requirements = GameRequest.Requirements.builder()
                .hardDrive(50)
                .osName("Windows 10")
                .ramGb(16)
                .videoCard("NVIDIA GTX 1060")
                .build();

        return GameRequest.builder()
                .company("Test Company")
                .description("Test Game Description")
                .dlcs(Collections.emptyList())
                .gameId(0)
                .genre("Action")
                .isFree(false)
                .price(59.99)
                .publishDate("2026-03-01T19:54:45.282Z")
                .rating(85)
                .requiredAge(true)
                .requirements(requirements)
                .tags(Collections.singletonList("test"))
                .title("Test Game " + System.currentTimeMillis())
                .build();
    }

    private List<GameRequest.DLCRequest> createTestDLCs() {
        GameRequest.DLCRequest.SimilarDlc similarDlc = GameRequest.DLCRequest.SimilarDlc.builder()
                .dlcNameFromAnotherGame("Another Game DLC")
                .isFree(false)
                .build();

        GameRequest.DLCRequest dlc = GameRequest.DLCRequest.builder()
                .description("Test DLC Description")
                .dlcName("Test DLC " + System.currentTimeMillis())
                .isDlcFree(false)
                .price(19.99)
                .rating(90)
                .similarDlc(similarDlc)
                .build();

        return Collections.singletonList(dlc);
    }
}