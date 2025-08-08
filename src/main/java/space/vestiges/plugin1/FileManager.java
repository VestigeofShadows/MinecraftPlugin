package space.vestiges.plugin1;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileManager {

    private File pluginFolder;
    private File pluginFile;

    // Constructor sets pluginFolder, and pluginFile
    public FileManager (){
        this.pluginFolder = Plugin1.getInstance().getDataFolder();
        this.pluginFile = new File(pluginFolder, "player_stats.json");
    }

    // Method that checks if folder exist, and if file exist, if not create them
    public void initStorage() {
        if (!pluginFolder.exists()) {
            pluginFolder.mkdirs();
            // If no storage, create storage json file
        }
        if(!pluginFile.exists()) {
            try {
                pluginFile.createNewFile();
                try (FileWriter writer = new FileWriter(pluginFile)) {
                    writer.write("{}");
                }
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Boolean checkFolder() {
        return pluginFolder.exists();
    }
    public Boolean checkFile() {
        return pluginFile.exists();
    }

    // Getters, you don't set the folder again, that would be insane
    public File getPluginFolder() {
        return pluginFolder;
    }
    public File getPluginFile() {
        return pluginFile;
    }
    // Creates folder, creates file, allow file editing of files


}
