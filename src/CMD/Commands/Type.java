package CMD.Commands;

import CMD.Interfaces.Helpable;
import CMD.Interfaces.Runnable;
import CMD.Main;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


public class Type implements Helpable, Runnable {

    private static String defCharset = "UTF-8";

    @Override
    public void help() {

    }

    @Override
    public void run() {
        help();
    }

    @Override
    public void run(String params) {
        String[] pathsParam = params.split(" ");
        boolean changed = false;
        if (pathsParam.length == 2) {
            if (defCharset.compareToIgnoreCase(pathsParam[1]) != 0)
                changed = true;
            defCharset = pathsParam[1];
        }
        Path paramPath = rePath(Paths.get(pathsParam[0]).normalize());
        if (Files.exists(paramPath)) {
            try {
                if (changed) {
                    System.out.println("Reader charset is " + Charset.forName(defCharset));
                    ProcessBuilder pb;
                    if (defCharset.contains("cp"))
                        pb = new ProcessBuilder("cmd.exe", "/c", "chcp", defCharset.substring(2)).inheritIO();
                    else
                        pb = new ProcessBuilder("cmd.exe", "/c", "chcp", defCharset.compareToIgnoreCase("UTF-8") == 0? "65001" : defCharset).inheritIO();
                    Process p = pb.start();
                    p.waitFor();
                }
//                String consoleEncoding = System.getProperty("consoleEncoding");
//                System.out.println(consoleEncoding);
//                    try {
//                        System.setOut(new PrintStream(System.out, true, "unicode"));
//                    } catch (java.io.UnsupportedEncodingException ex) {
//                        System.err.println("Unsupported encoding set for console: "+defCharset);
//                    }

//                System.setOut(new PrintStream(System.out, true, defCharset));
//                List<String> fileLines = Files.readAllLines(paramPath, Charset.forName(defCharset));
//                for (String line : fileLines)
//                    System.out.println(line);
//                PrintStream printStream = new PrintStream(System.out, true, defCharset);

                InputStreamReader reader = new InputStreamReader(new FileInputStream(pathsParam[0]), defCharset);
                BufferedReader bufferedReader = new BufferedReader(reader);
                char[] buf = new char[256];
                PrintStream ps = new PrintStream(System.out, false, defCharset);
                while ((bufferedReader.read(buf)) != -1) {
                    ps.print(buf);
                }
                ps.println();
                ps.flush();
                reader.close();
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (OutOfMemoryError e) {
                System.out.println("File is too large");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else
            help();
    }

    private Path rePath(Path path) {
        return Main.workingDir.resolve(path).toAbsolutePath().normalize();
    }
}
