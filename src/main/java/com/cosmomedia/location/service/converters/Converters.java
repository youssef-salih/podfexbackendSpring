package com.cosmomedia.location.service.converters;

import com.cosmomedia.location.dto.OrdersDto;
import com.cosmomedia.location.dto.ProductDto;
import com.cosmomedia.location.dto.SurfaceDto;
import com.cosmomedia.location.dto.UsersDto;
import com.cosmomedia.location.entities.Orders;
import com.cosmomedia.location.entities.Product;
import com.cosmomedia.location.entities.Surface;
import com.cosmomedia.location.entities.Users;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Component
public class Converters {
    private static final int MAX_DEPTH = 3;

    public UsersDto convertToUsersDto(Users users) {
        UsersDto usersDto = new UsersDto();
        usersDto.setId(users.getId());
        usersDto.setFirstName(users.getFirstName());
        usersDto.setLastName(users.getLastName());
        usersDto.setEmail(users.getEmail());
        usersDto.setRole(users.getRole());
        usersDto.setCreatedAt(users.getCreatedAt());
        usersDto.setModifiedAt(users.getModifiedAt());
        usersDto.setDeletedAt(users.getDeletedAt());
        usersDto.setDeletedBy(users.getDeletedBy());

        return usersDto;
    }

    public OrdersDto convertToOrdersDto(Orders order) {
        return convertToOrdersDto(order, 0);
    }

    private OrdersDto convertToOrdersDto(Orders order, int depth) {
        if (order == null || depth > MAX_DEPTH) {
            return null;
        }

        OrdersDto ordersDto = new OrdersDto();
        ordersDto.setId(order.getId());

        if (order.getPreview() != null) {
            String previewString = Base64.getEncoder().encodeToString(order.getPreview());
            ordersDto.setPreview(previewString);
        }

        ordersDto.setOrderNo(order.getOrderNo());
        ordersDto.setPrice(order.getPrice());
        ordersDto.setStatusUser(order.getStatusUser());
        ordersDto.setStatusAdmin(order.getStatusAdmin());
        ordersDto.setQuantity(order.getQuantity());
        ordersDto.setType(order.getType());
        ordersDto.setClient(order.getClient());

        // Avoid infinite recursion by passing depth + 1
        ordersDto.setOrder(convertToOrdersDto(order.getOrder(), depth + 1));

        // Convert Users to UsersDto for personnel
        if (order.getPersonnel() != null) {
            UsersDto personnelDto = convertToUsersDto(order.getPersonnel());
            ordersDto.setPersonnel(personnelDto);
        }

        // Convert Product to ProductDto
        if (order.getProduct() != null) {
            ProductDto productDto = convertToProductDto(order.getProduct());
            ordersDto.setProduct(productDto);
        }

        // Convert list of images if present
        if (order.getImages() != null) {
            List<String> imageStrings = new ArrayList<>();
            for (byte[] image : order.getImages()) {
                imageStrings.add(Base64.getEncoder().encodeToString(image));
            }
            ordersDto.setImages(imageStrings);
        }

        return ordersDto;
    }

    public Orders convertToOrders(OrdersDto ordersDto) {
        if (ordersDto == null) {
            return null;
        }

        Orders order = new Orders();
        order.setId(ordersDto.getId());

        if (ordersDto.getPreview() != null) {
            byte[] previewBytes = Base64.getDecoder().decode(ordersDto.getPreview());
            order.setPreview(previewBytes);
        }

        order.setOrderNo(ordersDto.getOrderNo());
        order.setPrice(ordersDto.getPrice());
        order.setStatusUser(ordersDto.getStatusUser());
        order.setStatusAdmin(ordersDto.getStatusAdmin());
        order.setQuantity(ordersDto.getQuantity());
        order.setType(ordersDto.getType());
        order.setClient(ordersDto.getClient());

        // Convert back to Orders for the order field
        order.setOrder(convertToOrders(ordersDto.getOrder()));

        // Convert UsersDto to Users for personnel
        if (ordersDto.getPersonnel() != null) {
            Users personnel = convertToUsers(ordersDto.getPersonnel());
            order.setPersonnel(personnel);
        }

        // Convert ProductDto to Product
        if (ordersDto.getProduct() != null) {
            Product product = convertToProduct(ordersDto.getProduct());
            order.setProduct(product);
        }

        // Convert list of image strings to byte arrays
        if (ordersDto.getImages() != null) {
            List<byte[]> imageBytes = new ArrayList<>();
            for (String imageString : ordersDto.getImages()) {
                imageBytes.add(Base64.getDecoder().decode(imageString));
            }
            order.setImages(imageBytes);
        }

        return order;
    }

    public Users convertToUsers(UsersDto usersDto) {
        if (usersDto == null) {
            return null;
        }

        Users users = new Users();
        users.setId(usersDto.getId());
        users.setFirstName(usersDto.getFirstName());
        users.setLastName(usersDto.getLastName());
        users.setEmail(usersDto.getEmail());
        users.setRole(usersDto.getRole());
        users.setCreatedAt(usersDto.getCreatedAt());
        users.setModifiedAt(usersDto.getModifiedAt());
        users.setDeletedAt(usersDto.getDeletedAt());
        users.setDeletedBy(usersDto.getDeletedBy());

        return users;
    }

    public ProductDto convertToProductDto(Product product) {
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setName(product.getName());
        productDto.setPrice(product.getPrice());
        if (product.getImage() != null) {
            String previewString = Base64.getEncoder().encodeToString(product.getImage());
            productDto.setImage(previewString);
        }
        // Convert Surface to SurfaceDto
        if (product.getSurface() != null) {
            SurfaceDto surfaceDto = convertToSurfaceDto(product.getSurface());
            productDto.setSurface(surfaceDto);
        }

        return productDto;
    }

    public Product convertToProduct(ProductDto productDto) {
        if (productDto == null) {
            return null;
        }

        Product product = new Product();
        product.setId(productDto.getId());
        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());
        if (productDto.getImage() != null) {
            byte[] imageBytes = Base64.getDecoder().decode(productDto.getImage());
            product.setImage(imageBytes);
        }
        // Convert SurfaceDto to Surface
        if (productDto.getSurface() != null) {
            Surface surface = convertToSurface(productDto.getSurface());
            product.setSurface(surface);
        }

        return product;
    }

    public SurfaceDto convertToSurfaceDto(Surface surface) {
        SurfaceDto surfaceDto = new SurfaceDto();
        surfaceDto.setId(surface.getId());
        if (surface.getBack() != null) {
            String backImageString = Base64.getEncoder().encodeToString(surface.getBack());
            surfaceDto.setBack(backImageString);
        }
        if (surface.getFront() != null) {
            String frontImageString = Base64.getEncoder().encodeToString(surface.getFront());
            surfaceDto.setFront(frontImageString);
        }
        return surfaceDto;
    }

    public Surface convertToSurface(SurfaceDto surfaceDto) {
        if (surfaceDto == null) {
            return null;
        }

        Surface surface = new Surface();
        surface.setId(surfaceDto.getId());
        if (surfaceDto.getBack() != null) {
            byte[] backImageBytes = Base64.getDecoder().decode(surfaceDto.getBack());
            surface.setBack(backImageBytes);
        }
        if (surfaceDto.getFront() != null) {
            byte[] frontImageBytes = Base64.getDecoder().decode(surfaceDto.getFront());
            surface.setFront(frontImageBytes);
        }
        return surface;
    }
}
