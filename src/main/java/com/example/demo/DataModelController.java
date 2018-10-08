package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import static java.util.Arrays.asList;

@Controller
@RequestMapping("/model-ui")
public class DataModelController {

        @Autowired
        ModelProvider provider;


        @GetMapping
        HttpEntity<String> getDictionary() {
            String content = provider.getModel();

            MultiValueMap headers = new LinkedMultiValueMap();
            headers.put("Content-Type", asList("text/plain"));
            return new HttpEntity(content, headers);
        }

}
