package com.example.lfg_source.service;

import android.os.AsyncTask;

import com.example.lfg_source.entity.Group;
import com.example.lfg_source.entity.GroupSuggestion;
import com.example.lfg_source.entity.User;
import com.example.lfg_source.entity.UserSuggestion;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class RestClientGetList<T> extends AsyncTask<Object, Void, ResponseEntity<T>> {
  private MyService myService;
  private String url;
  private String token;
  private Class<T> genericClass;

  public RestClientGetList(MyService myService, String token, String url, Class<T> genericClass) {
    this.myService = myService;
    this.token = token;
    this.url = url;
    this.genericClass = genericClass;
  }

  @Override
  protected ResponseEntity<T> doInBackground(Object... objects) {
    RestTemplate restTemplate = new RestTemplate();
    try {
      restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

      HttpHeaders headers = new HttpHeaders();
      headers.add("authorization", "Bearer " + token);
      HttpEntity<String> entity = new HttpEntity<String>(headers);
      ResponseEntity<T> response = restTemplate.exchange(url, HttpMethod.GET, entity, genericClass);
      return response;
    } catch (Exception e) {
      String message = e.getMessage();
      return null;
    }
  }

  @Override
  protected void onPostExecute(ResponseEntity<T> result) {
    if (result != null) {
      HttpStatus statusCode = result.getStatusCode();
      if (url.contains("My")) {
        myService.setDataMyGroup(new ArrayList<Group>(Arrays.asList((Group[])result.getBody())));
        return;
      }
      if (url.contains("User/Matches")) {
        myService.setDataMatchGroup(new ArrayList<>(Arrays.asList((Group[])result.getBody())));
        return;
      }
      if (url.contains("Group/Suggestions/")) {
        myService.setUserSuggestions(new ArrayList<>(Arrays.asList((UserSuggestion[])result.getBody())));
      }
      if (url.contains("User/Suggestions")) {
        myService.setGroupSuggestions(new ArrayList<>(Arrays.asList((GroupSuggestion[])result.getBody())));
      }
      if (url.contains("/Group/Matches/")) {
        myService.setDataMatchUsers(new ArrayList<>(Arrays.asList((User[])result.getBody())));
      }
    }
  }
}
