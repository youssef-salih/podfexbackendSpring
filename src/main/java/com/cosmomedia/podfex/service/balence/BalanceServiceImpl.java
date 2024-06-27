package com.cosmomedia.podfex.service.balance;

import com.cosmomedia.podfex.entities.Balance;
import com.cosmomedia.podfex.entities.Message;
import com.cosmomedia.podfex.entities.Transactions;
import com.cosmomedia.podfex.entities.Users;
import com.cosmomedia.podfex.enums.StatusTransaction;
import com.cosmomedia.podfex.enums.TransactionState;
import com.cosmomedia.podfex.repositories.BalanceRepository;
import com.cosmomedia.podfex.repositories.TransactionsRepository;
import com.cosmomedia.podfex.repositories.UserRepository;
import com.cosmomedia.podfex.service.balence.BalanceService;
import com.cosmomedia.podfex.util.AuthenticationUtils;
import com.cosmomedia.podfex.util.UniqueIdentifierUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BalanceServiceImpl implements BalanceService {

    private final BalanceRepository balanceRepository;
    private final TransactionsRepository transactionsRepository;
    private final UserRepository usersRepository;

    @Override
    public Balance balanceUser() {
        return balanceRepository.findByUsers_Email(AuthenticationUtils.getUserEmailFromAuthentication());

    }

    @Override
    public Message addBalance(Double amount) {
        String transactionNumber = UniqueIdentifierUtil.generateUniqueIdentifier("Tran-");
        Balance balance = balanceRepository.findByUsers_Email(AuthenticationUtils.getUserEmailFromAuthentication());
        Optional<Users> users = usersRepository.findByEmail(AuthenticationUtils.getUserEmailFromAuthentication());
//        Double newAmount = balance.getAmount() + amount;
//
//        balance.setAmount(newAmount);
//
//        balanceRepository.save(balance);
        Transactions transactions = Transactions.builder()
                .amount(amount)
                .status(StatusTransaction.DIPOSIT)
                .confirmed(false)
                .transactionNo(transactionNumber)
                .createdAt(new Date())
                .order(null)
                .transactionState(TransactionState.PENDING)
                .user(users.get())
                .build();
        transactionsRepository.save(transactions);
        return Message.builder()
                .success(true)
                .message("Balance Added")
                .build();
    }

    @Override
    public Message confirmBalance(String No) {
        Optional<Transactions> transaction = transactionsRepository.findByTransactionNo(No);

        if (transaction.isPresent()) {
            Transactions transactionUpdate = transaction.get();
            transactionUpdate.setConfirmed(true);
            transactionsRepository.save(transactionUpdate);
            return Message.builder()
                    .success(true)
                    .message("Transaction updated successfully")
                    .build();

        } else {
            return Message.builder()
                    .success(false)
                    .message("Transaction not found")
                    .build();
        }

    }
}
