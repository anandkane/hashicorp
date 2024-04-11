package org.opus.demo.sampleapplication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

@SpringBootApplication
@RestController
@RequestMapping("/sample")
public class SampleApplication {

    @Autowired
    private Environment environment;

    @Autowired
    private DataSource dataSource;

//    @Autowired
//    private ConsulConfiguration consulConfiguration;

    @Autowired
    private ConsulConfigurationYaml consulConfigurationYaml;

    public static void main(String[] args) {
        SpringApplication.run(SampleApplication.class, args);
//        System.exit(0);
    }

    @GetMapping("/print")
    public void print() {
//        System.out.println("printing store1...");
//        System.out.println("key1: " + consulConfiguration.getKey1());
//        System.out.println("key2: " + consulConfiguration.getKey2());
        System.out.println("printing yaml-store...");
        System.out.println("key1: " + consulConfigurationYaml.getKey1());
        System.out.println("key2: " + consulConfigurationYaml.getKey2());
    }

    @Bean
    CommandLineRunner commandLineRunner() {
        return args -> {
            System.out.println("username: " + environment.getProperty("example.username"));
            System.out.println("password: " + environment.getProperty("example.password"));
            System.out.println("myapp.key1: " + environment.getProperty("myapp.key1"));

            try (Connection connection = dataSource.getConnection();
                 Statement statement = connection.createStatement()) {

                ResultSet resultSet = statement.executeQuery("SELECT * from mes.person");
                while (resultSet.next()) {
                    System.out.println(resultSet.getString("first_name"));
                }
                resultSet.close();
            }
        };
    }
}
