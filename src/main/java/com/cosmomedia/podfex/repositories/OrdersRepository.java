package com.cosmomedia.podfex.repositories;

import com.cosmomedia.podfex.entities.Orders;
import com.cosmomedia.podfex.entities.Users;
import com.cosmomedia.podfex.enums.StatusAdmin;
import com.cosmomedia.podfex.enums.StatusUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrdersRepository extends JpaRepository<Orders,Long> {


    Optional<Orders> findByOrderNo(String orderNo);

    List<Orders> findByOrderNullAndConfirmedFalseAndTypeNotLikeAndProduct_Id(String type, Long id);

    Page<Orders> findBySeller(Users seller, Pageable pageable);


    List<Orders> findBySeller_Email(String email);

    List<Orders> findBySeller_EmailAndStatusUser(String email, StatusUser statusUser);

    Page<Orders> findByConfirmedTrue(Pageable pageable);
    List<Orders> findByConfirmedTrue();

    Page<Orders> findByPersonnel(Users personnel, Pageable pageable);

    List<Orders> findByStatusUserAndConfirmedTrueAndStatusAdminNot(StatusUser statusUser, StatusAdmin statusAdmin);

    List<Orders> findByPersonnel_Email(String email);

    List<Orders> findByPersonnel_EmailAndStatusUser(String email, StatusUser statusUser);

    List<Orders> findBySeller_EmailAndStatusUserAndStatusAdminNot(String email, StatusUser statusUser, StatusAdmin statusAdmin);
}
