
package first;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;	
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

@Test
public class DashboardPage {

	WebDriver driver;

	@BeforeTest
	public void openOticons() {

		System.setProperty("webdriver.chrome.driver", "/Users/krunal/Downloads/chromedriver_mac64_new/chromedriver");

		driver = new ChromeDriver();

		driver.get("https://sync-web-development.44db.com/login");
		driver.manage().window().maximize();

	}

	public void Loginpage() throws Exception {

		JSONParser parser = new JSONParser();
		JSONObject jsonObject = null;

		String Exp_LoginTitle = "Login - Oticons SYNC";

		String Act_LoginTitle = driver.getTitle();

		String apiUrl = "https://sync-api-development.44db.com/api/v1/auth/login";
		String email = "riddhi.p@thinkwik.com";
		String password = "12345678";
		String clientId = "someid";

		// Create HTTP client
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();

		// Create HTTP POST request
		HttpPost httpPost = new HttpPost(apiUrl);

		// Create MultipartEntityBuilder
		MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();

		// Add form fields

		Map<String, String> data = new HashMap<>();

		data.put("email", email);
		data.put("password", password);

		//
		MultipartEntityBuilder multipartEntityBuilder1 = createRequestObj(data, multipartEntityBuilder);

		
		// Set the multipart entity as the request body
		HttpEntity requestEntity = multipartEntityBuilder1.build();
		httpPost.setEntity(requestEntity);

		// Set the Content-Type header with the automatically generated boundary
		httpPost.setHeader("X-Client-Id", clientId);
		httpPost.setHeader("Content-Type", requestEntity.getContentType().getValue());

		if (Act_LoginTitle.equals(Exp_LoginTitle)) {
			System.out.println("Login page is found");
			driver.get("https://sync-web-development.44db.com/login");

			// Enter email
			WebElement emailInput = driver.findElement(By.id("email"));
			emailInput.sendKeys("riddhi.p@thinkwik.com");

			// Enter password
			WebElement passwordInput = driver.findElement(By.id("password"));
			passwordInput.sendKeys("12345678");

			// show Entered password
			driver.findElement(By
					.xpath("/html/body/div[1]/main/div[2]/div/main/div/div/div/div/div[2]/form/div[2]/div/span/button"))
					.click();

			// Click on the Login button
			driver.findElement(By.xpath("/html/body/div[1]/main/div[2]/div/main/div/div/div/div/div[2]/form/button"))
					.click();
			Thread.sleep(10000);
			
			// Parse the JSON response
			// JSONParser parser = new JSONParser();
			try {
				// Execute the request
				HttpResponse response = httpClient.execute(httpPost);
				Integer StatusCode = response.getStatusLine().getStatusCode();
				// System.out.println(response.getStatusLine().getReasonPhrase());
				// System.out.println(StatusCode);

				if (StatusCode == 200) {

					// Get the response body
					HttpEntity entity = response.getEntity();
					String jsonResponse = EntityUtils.toString(entity);

					JSONObject jsonObjects = (JSONObject) parser.parse(jsonResponse);

					// Extract and print the values from the JSON object
					JSONObject userData = (JSONObject) jsonObjects.get("data");
					JSONObject user = (JSONObject) userData.get("user");
					Boolean isMfaEnabled = (Boolean) user.get("isMfaEnabled");

					// Get Access Token
					String accessToken = (String) userData.get("accessToken");

					System.out.println(accessToken);

					// MFA is Enabled or not
					if (isMfaEnabled) {

						String Exp_MfapafgeTitle = "Multi Factor Authentication - Oticons • SYNC";

						String Act_MfapafgeTitle = driver.getTitle();

						if (Act_MfapafgeTitle.equals(Exp_MfapafgeTitle)) {

							System.out.println("Multi Factor Authentication page  is  found");

							// OTP screen is Pending
						} else {
							System.out.println("Multi Factor Authentication page  is not found");
						}

					} else {

						String Exp_DashboardTitle = "Oticons SYNC: Music Licensing For Video Production And Film";
					
						String Act_DashboardTitle = driver.getTitle();
						
						if (Act_DashboardTitle.equals(Exp_DashboardTitle)) {

							System.out.println("Dashboard page  is  found");
							// get Browse Text
							WebElement getBrowseText = driver.findElement(
									By.xpath("/html/body/div[1]/main/div[2]/header/nav/div/div/ul/li[1]/a"));
							String Act_getBrowseText = getBrowseText.getText();
							String Exp_getBrowseText = "Browse";
							Assert.assertEquals(Act_getBrowseText, Exp_getBrowseText, "Browse text mismatch");

							// get About Text
							WebElement getAboutText = driver.findElement(
									By.xpath("/html/body/div[1]/main/div[2]/header/nav/div/div/ul/li[2]/a"));
							String Act_getAboutText = getAboutText.getText();
							String Exp_getAboutText = "About";
							Assert.assertEquals(Act_getAboutText, Exp_getAboutText, "About text mismatch");

							// Get Price Text
							WebElement getPriceText = driver.findElement(
									By.xpath("/html/body/div[1]/main/div[2]/header/nav/div/div/ul/li[3]/a"));
							String Act_getPriceText = getPriceText.getText();
							String Exp_getPriceText = "Price";
							Assert.assertEquals(Act_getPriceText, Exp_getPriceText, "Price text mismatch");

							String apiUrlArtistofthemonth = "https://sync-api-development.44db.com/api/v1/composer";

							// set payload Value For Artist of the month
							Map<String, String> datanew = new HashMap<>();

							datanew.put("page", "1");
							datanew.put("limit", "5");
							datanew.put("sortBy", "ALPHABETICAL");
							datanew.put("composerRole", "ARTIST_OF_THE_MONTH");

							// set Access Token Value
							Map<String, String> accessTokenData = new HashMap<>();

							data.put("X-Oticons-Auth-Token", accessToken);

							MultipartEntityBuilder multipartEntityBuildernew = MultipartEntityBuilder.create();

							MultipartEntityBuilder callsecondAPI = createRequestObj(datanew, multipartEntityBuildernew);

							HttpEntity requestEntitynew = callsecondAPI.build();

							System.out.println(requestEntitynew);

							// call a Function and get composer Data
							HttpResponse getComposerData = getReponse(requestEntitynew, apiUrlArtistofthemonth,
									accessTokenData);
							System.out.println("Get Composer Response " + getComposerData);

							Integer StatusCodetemp = getComposerData.getStatusLine().getStatusCode();
							System.out.println("Get Composer Response Status Code" + StatusCodetemp);

							if (StatusCodetemp == 200) {

								HttpEntity getcomposerentity = getComposerData.getEntity();
								String getComposerjsonResponse = EntityUtils.toString(getcomposerentity);

								JSONObject getcomposerdatajsonObjects = (JSONObject) parser
										.parse(getComposerjsonResponse);

								
								JSONObject getData = (JSONObject) getcomposerdatajsonObjects.get("data");
								JSONArray getComposerDataOfMonth = (JSONArray) getData.get("composer");

								JSONObject getComposerFirstElement = (JSONObject) getComposerDataOfMonth.get(0);

								String composerName = (String) getComposerFirstElement.get("name");

								// get Composer ID
								String composerID = (String) getComposerFirstElement.get("id");

								WebElement getArtistOfTheMonthName = driver.findElement(By.xpath(
										"/html/body/div/main/div[2]/div[1]/section[3]/div/div/div/div/div/div/div/div/div/div/div[1]/div/div/h4"));

								String Act_Artistofthemonthname = getArtistOfTheMonthName.getText();

								String Exp_Artistofthemonthname = composerName;

								Assert.assertEquals(Act_Artistofthemonthname, Exp_Artistofthemonthname,
										"Artist of the month name is mismatch");

								
								// set payload Value For Artist of the month data
								Map<String, String> queryHeadervalue = new HashMap<>();

								queryHeadervalue.put("page", "1");
								queryHeadervalue.put("limit", "10");
								queryHeadervalue.put("sortBy", "ALPHABETICAL");
								queryHeadervalue.put("sortMode", "asc");
								queryHeadervalue.put("composerIds[0]", composerID);

								String queryApiURL = "https://sync-api-development.44db.com/api/v1/track/query";
								// set Access Token Value
								Map<String, String> accessTokenDataforQuery = new HashMap<>();

								accessTokenDataforQuery.put("X-Oticons-Auth-Token", accessToken);

								MultipartEntityBuilder multipartEntityBuilderQuery = MultipartEntityBuilder.create();

								MultipartEntityBuilder getQueryHeader = createRequestObj(queryHeadervalue,
										multipartEntityBuilderQuery);

								HttpEntity requestEntityforQuery = getQueryHeader.build();

								System.out.println(requestEntityforQuery);

								// call a Function and get composer Data
								HttpResponse getQueryData = getReponse(requestEntityforQuery, queryApiURL,
										accessTokenDataforQuery);
								System.out.println("Get Query Response " + getQueryData);

								Integer statusCodeQueryAPI = getQueryData.getStatusLine().getStatusCode();
								System.out.println("Get Query Response Status Code" + statusCodeQueryAPI);

								// Call My Favourite List API
								Map<String, String> myFavouriteHeadervalue = new HashMap<>();

								myFavouriteHeadervalue.put("page", "1");
								myFavouriteHeadervalue.put("limit", "10");

								String getFavouriteApiURL = "https://sync-api-development.44db.com/api/v1/auth/favorites/query";
								// set Access Token Value

								Map<String, String> accessTokenForFavouriteAPI = new HashMap<>();

								accessTokenForFavouriteAPI.put("X-Oticons-Auth-Token", accessToken);

								MultipartEntityBuilder multipartEntityBuilderFavourite = MultipartEntityBuilder
										.create();

								MultipartEntityBuilder getFavouriteHeader = createRequestObj(myFavouriteHeadervalue,
										multipartEntityBuilderFavourite);

								HttpEntity requestEntityforFavouriteAPI = getFavouriteHeader.build();

								System.out.println(requestEntityforFavouriteAPI);

								// call a Function and get My Favourite Data
								HttpResponse getFavouriteData = getReponse(requestEntityforFavouriteAPI,
										getFavouriteApiURL, accessTokenForFavouriteAPI);
								System.out.println("Get Favourite Response " + getFavouriteData);

								Integer statusCodeFavouriteAPI = getFavouriteData.getStatusLine().getStatusCode();
								System.out.println("Get Favourite Response Status Code" + statusCodeFavouriteAPI);

								Boolean isFavListAvailable = false;

								if (statusCodeFavouriteAPI == 200) {

									isFavListAvailable = true;

								}

								System.out.println(isFavListAvailable);
								HttpEntity getFavouriteSongEntity = getFavouriteData.getEntity();
								String getFavouriteSongjsonObject = EntityUtils.toString(getFavouriteSongEntity);

								JSONObject getFavouriteSongjsonResponse = (JSONObject) parser
										.parse(getFavouriteSongjsonObject);

								
								JSONObject getFavouriteSongData = (JSONObject) getFavouriteSongjsonResponse.get("data");
								JSONArray getFavouriteTrackJson = (JSONArray) getFavouriteSongData.get("tracks");
								int lenghtofFavouriteTrack = getFavouriteTrackJson.size();

								// check Status code is 200
								if (statusCodeQueryAPI == 200) {

									HttpEntity getComposerSongEntity = getQueryData.getEntity();
									String getComposerSongjsonObject = EntityUtils.toString(getComposerSongEntity);

									JSONObject getComposerSongjsonResponse = (JSONObject) parser
											.parse(getComposerSongjsonObject);

									JSONObject getQuerySongData = (JSONObject) getComposerSongjsonResponse.get("data");
									JSONArray getTrackJson = (JSONArray) getQuerySongData.get("tracks");
									int lenghtofTrack = getTrackJson.size();

									for (int i = 0; i < lenghtofTrack; i++) {
										JSONObject getTrackElement = (JSONObject) getTrackJson.get(i);

										String composerTrackId = (String) getTrackElement.get("id");

										System.out.println(getTrackElement);
										Boolean isUserFavourite = (Boolean) getTrackElement.get("isUserFavourite");
										System.out.println(isUserFavourite);

										if (isUserFavourite && isFavListAvailable) {

											
											for (int j = 0; j < lenghtofFavouriteTrack; j++) {
												JSONObject getFavouriteElement = (JSONObject) getFavouriteTrackJson
														.get(i);
												String composerFavouriteTrackId = (String) getFavouriteElement
														.get("trackId");

												if (composerTrackId.equals(composerFavouriteTrackId)) {

													System.out.println("Both id is matched");
													break;
												}
											}

										} else {
											System.out.println(
													"isUserFavourite && isFavListAvailable  Favourite is False ");

										}
									}

								} else {

									System.out.println(" Not Found , shows 400");

								}

								JavascriptExecutor j = (JavascriptExecutor) driver;
								j.executeScript("window.scrollBy(0,700)");

								Thread.sleep(5000);
								

								// Button for Follow / Unfollow
								WebElement buttonForFollow = driver.findElement(By.xpath(
										"/html/body/div[1]/main/div[2]/div[1]/section[3]/div/div/div/div/div/div/div/div/div/div/div[2]/div/div[2]/div/div/button"));

								String getTextForFollow = buttonForFollow.getText();

								System.out.println(getTextForFollow);

								// Set value for Follow/Unfollow Api
								String baseURL = "https://sync-api-development.44db.com/api/v1/composer/";
								String dynamicValueofComposerID = composerID;
								String follow = "/follow";
								String unFollow = "/unFollow";
								String exp_followButton = "Follow";
								System.out.println(getTextForFollow.equals(exp_followButton));

								if (getTextForFollow.equals(exp_followButton)) {

									WebElement followButton = driver.findElement(By.xpath(
											"/html/body/div[1]/main/div[2]/div[1]/section[3]/div/div/div/div/div/div/div/div/div/div/div[2]/div/div[2]/div/div/button"));

									followButton.click();

									String followapiUrl = String.format("%s%s%s", baseURL, dynamicValueofComposerID,
											follow);
									System.out.println(followapiUrl);
									System.out.println("FollowAPI Token" + accessToken);

									HttpURLConnection connection = createGetRequest(followapiUrl, accessToken);

									int responseCode = connection.getResponseCode();
									System.out.println(responseCode);

									// Click on Follow button
									// Call Follow API

									System.out.println("it shows Following button");
								} else {

									WebElement unFollowButton = driver.findElement(By.xpath(
											"/html/body/div[1]/main/div[2]/div[1]/section[3]/div/div/div/div/div/div/div/div/div/div/div[2]/div/div[2]/div/div/button"));

									unFollowButton.click();
									// Assert.assertTrue(isClicked, "Button is not clicked.");

									String unFollowapiUrl = String.format("%s%s%s", baseURL, dynamicValueofComposerID,
											unFollow);
									System.out.println(unFollowapiUrl);

									System.out.println("UNFollowAPI Token" + accessToken);

									HttpURLConnection connection = createGetRequest(unFollowapiUrl, accessToken);

									int responseCodeUnfollow = connection.getResponseCode();
									System.out.println("UnFollowAPI" + responseCodeUnfollow);
									// Call UnFollow API

									// If Following then check in M
									System.out.println("It shows Follow Button");
								}

							} else {

							}

							System.out.println(Act_getPriceText);

						} else {
							System.out.println("Dashboard page  is not found");
						}

					}

					// Close the HTTP client
					httpClient.close();
				}

			} catch (IOException | ParseException e) {
				e.printStackTrace();

			}

			Thread.sleep(5000);

		} else {
			System.out.println("Login page is not found");
		}

	}

	public MultipartEntityBuilder createRequestObj(Map<String, String> data,
			MultipartEntityBuilder multipartEntityBuilder) {
		for (Map.Entry<String, String> entry : data.entrySet()) {
			/// MultipartEntityBuilder builder = MultipartEntityBuilder.create();
			multipartEntityBuilder.addTextBody(entry.getKey(), entry.getValue());
			// multipartEntityBuilder.addTextBody("password", (String) entry.getValue());

		}

		return multipartEntityBuilder;

	}

	public HttpResponse getReponse(HttpEntity requestEntitynew, String apiUrl, Map<String, String> data)
			throws ClientProtocolException, IOException {

		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		System.out.println("calling getnew reponse function");
		String clientId = "someid";
		// String accessToken = AccessToken;

		System.out.println();
		HttpPost httpPost = new HttpPost(apiUrl);

		System.out.println(httpPost);
		httpPost.setEntity(requestEntitynew);

		// Set the Content-Type header with the automatically generated boundary
		httpPost.setHeader("X-Client-Id", clientId);
		httpPost.setHeader("Content-Type", requestEntitynew.getContentType().getValue());

		for (Map.Entry<String, String> entry : data.entrySet()) {

			httpPost.setHeader(entry.getKey(), entry.getValue());

		}
		// httpPost.setHeader(accessTokenData);

		// POST API endpoint URL

		HttpResponse responseforArtistofthemonth = httpClient.execute(httpPost);

		// Temp Print Status Code

		Integer statusCodeaa = responseforArtistofthemonth.getStatusLine().getStatusCode();
		System.out.println("Status Code: " + statusCodeaa);

		return responseforArtistofthemonth;

	}

	// make a function for GET Method
	public static HttpURLConnection createGetRequest(String apiUrl, String accessToken) throws Exception {

		String clientId = "someid";
		URL url = new URL(apiUrl);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");

		// Set common headers
		connection.setRequestProperty("Content-Type", "multipart/form-data");
		connection.setRequestProperty("X-Client-Id", clientId);
		connection.setRequestProperty("X-Oticons-Auth-Token", accessToken);

		return connection;
	}

	@AfterTest
	public void stylepage() throws InterruptedException {

		
        Actions browse = new Actions(driver);
        WebElement browseButton = driver.findElement(By.xpath("/html/body/div/main/div[2]/header/nav/div/div/ul/li[1]/a"));
        
        browse.moveToElement(browseButton, 10, 20).build().perform();
        Thread.sleep(5000);
        
		//Hover effect on Browse 
		WebElement styleButton = driver.findElement(By.xpath("/html/body/div/main/div[2]/header/nav/div/div/ul/li[1]/div/ul[2]/li/a"));
		browse.moveToElement(styleButton,10,20).build().perform();
        Thread.sleep(5000);
	
		styleButton.click();
				
        Thread.sleep(5000);
        
        WebElement getActGenresTitle = driver.findElement(By.xpath("/html/body/div/main/div[2]/div/div[2]/div/h1"));
       String Act_GenresTitle =   getActGenresTitle.getText();
       String Exp_Genres_Title = "Genres & Stories";
      
       if(Act_GenresTitle.equals(Exp_Genres_Title)) {
    	   
    	   System.out.println("now, Style page is open");
    	   
       }
              
           		
		String Act_StyleTitle = driver.getTitle();
		
		System.out.println(Act_StyleTitle);	
	
		driver.close();
	}

}
