package com.cosmomedia.podfex.service.transactions;

import com.cosmomedia.podfex.entities.Balance;
import com.cosmomedia.podfex.entities.Message;
import com.cosmomedia.podfex.entities.Transactions;
import com.cosmomedia.podfex.entities.Users;
import com.cosmomedia.podfex.enums.TransactionState;
import com.cosmomedia.podfex.repositories.BalanceRepository;
import com.cosmomedia.podfex.repositories.OrdersRepository;
import com.cosmomedia.podfex.repositories.TransactionsRepository;
import com.cosmomedia.podfex.repositories.UserRepository;
import com.cosmomedia.podfex.util.AuthenticationUtils;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class TransactionsServiceImpl implements TransactionsService {

    private final TransactionsRepository transactionsRepository;
    private final UserRepository userRepository;
    private final BalanceRepository balanceRepository;
    private final OrdersRepository ordersRepository;


    @Override
    public Page<Transactions> getTransactionList(Pageable pageable) {
        return transactionsRepository.findByConfirmedTrue(pageable);
    }

    @Override
    public Page<Transactions> getTransactionsForCurrentUser(Pageable pageable) {
        String email = AuthenticationUtils.getUserEmailFromAuthentication();
        if (email != null) {
            Optional<Users> currentUserOptional = userRepository.findByEmail(email);
            if (currentUserOptional.isPresent()) {
                Users currentUser = currentUserOptional.get();
                return transactionsRepository.findByUser(currentUser, pageable);
            }
        }
        return Page.empty(pageable);
    }

    @Override
    public Page<Transactions> getTransactionsNotconfirmed(Pageable pageable) {
        return transactionsRepository.findByConfirmedFalse(pageable);
    }

    @Override
    public Message confirmTransaction(String transactionNo) {
        try {
            Optional<Transactions> transaction = transactionsRepository.findByTransactionNo(transactionNo);
            if (transaction.isPresent()) {
                Transactions selectedTransaction = transaction.get();
                Balance userBalance = balanceRepository.findByUsers_Email(selectedTransaction.getUser().getEmail());
                userBalance.setAmount(userBalance.getAmount()+selectedTransaction.getAmount());
                System.out.println(userBalance);
                selectedTransaction.setConfirmed(true);
                selectedTransaction.setTransactionState(TransactionState.CONFIRMED);
                transactionsRepository.save(selectedTransaction);
                balanceRepository.save(userBalance);
                return new Message(true, "Transaction confirmed successfully");
            } else {
                return new Message(false, "Transaction not confirmed");
            }

        } catch (Exception e) {
            return new Message(false, "Error in confirming the transaction: " + e.getMessage());
        }

    }

    @Override
    public Message cancelTransaction(String transactionNo) {
        try {
            Optional<Transactions> transaction = transactionsRepository.findByTransactionNo(transactionNo);
            if (transaction.isPresent()) {
                Transactions selectedTransaction = transaction.get();
                selectedTransaction.setConfirmed(true);
                selectedTransaction.setTransactionState(TransactionState.CANCELED);
                transactionsRepository.save(selectedTransaction);
                return new Message(true, "Transaction confirmed successfully");
            } else {
                return new Message(false, "Transaction not confirmed");
            }

        } catch (Exception e) {
            return new Message(false, "Error in confirming the transaction: " + e.getMessage());
        }

    }

}
