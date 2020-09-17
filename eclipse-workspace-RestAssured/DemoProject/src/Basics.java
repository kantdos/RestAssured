import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import org.testng.Assert;

import files.ReUsableMethod;
import files.payload;

public class Basics {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// validate if Add Place API us working as expected
		// given - all input details
		// when - Submit the API, -resource , http method
		// then - Validate the response
		RestAssured.baseURI = "https://rahulshettyacademy.com";

		String response = given().queryParam("key", "qaclick123").header("Content-Type", "application/json")
				.body(payload.AddPlace()).when().post("/maps/api/place/add/json").then().log().all().assertThat()
				.statusCode(200).body("scope", equalTo("APP")).header("Server", "Apache/2.4.18 (Ubuntu)").extract()
				.response().asString();

		System.out.println("response:- " + response);

		JsonPath js = new JsonPath(response); // for parsing JSON
		String placeID = js.getString("place_id");
		System.out.println("placeID:- " + placeID);
		System.out.println("Address Added");
		// Update place

		String newAddress = "70 Summer walk";
		given().log().all().queryParam("key", "qaclick123").header("Content-Type", "application/json")
				.body("{\r\n" + "\"place_id\":\"" + placeID + "\",\r\n" + "\"address\":\"" + newAddress + "\",\r\n"
						+ "\"key\":\"qaclick123\"\r\n" + "}\r\n" + "")
				.when().put("/maps/api/place/update/json").then().assertThat().statusCode(200)
				.body("msg", equalTo("Address successfully updated"));
		System.out.println("Address Updated");

		// Get Place
		String getPlaceResponce = given().log().all().queryParam("key", "qaclick123").queryParam("place_id", placeID).when()
				.get("/maps/api/place/get/json").then().assertThat().statusCode(200).extract().response().asString();
				
		JsonPath js1 = ReUsableMethod.rawToJson(getPlaceResponce);
		String actualAddress = js1.getString("address");
		System.out.println(actualAddress);
		
		Assert.assertEquals(actualAddress, newAddress);
		
		System.out.println("Address Validated");
		
		

	}

}
