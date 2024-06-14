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

public class AktualizacjaDanychTest {

    // Metoda testująca aktualizację danych w MySQL
    @Test
    public void testAktualizacjiDanychMySQL() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "1234");
            PreparedStatement statement = connection.prepareStatement("UPDATE tabela SET kolumna = ? WHERE kolumna LIKE ?");

            // Aktualizuj 100 rekordów, których wartość w kolumnie "kolumna" zaczyna się od "Dane"
            for (int i = 0; i < 100; i++) {
                statement.setString(1, "nowa_wartosc_" + i); // Nowa wartość dla aktualizacji
                statement.setString(2, "Dane " + i); // Warunek dopasowania
                statement.addBatch();
            }

            long startTime = System.currentTimeMillis();

            int[] rowsUpdated = statement.executeBatch(); // Aktualizacja rekordów

            long endTime = System.currentTimeMillis();
            long executionTime = endTime - startTime;

            System.out.println("Czas aktualizacji danych w MySQL: " + executionTime + " ms");
            System.out.println("Liczba zaktualizowanych rekordów: " + Arrays.stream(rowsUpdated).sum());

            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Metoda testująca aktualizację danych w MongoDB
    @Test
    public void testAktualizacjiDanychMongoDB() {
        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            MongoDatabase database = mongoClient.getDatabase("testdb");
            MongoCollection<Document> collection = database.getCollection("collection");

            long startTime = System.currentTimeMillis();

            // Aktualizacja 100 rekordów, których wartość w polu "field" zaczyna się od "Dane"
            for (int i = 0; i < 100; i++) {
                collection.updateOne(Filters.eq("field", "Dane " + i), new Document("$set", new Document("field", "nowa_wartosc_" + i)));
            }

            long endTime = System.currentTimeMillis();
            long executionTime = endTime - startTime;

            System.out.println("Czas aktualizacji danych w MongoDB: " + executionTime + " ms");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
