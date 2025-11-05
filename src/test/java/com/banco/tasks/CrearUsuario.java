package com.banco.tasks;

import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;

import static net.serenitybdd.screenplay.Tasks.instrumented;

public class CrearUsuario implements Task {

    public static CrearUsuario conDatos() {
        return instrumented(CrearUsuario.class);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        String body = """
                {
                    "id": 1234,
                    "username": "julian_test",
                    "firstName": "Julian",
                    "lastName": "Rodriguez",
                    "email": "julian@test.com",
                    "password": "123456",
                    "phone": "3214567890",
                    "userStatus": 1
                }
                """;

        SerenityRest.given()
                .contentType("application/json")
                .body(body)
                .post("/user")
                .then()
                .log().all();
    }
}
