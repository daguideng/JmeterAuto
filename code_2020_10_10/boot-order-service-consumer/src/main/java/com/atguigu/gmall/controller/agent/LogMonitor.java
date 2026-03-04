package com.atguigu.gmall.controller.agent;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LogMonitor {
    private static final String LOG_DIRECTORY = "/Users/dengdagui/Documents/4444/log";
    private static final String KEYWORD = "Error";
    private static final int LINES_BEFORE_EXCEPTION = 30;

    public static void main(String[] args) {
        File directory = new File(LOG_DIRECTORY);
        File[] logFiles = directory.listFiles((dir, name) -> name.endsWith(".log"));

        if (logFiles != null) {
            for (File logFile : logFiles) {
                monitorLogFile(logFile);
            }
        }
    }

    private static void monitorLogFile(File logFile) {
        try (BufferedReader reader = new BufferedReader(new FileReader(logFile))) {
            String line;
            List<String> buffer = new ArrayList<>();
            boolean exceptionFound = false;

            while ((line = reader.readLine()) != null) {
                buffer.add(line);

                if (line.toLowerCase().contains(KEYWORD)) {
                    exceptionFound = true;
                    printExceptionDetails(buffer);
                }

                if (buffer.size() > LINES_BEFORE_EXCEPTION) {
                    buffer.remove(0); // Remove oldest line
                }
            }

            if (!exceptionFound) {
                System.out.println("No exception found in " + logFile.getName());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void printExceptionDetails(List<String> buffer) {
        System.out.println("Exception found:");
        for (String line : buffer) {
            System.out.println(line);
        }
        System.out.println("----------------------------------------");
    }
}
