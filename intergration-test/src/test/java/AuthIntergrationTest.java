import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class AuthIntergrationTest {

    @BeforeAll
    static void setup() {
        RestAssured.baseURI = "http://localhost:4004";
    }

    @Test
    public void shouldReturnOkForValidToken() {

        String loginPayload = """
                {
                  "email": "testuser@test.com",
                  "password": "password123"
                }
                """;
        //Arrange, Act, Assert
       Response response =  given().contentType("application/json")
                .body(loginPayload)
                .when().post("/auth/login")
                .then()
                .statusCode(200)
                 .extract().response();
         String token = response.jsonPath().getString("token");
         System.out.println(token);

    }

    @Test
    public void shouldReturnErrorOnInvalidCredentials() {

        String loginPayload = """
                {
                  "email": "testuser@test.com",
                  "password": "password1234"
                }
                """;
        //Arrange, Act, Assert
          given().contentType("application/json")

                .body(loginPayload)
                .when().post("/auth/login")
                .then()
                .statusCode(401);
//                .extract().response();
//        String token = response.jsonPath().getString("token");
//        System.out.println(token);

    }

}
