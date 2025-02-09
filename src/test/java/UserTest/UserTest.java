package UserTest;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import User.User;
import Wallet.Wallet;

public class UserTest {
    private User user;

    @BeforeEach
    void setUp() {
        user = new User("testUser", "password");
    }

    @Test
    void testUserCreation() {
        assertEquals("testUser", user.getUsername());
        assertEquals("password", user.getPassword());
        assertNotNull(user.getWallet());
    }

    @Test
    void testWalletInitialization() {
        Wallet wallet = user.getWallet();
        assertNotNull(wallet);
        assertEquals(0.0, wallet.getCurrentBalance());
    }
}
