package CMD.Commands;

import CMD.Main;
import CMD.Interfaces.Helpable;
import CMD.Interfaces.Runnable;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Cd implements Runnable, Helpable {

    @Override
    public void run() {
        System.out.println(Main.workingDir.toString());
    }

    public void run(String params) {
        Path paramPath = rePath(Paths.get(params).normalize());
        if (Files.exists(paramPath)) {
            Main.workingDir = paramPath;
            System.out.println(Main.workingDir);
        }
        else
            System.out.println("No such directory");
    }

    @Override
    public void help() {
        System.out.printf("%-10s %-10s", "CD", "PathToDir");
        System.out.printf("%15s%15s\n", "Example:", "CD \\Users");
    }

    private Path rePath(Path path) {
        if (path.toString().charAt(0) == '\\') {
            return (path.toAbsolutePath().normalize());
        }
        else
            return (Paths.get(Main.workingDir.toString() + "\\" + path.toString()).toAbsolutePath().normalize());
    }
}
