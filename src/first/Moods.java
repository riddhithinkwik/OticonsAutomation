package first;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Test;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
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
import org.asynchttpclient.uri.Uri;
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

//import first.DashboardPage;

public class Moods extends DashboardPage {

	// String newAccessToken ;

	@Test(dependsOnMethods = { "Loginpage" })
	public void Moodpage() throws ClientProtocolException, InterruptedException, IOException, ParseException {
		System.out.println("sdasdasddddddddddddddddddddddd");
//WebDriver driver = getDriver();		
//DashboardPage mood = new DashboardPage(); 
//mood.stylepage();
		//	System.out.println(DashboardPage.accessToken);
		
		// System.out.println(driver);
		WebDriver newDriver = DashboardPage.getDriver();
		Thread.sleep(5000);
		Actions browse = new Actions(newDriver);

		// System.out.println(newDriver);
		// get Mood Tab

		//
		WebElement buttonForMoods = newDriver
				.findElement(By.xpath("/html/body/div[1]/main/div[2]/div/div[3]/nav[2]/li[2]/a"));

		buttonForMoods.click();
			Thread.sleep(5000);	
		WebElement getActMoodsTitle = newDriver
				.findElement(By.xpath("/html/body/div[1]/main/div[2]/div/div[2]/div/h1"));

		/// html/body/div[1]/main/div[2]/div/div[2]/div/h1
		String Act_MoodsTitle = getActMoodsTitle.getText();

		System.out.println(Act_MoodsTitle);
		String Exp_Moods_Title = "Moods";

		// add New Button Text	

		System.out.println(Act_MoodsTitle.equals(Exp_Moods_Title));

		if (Act_MoodsTitle.equals(Exp_Moods_Title)) {

			System.out.println("now, Style page is open");

			// Wait until the Element is Not Find
			Thread.sleep(5000);
			WebElement firstEleofMoods = newDriver
					.findElement(By.xpath("/html/body/div[1]/main/div[2]/div/div[4]/div/div/div[2]/div/div[1]/div"));

			firstEleofMoods.click();

			Thread.sleep(1000);
			System.out.println("first element Clicked");

			String getUrlForMoodsPage = newDriver.getCurrentUrl(); //
			System.out.println(getUrlForMoodsPage);
			String yoururl = getUrlForMoodsPage;
			// URI theUri = new URI(yoururl);
				// String id = HttpUtility.ParseQueryString(theUri.Query).Get("ID"); //

			// Console.WriteLine(jid);
			try {
				URI theUri = new URI(yoururl);
				String query = theUri.getQuery();
				System.out.println(query);

				if (query != null) {
					String[] queryParams = query.split("&");
					for (String param : queryParams) {
						String[] keyValue = param.split("=");

						System.out.println(keyValue);
						System.out.println(tempaccessToken);
						String newAccessToken = DashboardPage.accessToken;
						System.out.println(newAccessToken);
						if (keyValue.length == 2 && keyValue[0].equals("style")) {

							// Use a First a Key value

							String style = keyValue[1];
							System.out.println("style: " + style);

							String apiUrlForMoodsData = "https://sync-api-development.44db.com/api/v1/category";

							Map<String, String> MoodsData = new HashMap<>();

							MoodsData.put("style[0]", style);

							Map<String, String> MoodsDataHeaderObject = new HashMap<>();

							MoodsDataHeaderObject.put("X-Oticons-Auth-Token", newAccessToken);

							MultipartEntityBuilder multipartEntityBuildernew = MultipartEntityBuilder.create();

							MultipartEntityBuilder callsecondAPI = createRequestObj(MoodsData,
									multipartEntityBuildernew);

							HttpEntity requestEntitynew = callsecondAPI.build();
							
							System.out.println(requestEntitynew);
							
							// call a Function and get composer Data
							HttpResponse getMoodsDataReq = getReponse(requestEntitynew, apiUrlForMoodsData,
									MoodsDataHeaderObject);
							System.out.println("Get getMoodsrData Response " + getMoodsDataReq);

							Integer StatusCodeOfMoodsData = getMoodsDataReq.getStatusLine().getStatusCode();
							System.out.println("Get getMoodsrData Response Status Code" + StatusCodeOfMoodsData);

							JSONParser parser = new JSONParser();


							// call a Function and get composer Data
							// HttpResponse getMoodsrData = getReponse(requestEntitynew, apiUrlForMoodsData,
							// MoodsDataHeaderObject);
							// System.out.println("Get getMoodsrData Response " + getMoodsrData);

							// Integer StatusCodeOfMoodsData =
							// getMoodsrData.getStatusLine().getStatusCode();
							// System.out.println("Get getMoodsrData Response Status Code" +
							// StatusCodeOfMoodsData);

					     	//	JSONParser parser = new JSONParser();

							if (StatusCodeOfMoodsData == 200) {

								HttpEntity getMoodsentity = getMoodsDataReq.getEntity();
								String getMoodsjsonResponse = EntityUtils.toString(getMoodsentity);

								JSONObject getMoodsDataJsonObjects = (JSONObject) parser.parse(getMoodsjsonResponse);

								JSONObject getMoodsData = (JSONObject) getMoodsDataJsonObjects.get("data");

								JSONArray getMoodsTrackJson = (JSONArray) getMoodsData.get("tracks");
								JSONObject getFirstObject = (JSONObject) getMoodsTrackJson.get(0);

								String getSongId = (String) getFirstObject.get("id");

								System.out.println("Song Name Of Moods " + getSongId);

								int lenghtofMoodsTrack = getMoodsTrackJson.size();

								for (int i = 0; i < lenghtofMoodsTrack; i++) {
									JSONObject getTrackElement = (JSONObject) getMoodsTrackJson.get(i);

									String getSongName = (String) getTrackElement.get("name");

									//String getFirstSongId = (String)getSongId.get([0]);

									System.out.println("Song Name Of Moods " + getSongName);

									for (int j = 1; j <= 6; j += 2) {

										WebElement elementsOfMoodsList = driver.findElement(By.xpath(
												"/html/body/div/main/div[2]/div/div[4]/div/div/div[2]/div/div/div[" + j
														+ "]/div[1]/div[2]/h4"));

										String getElementSongName = elementsOfMoodsList.getText();
										System.out.println(getElementSongName);

										if (getSongName.equals(getElementSongName)) {

											System.out.println("Song Name is Match");
											break;

										}

									}

								}

								
								WebElement addToCartIcon = driver.findElement(By.xpath(
										"/html/body/div/main/div[2]/div/div[5]/div/div/div/div/div[1]/div[1]/div[3]/div/button"));
								// click on Add to cart button
																
								addToCartIcon.click();

								WebElement getTitleNew = driver.findElement(
										By.xpath("/html/body/div[2]/div/div[1]/div/div/div/div[1]/div/div[1]/h2"));
								
								String Act_TitleOfAddToCart = getTitleNew.getText();

								System.out.println(Act_TitleOfAddToCart);

								String Exp_TitleOfAddToCart = "Select your Production & License Type";

								WebElement getAdvertisePageText = driver.findElement(
										By.xpath("/html/body/div[3]/div/div[1]/div/div/div/div[1]/div/div[2]/ul/li"));

								browse.moveToElement(getAdvertisePageText, 10, 20).build().perform();
								Thread.sleep(5000);

								String Act_AdvertiseText = getAdvertisePageText.getText();

								String Exp_AdvertiseText = "Advertise";

								Thread.sleep(5000);


								System.out.println((Act_TitleOfAddToCart.equals(Exp_TitleOfAddToCart)));

								if (Act_AdvertiseText.equals(Exp_AdvertiseText)) {

									getAdvertisePageText.click();

									System.out.println("Advertise button Clicked");

									// Call API For Advertise Data

									String apiUrlForAdvertiseData = "https://sync-api-development.44db.com/api/v1/license/plan";

									// set payload Value For Moods data
									Map<String, String> advertiseData = new HashMap<>();

									advertiseData.put("productionType[0]", "ADVERTISING");
									advertiseData.put("trackId", getSongId);

									// set Access Token Value
									Map<String, String> advertiseDataHeaderObject = new HashMap<>();

									advertiseDataHeaderObject.put("X-Oticons-Auth-Token", accessToken);

									MultipartEntityBuilder multipartEntityBuilderForAdvertise = MultipartEntityBuilder
											.create();

									MultipartEntityBuilder createMultipartData = createRequestObj(advertiseData,
											multipartEntityBuilderForAdvertise);

									HttpEntity requestAdvertiseEntity = createMultipartData.build();

									System.out.println(requestAdvertiseEntity);

									// call a Function and get composer Data
									HttpResponse getAdvertiseData = getReponse(requestAdvertiseEntity,
											apiUrlForAdvertiseData, advertiseDataHeaderObject);
									System.out.println("Get getMoodsrData Response " + getAdvertiseData);

									Integer StatusCodeOfAdvertiseData = getAdvertiseData.getStatusLine()
											.getStatusCode();
									System.out
											.println("Get Advertise Response Status Code" + StatusCodeOfAdvertiseData);

									JSONParser parserAdvertise = new JSONParser();

									if (StatusCodeOfMoodsData == 200) {

										HttpEntity getAdvertiseEntity = getAdvertiseData.getEntity();
										String getAdvertiseJsonResponse = EntityUtils.toString(getAdvertiseEntity);

										JSONObject getAdvertiseDataJsonObjects = (JSONObject) parserAdvertise
												.parse(getAdvertiseJsonResponse);

										System.out.println(getAdvertiseDataJsonObjects);

										System.out.println("get Advertise data ");

										JSONObject getAdvertiseJsonData = (JSONObject) getAdvertiseDataJsonObjects
												.get("data");
										
									
										/// /html/body/div[3]/div/div[1]/div/div/div/div[1]/div/div[2]/ul/li[2]
										Thread.sleep(5000);
										
										WebElement  getBudgetButton = driver.findElement(
												By.xpath("/html/body/div[3]/div/div[1]/div/div/div/div[1]/div/div[2]/ul/li[2]"));
																				
										getBudgetButton.click();
																			
										//Click on close icon 		
										                                                      
										WebElement  getCloseIcon = driver.findElement(
												By.xpath("/html/body/div[3]/div/div[1]/div/div/div/button"));
																																						
										// /html/body/div[3]/div/div[1]/div/div/div/button/svg
										getCloseIcon.click();
										
																				
									}

								}
							}
						}
					}
				} else {
					System.out.println("No query parameters found in the URL.");
				}
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}
	}
}
