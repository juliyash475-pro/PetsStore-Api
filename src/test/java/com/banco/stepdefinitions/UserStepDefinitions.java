package com.banco.stepdefinitions;

import io.cucumber.java.Before;
import io.cucumber.java.en.*;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.rest.abilities.CallAnApi;
import net.serenitybdd.screenplay.rest.interactions.*;
import net.serenitybdd.rest.SerenityRest;

import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class UserStepDefinitions {

    private static final String BASE_URL = "https://petstore.swagger.io/v2";
    private Actor tester;

    // ✅ Username dinámico para evitar conflictos o duplicados
    private String username = "user_demo_" + System.currentTimeMillis();

    @Before
    public void setUp() {
        tester = Actor.named("Tester");
        tester.can(CallAnApi.at(BASE_URL));
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
              "password": "1234",
              "phone": "3001234567",
              "userStatus": 1
            }
        """.formatted(username);

        tester.attemptsTo(
            Post.to("/user")
                .with(request -> request
                    .header("Content-Type", "application/json")
                    .body(body))
        );

        // 🧠 Verificar si realmente se creó correctamente
        System.out.println("🔹 [POST] Respuesta creación: " + SerenityRest.lastResponse().asString());

        tester.should(
            seeThat("el código de respuesta al crear usuario",
                response -> SerenityRest.lastResponse().statusCode(), is(200))
        );
    }

    @When("consulta el usuario creado")
    public void queries_the_created_user() {
        tester.attemptsTo(
            Get.resource("/user/{username}")
                .with(req -> req.pathParam("username", username))
        );

        // 🧠 Mostrar la respuesta para entender si el usuario existe
        System.out.println("🔹 [GET] Respuesta consulta: " + SerenityRest.lastResponse().asString());

        tester.should(
            seeThat("el usuario consultado tiene el nombre correcto",
                r -> SerenityRest.lastResponse().jsonPath().getString("username"),
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
              "password": "abcd",
              "phone": "3009998888",
              "userStatus": 1
            }
        """.formatted(username);

        tester.attemptsTo(
            Put.to("/user/{username}")
                .with(req -> req
                    .pathParam("username", username)
                    .header("Content-Type", "application/json")
                    .body(updatedBody))
        );

        System.out.println("🔹 [PUT] Respuesta actualización: " + SerenityRest.lastResponse().asString());

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

        System.out.println("🔹 [DELETE] Respuesta eliminación: " + SerenityRest.lastResponse().asString());

        tester.should(
            seeThat("la eliminación devuelve 200",
                r -> SerenityRest.lastResponse().statusCode(), is(200))
        );
    }

    @Then("el usuario es eliminado correctamente")
    public void user_is_deleted_correctly() {
        tester.attemptsTo(
            Get.resource("/user/{username}")
                .with(req -> req.pathParam("username", username))
        );

        System.out.println("🔹 [GET after DELETE] Verificación final: " + SerenityRest.lastResponse().asString());

        tester.should(
            seeThat("el usuario ya no existe",
                r -> SerenityRest.lastResponse().statusCode(), is(404))
        );
    }
}
