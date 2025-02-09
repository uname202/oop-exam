package FinanceManager;

import java.util.*;
import java.io.*;
import java.time.LocalDateTime;
import com.google.gson.*;
import com.google.gson.stream.*;
import User.User;
import Wallet.Wallet;

public class FinanceManager {
    private final Map<String, User> userRegistry;
    private static final String DATA_DIRECTORY = "userdata";

    public FinanceManager() {
        this.userRegistry = new HashMap<>();
    }

    public boolean createAccount(String username, String password) {
        return userRegistry.putIfAbsent(username, new User(username, password)) == null;
    }

    public Optional<User> authenticateUser(String username, String password) {
        return Optional.ofNullable(userRegistry.get(username))
            .filter(user -> user.getPassword().equals(password));
    }

    public void recordIncome(User user, double amount, String category) {
        user.getWallet().recordIncome(amount, category);
    }

    public void recordExpense(User user, double amount, String category) {
        user.getWallet().recordExpense(amount, category);
    }

    public void setCategoryBudget(User user, String category, double limit) {
        user.getWallet().setCategoryBudget(category, limit);
    }

    public void displayFinancialReport(User user) {
        Wallet wallet = user.getWallet();
        System.out.println("\nСТАТИСТИКА:");
        System.out.println("Общий баланс: " + wallet.getCurrentBalance());

        System.out.println("\nДОХОДЫ ПО КАТЕГОРИЯМ:");
        wallet.getIncomeByCategory().forEach((category, amount) ->
            System.out.println(category + ": " + amount));

        System.out.println("\nРАСХОДЫ ПО КАТЕГОРИЯМ:");
        wallet.getExpenseByCategory().forEach((category, amount) -> {
            System.out.println(category + ": " + amount);
            Double budget = wallet.getBudgetLimits().get(category);
            if (budget != null) {
                System.out.println("Бюджет: " + budget + " (Осталось: " + (budget - amount) + ")");
            }
        });
    }

    public void persistData() {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, new TypeAdapter<LocalDateTime>() {
                    @Override
                    public void write(JsonWriter jsonWriter, LocalDateTime localDateTime) throws IOException {
                        jsonWriter.value(localDateTime.toString());
                    }

                    @Override
                    public LocalDateTime read(JsonReader jsonReader) throws IOException {
                        return LocalDateTime.parse(jsonReader.nextString());
                    }
                })
                .create();
        createDataDirectoryIfNotExists();

        userRegistry.values().forEach(user -> {
            String filePath = DATA_DIRECTORY + "/" + user.getUsername() + ".json";
            try (FileWriter writer = new FileWriter(filePath)) {
                gson.toJson(user, writer);
            } catch (IOException e) {
                System.err.println("Ошибка сохранения данных для: " + user.getUsername());
            }
        });
    }

    public void loadPersistedData() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new TypeAdapter<LocalDateTime>() {
                    @Override
                    public void write(JsonWriter jsonWriter, LocalDateTime localDateTime) throws IOException {
                        jsonWriter.value(localDateTime.toString());
                    }

                    @Override
                    public LocalDateTime read(JsonReader jsonReader) throws IOException {
                        return LocalDateTime.parse(jsonReader.nextString());
                    }
                })
                .create();
        File dataDir = new File(DATA_DIRECTORY);
        if (!dataDir.exists()) return;

        Arrays.stream(dataDir.listFiles())
            .filter(file -> file.getName().endsWith(".json"))
            .forEach(file -> {
                try (FileReader reader = new FileReader(file)) {
                    User user = gson.fromJson(reader, User.class);
                    userRegistry.put(user.getUsername(), user);
                } catch (IOException e) {
                    System.err.println("Ошибка загрузки данных из: " + file.getName());
                }
            });
    }

    private void createDataDirectoryIfNotExists() {
        File directory = new File(DATA_DIRECTORY);
        if (!directory.exists()) {
            directory.mkdir();
        }
    }
}
