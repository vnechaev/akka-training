package support;

import akka.actor.ActorRef;
import akka.pattern.Patterns;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import playground.accounts.AccountProtocol;
import playground.accounts.AccountResponse;
import playground.transfers.TransferProtocol;
import playground.transfers.TransferResponse;
import support.web.RequestHandler;
import support.web.RequestMessage;
import support.web.ResponseMessage;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

public class AccountHandlerImpl implements RequestHandler {
    private final ActorRef accountsActor;
    private final ActorRef transfersActor;
    private final ActorRef connectionActor;
    private final ObjectMapper mapper = new ObjectMapper();

    public AccountHandlerImpl(ActorRef accountsActor, ActorRef transfersActor, ActorRef connectionActor) {
        this.accountsActor = accountsActor;
        this.transfersActor = transfersActor;
        this.connectionActor = connectionActor;
    }

    @Override
    public void handleRequest(RequestMessage message) {
        String[] data = message.getData();
        switch (message.getCommand()) {
            case "deposit":
                accountOperation(data, 2,
                        d -> new AccountProtocol.Deposit(d[0], Integer.parseInt(d[1])));
                break;
            case "withdraw":
                accountOperation(data, 2,
                        d -> new AccountProtocol.Withdraw(d[0], Integer.parseInt(d[1])));
                break;
            case "balance":
                accountOperation(data, 1,
                        d -> new AccountProtocol.GetBalance(d[0]));
                break;
            case "transfer":
                transferOperation(data, 4,
                        d -> new TransferProtocol.Transfer(d[0], d[1], d[2], Integer.parseInt(d[3])));
                break;
            case "check":
                transferOperation(data, 1,
                        d -> new TransferProtocol.Check(d[0]));
                break;
            default:
                connectionActor.tell(ResponseMessage.error("wrong command"), null);
                break;
        }
    }

    @Override
    public void handleEvent(Object event) {
    }

    private void accountOperation(String[] data, int numberOfParams, Function<String[], ? extends AccountProtocol> msg) {
        operation(accountsActor, data, numberOfParams, msg)
                .thenApply(v -> (AccountResponse) v)
                .thenAccept(this::balanceResponse)
                .handle(this::printError);
    }

    private void transferOperation(String[] data, int numberOfParams,
                                   Function<String[], ? extends TransferProtocol> msg) {
        operation(transfersActor, data, numberOfParams, msg)
                .thenApply(v -> (TransferResponse) v)
                .thenAccept(this::transferResponse)
                .handle(this::printError);
    }

    private <T, R> CompletionStage<Object> operation(ActorRef actor, String[] data, int numberOfParams,
                                                     Function<String[], ? extends T> msg) {
        if (data == null || data.length < numberOfParams) {
            connectionActor.tell(ResponseMessage.error(numberOfParams + " parameter(s)"), null);
            return failedStage();
        } else {
            return Patterns.ask(actor, msg.apply(data), Duration.ofSeconds(5));
        }
    }

    private void balanceResponse(AccountResponse r) {
        switch (r.getResponse()) {
            case OK:
                connectionActor.tell(ResponseMessage.reply("balance " + r.getAccountNumber() + " " + r.getBalance()), null);
                break;
            case ERROR:
                AccountResponse.ERROR error = (AccountResponse.ERROR) r;
                connectionActor.tell(ResponseMessage.error(error.getMessage()), null);
                break;
        }
    }

    private void transferResponse(TransferResponse r) {
        switch (r.getResponse()) {
            case OK:
                TransferResponse.OK ok = (TransferResponse.OK) r;
                connectionActor.tell(ResponseMessage.reply("transfer " + ok.getTransferId() + " " + ok.getStatus()), null);
                break;
            case ERROR:
                TransferResponse.ERROR error = (TransferResponse.ERROR) r;
                connectionActor.tell(ResponseMessage.error(error.getMessage()), null);
                break;
        }
    }

    private String toJson(Object o) {
        try {
            return mapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "error";
        }
    }

    private <T> CompletionStage<T> failedStage() {
        CompletableFuture<T> s = new CompletableFuture<>();
        s.completeExceptionally(new Exception());
        return s;
    }

    private <T> T printError(T o, Throwable e) {
        if (e != null) e.printStackTrace();
        return o;
    }
}