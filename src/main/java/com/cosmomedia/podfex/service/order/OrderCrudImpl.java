package com.cosmomedia.podfex.service.order;

import com.cosmomedia.podfex.dto.OneResponse;
import com.cosmomedia.podfex.dto.OrderItemDto;
import com.cosmomedia.podfex.dto.OrdersDto;
import com.cosmomedia.podfex.entities.*;
import com.cosmomedia.podfex.enums.StatusAdmin;
import com.cosmomedia.podfex.enums.StatusTransaction;
import com.cosmomedia.podfex.enums.StatusUser;
import com.cosmomedia.podfex.enums.TransactionState;
import com.cosmomedia.podfex.repositories.*;
import com.cosmomedia.podfex.service.balence.BalanceService;
import com.cosmomedia.podfex.service.converters.Converters;
import com.cosmomedia.podfex.util.AuthenticationUtils;
import com.cosmomedia.podfex.util.UniqueIdentifierUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderCrudImpl implements OrderCrud {
    private final OrdersRepository ordersRepository;
    private final Converters converters;
    private final UserRepository usersRepository;
    private final ClientRepository clientRepository;
    private final BalanceService balanceService;
    private final BalanceRepository balanceRepository;
    private final TransactionsRepository transactionsRepository;
    private final JavaMailSender javaMailSender;

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
    public Page<OrdersDto> getUsersOrdersList(Pageable pageable) {
        String userEmail = AuthenticationUtils.getUserEmailFromAuthentication();
        Optional<Users> userOptional = usersRepository.findByEmail(userEmail);

        if (!userOptional.isPresent()) {
            return Page.empty(pageable);
        }

        Users currentUser = userOptional.get();

        Set<Long> seen = new HashSet<>();
        List<Orders> validOrders = new ArrayList<>();

        Page<Orders> ordersPage;
        int page = 0;

        do {
            ordersPage = ordersRepository.findBySeller(currentUser, pageable);
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
    public Page<OrdersDto> getConfirmedOrdersList(Pageable pageable) {
        Set<Long> seen = new HashSet<>();
        List<Orders> validOrders = new ArrayList<>();

        Page<Orders> ordersPage;
        int page = 0;

        do {
            ordersPage = ordersRepository.findByConfirmedTrue(pageable);
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
    public Page<OrdersDto> getAssignedOrdersList(Pageable pageable) {
        String userEmail = AuthenticationUtils.getUserEmailFromAuthentication();
        Optional<Users> userOptional = usersRepository.findByEmail(userEmail);
        System.out.println(userOptional);
        if (!userOptional.isPresent()) {
            return Page.empty(pageable);
        }

        Users currentUser = userOptional.get();

        Set<Long> seen = new HashSet<>();
        List<Orders> validOrders = new ArrayList<>();

        Page<Orders> ordersPage;
        int page = 0;

        do {
            ordersPage = ordersRepository.findByPersonnel(currentUser, pageable);
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

    //counts
    @Override
    public int getCurrentUserOrdersCount() {
        String userEmail = AuthenticationUtils.getUserEmailFromAuthentication();

        Set<Long> seen = new HashSet<>();
        List<Orders> validOrders = new ArrayList<>();

        List<Orders> ordersPage;
        int page = 0;
        ordersPage = ordersRepository.findBySeller_Email(userEmail);
        for (Orders order : ordersPage) {
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

        return validOrders.size();
    }

    @Override
    public int getConfirmedOrder() {
        Set<Long> seen = new HashSet<>();
        List<Orders> validOrders = new ArrayList<>();

        List<Orders> ordersPage;
        int page = 0;
        ordersPage = ordersRepository.findByConfirmedTrue();
        for (Orders order : ordersPage) {
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

        return validOrders.size();
    }

    @Override
    public int getAssignedOrder() {
        String userEmail = AuthenticationUtils.getUserEmailFromAuthentication();

        Set<Long> seen = new HashSet<>();
        List<Orders> validOrders = new ArrayList<>();

        List<Orders> ordersPage = ordersRepository.findByPersonnel_Email(userEmail);

        for (Orders order : ordersPage) {
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

        return validOrders.size();
    }

    @Override
    public int getCurrentUserOrdersByStatus(StatusUser statusUser) {
        String userEmail = AuthenticationUtils.getUserEmailFromAuthentication();

        Set<Long> seen = new HashSet<>();
        List<Orders> validOrders = new ArrayList<>();

        List<Orders> ordersPage;
        int page = 0;
        ordersPage = ordersRepository.findBySeller_EmailAndStatusUserAndStatusAdminNot(userEmail, statusUser, StatusAdmin.CANCELED);
        for (Orders order : ordersPage) {
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

        return validOrders.size();
    }

    @Override
    public int getAssignedOrdersByStatus(StatusUser statusUser) {
        String userEmail = AuthenticationUtils.getUserEmailFromAuthentication();

        Set<Long> seen = new HashSet<>();
        List<Orders> validOrders = new ArrayList<>();

        List<Orders> ordersPage;
        int page = 0;
        ordersPage = ordersRepository.findByPersonnel_EmailAndStatusUser(userEmail, statusUser);
        for (Orders order : ordersPage) {
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

        return validOrders.size();
    }

    @Override
    public int getConfirmedOrdersByStatus(StatusUser statusUser) {


        Set<Long> seen = new HashSet<>();
        List<Orders> validOrders = new ArrayList<>();

        List<Orders> ordersPage;
        int page = 0;
        ordersPage = ordersRepository.findByStatusUserAndConfirmedTrueAndStatusAdminNot(statusUser, StatusAdmin.CANCELED);
        for (Orders order : ordersPage) {
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

        return validOrders.size();
    }

    //end
    private String getUserEmailFromAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Users currentUser) {
            System.out.println(currentUser.getEmail());
            return currentUser.getEmail();
        }
        return null;
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
    public List<OrdersDto> getOrdersByType(String type, Long productId) {
        List<Orders> orders = ordersRepository.findByOrderNullAndConfirmedFalseAndTypeNotLikeAndProduct_Id(type, productId);
        return orders.stream()
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

        // Retrieve the seller (user) using the email from the authentication token
        String userEmail = AuthenticationUtils.getUserEmailFromAuthentication();
        Optional<Users> sellerOptional = usersRepository.findByEmail(userEmail);

        if (!sellerOptional.isPresent()) {
            return Message.builder()
                    .success(false)
                    .message("Seller not found")
                    .build();
        }

        Users seller = sellerOptional.get();

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
                .seller(seller) // Set the seller
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
    public Message confirmOrder(String orderNo, Integer quantity, Client client, List<OrderItemDto> orderItems) {
        Optional<Orders> orderOptional = ordersRepository.findByOrderNo(orderNo);
        String transactionNumber = UniqueIdentifierUtil.generateUniqueIdentifier("Tran-");

        if (orderOptional.isPresent()) {
            Orders orderToConfirm = orderOptional.get();
            Balance balance = balanceService.balanceUser();
            Double newPrice = orderToConfirm.getPrice() * quantity;

            if (balance.getAmount() >= newPrice) {
                if (client.getId() == null) {
                    client = clientRepository.save(client);
                }

                orderToConfirm.setClient(client);
                orderToConfirm.setQuantity(quantity);
                orderToConfirm.setPrice(newPrice);
                orderToConfirm.setConfirmed(true);
                List<OrderItem> orderItemList = orderItems.stream()
                        .filter(orderItemDto -> orderItemDto.getQuantity() > 0)
                        .map(orderItemDto -> {
                            OrderItem orderItem = new OrderItem();
                            orderItem.setSize(orderItemDto.getSize());
                            orderItem.setQuantity(orderItemDto.getQuantity());
                            orderItem.setOrder(orderToConfirm); // Set the order for each item
                            orderItem.setColor(orderItemDto.getColor());
                            return orderItem;
                        })
                        .toList();
                orderToConfirm.setOrderItems(orderItemList);
                ordersRepository.save(orderToConfirm);

                balance.setAmount(balance.getAmount() - newPrice);
                balanceRepository.save(balance);

                Optional<Users> users = usersRepository.findByEmail(getUserEmailFromAuthentication());
                Transactions transactions = Transactions.builder()
                        .amount(newPrice)
                        .transactionNo(transactionNumber)
                        .status(StatusTransaction.BUYING)
                        .confirmed(true)
                        .transactionState(TransactionState.CONFIRMED)
                        .createdAt(new Date())
                        .order(orderToConfirm)
                        .user(users.get())
                        .build();
                transactionsRepository.save(transactions);

                if (orderToConfirm.getOrder() != null) {
                    Orders associatedOrder = orderToConfirm.getOrder();
                    associatedOrder.setClient(client);
                    associatedOrder.setQuantity(quantity);
                    associatedOrder.setPrice(newPrice);
                    associatedOrder.setConfirmed(true);
                    List<OrderItem> associatedorderItemList = orderItems.stream()
                            .filter(orderItemDto -> orderItemDto.getQuantity() > 0)
                            .map(orderItemDto -> {
                                OrderItem orderItem = new OrderItem();
                                orderItem.setSize(orderItemDto.getSize());
                                orderItem.setQuantity(orderItemDto.getQuantity());
                                orderItem.setOrder(associatedOrder); // Set the order for each item
                                orderItem.setColor(orderItemDto.getColor());
                                return orderItem;
                            })
                            .toList();
                    associatedOrder.setOrderItems(associatedorderItemList);
                    ordersRepository.save(associatedOrder);
                }

                return Message.builder()
                        .success(true)
                        .message("Order Confirmed")
                        .build();
            }

            return Message.builder()
                    .success(false)
                    .message("Balance Insufficient")
                    .build();
        }

        return Message.builder()
                .success(false)
                .message("Order not Confirmed, please try again")
                .build();
    }

    @Transactional
    @Override
    public Message validateOrder(String orderNo, StatusAdmin status, Long personnelId) {
        try {
            // Assuming these are the allowed values based on the check constraint
            Set<StatusAdmin> allowedStatuses = Set.of(StatusAdmin.VERIFIED, StatusAdmin.UNVERIFIED, StatusAdmin.CANCELED);
            String transactionNumber = UniqueIdentifierUtil.generateUniqueIdentifier("Tran-");

            // Validate if the provided status is allowed
            if (!allowedStatuses.contains(status)) {
                return new Message(false, "Invalid status value");
            }

            Optional<Orders> orderOptional = ordersRepository.findByOrderNo(orderNo);

            if (orderOptional.isPresent()) {
                Orders selectedOrder = orderOptional.get();
                selectedOrder.setStatusAdmin(status);

                // Handle order cancellation
                if (selectedOrder.getStatusAdmin() == StatusAdmin.CANCELED) {
                    Double paybackAmount = selectedOrder.getPrice();
                    Users currentUser = selectedOrder.getSeller();

                    if (currentUser != null) {
                        Balance userBalance = currentUser.getBalance();
                        if (userBalance != null) {
                            // Restore the balance
                            userBalance.setAmount(userBalance.getAmount() + paybackAmount);
                            balanceRepository.save(userBalance);

                            // Create a payback transaction
                            Transactions paybackTransaction = Transactions.builder()
                                    .amount(paybackAmount)
                                    .confirmed(true)
                                    .status(StatusTransaction.PAYBACK)
                                    .transactionNo(transactionNumber)
                                    .transactionState(TransactionState.CONFIRMED)
                                    .createdAt(new Date())
                                    .user(currentUser)
                                    .order(selectedOrder)
                                    .build();
                            transactionsRepository.save(paybackTransaction);
                        }
                    }
                }

                // Handle order Verified
                if (selectedOrder.getStatusAdmin() == StatusAdmin.VERIFIED) {
                    Optional<Users> userOptional = usersRepository.findById(selectedOrder.getSeller().getId());
                    if (userOptional.isPresent()) {
                        Users user = userOptional.get();
                        sendConfirmationEmail(user, selectedOrder);
                    } else {
                        return new Message(false, "Personnel not found");
                    }

                }

                // Update personnel if provided
                if (personnelId == null) {
                    selectedOrder.setPersonnel(null);
                } else {
                    Optional<Users> userOptional = usersRepository.findById(personnelId);
                    if (userOptional.isPresent()) {
                        Users selectedUser = userOptional.get();
                        selectedOrder.setPersonnel(selectedUser);
                    } else {
                        return new Message(false, "Personnel not found");
                    }
                }

                ordersRepository.save(selectedOrder);
                return Message.builder()
                        .success(true)
                        .message("Order status changed successfully")
                        .build();
            } else {
                return new Message(false, "Order not found");
            }
        } catch (DataIntegrityViolationException e) {
            return new Message(false, "Error in updating the Order Status: Constraint violation");
        } catch (Exception e) {
            return new Message(false, "Error in updating the Order Status");
        }
    }


    @Override
    public Message changeOrderStatus(String orderNo, StatusUser status) {
        try {
            Optional<Orders> orderOptional = ordersRepository.findByOrderNo(orderNo);

            if (orderOptional.isPresent()) {
                Orders selectedOrder = orderOptional.get();
                selectedOrder.setStatusUser(status);
                ordersRepository.save(selectedOrder);
                if (selectedOrder.getOrder() != null) {
                    Orders associatedOrder = selectedOrder.getOrder();
                    associatedOrder.setStatusUser(status);
                    ordersRepository.save(associatedOrder);
                }
                return Message.builder()
                        .success(true)
                        .message("Order status changed successfully")
                        .build();
            } else {
                return new Message(false, "Order not found");
            }
        } catch (DataIntegrityViolationException e) {
            return new Message(false, "Error in updating the Order Status: Constraint violation");
        } catch (Exception e) {
            return new Message(false, "Error in updating the Order Status");
        }
    }


    private void sendConfirmationEmail(Users user, Orders order) throws MessagingException, IOException {


        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        // Set email details
        messageHelper.setFrom("d.youssefsalih@gmail.com");
        messageHelper.setTo(user.getEmail());
        messageHelper.setSubject("Order confirmed in Podfex");
        // Load email content from the HTML file in the resources/static directory
        ClassPathResource resource = new ClassPathResource("static/confirmation.html");
        InputStream inputStream = resource.getInputStream();
        byte[] emailContentBytes = StreamUtils.copyToByteArray(inputStream);
        String emailContent = new String(emailContentBytes, StandardCharsets.UTF_8);

        // Replace placeholders in the email content
        if (user.getFirstName() != null) {
            emailContent = emailContent.replace("[nomClient]", user.getFirstName());
        }
        if (order.getProduct() != null) {
            emailContent = emailContent.replace("[nomProduit]", order.getProduct().getName());
        }
        if (order.getQuantity()!=null){
            emailContent = emailContent.replace("[quantite]", order.getQuantity().toString());
        }
        if (order.getId() != null) {
            emailContent = emailContent.replace("[idOrder]", order.getOrderNo());
        }
        // Set the HTML content
        messageHelper.setText(emailContent, true);

        javaMailSender.send(mimeMessage);
    }
}
