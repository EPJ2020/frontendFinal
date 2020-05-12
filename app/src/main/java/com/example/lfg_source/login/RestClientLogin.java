package com.example.lfg_source.login;

import android.os.AsyncTask;

import com.example.lfg_source.entity.LoginEntity;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public class RestClientLogin extends AsyncTask<String, Void, ResponseEntity<String>> {
  private LoginViewModel loginViewModel;
  private LoginEntity loginEntity;
  private String url;

  public RestClientLogin(LoginViewModel loginViewModel, LoginEntity loginEntity) {
    this.loginViewModel = loginViewModel;
    this.loginEntity = loginEntity;
  }

  @Override
  protected ResponseEntity<String> doInBackground(String... uri) {
    url = uri[0];
    RestTemplate restTemplate = new RestTemplate();
    try {
      restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
      HttpHeaders headers = new HttpHeaders();
      HttpEntity<String> entity = new HttpEntity<String>(headers);
      return restTemplate.postForEntity(url, loginEntity, String.class);
    } catch (Exception e) {
      String answer = e.getMessage();
    }
    return null;
  }

  protected void onPostExecute(ResponseEntity<String> result) {
    if (result == null) {
      loginViewModel.setLoginFailMessage("Login fehlgeschlagen");
    } else {
      HttpStatus statusCode = result.getStatusCode();
      if (statusCode != HttpStatus.OK) {
        loginViewModel.setLoginFailMessage("Login fehlgeschlagen");
        System.out.println(statusCode);
      } else {
        loginViewModel.setLoginData(result.getBody());
      }
    }
  }
}
