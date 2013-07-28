/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.stutiguias.mcpk;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

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
                String bonus = plugin.pkbonus.get(plugin.MCPlayers.get(killer).getKills());
                String[] ids = bonus.split(",");
                for (int i = 0; i < ids.length; i++) {
                    int amount = 1;
                    short data = 0;
                    ItemStack Item = new ItemStack(Integer.parseInt(ids[i]), amount , data );
                    _Pkiller.getInventory().addItem(Item);
                    _Pkiller.updateInventory();
                }
                _Pkiller.sendMessage("You get a Bonus this kill");
            }
    }
    
}
