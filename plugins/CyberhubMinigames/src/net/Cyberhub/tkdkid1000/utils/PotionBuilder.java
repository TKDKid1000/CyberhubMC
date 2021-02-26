package net.Cyberhub.tkdkid1000.utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PotionBuilder {

	private int count;
	private String name;
	private List<String> lore = new ArrayList<String>();
	private List<PotionEffect> effects = new ArrayList<PotionEffect>();
	
	public PotionBuilder() {
		
	}
	
	public PotionBuilder(int count) {
		this.count = count;
	}
	
	public PotionBuilder setCount(int count) {
		this.count = count;
		return this;
	}
	
	public int getCount() {
		return this.count;
	}
	
	public PotionBuilder setName(String name) {
		this.name = name;
		return this;
	}
	
	public String getName() {
		return this.name;
	}
	
	public List<String> getLore() {
		return this.lore;
	}
	
	public PotionBuilder addLore(String line) {
		this.lore.add(line);
		return this;
	}
	
	public PotionBuilder setLore(List<String> lines) {
		this.lore.clear();
		this.lore.addAll(lines);
		return this;
	}
	
	public PotionBuilder addEffect(PotionEffectType type, int duration, int amplifier, boolean ambient, boolean particles) {
		effects.add(new PotionEffect(type, duration, amplifier, ambient, particles));
		return this;
	}
	
	public PotionBuilder addEffect(PotionEffect effect) {
		effects.add(effect);
		return this;
	}
	
	public List<PotionEffect> getEffects() {
		return this.effects;
	}
	
	public PotionBuilder setEffects(List<PotionEffect> effectlist) {
		this.effects.clear();
		this.effects.addAll(effectlist);
		return this;
	}
	
	public ItemStack build() {
		ItemStack item = new ItemStack(Material.POTION, this.count);
		PotionMeta meta = (PotionMeta) item.getItemMeta();
		meta.setDisplayName(this.name);
		meta.setLore(this.lore);
		for (PotionEffect effect : this.effects) {
			meta.addCustomEffect(effect, false);
		}
		item.setItemMeta(meta);
		return item;
	}
}
