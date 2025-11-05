package com.banco.runners;

import io.cucumber.junit.CucumberOptions;
import net.serenitybdd.cucumber.CucumberWithSerenity;
import org.junit.runner.RunWith;

@RunWith(CucumberWithSerenity.class)
@CucumberOptions(
    features = "src/test/resources/features/user.feature",
    glue = "com.banco.stepdefinitions",
    plugin = {"pretty"},
    snippets = CucumberOptions.SnippetType.CAMELCASE
)
public class UserTestSuite {}
