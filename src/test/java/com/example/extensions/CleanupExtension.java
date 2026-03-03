package com.example.extensions;

import com.example.client.AuthApiClient;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import java.lang.reflect.Field;

public class CleanupExtension implements AfterTestExecutionCallback {

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        Object testInstance = context.getTestInstance().orElse(null);
        if (testInstance == null) return;

        CleanupUser cleanupUser = context.getTestMethod()
                .map(method -> method.getAnnotation(CleanupUser.class))
                .orElse(null);

        if (cleanupUser == null) return;

        String tokenFieldName = cleanupUser.tokenField();

        try {
            Field tokenField = testInstance.getClass().getDeclaredField(tokenFieldName);
            tokenField.setAccessible(true);
            String token = (String) tokenField.get(testInstance);

            if (token != null && !token.isEmpty()) {
                AuthApiClient authApiClient = new AuthApiClient();

                System.out.printf("[Thread: %d] Удаление пользователя с токеном: %s%n",
                        Thread.currentThread().getId(), token);

                authApiClient.deleteUser(token);
                System.out.printf("[Thread: %d] Пользователь успешно удален после теста%n",
                        Thread.currentThread().getId());
            }
        } catch (NoSuchFieldException e) {
            System.err.printf("[Thread: %d] Поле '%s' не найдено в тестовом классе%n",
                    Thread.currentThread().getId(), tokenFieldName);
        } catch (Exception e) {
            System.err.printf("[Thread: %d] Ошибка при удалении пользователя: %s%n",
                    Thread.currentThread().getId(), e.getMessage());
        }
    }
}