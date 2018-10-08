package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import static java.util.Arrays.asList;

@Controller
@RequestMapping("/dictionary")
public class DataDictionaryController {

        @Autowired
        ModelDictionaryProvider provider;


        @GetMapping
        HttpEntity<String> getDictionary() {
            String content = provider.getDictionary();

            MultiValueMap headers = new LinkedMultiValueMap();
            headers.put("Content-Type", asList("text/html"));
            return new HttpEntity(content, headers);
        }

}
