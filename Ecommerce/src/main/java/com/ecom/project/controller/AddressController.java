package com.ecom.project.controller;

import com.ecom.project.model.User;
import com.ecom.project.payload.AddressDTO;
import com.ecom.project.service.AddressService;
import com.ecom.project.util.AuthUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController

@RequestMapping("/api")
public class AddressController {

    @Autowired
    AuthUtil authUtil;

    @Autowired
    AddressService addressService;

    @PostMapping("/addresses")
    public ResponseEntity<AddressDTO> createAddress(@Valid  @RequestBody AddressDTO addressDTO){
        User user=authUtil.loggedInUser();
        AddressDTO savedAdressDTO=addressService.createAddress(addressDTO,user);
        return new ResponseEntity<>(savedAdressDTO,HttpStatus.CREATED);
    }
    @GetMapping("/addresses")
    public ResponseEntity<List<AddressDTO>> getAlladdresses(){


       List<AddressDTO> allAddressesDTO=addressService.getAddresses();
        return  new ResponseEntity<>(allAddressesDTO,HttpStatus.OK);
    }

    @GetMapping("/addresses/{addressId}")
    public ResponseEntity<AddressDTO> getAddressById(@PathVariable Long addressId){
        AddressDTO addressDTO=addressService.getAddressByid(addressId);
        return new ResponseEntity<>(addressDTO,HttpStatus.OK);
    }

    @GetMapping("/user/addresses")
    public ResponseEntity<List<AddressDTO>>getAddressByUser(){
        User user=authUtil.loggedInUser();
        List<AddressDTO> addressDTO=addressService.getAddressesOfUser(user);
        return new ResponseEntity<>(addressDTO,HttpStatus.OK);
    }
    @PutMapping("addresses/{addressId}")
    public ResponseEntity<AddressDTO> updateAddress(@PathVariable Long addressId,@RequestBody AddressDTO addressDTO) {
        AddressDTO addressById=addressService.updateAddress(addressId,addressDTO);
        return new ResponseEntity<>(addressById,HttpStatus.OK);
    }

    @DeleteMapping("addresses/{addressId}")
    public ResponseEntity<String> deleteAddress(@PathVariable Long addressId ) {
        String status=addressService.deleteAddress(addressId);
        return new ResponseEntity<>(status,HttpStatus.OK);
    }

}
