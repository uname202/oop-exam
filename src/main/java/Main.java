import java.util.*;
import FinanceManager.FinanceManager;
import User.User;
import Wallet.Wallet;

public class Main {
    private static final FinanceManager financeManager = new FinanceManager();
    private static final Scanner scanner = new Scanner(System.in);
    private static User currentUser = null;

    public static void main(String[] args) {
        financeManager.loadPersistedData();
        while (true) {
            if (currentUser == null) {
                System.out.println("1. Войти\n2. Зарегистрироваться\n3. Выход");
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        login();
                        break;
                    case 2:
                        register();
                        break;
                    case 3:
                        financeManager.persistData();
                        System.exit(0);
                        break;
                }
            } else {
                showMainMenu();
            }
        }
    }

    private static void login() {
        System.out.print("Имя пользователя: ");
        String username = scanner.nextLine();
        System.out.print("Пароль: ");
        String password = scanner.nextLine();

        var userOptional = financeManager.authenticateUser(username, password);
        if (userOptional.isEmpty()) {
            System.out.println("Неверные учетные данные!");
        } else {
            currentUser = userOptional.get();
            System.out.println("Добро пожаловать, " + currentUser.getUsername() + "!");
        }
    }

    private static void register() {
        System.out.print("Имя пользователя: ");
        String username = scanner.nextLine();
        System.out.print("Пароль: ");
        String password = scanner.nextLine();

        if (financeManager.createAccount(username, password)) {
            System.out.println("Регистрация прошла успешно!");
        } else {
            System.out.println("Имя пользователя уже существует!");
        }
    }

    private static void showMainMenu() {
        System.out.println("\n1. Добавить доход\n2. Добавить расход\n3. Посмотреть баланс\n4. Установить бюджет\n5. Посмотреть статистику\n6. Выйти\n");
        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                addIncome();
                break;
            case 2:
                addExpense();
                break;
            case 3:
                viewBalance();
                break;
            case 4:
                setBudget();
                break;
            case 5:
                viewStatistics();
                break;
            case 6:
                logout();
                break;
        }
    }

    private static double getValidAmount() {
        while (true) {
            try {
                String input = scanner.nextLine();
                double amount = Double.parseDouble(input);
                if (amount <= 0) {
                    System.out.print("Сумма должна быть положительной. Попробуйте снова: ");
                    continue;
                }
                return amount;
            } catch (NumberFormatException e) {
                System.out.print("Неверная сумма. Пожалуйста, введите число: ");
            }
        }
    }

    private static void addIncome() {
        System.out.print("Сумма: ");
        double amount = getValidAmount();
        System.out.print("Категория: ");
        String category = scanner.nextLine();

        financeManager.recordIncome(currentUser, amount, category);
        System.out.println("Доход добавлен успешно!");
    }

    private static void addExpense() {
        System.out.print("Сумма: ");
        double amount = getValidAmount();
        System.out.print("Категория: ");
        String category = scanner.nextLine();

        financeManager.recordExpense(currentUser, amount, category);
        System.out.println("Расход добавлен успешно!");
    }

    private static void viewBalance() {
        Wallet wallet = currentUser.getWallet();
        System.out.println("Текущий баланс: " + wallet.getCurrentBalance());
        System.out.println("Доходы по категориям: " + wallet.getIncomeByCategory());
        System.out.println("Расходы по категориям: " + wallet.getExpenseByCategory());
    }

    private static void setBudget() {
        System.out.print("Категория: ");
        String category = scanner.nextLine();
        System.out.print("Лимит бюджета: ");
        double limit = getValidAmount();

        financeManager.setCategoryBudget(currentUser, category, limit);
        System.out.println("Бюджет установлен успешно!");
    }

    private static void viewStatistics() {
        financeManager.displayFinancialReport(currentUser);
    }

    private static void logout() {
        financeManager.persistData();
        currentUser = null;
        System.out.println("Выход выполнен успешно!");
    }
}
