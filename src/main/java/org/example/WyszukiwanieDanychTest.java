package org.example;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class WyszukiwanieDanychTest {

    // Metoda testująca wyszukiwanie danych w MySQL
    @Test
    public void testWyszukiwaniaDanychMySQL() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "1234");
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM tabela WHERE kolumna = ?");
            statement.setString(1, "wartosc");

            long startTime = System.currentTimeMillis();

            ResultSet resultSet = statement.executeQuery();

            long endTime = System.currentTimeMillis();
            long executionTime = endTime - startTime;

            System.out.println("Czas wyszukiwania danych w MySQL: " + executionTime + " ms");

            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Metoda testująca wyszukiwanie danych w MongoDB
    @Test
    public void testWyszukiwaniaDanychMongoDB() {
        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            MongoDatabase database = mongoClient.getDatabase("testdb");
            long startTime = System.currentTimeMillis();
            database.getCollection("collection").find(new Document("field", "value")).first();
            long endTime = System.currentTimeMillis();
            long executionTime = endTime - startTime;
            System.out.println("Czas wyszukiwania danych w MongoDB: " + executionTime + " ms");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
