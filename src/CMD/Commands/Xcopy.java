package CMD.Commands;

import CMD.Interfaces.Helpable;
import CMD.Interfaces.Runnable;
import CMD.Main;
import javafx.util.Pair;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

import static java.nio.file.Files.*;

public class Xcopy implements Runnable, Helpable {
    private int dirs = 0;
    private int files = 0;
    @Override
    public void help() {
        System.out.printf("%-10s %-10s", "XCOPY", "FromPath ToPath");
        System.out.printf("%15s%50s\n", "Example:", "XCOPY myfile.txt \\Users\\ThisPC\\Desktop\\tofile.txt");
    }

    @Override
    public void run() {
        help();
    }

    private Pair<Integer, Integer> copyFile(Path source, Path dest, CopyOption options) {
        files = 0;
        dirs = 0;
        try {
            Files.walkFileTree(source, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
                        throws IOException {
                    createDirectories(dest.resolve(source.relativize(dir)));
                    dirs++;
                    return FileVisitResult.CONTINUE;
                }
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                        throws IOException {
                    copy(file, dest.resolve(source.relativize(file)), options);
                    files++;
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (DirectoryNotEmptyException e) {
            System.out.print("Directory is not empty. Enter another one.");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Pair<>(files, dirs);
    }
    @Override
    public void run(String params) {
        String[] pathsParam = params.split(" ");
        if (pathsParam.length != 2) {
            System.out.println("Invalid arguments. Enter 2 paths: path_from, path_to");
            help();
            return;
        }
        Path source = rePath(Paths.get(pathsParam[0]));
        Path dest = rePath(Paths.get(pathsParam[1]));
        Pair copied = new Pair(0, 0);
        if (Files.exists(source)) {
            if (Files.notExists(dest)) {
                copied = copyFile(source, dest, StandardCopyOption.REPLACE_EXISTING);
            }
            else {
                System.out.println("File is already exist. Choose another name");
                help();
            }
        }
        else {
            if (Files.isDirectory(source))
                copied = copyFile(source, dest, StandardCopyOption.REPLACE_EXISTING);
            else {
                System.out.println("Invalid arguments. Enter 2 paths: path_from, path_to");
                help();
            }
        }
        System.out.println("Copied: " + copied.getKey() + " files and " + copied.getValue() + " dirs.");
    }

    private Path rePath(Path path) {
        return (Main.workingDir.resolve(path).toAbsolutePath().normalize());
    }
}
