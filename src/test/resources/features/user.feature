Feature: Gestión de usuarios en Petstore API

  Scenario: Crear, consultar, actualizar y eliminar un usuario
    Given que el tester tiene acceso a la API de Petstore
    When crea un nuevo usuario
    And consulta el usuario creado
    And actualiza la información del usuario
    And elimina el usuario
    Then el usuario es eliminado correctamente
