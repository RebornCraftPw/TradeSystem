package de.codingair.tradesystem.spigot.extras.bstats;

import de.codingair.codingapi.files.ConfigFile;
import de.codingair.codingapi.files.loader.UTFConfig;
import de.codingair.tradesystem.spigot.TradeSystem;
import de.codingair.tradesystem.spigot.extras.blacklist.BlockedItem;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MetricsManager {
    public static int TRADES = 0;

    private static boolean isStandardWorldList(List<String> l) {
        if (l.size() == 2) {
            return "ExampleWorld-1".equals(l.get(0)) && "ExampleWorld-2".equals(l.get(1));
        } else return false;
    }

    private static boolean isStandardBlacklist(List<BlockedItem> l) {
        if (l.size() == 3) {
            return BlockedItem.create().material(Material.AIR).equals(l.get(0))
                    && BlockedItem.create().material(Material.AIR).displayName("&cExample").equals(l.get(1))
                    && BlockedItem.create().displayName("&cExample, which blocks all items with this strange name!").equals(l.get(2));
        } else return false;
    }

    public void start() {
        Metrics metrics = new Metrics(TradeSystem.getInstance(), 6959);

        ConfigFile file = TradeSystem.getInstance().getFileManager().getFile("Config");
        UTFConfig config = file.getConfig();

        metrics.addCustomChart(new Metrics.AdvancedPie("configuration", () -> {
            Map<String, Integer> map = new HashMap<>();

            map.put("Trade", 1);
            if (config.getBoolean("TradeSystem.Permissions", true)) map.put("Permissions", 1);
            if (!isStandardBlacklist(TradeSystem.getInstance().getTradeManager().getBlacklist())) map.put("Item blacklist", 1);
            if (!isStandardWorldList(TradeSystem.getInstance().getTradeManager().getBlockedWorlds())) map.put("Blocks worlds", 1);

            return map;
        }));

        metrics.addCustomChart(new Metrics.SingleLineChart("trades", () -> {
            int trades = TRADES;
            TRADES = 0;
            return trades;
        }));

        metrics.addCustomChart(new Metrics.SingleLineChart("layouts", () -> TradeSystem.getInstance().getLayoutManager().getPatterns().size() - 1));
    }
}
