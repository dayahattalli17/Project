package shoppersStack;

import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;

//import java.util.HashMap;

public class ShopperStack {
   String BaseUri=RestAssured.baseURI="https://www.shoppersstack.com/shopping";
   int shopperId;
   String token;
   int productId;
   int itemId;
   int addressId;
   int orderId;
	@Test(priority=1)
	public void login() throws Exception {
		/*
		 * HashMap hm=new HashMap(); hm.put("email", "daya.api@gmail.com");
		 * hm.put("password", "Daya@1234"); hm.put("role", "SHOPPER");
		 */
		
		String loginResponse=given().contentType(ContentType.JSON)
		                       .body("{\r\n"
		                       		+ "  \"email\":\"daya.api@gmail.com\",\r\n"
		                       		+ "  \"password\":\"Daya@1234\",\r\n"
		                       		+ "  \"role\":\"SHOPPER\"\r\n"
		                       		+ "}")
		                      
		                       
		.when().post(BaseUri+"/users/login")
		
		.then().extract().response().asString();
		
		JsonPath js=new JsonPath(loginResponse);
		shopperId=js.get("data.userId");
		token=js.get("data.jwtToken");	
		System.out.println(shopperId);
		System.out.println(token);
	}
	
	@Test(priority=2)
	public void viewProduct() throws Exception {
		
		
		productId=when()
		      .get(BaseUri+"/products/alpha")
		      .jsonPath().get("data[0].productId");
		System.out.println(productId);
	}
	
	@Test(priority=3)
	public void wishlist() throws Exception {
		given()
		       .contentType(ContentType.JSON)
		       .headers("Authorization","Bearer "+token)
		       .body("{\r\n"
		       		+ "  \"productId\": "+productId+",\r\n"
		       		+ "  \"quantity\": 0\r\n"
		       		+ "}")
		.when()
		       .post(BaseUri+"/shoppers/"+shopperId+"/wishlist")
		.then()
		       .statusCode(201).log().all();
	}
	
	@Test(priority=4)
	public void getProductFromWishlist() throws Exception {
		
		given().headers("Authorization","Bearer "+token)
		.when().get(BaseUri+"/shoppers/"+shopperId+"/wishlist")
		.then().assertThat().statusCode(200).log().all();
	}
	
	@Test(priority=5)
	public void deleteProductFromWishlist() throws Exception {
		given().headers("Authorization","Bearer "+token)
		.when().delete(BaseUri+"/shoppers/"+shopperId+"/wishlist/"+productId)
		.then().assertThat().statusCode(204).log().all();
	}
	
	@Test(priority=6)
	public void addProductToCart() throws Exception {
		itemId=given().contentType(ContentType.JSON).headers("Authorization","Bearer "+token)
		       .body("{\r\n"
		       		+ "  \"productId\": "+productId+",\r\n"
		       		+ "  \"quantity\": 1\r\n"
		       		+ "}")    
		.when().post(BaseUri+"/shoppers/"+shopperId+"/carts").jsonPath().get("data.itemId");
		
		
		System.out.println(itemId);
	}
	
	@Test(priority=7)
	public void getProductFromCart() throws Exception {
		given().headers("Authorization","Bearer "+token)
		.when().get(BaseUri+"/shoppers/"+shopperId+"/carts")
		.then().assertThat().statusCode(200).log().all();
	}
	
	@Test(priority=8)
	public void updateProductInCart() throws Exception {
		given().headers("Authorization","Bearer "+token)
		       .contentType(ContentType.JSON)
		       .body("{\r\n"
		       		+ "  \"productId\": "+productId+",\r\n"
		       		+ "  \"quantity\": 2\r\n"
		       		+ "}")
		.when().put(BaseUri+"/shoppers/"+shopperId+"/carts/"+itemId)
		.then().assertThat().statusCode(200).log().all();
	}
	
	@Test(enabled=false, priority=9)
	public void deleteProductFromCart() throws Exception {
		given().headers("Authorization","Bearer "+token)
		.when().delete(BaseUri+"/shoppers/"+shopperId+"/carts/"+productId)
		.then().assertThat().statusCode(200).log().all();
	}
	
	@Test(priority=10)
	public void addAddressForTheProduct() throws Exception {
		addressId=given()
		       .contentType(ContentType.JSON)
		       .headers("Authorization","Bearer "+token)
		       .body("{\r\n"		       		
		       		+ "  \"buildingInfo\": \"abc\",\r\n"
		       		+ "  \"city\": \"vijaypura\",\r\n"
		       		+ "  \"country\": \"India\",\r\n"
		       		+ "  \"landmark\": \"hattalli\",\r\n"
		       		+ "  \"name\": \"daya\",\r\n"
		       		+ "  \"phone\": \"7410\",\r\n"
		       		+ "  \"pincode\": \"432101\",\r\n"
		       		+ "  \"state\": \"uganda\",\r\n"
		       		+ "  \"streetInfo\": \"dont no\",\r\n"
		       		+ "  \"type\": \"home\"\r\n"
		       		+ "}")
		.when()
		       .post(BaseUri+"/shoppers/"+shopperId+"/address").jsonPath().get("data.addressId");
		//.then().statusCode(201).log().all();
		System.out.println(addressId);
	}
	
	@Test(priority=11)
	public void getAddress() throws Exception {
		given().headers("Authorization","Bearer "+token)
		.when().get(BaseUri+"/shoppers/"+shopperId+"/address/"+addressId)
		.then().statusCode(200).log().all();
	}
	
	@Test(priority=12)
	public void updateTheAddress() throws Exception {
		given().headers("Authorization","Bearer "+token)
		       .contentType(ContentType.JSON)
		       .body("{\r\n"
		       		+ "  \"buildingInfo\": \"abc\",\r\n"
		       		+ "  \"city\": \"vijaypura\",\r\n"
		       		+ "  \"country\": \"India\",\r\n"
		       		+ "  \"landmark\": \"hattalli\",\r\n"
		       		+ "  \"name\": \"daya\",\r\n"
		       		+ "  \"phone\": 7410,\r\n"
		       		+ "  \"pincode\": 432101,\r\n"
		       		+ "  \"state\": \"uganda\",\r\n"
		       		+ "  \"streetInfo\": \"dont no\",\r\n"
		       		+ "  \"type\": \"office\"\r\n"
		       		+ "}")
		.when().put(BaseUri+"/shoppers/"+shopperId+"/address/"+addressId)
		.then().assertThat().statusCode(200).log().all();
	}
	
	@Test(enabled=false,priority=13)
	public void deleteTheAddress() throws Exception {
		given().headers("Authorization","Bearer "+token)
		.when().delete(BaseUri+"/shoppers/"+shopperId+"/address/"+addressId)
		.then().statusCode(204).log().all();
	}
	
	@Test(priority=14)
	public void placeOrderOfProduct() throws Exception {
		orderId=given()
		       .contentType(ContentType.JSON)
		       .headers("Authorization","Bearer "+token)
		       .body("{\r\n"
		       		+ "  \"address\": {\r\n"
		       		+ "    \"addressId\": "+addressId+",\r\n"
		       		+ "    \"buildingInfo\": \"string\",\r\n"
		       		+ "    \"city\": \"string\",\r\n"
		       		+ "    \"country\": \"string\",\r\n"
		       		+ "    \"landmark\": \"string\",\r\n"
		       		+ "    \"name\": \"string\",\r\n"
		       		+ "    \"phone\": \"string\",\r\n"
		       		+ "    \"pincode\": \"string\",\r\n"
		       		+ "    \"state\": \"string\",\r\n"
		       		+ "    \"streetInfo\": \"string\",\r\n"
		       		+ "    \"type\": \"string\"\r\n"
		       		+ "  },\r\n"
		       		+ "  \"paymentMode\": \"COD\"\r\n"
		       		+ "}")
		.when()
		       .post(BaseUri+"/shoppers/"+shopperId+"/orders")
		       .jsonPath().get("data.orderId");
		System.out.println(orderId);
	}
	
	@Test(priority=15)
	public void getOrderedProduct() throws Exception {
		given().headers("Authorization","Bearer "+token)
		.when().get(BaseUri+"/shoppers/"+shopperId+"/orders")
		.then().assertThat().statusCode(200).log().all();
	}
	
	@Test(priority=16)
	public void udateTheOrderedProduct() throws Exception {
		given()
		       .headers("Authorization","Bearer "+token)
		       .queryParam("status","DELIVERED")
		.when().patch(BaseUri+"/shoppers/"+shopperId+"/orders/"+orderId)
		.then().assertThat().statusCode(200).log().all();
	}
	
	@Test(priority=17)
	public void CancelOrder() throws Exception {
		given().headers("Authorization","Bearer "+token)
		.when().get(BaseUri+"/shoppers/"+shopperId+"/orders/"+orderId+"/invoice")
		.then().assertThat().statusCode(200).log().all();
	}
}
