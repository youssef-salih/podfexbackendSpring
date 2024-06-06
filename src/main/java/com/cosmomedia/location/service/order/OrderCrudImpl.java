package com.cosmomedia.location.service.order;

import com.cosmomedia.location.dto.OneResponse;
import com.cosmomedia.location.dto.OrdersDto;
import com.cosmomedia.location.entities.Client;
import com.cosmomedia.location.entities.Message;
import com.cosmomedia.location.entities.Orders;
import com.cosmomedia.location.entities.Users;
import com.cosmomedia.location.enums.StatusAdmin;
import com.cosmomedia.location.enums.StatusUser;
import com.cosmomedia.location.repositories.ClientRepository;
import com.cosmomedia.location.repositories.OrdersRepository;
import com.cosmomedia.location.repositories.UserRepository;
import com.cosmomedia.location.service.converters.Converters;
import com.cosmomedia.location.util.UniqueIdentifierUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderCrudImpl implements OrderCrud {
    private final OrdersRepository ordersRepository;
    private final Converters converters;
    private final UserRepository usersRepository;
    private final ClientRepository clientRepository;

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
        String orderNumber = UniqueIdentifierUtil.generateUniqueIdentifier("ORDER-");
        byte[] previewBytes = null;
        if (order.getPreview() != null) {
            previewBytes = Base64.getDecoder().decode(order.getPreview());
        }
        Orders ordersInsert = Orders.builder()
                .orderNo(orderNumber)
                .price(order.getPrice())
                .statusUser(StatusUser.INQUEUE)
                .statusAdmin(StatusAdmin.UNVERIFIED)
                .quantity(null)
                .type(order.getType())
                .personnel(null)
                .confirmed(false)
                .order(null)
                .client(null)
                .product(order.getProduct())
                .preview(previewBytes)
                .images(order.getImages() != null ? order.getImages().stream()
                        .map(Base64.getDecoder()::decode)
                        .collect(Collectors.toList()) : null)
                .build();

        ordersRepository.save(ordersInsert);

        return Message.builder()
                .success(true)
                .message("Order added successfully")
                .build();
    }

    public Message updateOrderPersonnel(String orderNo, Long personnelId) {
        Optional<Orders> orderOptional = ordersRepository.findByOrderNo(orderNo);
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

    @Override
    public Message linkOrders(String orderId, String orderId2) {
        Optional<Orders> order1 = ordersRepository.findByOrderNo(orderId);
        Optional<Orders> order2 = ordersRepository.findByOrderNo(orderId2);

        if (order1.isPresent() && order2.isPresent()) {
            Orders newOrder = order1.get();
            Orders newOrder2 = order2.get();

            // Add the price of newOrder to newOrder2
            double totalPrice = newOrder.getPrice() + newOrder2.getPrice();
            newOrder.setPrice(totalPrice);
            newOrder2.setPrice(totalPrice);

            // Link the orders
            newOrder.setOrder(newOrder2);
            newOrder2.setOrder(newOrder);

            // Save the orders
            ordersRepository.save(newOrder);
            ordersRepository.save(newOrder2);

            return Message.builder()
                    .success(true)
                    .message("Order Linked successfully")
                    .build();
        }
        return Message.builder()
                .success(false)
                .message("Order wrong Linked")
                .build();
    }


    @Override
    public Message unlinkOrders(String orderNo, String orderNo2) {
        Optional<Orders> order1 = ordersRepository.findByOrderNo(orderNo);
        Optional<Orders> order2 = ordersRepository.findByOrderNo(orderNo2);

        if (order1.isPresent() && order2.isPresent()) {
            Orders newOrder = order1.get();
            Orders newOrder2 = order2.get();
            double totalPrice = newOrder.getPrice() / 2;
            newOrder.setPrice(totalPrice);
            newOrder2.setPrice(totalPrice);

            newOrder.setOrder(null);
            newOrder2.setOrder(null);
            ordersRepository.save(newOrder);
            ordersRepository.save(newOrder2);

            return Message.builder()
                    .success(true)
                    .message("Order unLinked successfully")
                    .build();
        }
        return Message.builder()
                .success(false)
                .message("Order wrong Linked")
                .build();
    }

    @Override
    public Message confirmOrder(String orderNo, Integer quantity, Client client) {
        Optional<Orders> orderOptional = ordersRepository.findByOrderNo(orderNo);

        if (orderOptional.isPresent()) {
            Orders orderToConfirm = orderOptional.get();
            Double newPrice = orderToConfirm.getPrice() * quantity;
            // Ensure the client is saved
            if (client.getId() == null) {
                client = clientRepository.save(client);
            }
            orderToConfirm.setClient(client);
            orderToConfirm.setQuantity(quantity);
            orderToConfirm.setPrice(newPrice);
            orderToConfirm.setConfirmed(true);

            ordersRepository.save(orderToConfirm);

            // Check if there's an associated order and update its client and price
            if (orderToConfirm.getOrder() != null) {
                Orders associatedOrder = orderToConfirm.getOrder();
                associatedOrder.setClient(client);
                associatedOrder.setQuantity(quantity);
                associatedOrder.setPrice(newPrice);
                associatedOrder.setConfirmed(true);
                ordersRepository.save(associatedOrder);
            }

            return Message.builder()
                    .success(true)
                    .message("Order Confirmed")
                    .build();
        }

        return Message.builder()
                .success(false)
                .message("Order not Confirmed please try again")
                .build();
    }


}
