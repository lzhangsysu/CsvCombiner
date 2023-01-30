package com.pmg.csvcombiner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CsvCombiner {
    final static String FILENAME = "filename";
    final static String CSV_DELIMITER = ",";

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please provide an input file path.");
            return;
        }

        List<File> inputFiles = new ArrayList<>();
        for (String arg : args) {
            inputFiles.add(new File(arg));
        }
        String output = combineFiles(inputFiles);
        System.out.println(output);
    }

    /**
     * Combine files into a single file and appending file name for each row
     * */
    public static String combineFiles(List<File> files) {
        String[] headers = null;
        String[] row;
        List<String[]> rows = new ArrayList<>();

        for (File file : files) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;

                // read first line to construct header
                if ((line = reader.readLine()) != null) {
                    if (headers == null) {
                        headers = constructHeaders(line);
                    }
                }

                // read rest of file to construct rows
                while ((line = reader.readLine()) != null) {
                    row = constructRowWithFileName(file, line);
                    rows.add(row);
                }
            } catch (IOException e) {
                System.err.println("Error reading file: " + file.getName());
                e.printStackTrace();
            }
        }

        // generate combined output string
        return generateCombinedOutput(headers, rows);
    }

    /**
     * Construct headers for combined file as array of strings
     * */
    private static String[] constructHeaders(String line) {
        String[] cells = line.split(CSV_DELIMITER);
        String[] headers = new String[cells.length+1];
        System.arraycopy(cells, 0, headers, 0, cells.length);
        headers[cells.length] = FILENAME;
        return headers;
    }

    /**
     * Construct row for combined file as array of strings
     * */
    private static String[] constructRowWithFileName(File file, String line) {
        String[] cells = line.split(CSV_DELIMITER);
        String filename = Paths.get(file.getName()).getFileName().toString();
        cells = Arrays.copyOf(cells, cells.length+1);
        cells[cells.length-1] = filename;
        return cells;
    }

    /**
     * Construct output file from headers and contents
     * */
    private static String generateCombinedOutput(String[] headers, List<String[]> content) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.join(CSV_DELIMITER, headers)).append(System.lineSeparator());
        for (String[] row : content) {
            sb.append(String.join(CSV_DELIMITER, row)).append(System.lineSeparator());
        }
        return sb.toString();
    }
}
