package com.ecom.project.service;

import com.ecom.project.exceptions.ApIException;
import com.ecom.project.exceptions.ResourceNotFoundException;
import com.ecom.project.model.Catogery;
import com.ecom.project.payload.CatogeryDTO;
import com.ecom.project.payload.CatogeryResponse;
import com.ecom.project.repositories.CatogeryRespository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CatogeryServiceImpl implements CatogeryService {

    //private List<Catogery> catogeries= new ArrayList<>();

    @Autowired
    private CatogeryRespository catogeryRespository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CatogeryResponse  getAllCatogeries(Integer pageNumber,Integer pageSize,String sortBy,String sortOrder) {
        Sort sortandOrder =
                sortOrder.equalsIgnoreCase("asc")? Sort.by(sortBy)
                .ascending():Sort.by(sortBy).descending();

        Pageable pageDeatils= PageRequest.of(pageNumber,pageSize,sortandOrder);
        Page<Catogery>catogeryPage=catogeryRespository.findAll(pageDeatils);
        List<Catogery> catogeries = catogeryPage.getContent();
        if(catogeries.isEmpty())
            throw new ApIException("No catogery created till now.");

        List<CatogeryDTO>catogeryDTOS= catogeries.stream()
                .map( catogery->modelMapper.map(catogery,CatogeryDTO.class)).toList();
        CatogeryResponse catogeryResponse=new CatogeryResponse();
        catogeryResponse.setContent(catogeryDTOS);
        catogeryResponse.setPageNumber(catogeryPage.getNumber());
        catogeryResponse.setPageSize(catogeryPage.getSize());
        catogeryResponse.setTotalPages(catogeryPage.getTotalPages());
        catogeryResponse.setTotalElements(catogeryPage.getTotalElements());
        catogeryResponse.setLastpage(catogeryPage.isLast());
        return catogeryResponse;
    }

    @Override
    public CatogeryDTO createCatogery(CatogeryDTO catogeryDTO) {
        Catogery catogery= modelMapper.map(catogeryDTO, Catogery.class);
        Catogery savedcatogery= catogeryRespository.findBycatogeryName(catogeryDTO.getCatogeryName());
        if(savedcatogery!=null){
            throw new ApIException("catogery with the name "+catogeryDTO.getCatogeryName()+
                    "already exists");
        }
        Catogery savecatogery = catogeryRespository.save(catogery);
        return modelMapper.map(savecatogery, CatogeryDTO.class);

    }

    @Override
    public CatogeryDTO deleteCatogery(Long catogeryId) {
        Catogery catogery = catogeryRespository.findById(catogeryId)
                .orElseThrow(()->new ResourceNotFoundException( "Catogery","catogeryId",catogeryId ));

          catogeryRespository.delete(catogery);
        return modelMapper.map(catogery, CatogeryDTO.class);

    }

    @Override
    public CatogeryDTO updateCatogery(CatogeryDTO catogeryDTO, Long catogeryId) {


        Catogery savedcat= catogeryRespository.findById(catogeryId).orElseThrow(()->
                new ResourceNotFoundException("Catogery","catogeryId", catogeryId ));

        Catogery catogery = modelMapper.map(catogeryDTO, Catogery.class);
        Catogery saved = catogeryRespository.save(catogery);
        catogery.setCatogeryId(catogeryId);
        return modelMapper.map(saved, CatogeryDTO.class);

    }
}
