package com.hackerrank.sample.controller;

import java.util.ArrayList;
import java.util.Arrays;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.hackerrank.sample.dto.FilteredProducts;
import com.hackerrank.sample.dto.SortedProducts;

import com.hackerrank.sample.dto.StringResponse;

@RestController
public class SampleController {

	@CrossOrigin
	@GetMapping("/filter/discount/{discount_percentage}")
	public ResponseEntity<ArrayList<FilteredProducts>> filterByDiscount(@PathVariable("discount_percentage") int discountPercentage) {
		try {
			ArrayList<FilteredProducts> products = new ArrayList<>();
			for (int i = 0; i < data.length(); i++) {
				JSONObject product = data.getJSONObject(i);
				double discount = product.optDouble("discount", 0);
				if (discount > discountPercentage) {
					String barcode = product.optString("barcode", "");
					products.add(new FilteredProducts(barcode));
				}
			}
			if (products.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
			return new ResponseEntity<>(products, HttpStatus.OK);
		} catch (Exception e) {
			System.out.println("Error encountered : " + e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	// ...existing code...
	@CrossOrigin
	@org.springframework.web.bind.annotation.PostMapping("/rating/compute/")
	public ResponseEntity<StringResponse[]> computeRatings() {
		try {
			StringResponse[] responses = new StringResponse[data.length()];
			for (int i = 0; i < data.length(); i++) {
				JSONObject product = data.getJSONObject(i);
				String name = product.optString("name", "");
				JSONArray ratings = product.optJSONArray("ratings");
				double avg = 0.0;
				if (ratings != null && ratings.length() > 0) {
					int sum = 0;
					for (int j = 0; j < ratings.length(); j++) {
						sum += ratings.getJSONObject(j).optInt("rating", 0);
					}
					avg = (double) sum / ratings.length();
				}
				responses[i] = new StringResponse(name + ":" + avg);
			}
			return new ResponseEntity<>(responses, HttpStatus.OK);
		} catch (Exception e) {
			System.out.println("Error encountered : " + e.getMessage());
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	final String uri = "https://jsonmock.hackerrank.com/api/inventory";
	RestTemplate restTemplate = new RestTemplate();
	String result = restTemplate.getForObject(uri, String.class);			
	JSONObject root = new JSONObject(result);
	
	JSONArray data = root.getJSONArray("data");
	
	
	
	@CrossOrigin
	@GetMapping("/filter/price/{initial_range}/{final_range}")
	public ResponseEntity<ArrayList<FilteredProducts>> filterByPrice(@PathVariable("initial_range") int initialRange,
																		@PathVariable("final_range") int finalRange) {
		try {
			ArrayList<FilteredProducts> products = new ArrayList<>();
			for (int i = 0; i < data.length(); i++) {
				JSONObject product = data.getJSONObject(i);
				double price = product.optDouble("price", 0);
				if (price >= initialRange && price <= finalRange) {
					String barcode = product.optString("barcode", "");
					products.add(new FilteredProducts(barcode));
				}
			}
			if (products.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
			return new ResponseEntity<>(products, HttpStatus.OK);
		} catch (Exception e) {
			System.out.println("Error encountered : " + e.getMessage());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	
	@CrossOrigin
		@GetMapping("/sort/price")
		public ResponseEntity<SortedProducts[]> sortByPrice() {
			try {
				ArrayList<JSONObject> products = new ArrayList<>();
				for (int i = 0; i < data.length(); i++) {
					products.add(data.getJSONObject(i));
				}
				products.sort((a, b) -> {
					double priceA = a.optDouble("price", 0);
					double priceB = b.optDouble("price", 0);
					return Double.compare(priceA, priceB);
				});
				SortedProducts[] sorted = new SortedProducts[products.size()];
				for (int i = 0; i < products.size(); i++) {
					String barcode = products.get(i).optString("barcode", "");
					sorted[i] = new SortedProducts(barcode);
				}
				return new ResponseEntity<>(sorted, HttpStatus.OK);
			} catch (Exception e) {
				System.out.println("Error encountered : " + e.getMessage());
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
		}
		
		
	
}
