import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static org.testng.Assert.assertEquals;
public class RestAssuredTest {

    @DataProvider(name = "DataOfCircuit")
    public Object[][] DataOfCircuit() {
        return new Object[][]{
                {1},
                {5}
                // Add more data as needed
        };
    }

    @Test(dataProvider = "DataOfCircuit")
    public void testCircuitCountry(int circuitIndex) {
        Response response = RestAssured.given()
                .when()
                .get("http://ergast.com/api/f1/2017/circuits.json");
        String circuitId = response.jsonPath().getString("MRData.CircuitTable.Circuits[" + (circuitIndex - 1) + "].circuitId");
        Response circuitResponse = RestAssured.given()
                .pathParam("circuitId", circuitId)
                .when()
                .get("http://ergast.com/api/f1/circuits/{circuitId}.json");
        String expectedCountry = response.jsonPath().getString("MRData.CircuitTable.Circuits[" + (circuitIndex - 1) + "].Location.country");
        String actualCountry = circuitResponse.jsonPath().getString("MRData.CircuitTable.Circuits[0].Location.country");
        System.out.println(expectedCountry + " - " + actualCountry);
        assertEquals(actualCountry, expectedCountry, "Country is not as expected for circuitIndex: " + circuitIndex);
    }
}
