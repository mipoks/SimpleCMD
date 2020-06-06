package CMD.Commands;

import CMD.Interfaces.Helpable;
import CMD.Interfaces.Runnable;
import CMD.Main;
import javafx.util.Pair;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class Rd implements Runnable, Helpable {
    private int dirs = 0;
    private int files = 0;

    @Override
    public void help() {
        System.out.printf("%-10s %-10s", "RD", "PathToDelete");
        System.out.printf("%15s%29s\n", "Example:", "RD \\Users\\ThisPC\\SomeDir\\");
    }

    @Override
    public void run() {
        help();
    }

    private Pair<Integer, Integer> deleteFile(Path rdSource) throws IOException {
        files = 0;
        dirs = 0;
        Files.walkFileTree(rdSource, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                try {
                    Files.delete(file);
                    files++;
                } catch (IOException e) {
                    System.out.println(file + " can't be deleted");
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc){
                try {
                    Files.delete(dir);
                    dirs++;
                } catch (IOException e) {
                    System.out.println(dir + " can't be deleted");
                }
                return FileVisitResult.CONTINUE;
            }
        });
        return new Pair<>(files, dirs);
    }

    @Override
    public void run(String params) {
        Path rdSource = rePath(Paths.get(params));
        Pair deleted = new Pair(0, 0);
        if (Files.exists(rdSource) || Files.isDirectory(rdSource)) {
            try {
                deleted = deleteFile(rdSource);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                System.out.println("No access to the path.");
            }
        } else {
            System.out.println("Invalid argument. Enter path to delete.");
            help();
        }
        System.out.println("Deleted: " + deleted.getKey() + " files and " + deleted.getValue() + " dirs");
    }

    private Path rePath(Path path) {
        return (Main.workingDir.resolve(path).toAbsolutePath().normalize());
    }
}
