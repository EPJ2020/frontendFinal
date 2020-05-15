package com.example.lfg_source.main;

import android.os.AsyncTask;

import com.example.lfg_source.entity.Group;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;

public class RestClientGroup extends AsyncTask<String, Void, ResponseEntity<Group[]>> {
  private MainViewModel mainViewModel;
  private String token;

  public RestClientGroup(MainViewModel matchViewModel, String token) {
    this.mainViewModel = matchViewModel;
    this.token = token;
  }

  @Override
  protected ResponseEntity<Group[]> doInBackground(String... uri) {
    final String url = uri[0];
    RestTemplate restTemplate = new RestTemplate();
    try {
      restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
      HttpHeaders headers = new HttpHeaders();
      headers.add("authorization", "Bearer " + token);
      HttpEntity<String> entity = new HttpEntity<String>(headers);
      ResponseEntity<Group[]> response =
          restTemplate.exchange(url, HttpMethod.GET, entity, Group[].class);
      return response;
    } catch (Exception e) {
      String message = e.getMessage();
      return null;
    }
  }

  protected void onPostExecute(ResponseEntity<Group[]> result) {
    if (result != null) {
      HttpStatus statusCode = result.getStatusCode();
      mainViewModel.setDataGroup(new ArrayList<Group>(Arrays.asList(result.getBody())));
    }
  }
}