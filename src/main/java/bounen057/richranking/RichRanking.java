package bounen057.richranking;

import bounen057.richranking.Commands.GetCommand;
import bounen057.richranking.Data.CustomConfig;
import bounen057.richranking.Listeners.SetBalanceWhenLogin;
import bounen057.richranking.Listeners.SetVault;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public final class RichRanking extends JavaPlugin {

    public String logo = "§6[&e&lRichRanking§6§l]";
    BukkitTask task = null;

    private CustomConfig config,vaultdata;
    @Override
    public void onEnable() {

        config = new CustomConfig(this);
        config.saveDefaultConfig();

        vaultdata = new CustomConfig(this,"vault.yml");
        vaultdata.saveDefaultConfig();

        Bukkit.getPluginCommand("rr").setExecutor(new GetCommand(this));

        task = this.getServer().getScheduler().runTaskTimer(this, new SetVault(this ,this), 0L, 20L);


        Bukkit.getPluginManager().registerEvents(new SetBalanceWhenLogin(this),this);
    }

    @Override
    public void onDisable() {
    }



    public CustomConfig GetVaultdata(){
        return vaultdata;
    }
    public CustomConfig GetConfig(){
        return config;
    }



    public void show(Player p){
        new VaultManager(this).showBalance(p.getUniqueId());
    }
    public double get(Player p){
        return new VaultManager(this).getBalance(p.getUniqueId());
    }
    public void withraw(Player p,Double money){
        new VaultManager(this).withdraw(p.getUniqueId(),money);
    }
    public void deposit(Player p,Double money){
        new VaultManager(this).deposit(p.getUniqueId(),money);
    }
}
