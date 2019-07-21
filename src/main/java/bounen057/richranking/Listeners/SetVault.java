package bounen057.richranking.Listeners;

import bounen057.richranking.RichRanking;
import bounen057.richranking.Util.GetRanking;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Calendar;

public class SetVault extends BukkitRunnable {
    RichRanking pl;
    JavaPlugin JavaPl;//BukkitのAPIにアクセスするためのJavaPlugin

    public SetVault(JavaPlugin JavaPl,RichRanking pl) {
        this.pl = pl;
        this.JavaPl = JavaPl;
    }

    @Override
    public void run() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            pl.GetVaultdata().getConfig().set("Vault." + p.getUniqueId().toString(),pl.get(p));
            pl.GetVaultdata().getConfig().set("UUIDs."+p.getName(),p.getUniqueId().toString());
        }

        Calendar cTime = Calendar.getInstance();

        if(cTime.get(Calendar.HOUR_OF_DAY) == 19 && cTime.get(Calendar.MINUTE) == 30 && cTime.get(Calendar.SECOND) == 1){
            new GetRanking(pl).Aggregate();
        }

        pl.GetVaultdata().saveConfig();
    }
}
