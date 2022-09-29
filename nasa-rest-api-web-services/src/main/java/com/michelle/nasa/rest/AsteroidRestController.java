package com.michelle.nasa.rest;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.json.*;

import com.michelle.nasa.entity.Asteroid;

@RestController
@RequestMapping("/api")
public class AsteroidRestController {

	List<Asteroid> asteroids = new ArrayList<Asteroid>();
	
	@PostConstruct
	public void loadData() throws MalformedURLException, IOException {
		
		Scanner scan = new Scanner(System.in);
		
		String startDate = ""; // "2015-09-26";
		String endDate = ""; // "2015-10-03";
		
		System.out.println("Enter start date (yyyy-mm-dd): ");
		startDate = scan.nextLine();
		
		System.out.println("Enter end date (yyyy-mm-dd): ");
		endDate = scan.nextLine();
		
		String apiKey = "bX99fC2l6yPGvFUCh6lfwfRuBhdL2LjGb5kNAnwr";
		String website = "https://api.nasa.gov/neo/rest/v1/feed?start_date=" + startDate + 
				"&end_date=" + endDate   // + "&detailed=false&"
						+ "&api_key=" + apiKey;
		
		String jsonString = getJsonData(website);
		
		String[] localDates = new String[8];
		int ctr = 0;
		String localDate = startDate;
		
		while (localDate.compareTo(endDate) <= 0) {
			localDates[ctr] = localDate;
			localDate = addOneDay(localDate);
			ctr++;
		}
		
		parseJson(jsonString, localDates, asteroids);

	}
	
	// define endpoint for "/asteroids"
	@GetMapping("/asteroids")
	public List<Asteroid> getAsteroids() {
		
		return asteroids;
	}
	
	@GetMapping("/10nearestasteroids")
	public List<Asteroid> get10NearestAsteroids() {
		
		List<Asteroid> sortedAsteroids = asteroids.stream()
				.sorted(Comparator.comparingDouble(Asteroid::getMissDistance))
				.collect(Collectors.toList());
		
		List<Asteroid> tenNearest = new ArrayList<Asteroid>();
		
		for (int i = 0; i < 10; i++) {
			
			tenNearest.add(sortedAsteroids.get(i));
		}
		
		return tenNearest;
	}
	
	@GetMapping("/largestasteroid")
	public Asteroid getLargestAsteroids() {
		
		List<Asteroid> sortedAsteroids = asteroids.stream()
				.sorted(Comparator.comparing(Asteroid::getEstimatedDiameter).reversed())
				.collect(Collectors.toList());
		
		return sortedAsteroids.get(0);
	}
	
	public String getJsonData(String website) throws MalformedURLException, IOException {
		
		URL url = new URL(website);
		
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		
		connection.setRequestMethod("GET");
		connection.connect();
		
		int responseCode = connection.getResponseCode();
		if (responseCode == 200) {
			
			String response = "";
			Scanner scanner = new Scanner(url.openStream()); // (connection.getInputStream());
			while (scanner.hasNextLine()) {
				response += scanner.nextLine();
				response += "\n";
			}
			
			scanner.close();
			
			return response;
		}
		
		return null;
	}
	
	public static void parseJson(String jsonString, String[] dates, List<Asteroid> asteroids) {
		
//		JSONParser parse = new JSONParser();
//		JSONObject jsonObject = (JSONObject) parse.parse(jsonString);
//		JSONObject nearEarthObj = (JSONObject) jsonObject.get("near_earth_objects");
		
		JSONObject jsonObject = new JSONObject(jsonString);
		JSONObject nearEarthObj = (JSONObject) jsonObject.get("near_earth_objects");
		
		int ctr= 0;
		Asteroid asteroid = new Asteroid();
		
		for (String date : dates) {
			JSONArray jsonArray = (JSONArray) nearEarthObj.get(date); 
			
			for (int i = 0; i < jsonArray.length(); i++) {
				
				ctr++;
				
				JSONObject asteroidObj = (JSONObject) jsonArray.get(i);
				
//				System.out.println(ctr);
//				
				
				asteroid.setId(asteroidObj.get("id").toString());
				asteroid.setName(asteroidObj.get("name").toString());
				
				JSONObject diameterObj = (JSONObject) asteroidObj.get("estimated_diameter");
				JSONObject diameterKmObj = (JSONObject) diameterObj.get("kilometers");
				BigDecimal minDiameter = (BigDecimal) diameterKmObj.get("estimated_diameter_min");
				BigDecimal maxDiameter = (BigDecimal) diameterKmObj.get("estimated_diameter_max");
				BigDecimal averageDiameter = (minDiameter.add(maxDiameter)).divide(BigDecimal.valueOf(2));
				asteroid.setEstimatedDiameter(averageDiameter);
				
				JSONArray closeAppData = (JSONArray) asteroidObj.get("close_approach_data");
				
				for (int j = 0; j < closeAppData.length(); j++) {
					JSONObject closeAppAsteroid = (JSONObject) closeAppData.get(j);
					
					String closeAppDate = (String) closeAppAsteroid.get("close_approach_date");
					LocalDate ldCloseApp = convertToLocalDate(closeAppDate);
					asteroid.setCloseApproachDate(ldCloseApp);
	
					JSONObject missDistance = (JSONObject) closeAppAsteroid.get("miss_distance");
					
					String distanceKmStr = (String) missDistance.get("kilometers");
					double distanceKm = Double.parseDouble(distanceKmStr);
					asteroid.setMissDistance(distanceKm);
				}
				
				asteroids.add(asteroid);
				asteroid = new Asteroid();
			}
		}
		
	}

	public static String addOneDay(String date) {
		return LocalDate
				.parse(date)
				.plusDays(1)
				.toString();
	}

	public static LocalDate convertToLocalDate(String date) {
		
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate ld = LocalDate.parse(date, dateFormatter);
		
		return ld;
	}
}
