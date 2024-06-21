package com.cosmomedia.location.repositories;

import com.cosmomedia.location.entities.Orders;
import com.cosmomedia.location.entities.Users;
import com.cosmomedia.location.enums.StatusUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrdersRepository extends JpaRepository<Orders,Long> {


    Optional<Orders> findByOrderNo(String orderNo);

    List<Orders> findByType(String type);

    List<Orders> findByTypeNotLike(String type);

    List<Orders> findByTypeNotLikeIgnoreCaseAndProduct_NameIgnoreCase(String type, String name);

    List<Orders> findByOrderNullAndConfirmedFalseAndTypeNotLikeAndProduct_Id(String type, Long id);

    Page<Orders> findBySeller(Users seller, Pageable pageable);


    Long countBySeller(Users user);

    Long countByStatusUser(StatusUser status);

    List<Orders> findBySeller_Email(String email);

    List<Orders> findBySeller_EmailAndStatusUser(String email, StatusUser statusUser);

    Page<Orders> findByConfirmedTrue(Pageable pageable);

    Page<Orders> findByPersonnel(Users personnel, Pageable pageable);

}
