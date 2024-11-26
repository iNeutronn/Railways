package com.railways.railways.Configuration;

import com.railways.railways.domain.station.Direction;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * MapGenerationHelper is responsible for managing and updating the configurations for entrances and cash points
 * on the map. It interacts with the EntranceLocationGenerator and CashPointLocationGenerator to generate and
 * update the placement of entrances and cash points within the given map dimensions.
 */
@Component
public class MapGenerationHelper {
    // Entrance location generator for creating and updating entrance configurations
    private final EntranceLocationGenerator entranceLocationGenerator;
    // Cash point location generator for creating and updating cash point configurations
    private final CashPointLocationGenerator cashPointLocationGenerator;
    // Predefined size for entrances (width)
    private final int xEntranceSize = 1;
    // Predefined size for entrances (height)
    private final int yEntranceSize = 1;
    // Predefined size for cash points (width)
    private final int xCashPointSize = 1;
    // Predefined size for cash points (height)
    private final int yCashPointSize = 1;
    // Configuration model that holds the current settings for the map
    private final ConfigModel configModel;

    /**
     * Constructor for MapGenerationHelper. Initializes entrance and cash point location generators
     * with the current map size and predefined entrance and cash point dimensions.
     *
     * @param configModel the configuration model that holds the map's settings
     */
    public MapGenerationHelper(ConfigModel configModel) {
        this.configModel = configModel;
        this.entranceLocationGenerator = new EntranceLocationGenerator(
                configModel.getMapSize(), xEntranceSize, yEntranceSize);

        this.cashPointLocationGenerator = new CashPointLocationGenerator(
                configModel.getMapSize(), xCashPointSize, yCashPointSize);
    }

    /**
     * Updates the entrance configurations by generating new entrance locations.
     * The number of entrances is based on the provided entrance count.
     *
     * @param entranceCount the number of entrances to be configured
     */
    public void updateEntranceConfigs(int entranceCount) {
        configModel.setEntranceCount(entranceCount);
        configModel.setEntranceConfigs(entranceLocationGenerator.getLocations(entranceCount, false));
    }

    /**
     * Updates the cash point configurations by generating new cash point locations.
     * The number of cash points is based on the provided cash point count.
     *
     * @param cashPointCount the number of cash points to be configured
     */
    public void updateCashPointConfigs(int cashPointCount) {
        configModel.setCashPointCount(cashPointCount);
        cashPointLocationGenerator.reset();
        configModel.setCashpointConfigs(cashPointLocationGenerator.getLocations(cashPointCount, false));
        configModel.setReservCashPointConfig(cashPointLocationGenerator.getConfigByDirection(Direction.Left));
    }

    /**
     * Updates the map size and regenerates the entrance and cash point configurations
     * based on the new map dimensions.
     *
     * @param mapSize the new dimensions of the map
     */
    public void updateMapSize(MapSize mapSize) {
        configModel.setMapSize(mapSize);
        entranceLocationGenerator.updateMapSize(mapSize);
        cashPointLocationGenerator.updateMapSize(mapSize);
        updateCashPointConfigs(configModel.getCashPointCount());
        updateEntranceConfigs(configModel.getEntranceCount());
    }
}
