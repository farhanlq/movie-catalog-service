package com.example.microservices.moviecatalogservice.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.example.microservices.moviecatalogservice.model.CatalogItem;
import com.example.microservices.moviecatalogservice.model.Movie;
import com.example.microservices.moviecatalogservice.model.UserRating;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogController {

	@Autowired
	private RestTemplate restTemplate;

	@GetMapping("/{userId}")
	public List<CatalogItem> getCatalog(@PathVariable("userId") String userId) {

		UserRating ratings = restTemplate.getForObject("http://localhost:8083/ratingsdata/users/" + userId,
				UserRating.class);

		return ratings.getUserRating().stream().map(rating -> {
			Movie movie = restTemplate.getForObject("http://localhost:8082/movies/" + rating.getMovieId(), Movie.class);
			return new CatalogItem(movie.getName(), "Desc", rating.getRating());

		})

				.collect(Collectors.toList());

		// for each movie id, call movie info service and get details

		// put them all together
	}
}
