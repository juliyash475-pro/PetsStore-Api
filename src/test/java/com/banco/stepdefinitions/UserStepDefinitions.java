package com.banco.stepdefinitions;

import java.util.concurrent.atomic.AtomicInteger;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import com.banco.tasks.LoginUser;

import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Actor;
import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import net.serenitybdd.screenplay.rest.abilities.CallAnApi;
import net.serenitybdd.screenplay.rest.interactions.Delete;
import net.serenitybdd.screenplay.rest.interactions.Get;
import net.serenitybdd.screenplay.rest.interactions.Post;
import net.serenitybdd.screenplay.rest.interactions.Put;

public class UserStepDefinitions {

    private static final String BASE_URL = "https://petstore.swagger.io/v2";
    private Actor tester;
    private String username;
    private final String password = "1234";

    @Before
    public void setUp() {
        tester = Actor.named("Tester");
        tester.can(CallAnApi.at(BASE_URL));
        username = "user_demo_" + System.currentTimeMillis();
    }

    @Given("que el tester tiene acceso a la API de Petstore")
    public void given_tester_has_access_to_api() {
        SerenityRest.given()
                .baseUri(BASE_URL)
                .when()
                .get("/store/inventory")
                .then()
                .statusCode(200);
    }

    @When("crea un nuevo usuario")
    public void creates_a_new_user() {
        String body = """
            {
              "id": 1,
              "username": "%s",
              "firstName": "Juan",
              "lastName": "Pérez",
              "email": "juan.perez@example.com",
              "password": "%s",
              "phone": "3001234567",
              "userStatus": 1
            }
        """.formatted(username, password);

        tester.attemptsTo(
            Post.to("/user")
                .with(request -> request
                    .header("Content-Type", "application/json")
                    .body(body))
        );

        System.out.println("🟢 [POST] Usuario creado: " + username);
        System.out.println("Respuesta creación: " + SerenityRest.lastResponse().asString());

        tester.should(
            seeThat("la creación devuelve 200",
                response -> SerenityRest.lastResponse().statusCode(), is(200))
        );
    }

    @When("inicia sesión con el usuario y la contraseña")
    public void logs_in_with_user_and_password() {
        tester.attemptsTo(LoginUser.withCredentials(username, password));
    }

    @When("consulta el usuario creado")
    public void queries_the_created_user() {
        int maxRetries = 3;
        int waitSeconds = 2;
        String responseUsername = null;

        for (int i = 0; i < maxRetries; i++) {
            tester.attemptsTo(
                Get.resource("/user/{username}")
                    .with(req -> req.pathParam("username", username))
            );

            responseUsername = SerenityRest.lastResponse().jsonPath().getString("username");

            if (username.equals(responseUsername)) {
                System.out.println("✅ Usuario encontrado en intento " + (i + 1));
                break;
            }

            System.out.println("⚠️ Intento " + (i + 1) + ": Usuario no disponible aún. Reintentando...");
            try {
                Thread.sleep(waitSeconds * 1000L);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        final String finalResponseUsername = responseUsername;

        System.out.println("🔵 [GET] Respuesta consulta: " + SerenityRest.lastResponse().asString());

        tester.should(
            seeThat("el usuario consultado tiene el nombre correcto",
                r -> finalResponseUsername,
                equalTo(username))
        );
    }

    @When("actualiza la información del usuario")
    public void updates_the_user_information() {
        String updatedBody = """
            {
              "id": 1,
              "username": "%s",
              "firstName": "Carlos",
              "lastName": "Pérez",
              "email": "carlos.perez@example.com",
              "password": "%s",
              "phone": "3009998888",
              "userStatus": 1
            }
        """.formatted(username, password);

        tester.attemptsTo(
            Put.to("/user/{username}")
                .with(req -> req
                    .pathParam("username", username)
                    .header("Content-Type", "application/json")
                    .body(updatedBody))
        );

        System.out.println("🟣 [PUT] Respuesta actualización: " + SerenityRest.lastResponse().asString());

        tester.should(
            seeThat("la actualización devuelve 200",
                r -> SerenityRest.lastResponse().statusCode(), is(200))
        );
    }

    @When("elimina el usuario")
    public void deletes_the_user() {
        tester.attemptsTo(
            Delete.from("/user/{username}")
                .with(req -> req.pathParam("username", username))
        );

        System.out.println("🔴 [DELETE] Respuesta eliminación: " + SerenityRest.lastResponse().asString());

        tester.should(
            seeThat("la eliminación devuelve 200",
                r -> SerenityRest.lastResponse().statusCode(), is(200))
        );
    }

    @Then("el usuario es eliminado correctamente")
    public void user_is_deleted_correctly() {
        int maxRetries = 3;
        int waitSeconds = 3;
        AtomicInteger statusCode = new AtomicInteger(0); // 🔧 permite modificar dentro de lambda

        for (int i = 0; i < maxRetries; i++) {
            tester.attemptsTo(
                Get.resource("/user/{username}")
                    .with(req -> req.pathParam("username", username))
            );

            statusCode.set(SerenityRest.lastResponse().statusCode());

            System.out.println("⚪ [GET after DELETE] Intento " + (i + 1) +
                ": código " + statusCode.get() + " → " + SerenityRest.lastResponse().asString());

            if (statusCode.get() == 404) {
                System.out.println("✅ Usuario eliminado correctamente en intento " + (i + 1));
                break;
            }

            try {
                Thread.sleep(waitSeconds * 1000L);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        tester.should(
            seeThat("el usuario ya no existe",
                r -> statusCode.get(), is(404))
        );
    }
}
