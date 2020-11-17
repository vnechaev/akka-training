package playground.transfers;

public interface TransferProtocol {
    String getTransferId();

    class Check implements TransferProtocol {
        private final String transferId;

        public Check(String transferId) {
            this.transferId = transferId;
        }

        @Override
        public String getTransferId() {
            return transferId;
        }
    }

    class Transfer implements TransferProtocol {
        private final String transferId;
        private final String fromAccountNumber;
        private final String toAccountNumber;
        private final int value;

        public Transfer(String transferId, String fromAccountNumber, String toAccountNumber, int value) {
            this.transferId = transferId;
            this.fromAccountNumber = fromAccountNumber;
            this.toAccountNumber = toAccountNumber;
            this.value = value;
        }

        public String getTransferId() {
            return transferId;
        }

        public String getFromAccountNumber() {
            return fromAccountNumber;
        }

        public String getToAccountNumber() {
            return toAccountNumber;
        }

        public int getValue() {
            return value;
        }
    }
}
