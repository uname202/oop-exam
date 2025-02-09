package Transaction;

import java.time.LocalDateTime;

public class Transaction {
    private final TransactionType type;
    private final double amount;
    private final String category;
    private final LocalDateTime timestamp;

    public Transaction(TransactionType type, double amount, String category) {
        this.type = type;
        this.amount = amount;
        this.category = category;
        this.timestamp = LocalDateTime.now();
    }

    public TransactionType getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public String getCategory() {
        return category;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
