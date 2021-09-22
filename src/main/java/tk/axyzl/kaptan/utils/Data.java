package tk.axyzl.kaptan.utils;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import tk.axyzl.kaptan.Kaptan;

import java.io.File;

public class Data extends YamlConfiguration {
    private File file;
    private Kaptan kaptan;

    public Data(Kaptan kaptan){
        this.kaptan = kaptan;
        file = new File(kaptan.getDataFolder(), "data.yml");

        if (!file.exists()) {
            file.getParentFile().mkdirs();
            kaptan.saveResource("data.yml", false);
        }
        reload();
    }


    public void reload() {
        try {
            this.load(this.file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            this.save(this.file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
