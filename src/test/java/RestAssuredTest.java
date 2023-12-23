import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

public class RestAssuredTest {

    @DataProvider(name = "circuitData")
    public Object[][] circuitData() {
        return new Object[][]{
                {1, "Australia"},
                {5, "Spain"}
                // Add more data as needed
        };
    }

    @Test(dataProvider = "circuitData")
    public void testCircuitCountry(int circuitIndex, String expectedCountry) {
        // Step 1: Call http://ergast.com/api/f1/2017/circuits.json API
        Response response = RestAssured.given()
                .when()
                .get("http://ergast.com/api/f1/2017/circuits.json");

        // Extract circuitId for the specified index
        String circuitId = response.jsonPath().getString("MRData.CircuitTable.Circuits[" + (circuitIndex - 1) + "].circuitId");

        // Step 2: Call http://ergast.com/api/f1/circuits/{circuitId}.json
        Response circuitResponse = RestAssured.given()
                .pathParam("circuitId", circuitId)
                .when()
                .get("http://ergast.com/api/f1/circuits/{circuitId}.json");

        // Step 3: Validate country
        String actualCountry = circuitResponse.jsonPath().getString("MRData.CircuitTable.Circuits[0].Location.country");
        assertEquals(actualCountry, expectedCountry, "Country is not as expected for circuitIndex: " + circuitIndex);
    }
}
