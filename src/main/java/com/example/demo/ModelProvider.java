package com.example.demo;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.Metamodel;
import javax.persistence.metamodel.PluralAttribute;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Transactional
@Service
public class ModelProvider {

    @Autowired
    EntityManager entityManager;

    public String getModel() {
        List<String> lines = new ArrayList<>();

        lines.add("// {type: class}");

        Metamodel m = entityManager.getMetamodel();

        m.getEntities().forEach(entity -> {

            //sb.append("[" + entity.getJavaType().getSimpleName() + "]");
            System.out.println(entity.getJavaType().getSimpleName());

            entity.getAttributes().forEach(attr -> {
                System.out.println(" - " + attr.getName());
                System.out.println("     " + attr.getClass().getSimpleName());
                if(attr instanceof PluralAttribute) {
                    PluralAttribute pa = (PluralAttribute) attr;
                    System.out.println( "    --> " + pa.getElementType().getJavaType().getSimpleName());
                    lines.add("[" + entity.getJavaType().getSimpleName() + "]1-" +  attr.getJavaMember().getName() + "*>[" + pa.getElementType().getJavaType().getSimpleName() + "]");
                }
            });
        });


        return lines.stream().collect(Collectors.joining("\n"));
    }
}
