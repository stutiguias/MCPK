/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.stutiguias.mcpk;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

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
                String[] materials = bonus.split(",");
                for (String materialstring : materials) {
                    int amount = 1;
                    Material material = Material.matchMaterial(materialstring);
                    ItemStack Item = new ItemStack(Objects.requireNonNull(material), amount);
                    _Pkiller.getInventory().addItem(Item);
                    _Pkiller.updateInventory();
                }
                _Pkiller.sendMessage("You get a Bonus this kill");
            }
    }
    
}
