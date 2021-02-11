package net.Cyberhub.tkdkid1000.gamemaker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

import com.google.gson.Gson;

import net.Cyberhub.tkdkid1000.CyberhubMinigames;

public class GameMaker {

	private File dataFolder;
	
	public GameMaker(CyberhubMinigames cyberhubminigames) {
		this.dataFolder = cyberhubminigames.getDataFolder();
	}
	@SuppressWarnings("rawtypes")
	public HashMap getGames() {
		Gson gson = new Gson();
		File f = new File(dataFolder, "games.json");
		try {
			Scanner s = new Scanner(f);
			String json = "";
			while (s.hasNextLine()) {
				json = json + s.nextLine() + "\n";
			}
			s.close();
			HashMap games = gson.fromJson(json, HashMap.class);
			return games;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@SuppressWarnings("rawtypes")
	public void saveGames(HashMap games) {
		Gson gson = new Gson();
		File f = new File(dataFolder, "games.json");
		String json = gson.toJson(games);
		try {
			FileWriter fw = new FileWriter(f);
			fw.write(json);
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void editGame(String game, String setting, Object value) {
		HashMap games = getGames();
		if (games.containsKey(game)) {
			HashMap values = (HashMap) games.get(game);
			if (setting.equalsIgnoreCase("player1")) {
				values.replace("player1", value);
			} else if (setting.equalsIgnoreCase("player2")) {
				values.replace("player2", value);
			} else if (setting.equalsIgnoreCase("kit")) {
				values.replace("kit", value);
			}
			games.replace(game, values);
			saveGames(values);
		}
	}
	
	public void createGame() {
		
	}
}
