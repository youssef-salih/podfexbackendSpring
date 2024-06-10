package com.cosmomedia.location.service.transactions;

import com.cosmomedia.location.entities.Product;
import com.cosmomedia.location.entities.Transactions;
import com.cosmomedia.location.entities.Users;
import com.cosmomedia.location.repositories.TransactionsRepository;
import com.cosmomedia.location.repositories.UserRepository;
import com.cosmomedia.location.util.AuthenticationUtils;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class TransactionsServiceImpl implements TransactionsService {

    private final TransactionsRepository transactionsRepository;
    private final UserRepository userRepository;


    @Override
    public Page<Transactions> getTransactionList(Pageable pageable) {
        return transactionsRepository.findAll(pageable);
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

}
