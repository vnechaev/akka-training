package playground.accounts;

import akka.persistence.AbstractPersistentActor;

import java.util.HashMap;
import java.util.Map;

public class AccountEntity extends AbstractPersistentActor {
    Map<String, Integer> accounts = new HashMap<>();

    private AccountState state;

    public String persistenceId() {
        return context().self().path().name();
    }

    @Override
    public void preStart() throws Exception {
        super.preStart();
        state = new AccountState(0);
    }

    public Receive createReceive() {
        return receiveBuilder()
//                .match(AccountProtocol.class,
//                        v -> sender().tell(new AccountResponse.ERROR(v.getAccountNumber(), "Not Implemented"), null))
                .match(AccountProtocol.GetBalance.class, this::onGetBalance)
                .match(AccountProtocol.Deposit.class, this::onDeposit)
                .match(AccountProtocol.Withdraw.class, this::onWithDraw)
                .build();
    }

    private void onWithDraw(AccountProtocol.Withdraw withdraw) {


        getSender().tell(new AccountResponse.ERROR(withdraw.getAccountNumber(), "Not implemented"), self());
//        String accountNumber = withdraw.getAccountNumber();
//        Integer balance = accounts.get(accountNumber);
//        if (balance == null) {
//            getSender().tell(new AccountResponse.ERROR(accountNumber, "Account not found"), self());
//        } else {
//            if (balance - withdraw.getValue() < 0) {
//                getSender().tell(new AccountResponse.ERROR(accountNumber, "Balance bacomes less than zero"), self());
//            } else {
//                int newBalance = balance - withdraw.getValue();
//                accounts.put(accountNumber, newBalance);
//                getSender().tell(new AccountResponse.OK(accountNumber, newBalance), self());
//            }
//
//        }
    }


    private void onDeposit(AccountProtocol.Deposit deposit) {
//TODO
    }

    private void reactOnDeposited(AccountEvents.Deposited evt) {
            state = state.deposit(evt.getValue());
    }

    private void onGetBalance(AccountProtocol.GetBalance balanceRequest) {
        getSender().tell(new AccountResponse.OK(balanceRequest.getAccountNumber(), state.getBalance()), null);
    }

    public Receive createReceiveRecover() {
        return receiveBuilder()
                .match(AccountEvents.Deposited.class, this::reactOnDeposited)
                .build();
    }
}
