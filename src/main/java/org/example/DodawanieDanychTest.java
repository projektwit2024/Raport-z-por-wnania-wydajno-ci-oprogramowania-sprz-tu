package org.example;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DodawanieDanychTest {

    // Metoda testująca dodawanie danych do MySQL
    @Test
    public void testDodawaniaDanychMySQL() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "1234");
            PreparedStatement statement = connection.prepareStatement("INSERT INTO tabela (kolumna) VALUES (?)");

            long startTime = System.currentTimeMillis();

            for (int i = 0; i < 100000; i++) {
                statement.setString(1, "Dane " + i);
                statement.executeUpdate();
            }

            long endTime = System.currentTimeMillis();
            long executionTime = endTime - startTime;

            System.out.println("Czas dodawania danych do MySQL: " + executionTime + " ms");

            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Metoda testująca dodawanie danych do MongoDB
    @Test
    public void testDodawaniaDanychMongoDB() {
        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            MongoDatabase database = mongoClient.getDatabase("testdb");
            MongoCollection<Document> collection = database.getCollection("collection");

            long startTime = System.currentTimeMillis();

            for (int i = 0; i < 100000; i++) {
                Document document = new Document("field", "value" + i);
                collection.insertOne(document);
            }

            long endTime = System.currentTimeMillis();
            long executionTime = endTime - startTime;

            System.out.println("Czas dodawania danych do MongoDB: " + executionTime + " ms");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
