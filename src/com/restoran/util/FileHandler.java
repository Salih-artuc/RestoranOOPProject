package com.restoran.util;

import com.restoran.exception.FileOperationException;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileHandler {
    private static final String DATA_DIR = "data";

    static {
        try {
            Files.createDirectories(Paths.get(DATA_DIR));
        } catch (IOException e) {
            System.err.println("Data folder could not be created: " + e.getMessage());
        }
    }

    public static void writeToFile(String fileName, String content) throws FileOperationException {
        String filePath = DATA_DIR + File.separator + fileName;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(content);
            writer.newLine();
        } catch (IOException e) {
            throw new FileOperationException("File write error: " + e.getMessage());
        }
    }

    public static void overwriteFile(String fileName, String content) throws FileOperationException {
        String filePath = DATA_DIR + File.separator + fileName;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, false))) {
            writer.write(content);
        } catch (IOException e) {
            throw new FileOperationException("File write error: " + e.getMessage());
        }
    }

    public static String readFromFile(String fileName) throws FileOperationException {
        String filePath = DATA_DIR + File.separator + fileName;
        StringBuilder content = new StringBuilder();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (FileNotFoundException e) {
            return "";
        } catch (IOException e) {
            throw new FileOperationException("File read error: " + e.getMessage());
        }
        
        return content.toString();
    }

    public static boolean fileExists(String fileName) {
        String filePath = DATA_DIR + File.separator + fileName;
        return Files.exists(Paths.get(filePath));
    }
}

