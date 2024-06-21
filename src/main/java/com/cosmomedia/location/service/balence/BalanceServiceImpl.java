package com.cosmomedia.location.service.balance;

import com.cosmomedia.location.entities.Balance;
import com.cosmomedia.location.entities.Message;
import com.cosmomedia.location.entities.Transactions;
import com.cosmomedia.location.entities.Users;
import com.cosmomedia.location.enums.StatusTransaction;
import com.cosmomedia.location.repositories.BalanceRepository;
import com.cosmomedia.location.repositories.TransactionsRepository;
import com.cosmomedia.location.repositories.UserRepository;
import com.cosmomedia.location.service.balence.BalanceService;
import com.cosmomedia.location.util.AuthenticationUtils;
import com.cosmomedia.location.util.UniqueIdentifierUtil;
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
