package com.example.lfg_source.rest;

import android.os.AsyncTask;

import com.example.lfg_source.entity.Group;
import com.example.lfg_source.entity.User;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;

public class RestClientNewGroupPost extends AsyncTask<String, Void, Void> {
    private Group message;
    private String url;

    public RestClientNewGroupPost(Group message){
        this.message = message;
    }

    @Override
    protected Void doInBackground(String... uri) {
        url = uri[0];
        RestTemplate restTemplate = new RestTemplate();
        try {
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<String> entity = new HttpEntity<String>(headers);
            ResponseEntity<Group> response = restTemplate.postForEntity(url, message, Group.class);
        } catch (Exception e) {
            String answer = e.getMessage();
        }
        return null;
    }

    protected void onPostExecute(ResponseEntity<Group> result) {
        HttpStatus statusCode = result.getStatusCode();
        if(statusCode != HttpStatus.OK){
            this.execute(url);
        }
    }
}