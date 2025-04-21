package com.ecom.project.repositories;

import com.ecom.project.model.Catogery;
import com.ecom.project.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository  extends JpaRepository<Product,Long>, JpaSpecificationExecutor {
    Page<Product> findByCatogeryOrderByPriceAsc(Catogery catogery, Pageable pageDetails);

    Page<Product> findByProductNameLikeIgnoreCase(String keyword, Pageable pageDetails);
}
