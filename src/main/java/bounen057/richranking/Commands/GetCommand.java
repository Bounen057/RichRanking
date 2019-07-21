package bounen057.richranking.Commands;

import bounen057.richranking.RichRanking;
import bounen057.richranking.Util.GetRanking;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GetCommand implements CommandExecutor {
    RichRanking pl;

    public GetCommand(RichRanking pl) {
        this.pl = pl;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player p = (Player) sender;

        //ランキング報酬の受け取り
        if(args.length == 0){
            int amount = pl.GetVaultdata().getConfig().getInt("Rich."+p.getUniqueId().toString());

            if(amount == 0){
                p.sendMessage(pl.logo + "§4受け取れる報酬はありません!");
                return false;
            }

            for(int i = 1;i < amount + 1;i++){
                if(amount - i >= 5) {
                    p.getInventory().addItem(pl.GetConfig().getConfig().getItemStack("reward.gold"));
                    i += 5;
                }

                if(i <= amount){
                    p.getInventory().addItem(pl.GetConfig().getConfig().getItemStack("reward.silver"));
                }
            }

            pl.GetVaultdata().getConfig().set("Rich."+p.getUniqueId().toString(),0);

            return false;
        }

        if(!p.hasPermission("RichRanking.admin")){
            return false;
        }

        if(args[0].equals("help")){
            p.sendMessage("§b§l[§d§l§m====§f " + "§e§lRichRanking HELP" + "§d§l §m===§b§l]");
            p.sendMessage("§b/rr ranking <順位> §7-ランキングの表示(Debug用)");
            p.sendMessage("§b/rr aggregate §7-ランキングの集計(Debug用)");
            p.sendMessage("§b/rr setitem <id> §7-報酬アイテムのセット(0=gold,1=silver)");
        }

        if(args[0].equals("ranking")){
            try {
                p.sendMessage(new GetRanking(pl).get(Integer.parseInt(args[1])));
            }catch (Exception e){
                p.sendMessage(pl.logo+"§4ERROR:正しい値を入力してね!");
            }
        }

        if(args[0].equals("aggregate")){
            new GetRanking(pl).Aggregate();
        }

        if(args[0].equals("setitem")){
            ItemStack item = p.getInventory().getItemInMainHand();

            switch (Integer.parseInt(args[1])) {
                case 0:
                    pl.GetConfig().getConfig().set("reward.gold",item);
                    break;
                case 1:
                    pl.GetConfig().getConfig().set("reward.silver",item);
            }

            p.sendMessage(pl.logo+"§a登録できました!");
            pl.GetConfig().saveConfig();
        }

        return false;
    }
}
