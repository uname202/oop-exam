package Wallet;

import java.util.*;
import Transaction.Transaction;
import Transaction.TransactionType;

public class Wallet {
    private double currentBalance;
    private final Map<String, Double> budgetLimits;
    private final Map<String, Double> expenseTracker;
    private final Map<String, Double> incomeTracker;
    private final List<Transaction> transactionHistory;

    public Wallet() {
        this.currentBalance = 0;
        this.budgetLimits = new HashMap<>();
        this.expenseTracker = new HashMap<>();
        this.incomeTracker = new HashMap<>();
        this.transactionHistory = new ArrayList<>();
    }

    public void recordIncome(double amount, String category) {
        currentBalance += amount;
        incomeTracker.merge(category, amount, Double::sum);
        logTransaction(TransactionType.INCOME, amount, category);
    }

    public boolean recordExpense(double amount, String category) {
        if (!processExpense(amount, category)) {
            return false;
        }
        if (!checkBudgetLimit(category)) {
            // Отменяем транзакцию, если превышен бюджет
            currentBalance += amount;
            expenseTracker.merge(category, -amount, Double::sum);
            if (expenseTracker.get(category) == 0) {
                expenseTracker.remove(category);
            }
            return false;
        }
        return true;
    }

    public void setCategoryBudget(String category, double limit) {
        budgetLimits.put(category, limit);
    }

    private boolean hasInsufficientFunds(double amount) {
        return currentBalance < amount;
    }

    private boolean processExpense(double amount, String category) {
        if (hasInsufficientFunds(amount)) {
            System.out.println("Недостаточно средств!");
            return false;
        }
        currentBalance -= amount;
        expenseTracker.merge(category, amount, Double::sum);
        logTransaction(TransactionType.EXPENSE, amount, category);
        return true;
    }

    private boolean checkBudgetLimit(String category) {
        Double limit = budgetLimits.get(category);
        Double spent = expenseTracker.get(category);
        if (limit != null && spent > limit) {
            System.out.println("ВНИМАНИЕ: Превышен бюджет для категории: " + category);
            return false;
        }
        return true;
    }

    private void logTransaction(TransactionType type, double amount, String category) {
        transactionHistory.add(new Transaction(type, amount, category));
    }

    public double getCurrentBalance() { return currentBalance; }
    public double getTotalIncome() { return incomeTracker.values().stream().mapToDouble(Double::doubleValue).sum(); }
    public double getTotalExpenses() { return expenseTracker.values().stream().mapToDouble(Double::doubleValue).sum(); }

    public Map<String, Double> getBudgetLimits() { return Collections.unmodifiableMap(budgetLimits); }
    public Map<String, Double> getExpenseByCategory() { return Collections.unmodifiableMap(expenseTracker); }
    public Map<String, Double> getIncomeByCategory() { return Collections.unmodifiableMap(incomeTracker); }
    public List<Transaction> getTransactionHistory() { return Collections.unmodifiableList(transactionHistory); }
}
