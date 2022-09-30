package ru.netology.testmode.test;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static io.restassured.RestAssured.given;
import static ru.netology.testmode.data.DataGenerator.Registration.getRegisteredUser;
import static ru.netology.testmode.data.DataGenerator.Registration.getUser;
import static ru.netology.testmode.data.DataGenerator.getRandomLogin;
import static ru.netology.testmode.data.DataGenerator.getRandomPassword;

class AuthTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successfully login with active registered user")
    void shouldSuccessfulLoginIfRegisteredActiveUser() {
        var registeredUser = getRegisteredUser("active");
        $("[name=\"login\"]").setValue(registeredUser.getLogin());
        $("[name=\"password\"]").setValue(registeredUser.getPassword());
        $("[data-test-id=\"action-login\"]").click();
        $(withText("Личный кабинет")).shouldBe(visible);
    }

    @Test
    @DisplayName("Should get error message if login with not registered user")
    void shouldGetErrorIfNotRegisteredUser() {
        var notRegisteredUser = getUser("active");
        $("[name=\"login\"]").setValue(getUser("active").getLogin());
        $("[name=\"password\"]").setValue(getUser("active").getPassword());
        $("[data-test-id=\"action-login\"]").click();
        $(withText("Неверно указан логин или пароль")).shouldBe(visible);
    }

    @Test
    @DisplayName("Should get error message if login with blocked registered user")
    void shouldGetErrorIfBlockedUser() {
        var blockedUser = getRegisteredUser("blocked");
        $("[name=\"login\"]").setValue(getRegisteredUser("blocked").getLogin());
        $("[name=\"password\"]").setValue(getRegisteredUser("blocked").getPassword());
        $("[data-test-id=\"action-login\"]").click();
        $(withText("Неверно указан логин или пароль")).shouldBe(visible);
    }

    @Test
    @DisplayName("Should get error message if login with wrong login")
    void shouldGetErrorIfWrongLogin() {
        var registeredUser = getRegisteredUser("active");
        var wrongLogin = getRandomLogin();
        $("[name=\"login\"]").setValue(wrongLogin);
        $("[name=\"password\"]").setValue(getRegisteredUser("active").getPassword());
        $("[data-test-id=\"action-login\"]").click();
        $(withText("Неверно указан логин или пароль")).shouldBe(visible);
    }

    @Test
    @DisplayName("Should get error message if login with wrong password")
    void shouldGetErrorIfWrongPassword() {
        var registeredUser = getRegisteredUser("active");
        var wrongPassword = getRandomPassword();
        $("[name=\"login\"]").setValue(getRegisteredUser("active").getLogin());
        $("[name=\"password\"]").setValue(wrongPassword);
        $("[data-test-id=\"action-login\"]").click();
        $(withText("Неверно указан логин или пароль")).shouldBe(visible);
    }
}
