package com.castle.config;

import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

public class HustleCastleBotConfig {

    private int myPower;
    private boolean savePowers;
    private int seletorEstrategia;
    private Map<String, Integer> powerCut;


    private static HustleCastleBotConfig instance;

    public static synchronized HustleCastleBotConfig getInstance() {
        if(instance == null) {
            instance = new HustleCastleBotConfig();
        }
        return instance;
    }

    public static void loadConfig(String configFilePath) throws IOException {
        Yaml yaml = new Yaml();
        InputStream in = Files.newInputStream(Paths.get(configFilePath));
        instance = yaml.loadAs(in, HustleCastleBotConfig.class);

    }

    public int getMyPower() {
        return myPower;
    }

    public void setMyPower(int myPower) {
        this.myPower = myPower;
    }

    public boolean isSavePowers() {
        return savePowers;
    }

    public void setSavePowers(boolean savePowers) {
        this.savePowers = savePowers;
    }

    public int getSeletorEstrategia() { return seletorEstrategia; }

    public void setSeletorEstrategia(int seletorEstrategia) { this.seletorEstrategia = seletorEstrategia; }

    public Map<String, Integer> getPowerCut() { return powerCut; }

    public void setPowerCut(Map<String, Integer> powerCut) { this.powerCut = powerCut; }
}
