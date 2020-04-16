package CMD.Commands;

import CMD.Interfaces.Helpable;
import CMD.Interfaces.Runnable;
import CMD.Main;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;

public class Dir implements Runnable, Helpable {
    @Override
    public void run() {
        printFiles(Main.workingDir);
    }
    public void run(String params) {
        Path paramPath = rePath(Paths.get(params).normalize());
        printFiles(paramPath);
    }

    public void help() {
        System.out.printf("%-10s %-10s", "DIR", "PathToDir");
        System.out.printf("%15s%15s\n", "Example:", "DIR \\Users\\pc");
    }

    private void printFiles(Path paramPath) {
        File folder = new File(paramPath.toString());
        if (Files.exists(paramPath.normalize()))
            System.out.println(paramPath.toAbsolutePath());
        else {
            System.out.println("No such directory");
        }
        File[] files = folder.listFiles();
        int len = 0;
        try {
            len = files.length;
        }
        catch (Exception ex) {
        }
        for (int i = 0; i < len; i++) {
            try {
                FileTime str = Files.getLastModifiedTime(Paths.get(files[i].getPath()));
                System.out.print(str.toString().substring(0, 10) + "    " +  str.toString().substring(12, 16) + "    ");
                System.out.println((files[i].isDirectory() == true?"DIR":"   ") + "      " + files[i].getName());
                files[i].getClass();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private Path rePath(Path path) {
        if (path.toString().charAt(0) == '\\') {
            return (path.toAbsolutePath().normalize());
        }
        else
            return (Paths.get(Main.workingDir.toString() + "\\" + path.toString()).toAbsolutePath().normalize());
    }
}
