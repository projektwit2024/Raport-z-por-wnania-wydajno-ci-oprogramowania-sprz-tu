package org.example;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;

public class UsuwanieDanychTest {

    // Metoda testująca usuwanie danych w MySQL
    @Test
    public void testUsuwanieDanychMySQL() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "1234");
            PreparedStatement statement = connection.prepareStatement("DELETE FROM tabela WHERE kolumna LIKE ?");

            // Usuń 100 rekordów zaczynając od 'Dane 99860'
            for (int i = 0; i < 100; i++) {
                statement.setString(1, "Dane 9986" + i);
                statement.addBatch();
            }

            long startTime = System.currentTimeMillis();

            int[] rowsDeleted = statement.executeBatch(); // Usunięcie rekordów

            long endTime = System.currentTimeMillis();
            long executionTime = endTime - startTime;

            System.out.println("Czas usuwania danych w MySQL: " + executionTime + " ms");
            System.out.println("Liczba usuniętych rekordów: " + Arrays.stream(rowsDeleted).sum());

            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Metoda testująca usuwanie danych w MongoDB
    @Test
    public void testUsuwanieDanychMongoDB() {
        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            MongoDatabase database = mongoClient.getDatabase("testdb");
            MongoCollection<Document> collection = database.getCollection("collection");

            long startTime = System.currentTimeMillis();

            // Usuń 100 rekordów zaczynając od 'Dane 99860'
            for (int i = 0; i < 100; i++) {
                collection.deleteMany(Filters.eq("field", "Dane 9986" + i));
            }

            long endTime = System.currentTimeMillis();
            long executionTime = endTime - startTime;

            System.out.println("Czas usuwania danych w MongoDB: " + executionTime + " ms");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}