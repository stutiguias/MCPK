/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.stutiguias.mcpk;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.logging.Level;

/**
 *
 * @author Daniel
 */
public class Bonus {
    
    Mcpk plugin;
    
    public Bonus(Mcpk instance) {
        plugin = instance;
    }
    
    public void getBonusForPK(String killer,Player _Pkiller) {
        if(plugin.pkbonus.get(plugin.MCPlayers.get(killer).getKills()) != null && plugin.EnableBonusForPK) {
            giveBonus(_Pkiller, plugin.pkbonus.get(plugin.MCPlayers.get(killer).getKills()));
            _Pkiller.sendMessage("You get a Bonus this kill");
        }
    }
    
    public void getBonusForKillPK(Player killer) {
        if(plugin.EnableBonusForKillPK && plugin.BonusForKillPK != null && !plugin.BonusForKillPK.isEmpty()) {
            giveBonus(killer, plugin.BonusForKillPK);
            killer.sendMessage("You get a Bonus for killing a PK");
        }
    }
    
    private void giveBonus(Player player, String bonus) {
        String[] materials = bonus.split(",");
        for (String materialstring : materials) {
            int amount = 1;
            Material material = Material.matchMaterial(materialstring.trim());
            if(material == null) {
                Mcpk.logger.log(Level.WARNING, "{0} Invalid bonus material: {1}", new Object[]{Mcpk.logPrefix, materialstring});
                continue;
            }
            ItemStack Item = new ItemStack(material, amount);
            player.getInventory().addItem(Item);
            player.updateInventory();
        }
    }
    
}
