package steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.And;

public class IamSteps {

    @Given("el granjero con poca experiencia quiere explorar el catálogo de asesores")
    public void el_granjero_con_poca_experiencia_quiere_explorar_el_catálogo_de_asesores() {
        System.out.println("El granjero quiere explorar el catálogo de asesores.");
    }

    @And("se encuentra en la plataforma")
    public void se_encuentra_en_la_plataforma() {
        System.out.println("El granjero está en la plataforma.");
    }

    @When("seleccione el botón relacionado con el {string}")
    public void seleccione_el_botón_relacionado_con_el(String catalogo) {
        System.out.println("El granjero selecciona el botón: " + catalogo);
    }

    @Then("el sistema le mostrará una lista de todos los asesores disponibles en la plataforma")
    public void el_sistema_le_mostrará_una_lista_de_todos_los_asesores_disponibles_en_la_plataforma() {
        System.out.println("El sistema muestra la lista de asesores.");
    }

    @Given("el granjero con poca experiencia quiere personalizar su búsqueda")
    public void el_granjero_con_poca_experiencia_quiere_personalizar_su_búsqueda() {
        System.out.println("El granjero quiere personalizar su búsqueda.");
    }

    @And("se encuentra en el apartado de {string}")
    public void se_encuentra_en_el_apartado_de(String asesores) {
        System.out.println("El granjero está en el apartado: " + asesores);
    }

    @When("seleccione el botón de filtros")
    public void seleccione_el_botón_de_filtros() {
        System.out.println("El granjero selecciona el botón de filtros.");
    }

    @Then("el sistema le permitirá filtrar el catálogo de asesores por nombre o reputación")
    public void el_sistema_le_permitirá_filtrar_el_catálogo_de_asesores_por_nombre_o_reputación() {
        System.out.println("El sistema permite filtrar el catálogo.");
    }
}