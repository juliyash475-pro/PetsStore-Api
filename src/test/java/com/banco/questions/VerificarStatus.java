package com.banco.questions;

import net.serenitybdd.rest.SerenityRest;
import net.serenitybdd.screenplay.Question;

public class VerificarStatus {
    public static Question<Integer> codigo() {
        return actor -> SerenityRest.lastResponse().statusCode();
    }
}
