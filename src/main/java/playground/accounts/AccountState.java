package playground.accounts;

import javax.security.auth.login.AccountException;
import java.util.HashMap;
import java.util.Map;

public class AccountState {
    private final int balance;

    public AccountState(int balance) {
        this.balance = balance;
    }

    public int getBalance() {
        return balance;
    }

    public AccountState deposit(int value) {
        return new AccountState(balance + value);
    }

    public AccountState withdraw(int value) throws AccountMyException {
        if (balance - value < 0) {
            throw new AccountMyException();
        }
        return null;
    }

}
