package com.cosmomedia.location.service.product;

import com.cosmomedia.location.dto.UsersDto;
import com.cosmomedia.location.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductCRUD {
    Page<Product> getProductList(Pageable pageable);
}
