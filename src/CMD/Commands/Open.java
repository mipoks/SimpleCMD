package CMD.Commands;

import CMD.Interfaces.Helpable;
import CMD.Interfaces.Runnable;
import CMD.Main;
import javafx.util.Pair;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

public class Open implements Runnable, Helpable {

    public HashMap<String, String> hashMap;
    private boolean inited = false;
    public String videoPlayer = "\\Program Files\\VideoLAN\\VLC\\vlc.exe";
    public String audioPlayer = "\\Program Files (x86)\\Windows Media Player\\wmplayer.exe";
    public String imageViewer = "\\Windows\\System32\\mspaint.exe";
    public String textViewer = "\\Program Files\\Notepad++\\notepad++.exe";
    public String wordViewer = "\\Program Files\\Microsoft Office\\root\\Office16\\WINWORD.EXE";
    public String archiveViewer = "\\Program Files\\WinRAR\\WinRar.exe";

    private void init() {

        hashMap = new HashMap<>();
        hashMap.put("video/x-matroska", videoPlayer);
        hashMap.put("video/mpeg", videoPlayer);
        hashMap.put("video/mp4", videoPlayer);
        hashMap.put("video/ogg", videoPlayer);
        hashMap.put("video/webm", videoPlayer);
        hashMap.put("video/x-ms-wmv", videoPlayer);
        hashMap.put("video/x-flv", videoPlayer);
        hashMap.put("video/3gpp", videoPlayer);

        hashMap.put("audio/mp4", audioPlayer);
        hashMap.put("audio/aac", audioPlayer);
        hashMap.put("audio/ogg", audioPlayer);
        hashMap.put("audio/vorbis", audioPlayer);
        hashMap.put("audio/x-ms-wma", audioPlayer);
        hashMap.put("audio/mpeg", audioPlayer);
        hashMap.put("audio/webm", audioPlayer);

        hashMap.put("image/gif", imageViewer);
        hashMap.put("image/jpeg", imageViewer);
        hashMap.put("image/pjpeg", imageViewer);
        hashMap.put("image/png", imageViewer);
        hashMap.put("image/svg+xml", imageViewer);
        hashMap.put("image/webp", imageViewer);

        hashMap.put("text/html", textViewer);
        hashMap.put("text/css", textViewer);
        hashMap.put("text/cmd", textViewer);
        hashMap.put("text/plain", textViewer);
        hashMap.put("text/php", textViewer);
        hashMap.put("text/xml", textViewer);
        hashMap.put("text/cache-manifest", textViewer);

        hashMap.put("application/json", textViewer);
        hashMap.put("application/javascript", textViewer);
        hashMap.put("application/zip", archiveViewer);
        hashMap.put("application/gzip", archiveViewer);
        hashMap.put("application/xml", textViewer);
        hashMap.put("application/msword", wordViewer);

        inited = true;
    }

    @Override
    public void help() {
        System.out.printf("%-10s %-10s", "OPEN", "PathToFile");
        System.out.printf("%15s%25s\n", "Example:", "OPEN \\Users\\pc\\myfile.mp3");
    }

    private void openFile(String typeFormat, Path path) throws IOException {
        if (hashMap.containsKey(typeFormat)) {
            String opener = hashMap.get(typeFormat);
            Runtime.getRuntime().exec(opener + " " + path.toString());
        } else {
            System.out.println("This program does not support such type or format");
        }
    }


    private Path rePath(Path path) {
        return (Main.workingDir.resolve(path).toAbsolutePath().normalize());
    }

    @Override
    public void run() {
        help();
    }

    @Override
    public void run(String path) {
        if (!inited)
            init();
        Path paramPath = rePath(Paths.get(path).normalize());
        if (Files.exists(paramPath)) {
            try {
                String fileType = Files.probeContentType(paramPath);
                System.out.println(fileType);
                if (fileType != null) {
                    openFile(fileType, paramPath);
                } else {
                    System.out.println("This program does not support such type or format");
                }
            } catch (IOException e) {
                System.out.println("Unexpected file or situation");
            }
        }
        else
            help();
    }
}
