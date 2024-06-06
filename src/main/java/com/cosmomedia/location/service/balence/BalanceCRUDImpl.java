package com.cosmomedia.location.service.balance;

import com.cosmomedia.location.dto.OneResponse;
import com.cosmomedia.location.dto.UsersDto;
import com.cosmomedia.location.entities.Balance;
import com.cosmomedia.location.entities.Users;
import com.cosmomedia.location.repositories.BalanceRepository;
import com.cosmomedia.location.service.balence.BalanceCRUD;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BalanceCRUDImpl implements BalanceCRUD {

    private final BalanceRepository balanceRepository;



}
