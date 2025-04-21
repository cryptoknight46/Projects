package com.ecom.project.repositories;

import com.ecom.project.model.Catogery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CatogeryRespository extends JpaRepository<Catogery,Long> {

    Catogery findBycatogeryName(String catogeryName);
}
