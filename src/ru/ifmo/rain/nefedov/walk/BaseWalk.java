package ru.ifmo.rain.nefedov.walk;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.lang.String.format;

public abstract class BaseWalk {
    private static final String INCORRECT_INITIAL_ARGUMENTS_ERROR_TEMPLATE = "" +
            "Too %s initial arguments: %d\n" +
            "Please run program like this:\n" +
            "\tjava *Walk* <input file> <output file>\n";
    private static final String ERROR_WITH = "" +
            "Error with %s: \n" +
            "\t%s";
    private static final String OUTPUT_LINE_TEMPLATE = "%08x %s\n";


    protected void walk(String[] args) {
        if (args == null || args.length != 2) {
            int length = args == null ? 0 : args.length;
            System.err.println(format(
                    INCORRECT_INITIAL_ARGUMENTS_ERROR_TEMPLATE,
                    length < 2 ? "few" : "much", length));
            return;
        }

        String inputFilePathStr = args[0];
        String outputFilePathStr = args[1];

        Path inputFilePath;
        Path outputFilePath;

        try {
            inputFilePath = Path.of(inputFilePathStr);
            outputFilePath = Path.of(outputFilePathStr);

            if (!Files.exists(outputFilePath)) {
                Files.createDirectories(outputFilePath.getParent());
            }
        } catch (IllegalArgumentException | FileSystemNotFoundException | SecurityException e) {
            System.err.println(format(ERROR_WITH, "parsing initial paths", e.getMessage()));
            return;
        } catch (IOException e) {
            System.err.println(format(ERROR_WITH, "creating of output file's directory", e.getMessage()));
            return;
        }

        try (BufferedReader bufferedReader = Files.newBufferedReader(inputFilePath, StandardCharsets.UTF_8);
             BufferedWriter bufferedWriter = Files.newBufferedWriter(outputFilePath, StandardCharsets.UTF_8)) {
            try {
                String pathStr;
                while ((pathStr = bufferedReader.readLine()) != null) {
                    processPathStr(pathStr, bufferedWriter);
                }
            } catch (IOException e) {
                System.err.println(format(ERROR_WITH, "resolving initial files", e.getMessage()));
            }
        } catch (IOException e) {
            System.err.println(format(ERROR_WITH, "opening initial files", e.getMessage()));
        }
    }


    protected abstract void processPathStr(String pathStr, BufferedWriter bufferedWriter) throws IOException;

    protected void processFile(String filePathStr, BufferedWriter bufferedWriter) throws IOException {
        long hash = FNVHash.INITIAL_HASH;

        try {
            Path filePath = Path.of(filePathStr);

            try (InputStream bufferedInputStream = Files.newInputStream(filePath)) {
                byte[] buffer = new byte[1024];
                int bufferLength;
                while ((bufferLength = bufferedInputStream.read(buffer)) >= 0) {
                    hash = FNVHash.getHash(hash, buffer, bufferLength);
                }
            }

        } catch (IOException | IllegalArgumentException | FileSystemNotFoundException | SecurityException e) {
            hash = 0;
        }

        bufferedWriter.write(format(OUTPUT_LINE_TEMPLATE, hash, filePathStr));
    }

    protected void outErrorPathHash(String filePathStr, BufferedWriter bufferedWriter) throws IOException {
        bufferedWriter.write(format(OUTPUT_LINE_TEMPLATE, 0, filePathStr));
    }
}
