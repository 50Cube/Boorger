package pl.lodz.p.it.boorger.controllers.impl;

import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.boorger.controllers.AddressController;
import pl.lodz.p.it.boorger.dto.AddressDTO;
import pl.lodz.p.it.boorger.dto.mappers.AddressMapper;
import pl.lodz.p.it.boorger.exceptions.AppBaseException;
import pl.lodz.p.it.boorger.services.AddressService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@AllArgsConstructor
@Transactional(propagation = Propagation.NEVER)
public class AddressControllerImpl implements AddressController {

    private AddressService addressService;

    @GetMapping("/addresses")
    public List<AddressDTO> getAddresses() throws AppBaseException {
        return addressService.getAddresses().stream().map(AddressMapper::mapToDto).collect(Collectors.toList());
    }

    @PostMapping("/address")
    public void addAddress(@Valid @RequestBody AddressDTO addressDTO) throws AppBaseException {
        addressService.addAddress(AddressMapper.mapFromDto(addressDTO));
    }
}
