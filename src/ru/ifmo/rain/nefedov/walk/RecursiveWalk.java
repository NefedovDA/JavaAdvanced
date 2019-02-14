package ru.ifmo.rain.nefedov.walk;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class RecursiveWalk extends BaseWalk {
    public static void main(String[] args) {
        (new RecursiveWalk()).walk(args);
    }

    @Override
    protected void processPathStr(String pathStr, BufferedWriter bufferedWriter) throws IOException {
        try {
            Path path = Path.of(pathStr);
            if (Files.isDirectory(path)) {
                Files.walkFileTree(path, new SimpleFileVisitor<>() {
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                        processPathStr(file.toString(), bufferedWriter);
                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                        outErrorPathHash(file.toString(), bufferedWriter);
                        return FileVisitResult.CONTINUE;
                    }
                });
            } else {
                processFile(pathStr, bufferedWriter);
            }
        } catch (IllegalArgumentException | FileSystemNotFoundException | SecurityException e) {
            outErrorPathHash(pathStr, bufferedWriter);
        }
    }
}
