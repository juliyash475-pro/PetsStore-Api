package com.banco.tasks;

import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;

import static net.serenitybdd.screenplay.Tasks.instrumented;

public class ConsultarUsuario implements Task {

    public static ConsultarUsuario porUsername() {
        return instrumented(ConsultarUsuario.class);
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        SerenityRest.given()
                .get("/user/julian_test")
                .then()
                .log().all();
    }
}
