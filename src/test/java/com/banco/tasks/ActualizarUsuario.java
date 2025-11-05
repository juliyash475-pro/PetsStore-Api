package com.banco.tasks;

import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;

import static net.serenitybdd.screenplay.Tasks.instrumented;

public class ActualizarUsuario implements Task {

    public static ActualizarUsuario conDatosActualizados() {
        return instrumented(ActualizarUsuario.class);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        String body = """
                {
                    "id": 1234,
                    "username": "julian_test",
                    "firstName": "Julian Updated",
                    "lastName": "Rodriguez",
                    "email": "julian_updated@test.com",
                    "password": "654321",
                    "phone": "3214567890",
                    "userStatus": 1
                }
                """;

        SerenityRest.given()
                .contentType("application/json")
                .body(body)
                .put("/user/julian_test")
                .then()
                .log().all();
    }
}
