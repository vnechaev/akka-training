package playground.accounts;


public abstract class AccountResponse {
    public enum Status {
        OK, ERROR
    }

    private final Status response;
    private final String accountNumber;
    private final Integer balance;

    public AccountResponse(Status response, String accountNumber, Integer balance) {
        this.response = response;
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public Integer getBalance() {
        return balance;
    }

    public Status getResponse() {
        return response;
    }

    public static class OK extends AccountResponse {
        public OK(String accountNumber, Integer balance) {
            super(Status.OK, accountNumber, balance);
        }
    }

    public static class ERROR extends AccountResponse {
        public final String message;

        public ERROR(String accountNumber, String message) {
            super(Status.ERROR, accountNumber, null);
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}
