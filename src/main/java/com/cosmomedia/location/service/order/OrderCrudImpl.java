package com.cosmomedia.location.service.order;

import com.cosmomedia.location.dto.OneResponse;
import com.cosmomedia.location.dto.OrdersDto;
import com.cosmomedia.location.entities.Message;
import com.cosmomedia.location.entities.Orders;
import com.cosmomedia.location.entities.Users;
import com.cosmomedia.location.enums.StatusAdmin;
import com.cosmomedia.location.enums.StatusUser;
import com.cosmomedia.location.repositories.OrdersRepository;
import com.cosmomedia.location.repositories.UserRepository;
import com.cosmomedia.location.service.converters.Converters;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderCrudImpl implements OrderCrud {
    private final OrdersRepository ordersRepository;
    private final Converters converters;
    private final UserRepository usersRepository;

    @Override
    public Page<OrdersDto> getOrdersList(Pageable pageable) {
        Set<Long> seen = new HashSet<>();
        List<Orders> validOrders = new ArrayList<>();

        Page<Orders> ordersPage;
        int page = 0;

        do {
            ordersPage = ordersRepository.findAll(pageable);
            for (Orders order : ordersPage.getContent()) {
                if (order.getOrder() == null) {
                    validOrders.add(order);
                    continue;
                }

                Long currentId = order.getId();
                Long referredId = order.getOrder().getId();

                if (seen.contains(referredId)) {
                    continue; // Skip this order if the referred one is already seen
                }

                seen.add(currentId);
                seen.add(referredId);
                validOrders.add(order);
            }
            page++;
        } while (ordersPage.hasNext() && page < pageable.getPageNumber());

        return new PageImpl<>(
                validOrders.stream()
                        .map(converters::convertToOrdersDto)
                        .toList(),
                pageable,
                ordersPage.getTotalElements()
        );
    }


    @Override
    public OneResponse<OrdersDto> getOneOrder(String orderNo) {
        Optional<Orders> orderOptional = ordersRepository.findByOrderNo(orderNo);

        if (orderOptional.isPresent()) {
            OrdersDto orderDto = converters.convertToOrdersDto(orderOptional.get());
            return OneResponse.<OrdersDto>builder()
                    .success(true)
                    .content(orderDto)
                    .message("Order found")
                    .build();
        } else {
            return OneResponse.<OrdersDto>builder()
                    .success(false)
                    .content(null)
                    .message("Order not found")
                    .build();
        }
    }

    @Override
    public List<OrdersDto> getOrdersByType(String type) {
        List<Orders> orders = ordersRepository.findByTypeNotLike(type);
        return orders.stream()
                .filter(order -> order.getOrder() == null) // Add this line
                .map(converters::convertToOrdersDto)
                .collect(Collectors.toList());
    }

    @Override
    public Message addOrder(Orders order) {
        Orders ordersInsert = Orders.builder()
                .orderNo(order.getOrderNo())
                .price(order.getPrice())
                .statusUser(StatusUser.INQUEUE)
                .statusAdmin(StatusAdmin.UNVERIFIED)
                .quantity(null)
                .type(order.getType())
                .personnel(null)
                .order(null)
                .client(null)
                .product(order.getProduct())
                .preview(order.getPreview())
                .images(order.getImages())
                .build();

        ordersRepository.save(ordersInsert);

        return Message.builder()
                .success(true)
                .message("Order added successfully")
                .build();
    }

    public Message updateOrderPersonnel(Long orderId, Long personnelId) {
        Optional<Orders> orderOptional = ordersRepository.findById(orderId);
        Optional<Users> personnelOptional = usersRepository.findById(personnelId);

        if (orderOptional.isPresent() && personnelOptional.isPresent()) {
            Orders order = orderOptional.get();
            order.setPersonnel(personnelOptional.get());
            ordersRepository.save(order);

            return Message.builder()
                    .success(true)
                    .message("Personnel updated successfully")
                    .build();
        } else {
            return Message.builder()
                    .success(false)
                    .message("Order or Personnel not found")
                    .build();
        }
    }
}
