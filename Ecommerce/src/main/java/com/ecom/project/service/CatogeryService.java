package com.ecom.project.service;

import com.ecom.project.payload.CatogeryDTO;
import com.ecom.project.payload.CatogeryResponse;

public interface CatogeryService {

    CatogeryResponse getAllCatogeries(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

    CatogeryDTO createCatogery(CatogeryDTO catogeryDTO);

    CatogeryDTO deleteCatogery(Long catogeryId);

    CatogeryDTO updateCatogery(CatogeryDTO catogeryDTO, Long catogeryId);

}
