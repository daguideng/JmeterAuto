package com.atguigu.gmall.controller.agent;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LogFileMonitor {
    private static final String LOG_DIRECTORY = "/Users/dengdagui/Documents/4444/log";
    private static final ExecutorService executor = Executors.newCachedThreadPool();
    private static final int LINES_BEFORE_EXCEPTION = 30;

    public static void main(String[] args) {
        try {
            mainTest();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String mainTest() throws IOException {
        WatchService watchService = FileSystems.getDefault().newWatchService();
        Path logDir = Paths.get(LOG_DIRECTORY);
        logDir.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_MODIFY);

        System.out.println("Monitoring log files in directory: " + LOG_DIRECTORY);

        StringBuffer  sb = new StringBuffer();

        while (true) {
            WatchKey key;
            try {
                key = watchService.take();
            } catch (InterruptedException ex) {
                return null ;
            }

            for (WatchEvent<?> event : key.pollEvents()) {
                WatchEvent.Kind<?> kind = event.kind();
                if (kind == StandardWatchEventKinds.OVERFLOW) {
                    continue;
                }

                @SuppressWarnings("unchecked")
                WatchEvent<Path> ev = (WatchEvent<Path>) event;
                Path logFile = logDir.resolve(ev.context());

                if (logFile.toString().endsWith(".log")) {
                    sb.append(checkLogFile(logFile)).append("\n");
                  //  executor.submit(() -> checkLogFile(logFile));
                    executor.submit(() -> sb.toString());


                }
            }

            boolean valid = key.reset();
            if (!valid) {
                break;
            }
        }

         return sb.toString();
    }

    private static String checkLogFile(Path logFile) {

         String exception_tage= "error";
        StringBuffer sbf = new StringBuffer();

        try (BufferedReader reader = new BufferedReader(new FileReader(logFile.toFile()))) {
            String line;
            int lineNumber = 0;
            int exceptionLine = -1;

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (line.toLowerCase().contains(exception_tage)) {
                    exceptionLine = lineNumber;
                    System.out.println("Exception found in file: " + logFile.getFileName() + " at line " + lineNumber);
                    break; // Stop reading after the first exception is found
                }
            }


            if (exceptionLine != -1) {

                System.out.println("Printing " + LINES_BEFORE_EXCEPTION + " lines before the exception:");
                reader.lines()
                        .skip(Math.max(0, exceptionLine - LINES_BEFORE_EXCEPTION))
                        .limit(LINES_BEFORE_EXCEPTION)
                        // .forEach(System.out::println);
                        .forEach(line1 -> sbf.append(line1).append("\n"));
            }

            System.out.printf("sbf:%s", sbf.toString());
        } catch (IOException e) {
            System.err.println("Error reading file: " + logFile.getFileName());
            e.printStackTrace();
        }


        return sbf.toString();
    }
}


