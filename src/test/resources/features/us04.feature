Feature: Programación de citas con asesores
  Como granjero con poca experiencia
  quiero programar una cita con un asesor
  para recibir orientación personalizada en el sector agropecuario

  Scenario: Programar cita
    Given el granjero con poca experiencia desea programar una cita
    And se encuentra en el apartado de "Horarios Disponibles" del perfil de un asesor
    When seleccione un horario disponible
    And complete los campos solicitados
    And haga clic en el botón "Reservar Cita"
    Then el sistema le mostrará un mensaje de confirmación

  Scenario: Fallar al programar cita
    Given el granjero con poca experiencia desea programar una cita
    And se encuentra en el apartado de "Horarios Disponibles" del perfil de un asesor
    When seleccione un horario disponible
    And se encuentra un error técnico o de conexión que impide completar la programación
    Then el sistema le mostrará un mensaje de error