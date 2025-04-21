package com.ecom.project.controller;

import com.ecom.project.config.AppConstants;
import com.ecom.project.payload.CatogeryDTO;
import com.ecom.project.payload.CatogeryResponse;
import com.ecom.project.service.CatogeryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;




@RestController
@RequestMapping("/api")
public class ControllerCatogery {


    private CatogeryService catogeryService;

    public ControllerCatogery(CatogeryService catogeryService) {
        this.catogeryService = catogeryService;
    }

    @GetMapping("/public/catogeries")
    public ResponseEntity<CatogeryResponse> getAllCatogeries(@RequestParam(name="pageNumber",defaultValue = AppConstants.PAGE_NUMBER, required = false)Integer pageNumber,
                                                             @RequestParam(name="pageSize",defaultValue = AppConstants.PAGE_SIZE, required = false)Integer pageSize,
                                                             @RequestParam(name="sortBy",defaultValue = AppConstants.SORT_CATOGERY_BY, required = false) String sortBy,
                                                             @RequestParam(name="sortOrder",defaultValue = AppConstants.SORT_DIR, required = false)String sortOrder){
        CatogeryResponse catogeryResponse = catogeryService.getAllCatogeries(pageNumber,pageSize,sortBy,sortOrder);
        return new ResponseEntity<>(catogeryResponse,HttpStatus.OK);
    }

    @PostMapping("/public/catogeries")
    public ResponseEntity<CatogeryDTO> createCatogery(@RequestBody CatogeryDTO catogeryDTO){
        CatogeryDTO savedcatogerydto=catogeryService.createCatogery(catogeryDTO);
        return new ResponseEntity<>(savedcatogerydto,HttpStatus.CREATED);
    }


    @DeleteMapping("/admin/catogeries/{catogeryId}")
    public ResponseEntity<CatogeryDTO> deleteCatogery(@PathVariable Long catogeryId) {

            CatogeryDTO status = catogeryService.deleteCatogery(catogeryId);
            return new ResponseEntity<>(status, HttpStatus.OK);
    }
    @PutMapping("/admin/catogeries/{catogeryId}")
    public ResponseEntity<CatogeryDTO> updateCatogery(@Valid @RequestBody CatogeryDTO catogeryDTO,
                                                    @PathVariable Long catogeryId){

                CatogeryDTO catogeryDTO1= catogeryService.updateCatogery(catogeryDTO, catogeryId);
               return  new ResponseEntity<>(catogeryDTO1, HttpStatus.OK);
        }

    }







