package gg.api42;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.springframework.stereotype.Component;

import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;

@Component
public class ApiClient {
	private String appId = "u-s4t2ud-c7e81a6ebe4feb0e6d9b40e36455e546e86a75f22695a82292d4d368e7b59773";
	private String appSecret = "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";
	private String apiTokenUrl = "https://api.intra.42.fr/oauth/token";
	private String token;

	public String getToken() {
		try {
			// Prepare JSON payload
			JSONObject parameters = new JSONObject();
			parameters.put("grant_type", "client_credentials");
			parameters.put("client_id", appId);
			parameters.put("client_secret", appSecret);
			String jsonInputString = parameters.toString();

			System.out.println("Request: " + jsonInputString);
			System.out.println("URL: " + apiTokenUrl);

			// Create connection
			URL url = new URL(apiTokenUrl);
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Content-Length", String.valueOf(jsonInputString.getBytes().length));
			conn.setDoOutput(true);

			// Send request
			try (OutputStream os = conn.getOutputStream()) {
				byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
				os.write(input, 0, input.length);
			}

			// Check response code
			int responseCode = conn.getResponseCode();
			if (responseCode != 200) {
				System.out.println("HTTP Error: " + responseCode);
				System.out.println("Response: " + conn.getResponseMessage());
				return null;
			}

			// Read response
			try (BufferedReader br = new BufferedReader(
				new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
				StringBuilder response = new StringBuilder();
				String responseLine;
				while ((responseLine = br.readLine()) != null) {
					response.append(responseLine.trim());
				}

				// Parse JSON response
				JSONParser parser = new JSONParser();
				JSONObject jsonResponse = (JSONObject)parser.parse(response.toString());
				this.token = (String)jsonResponse.get("access_token");
				return this.token;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public String getEvents() {
		try {
			// 토큰이 없으면 새로 받아오기
			if (token == null) {
				token = getToken();
			}

			URL url = new URL("https://api.intra.42.fr/v2/campus/29/events");
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Authorization", "Bearer " + token);

			// Check response code
			int responseCode = conn.getResponseCode();
			if (responseCode != 200) {
				System.out.println("HTTP Error: " + responseCode);
				System.out.println("Response: " + conn.getResponseMessage());
				return null;
			}
			// Read response
			try (BufferedReader br = new BufferedReader(
				new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
				StringBuilder response = new StringBuilder();
				String responseLine;
				while ((responseLine = br.readLine()) != null) {
					response.append(responseLine.trim());
				}
				return response.toString();
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		// return "aaa";
	}

	public String getCurrentToken() {
		return this.token;
	}
}
