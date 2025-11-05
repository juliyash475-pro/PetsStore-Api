package com.banco.hooks;

import io.cucumber.java.Before;
import net.serenitybdd.rest.SerenityRest;

public class Hook {
    @Before
    public void setBaseUri() {
        SerenityRest.useRelaxedHTTPSValidation();
    }
}
