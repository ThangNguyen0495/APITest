import io.restassured.RestAssured;
import org.testng.annotations.Test;
import utitlities.API;

import java.util.List;

public class APITest {
    public String accessToken;
    public String token;
    public List<Integer> listID;


    @Test()
    public void Login() {
        RestAssured.baseURI = "https://api.beecow.info";
        String body = """
                {
                    "username": "stgaboned@nbobd.com",
                    "password": "Abc@12345",
                    "rememberMe": true
                }""";
        accessToken = new API().login("/api/authenticate/store/email/gosell", body).jsonPath().getString("accessToken");
    }

    @Test(priority = 1)
    public void getListProducts() {
        RestAssured.baseURI = "https://api.beecow.info";
        listID = new API().list("/itemservice/api/store/dashboard/127103/items-v2?size=2", accessToken).jsonPath().get("id");
        System.out.println(listID);
    }

    @Test
    public void getUnit() {
        RestAssured.baseURI = "https://api.beecow.info";
        String body = """
                {
                    "lstItemId": [],
                    "key": null
                }
                """;
        listID = new API().search("/itemservice/api/item/conversion-units/search", accessToken, body).jsonPath().get("content.id");
        System.out.println(listID);
    }

    @Test(priority = 2)
    public void loginSF() {
        RestAssured.baseURI = "https://stgabonedvn.unisell.vn";
        String body = """
                {
                     "username": "stgbuyer@nbobd.com",
                     "password": "Abc@12345",
                     "phoneCode": "+84"
                 }""";
        token = new API().login("/api/login", body).jsonPath().getString("id_token");
        System.out.println(token);
    }

    @Test(priority = 3)
    public void addToCart() {
        RestAssured.baseURI = "https://api.beecow.info";
        String body;
        for (int id : listID) {
            body = """
                    {
                        "itemId": %d,
                        "quantity": %d,
                        "branchId": %d,
                        "langKey": "en"
                    }
                    """.formatted(id, 1, 127114);
            new API().create("/orderservices2/api/shop-carts/add-to-cart/domain/gosell",token, body);
        }

    }
}
