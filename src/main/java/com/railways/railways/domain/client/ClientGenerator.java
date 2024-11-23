package com.railways.railways.domain.client;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.Random;

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

    public ClientGenerator(Map<PrivilegeEnum, Integer> privilegeWeights) {
        this.privilegeWeights = privilegeWeights;
    }

    // Generate a random client
    public Client generateClient(int clientID) {
        String firstName = getRandomElement(FIRST_NAMES);
        String lastName = getRandomElement(LAST_NAMES);
        int ticketsToBuy = random.nextInt(5) + 1; // Random tickets (1-5)
        Point position = new Point();
        PrivilegeEnum privilege = getWeightedRandomPrivilege();

        return new Client(clientID, firstName, lastName, ticketsToBuy, position, privilege);
    }

    // Helper to select a weighted random privilege
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

    // Helper to select a random element from a list
    private <T> T getRandomElement(List<T> list) {
        return list.get(random.nextInt(list.size()));
    }
}
