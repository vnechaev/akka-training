package playground.accounts;

public interface AccountProtocol {
    String getAccountNumber();

    class GetBalance implements AccountProtocol {
        private final String accountNumber;

        public GetBalance(String accountNumber) {
            this.accountNumber = accountNumber;
        }

        @Override
        public String getAccountNumber() {
            return accountNumber;
        }
    }

    class Deposit implements AccountProtocol {
        private final String accountNumber;
        private final int value;

        public Deposit(String accountNumber, int value) {
            this.accountNumber = accountNumber;
            this.value = value;
        }

        @Override
        public String getAccountNumber() {
            return accountNumber;
        }

        public int getValue() {
            return value;
        }
    }

    class Withdraw implements AccountProtocol {
        private final String accountNumber;
        private final int value;

        public Withdraw(String accountNumber, int value) {
            this.accountNumber = accountNumber;
            this.value = value;
        }

        @Override
        public String getAccountNumber() {
            return accountNumber;
        }

        public int getValue() {
            return value;
        }
    }

    //TODO
}
