package steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.And;

public class ManagementSteps {

    @Given("el granjero con poca experiencia desea programar una cita")
    public void el_granjero_con_poca_experiencia_desea_programar_una_cita() {
        System.out.println("El granjero desea programar una cita.");
    }

    @And("se encuentra en el apartado de {string} del perfil de un asesor")
    public void se_encuentra_en_el_apartado_de_del_perfil_de_un_asesor(String horariosDisponibles) {
        System.out.println("El granjero está en el apartado: " + horariosDisponibles);
    }

    @When("seleccione un horario disponible")
    public void seleccione_un_horario_disponible() {
        System.out.println("El granjero selecciona un horario disponible.");
    }

    @When("complete los campos solicitados")
    public void complete_los_campos_solicitados() {
        System.out.println("El granjero completa los campos solicitados.");
    }

    @When("haga clic en el botón {string}")
    public void haga_clic_en_el_botón(String reservarCita) {
        System.out.println("El granjero hace clic en el botón: " + reservarCita);
    }

    @Then("el sistema le mostrará un mensaje de confirmación")
    public void el_sistema_le_mostrará_un_mensaje_de_confirmación() {
        System.out.println("El sistema muestra un mensaje de confirmación.");
    }

    @When("se encuentra un error técnico o de conexión que impide completar la programación")
    public void se_encuentra_un_error_técnico_o_de_conexión_que_impide_completar_la_programación() {
        System.out.println("Se encuentra un error técnico o de conexión.");
    }

    @Then("el sistema le mostrará un mensaje de error")
    public void el_sistema_le_mostrará_un_mensaje_de_error() {
        System.out.println("El sistema muestra un mensaje de error.");
    }
}