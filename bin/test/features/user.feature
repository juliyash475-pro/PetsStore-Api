Feature: Gestión de usuarios en Petstore API
  Para garantizar la correcta interacción con la API de Petstore
  Como tester de la aplicación
  Quiero crear, iniciar sesión, consultar, actualizar y eliminar un usuario

  Scenario: Crear, iniciar sesión, consultar, actualizar y eliminar un usuario
    Given que el tester tiene acceso a la API de Petstore
    When crea un nuevo usuario
    And inicia sesión con el usuario y la contraseña
    And consulta el usuario creado
    And actualiza la información del usuario
    And elimina el usuario
    Then el usuario es eliminado correctamente
