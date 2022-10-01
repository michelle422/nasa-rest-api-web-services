package com.michelle.nasa.rest;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.json.*;

import com.michelle.nasa.entity.Asteroid;
import com.michelle.nasa.service.AsteroidService;

@RestController
@RequestMapping("/api")
public class AsteroidRestController {
	
	@Autowired
	private AsteroidService asteroidService;
	
	// define endpoint for "/asteroids"
	@GetMapping("/asteroids")
	public List<Asteroid> getAsteroids() throws MalformedURLException, IOException {
		
		return asteroidService.getAsteroids();
	}
	
	@GetMapping("/10nearestasteroids")
	public List<Asteroid> get10NearestAsteroids() throws MalformedURLException, IOException {

		return asteroidService.get10NearestAsteroids();
	}
	
	@GetMapping("/largestasteroid")
	public Asteroid getLargestAsteroid() throws MalformedURLException, IOException {
		
		return asteroidService.getLargestAsteroid();
	}
	

}
