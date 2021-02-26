package net.Cyberhub.tkdkid1000.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemBuilder {

	private Material mat;
	private int count;
	private String name;
	private List<String> lore = new ArrayList<String>();
	private Map<Enchantment, Integer> enchants = new HashMap<Enchantment, Integer>();
	private boolean unbreakable;
	private short durability;
	
	public ItemBuilder() {
		
	}
	
	public ItemBuilder(Material mat, int count) {
		this.mat = mat;
		this.count = count;
	}
	
	public ItemBuilder setMaterial(Material mat) {
		this.mat = mat;
		return this;
	}
	
	public Material getMaterial() {
		return this.mat;
	}
	
	public ItemBuilder setCount(int count) {
		this.count = count;
		return this;
	}
	
	public int getCount() {
		return this.count;
	}
	
	public ItemBuilder setName(String name) {
		this.name = name;
		return this;
	}
	
	public String getName() {
		return this.name;
	}
	
	public List<String> getLore() {
		return this.lore;
	}
	
	public ItemBuilder addLore(String line) {
		this.lore.add(line);
		return this;
	}
	
	public ItemBuilder setLore(List<String> lines) {
		this.lore.clear();
		this.lore.addAll(lines);
		return this;
	}
	
	public ItemBuilder setUnbreakable(boolean canbreak) {
		this.unbreakable = canbreak;
		return this;
	}
	
	public boolean isUnbreakable() {
		return this.unbreakable;
	}
	
	public ItemBuilder setDurability(short dura) {
		this.durability = dura;
		return this;
	}
	
	public short getDurability() {
		return this.durability;
	}
	
	public Map<Enchantment, Integer> getEnchants() {
		return this.enchants;
	}
	
	public ItemBuilder addEnchant(Enchantment ench, int level) {
		this.enchants.put(ench, level);
		return this;
	}
	
	public ItemBuilder setEnchants(Map<Enchantment, Integer> enchant) {
		this.enchants.clear();
		this.enchants.putAll(enchant);
		return this;
	}
	
	public ItemStack build() {
		ItemStack item = new ItemStack(this.mat, this.count);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(this.name);
		meta.setLore(this.lore);
		item.setDurability(this.durability);
		meta.setUnbreakable(this.unbreakable);
		for (Map.Entry<Enchantment, Integer> enchant : this.enchants.entrySet()) {
			meta.addEnchant(enchant.getKey(), enchant.getValue(), true);
		}
		item.setItemMeta(meta);
		return item;
	}
}
