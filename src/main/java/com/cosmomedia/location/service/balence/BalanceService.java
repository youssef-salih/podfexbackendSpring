package com.cosmomedia.location.service.balence;

import com.cosmomedia.location.dto.OneResponse;
import com.cosmomedia.location.entities.Balance;
import com.cosmomedia.location.entities.Message;

public interface BalanceService {

    Balance balanceUser();


    Message  addBalance(Double amount);
}
