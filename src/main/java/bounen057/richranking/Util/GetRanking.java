package bounen057.richranking.Util;

import bounen057.richranking.RichRanking;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class GetRanking {
    RichRanking pl;

    public GetRanking(RichRanking pl) {
        this.pl = pl;
    }

    public String get(int ran) {

        HashMap<String, String> mcid = new HashMap<>();
        HashMap<Double, String> uuidmoney = new HashMap<>();
        List<String> uuid = new ArrayList<>();
        List<Double> balance = new ArrayList<>();

        for (Map.Entry<String, Object> entry : pl.GetVaultdata().getConfig().getConfigurationSection("UUIDs").getValues(false).entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            mcid.put(value.toString(), key);
            uuid.add(value.toString());
        }

        for (Map.Entry<String, Object> entry : pl.GetVaultdata().getConfig().getConfigurationSection("Vault").getValues(false).entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            uuidmoney.put(Double.parseDouble(value.toString()), key);
            balance.add(Double.parseDouble(value.toString()));
        }

        Collections.sort(balance, Collections.reverseOrder());
        return mcid.get(uuidmoney.get(balance.get(ran - 1)));

    }

    // 順位を表示してアイテムを渡す
    public void Aggregate() {

        pl.GetVaultdata().reloadConfig();

        //ItemStack reward_gold = pl.GetConfig().getConfig().getItemStack("reward.gold");
        //ItemStack reward_silver = pl.GetConfig().getConfig().getItemStack("reward.silver");


        String[] top_mcid = new String[8];
        String[] top_uuid = new String[8];
        int[] top_amount = new int[8];
        Double[] top_balance = new Double[8];

        Double[] top_tax = {0.0,10.0,10.0,10.0};
        int[] add_amount = {0,5,1,1};

        for (int i = 1; i < 3 + 1; i++) {
            top_mcid[i] = get(i);
            top_uuid[i] = pl.GetVaultdata().getConfig().getString("UUIDs." + top_mcid[i]);
            top_amount[i] = pl.GetVaultdata().getConfig().getInt("Rich."+top_uuid[i]);
            top_balance[i] = pl.GetVaultdata().getConfig().getDouble("Vault." + top_uuid[i]);
        }


        Bukkit.broadcastMessage("§6§l§k||" + "§5§l§n 19:30 所持金ランキング " + "§6§l§k||");
        Bukkit.broadcastMessage("§e§l1位 §5§l" + top_mcid[1] + " §6§l所持金:" + Math.floor(top_balance[1]) + " §c税:" + Math.floor(top_balance[1] * (top_tax[1]/100) ) );
        Bukkit.broadcastMessage("§7§l2位 §5§l" + top_mcid[2] + " §6§l所持金:" + Math.floor(top_balance[2]) + " §c税:" + Math.floor(top_balance[2] * (top_tax[2]/100) ) );
        Bukkit.broadcastMessage("§7§l3位 §5§l" + top_mcid[3] + " §6§l所持金:" + Math.floor(top_balance[3]) + " §c税:" + Math.floor(top_balance[3] * (top_tax[3]/100) ) );
        Bukkit.broadcastMessage("§4§l/rr で受け取れます!");


        for(int i=1;i < 3 + 1;i++) {
            if (Bukkit.getPlayer(top_mcid[i]) != null){

                pl.withraw(Bukkit.getPlayer(top_mcid[i]),(top_balance[i] * (top_tax[i] / 100) ) );

            }else{

                pl.GetVaultdata().getConfig().set("Vault."+top_uuid[i],(top_balance[i] * (1 - top_tax[i] / 100) ) );

            }

            pl.GetVaultdata().getConfig().set("Rich." + top_uuid[i],top_amount[i] + add_amount[i]);
        }
        return;
    }
}
