package org.opus.demo.sampleapplication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.vault.authentication.TokenAuthentication;
import org.springframework.vault.client.VaultEndpoint;
import org.springframework.vault.core.VaultTemplate;
import org.springframework.vault.support.Versioned;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@RestController
@RequestMapping
public class SampleApplication {

    @Autowired
    private Environment environment;

    @Autowired
    private DataSource dataSource;

    public static void main(String[] args) {
        SpringApplication.run(SampleApplication.class, args);
        System.exit(0);
    }

    @GetMapping("/test")
    public void test() throws Exception {
        VaultEndpoint vaultEndpoint = new VaultEndpoint();

        vaultEndpoint.setHost("localhost");
        vaultEndpoint.setPort(8200);
        vaultEndpoint.setScheme("https");

        // Authenticate
        VaultTemplate vaultTemplate = new VaultTemplate(
                vaultEndpoint,
                new TokenAuthentication("dev-only-token"));

        // Write a secret
        Map<String, String> data = new HashMap<>();
        data.put("password", "Hashi123");

        Versioned.Metadata createResponse = vaultTemplate
                .opsForVersionedKeyValue("secret")
                .put("my-secret-password", data);

        System.out.println("Secret written successfully.");

        // Read a secret
        Versioned<Map<String, Object>> readResponse = vaultTemplate
                .opsForVersionedKeyValue("secret")
                .get("my-secret-password");

        String password = "";
        if (readResponse != null && readResponse.hasData()) {
            password = (String) readResponse.getData().get("password");
        }

        if (!password.equals("Hashi123")) {
            throw new Exception("Unexpected password");
        }

        System.out.println("Access granted!");
    }

    @Bean
    CommandLineRunner commandLineRunner() {
        return args -> {
            try (Connection connection = dataSource.getConnection();
                 Statement statement = connection.createStatement()) {

                ResultSet resultSet = statement.executeQuery("SELECT * from mes.person");
                while (resultSet.next()) {
                    System.out.println(resultSet.getString("first_name"));
                }
                resultSet.close();

//                Statement statement1 = connection.createStatement();
//                int count = statement1.executeUpdate("delete from mes.person");
//                System.out.println("Number of records deleted: " + count);
            }
        };
    }
}
