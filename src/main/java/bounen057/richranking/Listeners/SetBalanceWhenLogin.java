package bounen057.richranking.Listeners;

import bounen057.richranking.RichRanking;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class SetBalanceWhenLogin implements Listener {
    RichRanking pl;

    public SetBalanceWhenLogin(RichRanking pl) {
        this.pl = pl;
    }

    @EventHandler
    public void OnJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        double offline_balance = pl.GetVaultdata().getConfig().getDouble("Vault." + p.getUniqueId().toString());
        double player_balance = pl.get(p);

        if(offline_balance < 100){
            return;
        }
        if(offline_balance == player_balance){
            return;
        }

        if(offline_balance > player_balance){
            pl.deposit(p,offline_balance - player_balance);
        }
        if(offline_balance < player_balance){
            pl.withraw(p,player_balance - offline_balance);
        }

        p.sendMessage("§a§l[Notice]≫§e§l あなたがオフラインの間に金額の変動がありました!");
    }

    @EventHandler
    public void OnDeath(PlayerDeathEvent e){
        Player p = e.getEntity();
        double tax = 8;

        pl.withraw(p,pl.get(p) * (tax/100));
        p.sendMessage("§a§l[Notice]≫§cあなたは死んだため所持金の"+tax+"%が失われました!");
    }
}
