package com.pmg.csvcombiner;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CsvCombinerTest {
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    // Create File Utility
    private File createTmpFile() throws Exception {
        File tmpfile = temporaryFolder.newFile();
        tmpfile.deleteOnExit();
        return tmpfile;
    }

    // Write File Utility
    private File createInputFile(String input) throws Exception {
        File file =  createTmpFile();

        OutputStreamWriter fileWriter =
                new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);

        fileWriter.write(input);

        fileWriter.close();
        return file;
    }

    @Test
    public void test_combineOneFile() throws Exception {
        String input = "email_hash,category" + System.lineSeparator() +
                "b9f6f,Satchels" +  System.lineSeparator() +
                "c2b5f,Purses" + System.lineSeparator() +
                "faaee,Purses" + System.lineSeparator();

        File inputFile = createInputFile(input);
        List<File> inputFiles = new ArrayList() {{ add(inputFile); }};
        String filename = Paths.get(inputFile.getName()).getFileName().toString();

        String expected = "email_hash,category" + CsvCombiner.CSV_DELIMITER + CsvCombiner.FILENAME + System.lineSeparator() +
                "b9f6f,Satchels" + CsvCombiner.CSV_DELIMITER + filename + System.lineSeparator() +
                "c2b5f,Purses" + CsvCombiner.CSV_DELIMITER + filename + System.lineSeparator() +
                "faaee,Purses" + CsvCombiner.CSV_DELIMITER + filename + System.lineSeparator();

        String output = CsvCombiner.combineFiles(inputFiles);
        assertEquals(output, expected);
    }

    @Test
    public void test_combineOneFileNoEntry() throws Exception {
        String input = "email_hash,category" + System.lineSeparator();
        File inputFile = createInputFile(input);
        List<File> inputFiles = new ArrayList() {{ add(inputFile); }};

        String expected = "email_hash,category" + CsvCombiner.CSV_DELIMITER + CsvCombiner.FILENAME + System.lineSeparator();

        String output = CsvCombiner.combineFiles(inputFiles);
        assertEquals(output, expected);
    }

    @Test
    public void test_combineTwoFiles() throws Exception {
        String input1 = "email_hash,category" + System.lineSeparator() +
                "b9f6f,Satchels" +  System.lineSeparator() +
                "c2b5f,Purses" + System.lineSeparator();
        String input2 = "email_hash,category" + System.lineSeparator() +
                "b9f6f,Blouses" + System.lineSeparator() +
                "c2b5f,Shirts" + System.lineSeparator();

        File inputFile1 = createInputFile(input1);
        File inputFile2 = createInputFile(input2);
        String filename1 = Paths.get(inputFile1.getName()).getFileName().toString();
        String filename2 = Paths.get(inputFile2.getName()).getFileName().toString();
        List<File> inputFiles = new ArrayList() {{
            add(inputFile1);
            add(inputFile2);
        }};

        String expected = "email_hash,category" + CsvCombiner.CSV_DELIMITER + CsvCombiner.FILENAME + System.lineSeparator() +
                "b9f6f,Satchels" + CsvCombiner.CSV_DELIMITER + filename1 + System.lineSeparator() +
                "c2b5f,Purses" + CsvCombiner.CSV_DELIMITER + filename1 + System.lineSeparator() +
                "b9f6f,Blouses" + CsvCombiner.CSV_DELIMITER + filename2 + System.lineSeparator() +
                "c2b5f,Shirts" + CsvCombiner.CSV_DELIMITER + filename2 + System.lineSeparator();
        String output = CsvCombiner.combineFiles(inputFiles);
        assertEquals(output, expected);
    }

    @Test
    public void test_combineTwoFilesWithOneEmpty() throws Exception {
        String input1 = "email_hash,category" + System.lineSeparator();
        String input2 = "email_hash,category" + System.lineSeparator() +
                "b9f6f,Blouses" + System.lineSeparator() +
                "c2b5f,Shirts" + System.lineSeparator();

        File inputFile1 = createInputFile(input1);
        File inputFile2 = createInputFile(input2);
        String filename2 = Paths.get(inputFile2.getName()).getFileName().toString();
        List<File> inputFiles = new ArrayList() {{
            add(inputFile1);
            add(inputFile2);
        }};

        String expected = "email_hash,category" + CsvCombiner.CSV_DELIMITER + CsvCombiner.FILENAME + System.lineSeparator() +
                "b9f6f,Blouses" + CsvCombiner.CSV_DELIMITER + filename2 + System.lineSeparator() +
                "c2b5f,Shirts" + CsvCombiner.CSV_DELIMITER + filename2 + System.lineSeparator();
        String output = CsvCombiner.combineFiles(inputFiles);
        assertEquals(output, expected);
    }

    @Test
    public void test_combineThreeFiles() throws Exception {
        String input1 = "email_hash,category" + System.lineSeparator() +
                "b9f6f,Satchels" +  System.lineSeparator() +
                "c2b5f,Purses" + System.lineSeparator();
        String input2 = "email_hash,category" + System.lineSeparator() +
                "b9f6f,Blouses" + System.lineSeparator() +
                "c2b5f,Shirts" + System.lineSeparator();
        String input3 = "email_hash,category" + System.lineSeparator() +
                "a5d5c,Electronics" + System.lineSeparator();

        File inputFile1 = createInputFile(input1);
        File inputFile2 = createInputFile(input2);
        File inputFile3 = createInputFile(input3);
        String filename1 = Paths.get(inputFile1.getName()).getFileName().toString();
        String filename2 = Paths.get(inputFile2.getName()).getFileName().toString();
        String filename3 = Paths.get(inputFile3.getName()).getFileName().toString();
        List<File> inputFiles = new ArrayList() {{
            add(inputFile1);
            add(inputFile2);
            add(inputFile3);
        }};

        String expected = "email_hash,category" + CsvCombiner.CSV_DELIMITER + CsvCombiner.FILENAME + System.lineSeparator() +
                "b9f6f,Satchels" + CsvCombiner.CSV_DELIMITER + filename1 + System.lineSeparator() +
                "c2b5f,Purses" + CsvCombiner.CSV_DELIMITER + filename1 + System.lineSeparator() +
                "b9f6f,Blouses" + CsvCombiner.CSV_DELIMITER + filename2 + System.lineSeparator() +
                "c2b5f,Shirts" + CsvCombiner.CSV_DELIMITER + filename2 + System.lineSeparator() +
                "a5d5c,Electronics" + CsvCombiner.CSV_DELIMITER + filename3 + System.lineSeparator();
        String output = CsvCombiner.combineFiles(inputFiles);
        assertEquals(output, expected);
    }
}
