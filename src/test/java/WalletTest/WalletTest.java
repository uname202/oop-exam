package WalletTest;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import Wallet.Wallet;

public class WalletTest {
    private Wallet wallet;

    @BeforeEach
    void setUp() {
        wallet = new Wallet();
    }

    @Test
    void testInitialBalance() {
        assertEquals(0.0, wallet.getCurrentBalance());
    }

    @Test
    void testRecordIncome() {
        wallet.recordIncome(100.0, "Зарплата");
        assertEquals(100.0, wallet.getCurrentBalance());
    }

    @Test
    void testRecordExpenseSuccess() {
        wallet.recordIncome(100.0, "Зарплата");
        assertTrue(wallet.recordExpense(50.0, "Продукты"));
        assertEquals(50.0, wallet.getCurrentBalance());
    }

    @Test
    void testRecordExpenseInsufficientFunds() {
        wallet.recordIncome(50.0, "Зарплата");
        assertFalse(wallet.recordExpense(100.0, "Продукты"));
        assertEquals(50.0, wallet.getCurrentBalance());
    }

    @Test
    void testSetCategoryBudget() {
        wallet.setCategoryBudget("Продукты", 100.0);
        assertEquals(100.0, wallet.getBudgetLimits().get("Продукты"));
    }

    @Test
    void testMultipleTransactions() {
        wallet.recordIncome(100.0, "Зарплата");
        wallet.recordIncome(50.0, "Премия");
        assertTrue(wallet.recordExpense(30.0, "Продукты"));
        assertTrue(wallet.recordExpense(20.0, "Транспорт"));
        
        assertEquals(100.0, wallet.getCurrentBalance());
        assertEquals(2, wallet.getIncomeByCategory().size());
        assertEquals(2, wallet.getExpenseByCategory().size());
    }
}
