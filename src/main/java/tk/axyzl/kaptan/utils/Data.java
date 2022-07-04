package tk.axyzl.kaptan.utils;

import org.bukkit.Location;
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

    public Location getLocation(String path) {
        if(this.get(path) != null) {
            String world = this.getString(path + ".world");
            System.out.println(world);
            double x = this.getDouble(path + ".x");
            double y = this.getDouble(path + ".y");
            double z = this.getDouble(path + ".z");
            double yaw = this.getDouble(path + ".yaw");
            double pitch = this.getDouble(path + ".pitch");

            Location loc = new Location(Kaptan.instance.getServer().getWorld(world), x,y,z,(float) yaw,(float) pitch);
            return loc;
        } return null;
    }
    public void setLocation(String path, Location location) {
        this.set(path + ".world", location.getWorld().getName());
        this.set(path + ".x", location.getX());
        this.set(path + ".y", location.getY());
        this.set(path + ".z", location.getZ());
        this.set(path + ".yaw", location.getYaw());
        this.set(path + ".pitch", location.getPitch());
        this.save();
    }
}
