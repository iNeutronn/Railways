package com.railways.railways.Configuration;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MapGenerationHelper {

    private final EntranceLocationGenerator entranceLocationGenerator;
    private final CashPointLocationGenerator cashPointLocationGenerator;
    private final int xEntranceSize = 5;
    private final int yEntranceSize = 8;
    private final int xCashPointSize = 3;
    private final int yCashPointSize = 5;
    private final ConfigModel configModel;

    public MapGenerationHelper(ConfigModel configModel) {
        this.configModel = configModel;
        this.entranceLocationGenerator = new EntranceLocationGenerator(
                configModel.getMapSize(), xEntranceSize, yEntranceSize);

        this.cashPointLocationGenerator = new CashPointLocationGenerator(
                configModel.getMapSize(), xCashPointSize, yCashPointSize);
    }

    public void updateEntranceConfigs(int entranceCount) {
        configModel.setEntranceCount(entranceCount);
        configModel.setEntranceConfigs(entranceLocationGenerator.getLocations(entranceCount, false));
    }

    public void updateCashPointConfigs(int cashPointCount) {
        configModel.setCashPointCount(cashPointCount);
        configModel.setCashpointConfigs(cashPointLocationGenerator.getLocations(cashPointCount, false));
    }

    public void updateMapSize(MapSize mapSize) {
        configModel.setMapSize(mapSize);
        entranceLocationGenerator.updateMapSize(mapSize);
        cashPointLocationGenerator.updateMapSize(mapSize);
        updateCashPointConfigs(configModel.getCashPointCount());
        updateEntranceConfigs(configModel.getEntranceCount());
    }
}
