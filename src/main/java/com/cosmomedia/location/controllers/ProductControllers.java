package com.cosmomedia.location.controllers;

import com.cosmomedia.location.dto.OneResponse;
import com.cosmomedia.location.dto.OrdersDto;
import com.cosmomedia.location.dto.ProductDto;
import com.cosmomedia.location.dto.UsersDto;
import com.cosmomedia.location.entities.Client;
import com.cosmomedia.location.entities.Message;
import com.cosmomedia.location.entities.Product;
import com.cosmomedia.location.service.product.ProductCRUD;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ProductControllers {
    private final ProductCRUD productCRUD;

    @QueryMapping
    public Page<ProductDto> allProduct(@Argument(name = "page") int page, @Argument(name = "size") int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return productCRUD.getProductList(pageable);
    }

    @QueryMapping
    public Page<ProductDto> allActiveProduct(@Argument(name = "page") int page, @Argument(name = "size") int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return productCRUD.getActiveProductList(pageable);
    }

    @QueryMapping
    public OneResponse<ProductDto> getOneProduct(@Argument(name = "name") String name) {
        return productCRUD.getOneProduct(name);
    }


    @MutationMapping
    public Message addProduct(@Argument(name = "product") Product product) {
        return productCRUD.addProduct(product);
    }

    @MutationMapping
    public Message deleteProduct(@Argument(name = "id") Long id) {
        return productCRUD.deleteProduct(id);
    }

    @MutationMapping
    public Message updateProduct(@Argument(name = "name") String name, @Argument(name = "product") Product product) {
        return productCRUD.updateProduct(name, product);
    }
}
