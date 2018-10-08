package com.example.demo;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.SessionFactoryBuilder;
import org.hibernate.boot.registry.BootstrapServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.PostgreSQL95Dialect;
import org.hibernate.engine.jdbc.spi.JdbcServices;
import org.hibernate.engine.spi.Mapping;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.internal.SessionFactoryImpl;
import org.hibernate.jpa.boot.internal.EntityManagerFactoryBuilderImpl;
import org.hibernate.mapping.Column;
import org.hibernate.mapping.Property;
import org.hibernate.metamodel.spi.MetamodelImplementor;
import org.hibernate.persister.collection.BasicCollectionPersister;
import org.hibernate.persister.entity.SingleTableEntityPersister;
import org.hibernate.type.StringType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.sql.Types;
import java.util.Iterator;

@Transactional
@Service
public class ModelDictionaryProvider {

//    @Autowired
//    EntityManagerFactory entityManagerFactory;

    @Autowired
    EntityManager entityManager;

    @Autowired
    EntityManagerFactoryBuilder entityManagerFactoryBuilder;

    @Autowired
    LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean;

    public String getDictionary() {

        Session session = entityManager.unwrap(Session.class);
        SessionFactory sessionFactory = session.getSessionFactory();
        SessionFactoryImpl sfi = ((SessionFactoryImpl)sessionFactory);

        Dialect dialect = sfi.getServiceRegistry().getService( JdbcServices.class ).getDialect();

        MetadataSources metadataSources = new MetadataSources(sfi.getServiceRegistry().getParentServiceRegistry());

        localContainerEntityManagerFactoryBean.getPersistenceUnitInfo().getManagedClassNames().forEach(c -> {
            metadataSources.addAnnotatedClassName(c);
        });

        Metadata metadataimpl = metadataSources.getMetadataBuilder().build();

        SessionFactoryBuilder sfBuilder = metadataimpl.getSessionFactoryBuilder();
        sfBuilder.build();


        StringBuilder b = new StringBuilder();
        b.append("<!doctype html>\n" +
                "<html lang=\"fr\">");
        b.append("<head></head>");
        b.append("<body>");

        b.append("<h1> Entities </h1>");

        metadataimpl.getEntityBindings().forEach(x -> {

                b.append("<h2 id="+x.getJpaEntityName()+">");
                b.append(x.getJpaEntityName());
                b.append("</h2>");

                b.append("<h3>Table <code>");
                b.append(x.getTable().getName());
                b.append("</code></h3>");

                b.append("<p> primary key &#128273; : ");
                b.append(((Column)x.getTable().getPrimaryKey().getColumnIterator().next()).getName());
                b.append("</p>");

                b.append("<table border=\"1px solid #ccc\">");
                b.append("<thead>");
                b.append("<tr>");
                b.append("<th>Name</th>");
                b.append("<th>Type</th>");
                b.append("<th>SQL name</th>");
                b.append("<th>SQL Type</th>");
                b.append("<th>Nullable</th>");
                b.append("<th>Unique</th>");
                b.append("<th>Format Constraint</th>");
                b.append("<th>Check Constraint</th>");
                b.append("</thead>");
                b.append("<tbody>");

                Iterator propIterator = x.getPropertyIterator();

                while(propIterator.hasNext()) {
                    Property prop = (Property)propIterator.next();

                    if(!prop.getColumnIterator().hasNext()) {
                        continue;
                    }

                    b.append("<tr>");
                    b.append("<td>");
                    b.append(prop.getName());
                    b.append("</td>");

                    b.append("<td>");
                    Column column = (Column)prop.getColumnIterator().next();
                    if(prop.getType().isAssociationType()) {
                        b.append("<a href=\"#" +  prop.getType().getReturnedClass().getSimpleName() + "\">"+prop.getType().getReturnedClass().getSimpleName()+"</a>");
                    } else {
                        b.append(prop.getType().getName());
                    }
                    b.append("</td>");

                    b.append("<td>");
                    b.append(column.getName());
                    b.append("</td>");

                    b.append("<td>");
                    b.append(column.getSqlType(dialect, metadataimpl));
                    b.append("</td>");


                    b.append("<td>");
                    b.append(column.isNullable() ? "&#10004" : "");
                    b.append("</td>");

                    b.append("<td>");
                    b.append(column.isUnique() ? "&#10004" : "");
                    b.append("</td>");

                    b.append("<td>");
                    b.append(getTypeConstraint(column, metadataimpl));
                    b.append("</td>");

                    b.append("<td>");
                    b.append(column.getCheckConstraint() != null ? column.getCheckConstraint() : "");
                    b.append("</td>");

                    b.append("</tr>");


                }

                b.append("</tbody>");
                b.append("</table>");




            });

            b.append("<h1> Association tables </h1>");
            metadataimpl.getCollectionBindings().forEach(x -> {
                b.append("<h2>");
                b.append(x.getCollectionTable().getName());


                b.append("</h2>");

                Iterator columnIterator = x.getCollectionTable().getColumnIterator();
                while(columnIterator.hasNext()) {
                    Column column = (Column)columnIterator.next();

                    b.append(column.getName());
                    b.append(column.getSqlType(dialect, metadataimpl));
                }

            });

            b.append("</body>");
            b.append("</html>");

            //File reportOutput = new File(path, "report.html");
//            FileUtils.write(reportOutput, b.toString());

        System.out.println(b.toString());

        return b.toString();
    }


    String getTypeConstraint(Column column, Mapping mapping) {
//        PostgreSQL95Dialect dialect1 = new PostgreSQL95Dialect();


        if(column.getSqlTypeCode(mapping) == Types.VARCHAR) {
            if (column.getLength() != Column.DEFAULT_LENGTH) {
                return "length = " + column.getLength();
            }
        }
        if(column.getSqlTypeCode(mapping) == Types.NUMERIC) {

            if (column.getScale() != Column.DEFAULT_SCALE || column.getPrecision() != Column.DEFAULT_PRECISION) {
                return "precision = " + column.getPrecision() + ", scale = " + column.getScale() ;
            }
        }
        return "";

    }

}
