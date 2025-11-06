package com.banco.tasks;

import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Actor;
import net.serenitybdd.screenplay.Task;
import net.serenitybdd.screenplay.rest.interactions.Get;
import static net.serenitybdd.screenplay.Tasks.instrumented;
import static net.serenitybdd.screenplay.GivenWhenThen.seeThat;
import static org.hamcrest.Matchers.is;

public class LoginUser implements Task {

    private final String username;
    private final String password;

    public LoginUser(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public <T extends Actor> void performAs(T actor) {
        actor.attemptsTo(
            Get.resource("/user/login")
                .with(req -> req
                    .queryParam("username", username)
                    .queryParam("password", password))
        );

        System.out.println("🟠 [LOGIN] Respuesta: " + SerenityRest.lastResponse().asString());

        actor.should(
            seeThat("el login responde 200",
                r -> SerenityRest.lastResponse().statusCode(), is(200))
        );
    }

    public static LoginUser withCredentials(String username, String password) {
        return instrumented(LoginUser.class, username, password);
    }
}
