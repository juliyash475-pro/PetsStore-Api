Feature: Compra exitosa en SauceDemo

  Scenario: Usuario realiza una compra completa
    Given el usuario accede a la web de SauceDemo
    When inicia sesión con usuario "standard_user" y contraseña "secret_sauce"
    And agrega el producto "Sauce Labs Backpack" al carrito
    And completa el proceso de compra
    Then debería ver el mensaje "Thank you for your order!"
