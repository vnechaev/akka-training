package playground.accounts;

public class AccountEvents {
    private final String accountNumber;

    public String getAccountNumber() {
        return accountNumber;
    }

    public AccountEvents(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public static class Deposited extends AccountEvents {
        private final int value;

        public Deposited(String accountNumber, int value) {
            super(accountNumber);
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}
