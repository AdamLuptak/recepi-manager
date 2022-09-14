package eu.rebase.recipe.controller;

import eu.rebase.recipe.Application;
import eu.rebase.recipe.model.EaterType;
import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import static eu.rebase.recipe.controller.IdentityArgumentResolver.X_USERID;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(classes = {Application.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith({PostgresqlTestContainerExtension.class})
@ActiveProfiles("test")
@Sql("/insert_data.sql")
@Sql(value = "/clean_data.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class RecipeControllerTest {

    @LocalServerPort
    int port;


    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    @Test
    void shouldGetOnlyVegetarianRecipe() {
        given()
                .header(X_USERID, 1)
                .param("eaterType", EaterType.VEGETARIAN)
                .when()
                .get("/api/v1/recipes")
                .then()
                .log()
                .all()
                .statusCode(HttpStatus.SC_OK).body(
                        "content[0].id", equalTo("ea0a5553-1c34-478a-bacb-a463a74b4980"),
                        "content[0].eaterType", equalTo("VEGETARIAN"),
                        "content.size()", equalTo(1)
                );
    }

    @Test
    void shouldGetRecipeToServeFourPersonsAndHavePotatoesAsAnIngredient() {
        given()
                .header(X_USERID, 1)
                .param("servingsCount", 4)
                .param("includeIngredients", "potatoes")
                .when()
                .get("/api/v1/recipes")
                .then()
                .log()
                .all()
                .statusCode(HttpStatus.SC_OK).body(
                        "content[0].id", equalTo("e46067f1-64ae-4286-bb2f-07c34d0e05e8"),
                        "content[0].servingsCount", equalTo(4),
                        "content[0].ingredients", hasItem(allOf(hasEntry("description", "potatoes"))),
                        "content.size()", equalTo(1)
                );
    }

    @Test
    void shouldGetRecipeWithoutSalmonAsAnIngredientAndOvenInInstructions() {
        given()
                .header(X_USERID, 1)
                .param("instructions", "oven")
                .param("excludeIngredients", "salmon")
                .when()
                .get("/api/v1/recipes")
                .then()
                .log()
                .all()
                .statusCode(HttpStatus.SC_OK).body(
                        "content[0].id", equalTo("dcc934e7-1cf0-4aca-a76e-df7a6a65b8a8"),
                        "content[0].instructions", containsString("oven"),
                        "content[0].ingredients", not(hasItem(allOf(hasEntry("description", "salmon")))),
                        "content.size()", equalTo(1)
                );
    }
}
