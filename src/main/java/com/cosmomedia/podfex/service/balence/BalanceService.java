package com.cosmomedia.podfex.service.balence;

import com.cosmomedia.podfex.entities.Balance;
import com.cosmomedia.podfex.entities.Message;
import org.springframework.stereotype.Service;

@Service
public interface BalanceService {

    Balance balanceUser();

    Message  addBalance(Double amount);

    Message confirmBalance(String No);
}
