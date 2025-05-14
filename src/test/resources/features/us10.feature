Feature: Inicio de sesión
  Como usuario
  quiero acceder a mi cuenta registrada
  para acceder a las funciones de usuario

  Scenario Outline: Inicio de sesión exitoso
    Given el usuario quiere loguearse con el servicio
    When el usuario ingrese su usuario "<user>" y contraseña "<password>"
    Then la respuesta del login contiene un status 200
    And la respuesta contiene un token "token"

    Examples:
    | user | password |
    | example@gmail.com | 12345678 |

  Scenario Outline: Inicio de sesión fallido
    Given el usuario quiere loguearse con el servicio
    When el usuario ingrese su usuario "<invalid_user>" y contraseña "<invalid_password>"
    Then la respuesta del login contiene un status 400
    And la respuesta contiene un error "error"

    Examples:
    | invalid_user | invalid_password |
    | example@gmail.com | 1234567 |
    | example2 | 12345678 |