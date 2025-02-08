import java.io.*;
import java.util.*;

public class DriverDatabase {
    private static final String FILE_NAME = "drivers.txt";

    public static void saveDrivers(List<Driver> drivers) {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(FILE_NAME), "UTF-8"))) {
            for (Driver d : drivers) {
                writer.write(d.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving drivers: " + e.getMessage());
        }
    }

    public static List<Driver> loadDrivers() {
        List<Driver> drivers = new ArrayList<>();
        File file = new File(FILE_NAME);

        if (!file.exists()) {
            return drivers;
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(FILE_NAME), "UTF-8"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    drivers.add(new Driver(parts[0], parts[1], parts[2], parts[3]));
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading drivers: " + e.getMessage());
        }
        return drivers;
    }
}
