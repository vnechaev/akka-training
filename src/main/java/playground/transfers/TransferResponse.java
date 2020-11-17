package playground.transfers;


public abstract class TransferResponse {
    public enum Status {
        OK, ERROR
    }

    private final Status response;
    private final String transferId;

    public TransferResponse(Status response, String transferId) {
        this.response = response;
        this.transferId = transferId;
    }

    public Status getResponse() {
        return response;
    }

    public String getTransferId() {
        return transferId;
    }

    public static class OK extends TransferResponse {
        private final TransferStatus status;

        public OK(String transferId, TransferStatus status) {
            super(Status.OK, transferId);
            this.status = status;
        }

        public TransferStatus getStatus() {
            return status;
        }
    }

    public static class ERROR extends TransferResponse {
        private final String message;

        public ERROR(String transferId, String message) {
            super(Status.ERROR, transferId);
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}
