package com.example.lfg_source.main.match;

import android.os.AsyncTask;

import com.example.lfg_source.entity.User;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;

public class RestClientMatchUser extends AsyncTask<String, Void, ResponseEntity<User[]>> {
  private MatchViewModel swipeViewModel;
  private String token;

  public RestClientMatchUser(MatchViewModel swipeViewModel, String token) {
    this.swipeViewModel = swipeViewModel;
    this.token = token;
  }

  @Override
  protected ResponseEntity<User[]> doInBackground(String... uri) {
    final String url = uri[0];
    RestTemplate restTemplate = new RestTemplate();
    try {
      restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

      HttpHeaders headers = new HttpHeaders();
      headers.add("authorization", "Bearer " + token);
      HttpEntity<String> entity = new HttpEntity<String>(headers);
      ResponseEntity<User[]> response =
          restTemplate.exchange(url, HttpMethod.GET, entity, User[].class);
      return response;
    } catch (Exception e) {
      String message = e.getMessage();
      return null;
    }
  }

  protected void onPostExecute(ResponseEntity<User[]> result) {
    HttpStatus statusCode = result.getStatusCode();
    swipeViewModel.setDataUser(new ArrayList<User>(Arrays.asList(result.getBody())));
  }
}
