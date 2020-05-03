package com.example.lfg_source.rest;

import android.os.AsyncTask;

import com.example.lfg_source.entity.User;
import com.example.lfg_source.main.match.MatchViewModel;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;

public class RestClientMatchUser extends AsyncTask<String, Void, ResponseEntity<User[]>> {
  public MatchViewModel swipeViewModel;

  public RestClientMatchUser(MatchViewModel swipeViewModel) {
    this.swipeViewModel = swipeViewModel;
  }

  @Override
  protected ResponseEntity<User[]> doInBackground(String... uri) {
    final String url = uri[0];
    RestTemplate restTemplate = new RestTemplate();
    try {
      restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

      HttpHeaders headers = new HttpHeaders();
      HttpEntity<String> entity = new HttpEntity<String>(headers);

      ResponseEntity<User[]> response = restTemplate.getForEntity(url, User[].class);
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
