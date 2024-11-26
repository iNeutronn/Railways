package com.railways.railways.domain.client;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.Random;
import com.railways.railways.logging.LogLevel;
import com.railways.railways.logging.Logger;

/**
 * Generates a random client with a specified ID, random first and last name,
 * a random number of tickets to buy, and a random privilege level based on weighted values.
 */
public class ClientGenerator {
    private static final List<String> FIRST_NAMES = List.of("John", "Jane", "Alice", "Bob", "Charlie", "David",
            "Eve", "Frank", "Grace", "Heidi", "Ivan", "Jack", "Kate", "Liam", "Mia", "Nina", "Oliver", "Pam", "Quinn",
            "Ruth", "Sam", "Tina", "Uma", "Vince", "Wendy", "Xander", "Yvonne", "Zack");
    private static final List<String> LAST_NAMES = List.of("Smith", "Johnson", "Williams", "Jones", "Brown", "Davis",
            "Miller", "Wilson", "Moore", "Taylor", "Anderson", "Thomas", "Jackson", "White", "Harris", "Martin",
            "Thompson", "Garcia", "Martinez", "Robinson", "Clark", "Rodriguez", "Lewis", "Lee", "Walker", "Hall",
            "Allen", "Young", "Hernandez");

    private final Map<PrivilegeEnum, Integer> privilegeWeights;
    private final Random random = new Random();

    private final Logger logger;

    /**
     * Constructs a ClientGenerator with the given privilege weights and logger.
     *
     * @param privilegeWeights A map containing the weight for each privilege level.
     * @param logger The logger to log client generation details.
     */
    public ClientGenerator(Map<PrivilegeEnum, Integer> privilegeWeights, Logger logger) {
        this.privilegeWeights = privilegeWeights;
        this.logger = logger;
    }

    /**
     * Generates a random client with the given client ID, random first and last names,
     * a random number of tickets to buy (between 1 and 5), and a random privilege.
     *
     * @param clientID The ID of the client to be generated.
     * @return A new Client object with random attributes.
     */
    public Client generateClient(int clientID) {
        String firstName = getRandomElement(FIRST_NAMES);
        String lastName = getRandomElement(LAST_NAMES);
        int ticketsToBuy = random.nextInt(5) + 1; // Random tickets (1-5)
        PrivilegeEnum privilege = getWeightedRandomPrivilege();

        logger.log("Generated client: " + firstName + " " + lastName +
                ", Tickets: " + ticketsToBuy + ", Privilege: " + privilege, LogLevel.Info);

        return new Client(clientID, firstName, lastName, ticketsToBuy, privilege);
    }

    /**
     * Selects a privilege randomly, considering the weight of each privilege.
     * This method generates a cumulative weight distribution and selects a privilege based on it.
     *
     * @return A randomly selected privilege level.
     */
    private PrivilegeEnum getWeightedRandomPrivilege() {
        int totalWeight = privilegeWeights.values().stream().mapToInt(Integer::intValue).sum();
        int randomValue = random.nextInt(totalWeight);

        int cumulativeWeight = 0;
        for (Map.Entry<PrivilegeEnum, Integer> entry : privilegeWeights.entrySet()) {
            cumulativeWeight += entry.getValue();
            if (randomValue < cumulativeWeight) {
                return entry.getKey();
            }
        }

        throw new IllegalStateException("No privilege selected. Check weights.");
    }

    /**
     * Helper method to select a random element from a list.
     *
     * @param list The list from which to pick a random element.
     * @param <T> The type of the elements in the list.
     * @return A randomly selected element from the list.
     */
    private <T> T getRandomElement(List<T> list) {
        return list.get(random.nextInt(list.size()));
    }
}
