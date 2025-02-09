package FinanceManagerTest;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import FinanceManager.FinanceManager;
import User.User;

public class FinanceManagerTest {
    private FinanceManager manager;

    @BeforeEach
    void setUp() {
        manager = new FinanceManager();
    }

    @Test
    void testCreateAccountSuccess() {
        assertTrue(manager.createAccount("testUser", "password"));
    }

    @Test
    void testCreateAccountDuplicate() {
        manager.createAccount("testUser", "password");
        assertFalse(manager.createAccount("testUser", "password2"));
    }

    @Test
    void testAuthenticateUserSuccess() {
        manager.createAccount("testUser", "password");
        var user = manager.authenticateUser("testUser", "password");
        assertTrue(user.isPresent());
        assertEquals("testUser", user.get().getUsername());
    }

    @Test
    void testAuthenticateUserFailure() {
        manager.createAccount("testUser", "password");
        assertTrue(manager.authenticateUser("testUser", "wrongPassword").isEmpty());
        assertTrue(manager.authenticateUser("nonExistentUser", "password").isEmpty());
    }

    @Test
    void testRecordIncome() {
        manager.createAccount("testUser", "password");
        var user = manager.authenticateUser("testUser", "password").get();
        manager.recordIncome(user, 100.0, "Зарплата");
        assertEquals(100.0, user.getWallet().getCurrentBalance());
    }

    @Test
    void testRecordExpense() {
        manager.createAccount("testUser", "password");
        var user = manager.authenticateUser("testUser", "password").get();
        manager.recordIncome(user, 100.0, "Зарплата");
        manager.recordExpense(user, 50.0, "Продукты");
        assertEquals(50.0, user.getWallet().getCurrentBalance());
    }

    @Test
    void testSetCategoryBudget() {
        manager.createAccount("testUser", "password");
        var user = manager.authenticateUser("testUser", "password").get();
        manager.setCategoryBudget(user, "Продукты", 100.0);
        assertEquals(100.0, user.getWallet().getBudgetLimits().get("Продукты"));
    }
}
