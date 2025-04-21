package com.ecom.project.service;

import com.ecom.project.exceptions.ResourceNotFoundException;
import com.ecom.project.model.Address;
import com.ecom.project.model.User;
import com.ecom.project.payload.AddressDTO;
import com.ecom.project.repositories.AddressRepository;
import com.ecom.project.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    AddressRepository addressRepository;

    @Autowired
    UserRepository userRepository;



    @Override
    public AddressDTO createAddress(AddressDTO addressDTO, User user) {

        Address address=modelMapper.map(addressDTO, Address.class);
        List<Address>addressList=user.getAddresses();
        addressList.add(address);
        user.setAddresses(addressList);
        address.setUser(user);
        Address savedAddress=addressRepository.save(address);
        return modelMapper.map(savedAddress, AddressDTO.class);
    }

    @Override
    public List<AddressDTO> getAddresses() {
        List<Address>addresses=addressRepository.findAll();
        return addresses.stream().map(address -> modelMapper.map(address, AddressDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public AddressDTO getAddressByid(Long addressId) {
        Address address=addressRepository.findById(addressId)
                .orElseThrow(()->new ResourceNotFoundException("Address","addressId",addressId));


        return modelMapper.map(address,AddressDTO.class);
    }

    @Override
    public List<AddressDTO> getAddressesOfUser(User user) {
        List<Address>addresses=user.getAddresses();
        return addresses.stream().map(address -> modelMapper.map(address, AddressDTO.class))
                .collect(Collectors.toList());


    }

    @Override
    public AddressDTO updateAddress(Long addressId, AddressDTO addressDTO){
       Address addressFromDb=addressRepository.findById(addressId)
               .orElseThrow(()->new ResourceNotFoundException("Address","addressId",addressId));
        addressFromDb.setCity(addressDTO.getCity());
        addressFromDb.setBuildingName(addressDTO.getBuildingName());
        addressFromDb.setPincode(addressDTO.getPincode());
        addressFromDb.setState(addressDTO.getState());
        addressFromDb.setCountry(addressDTO.getCountry());
        addressFromDb.setStreet(addressDTO.getStreet());

        Address updatedAddresses = addressRepository.save(addressFromDb);

        User user=addressFromDb.getUser();
        user.getAddresses().removeIf(address ->
                address.getAddressId().equals(addressId));
        user.getAddresses().add(updatedAddresses);
        userRepository.save(user);

        return modelMapper.map(updatedAddresses,AddressDTO.class);
    }

    @Override
    public String deleteAddress(Long addressId) {
        Address addressFromDb=addressRepository.findById(addressId)
                .orElseThrow(()->new ResourceNotFoundException("Address","addressId",addressId));

        User user=addressFromDb.getUser();
        user.getAddresses().removeIf(address ->
                address.getAddressId().equals(addressId));

        userRepository.delete(user);
        addressRepository.delete(addressFromDb);


        return "Deleted successfully with AddressId";

    }


}
