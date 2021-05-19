package com.logistics.packagetracker.controller;

import com.logistics.packagetracker.dto.PackageDTO;
import com.logistics.packagetracker.dto.PackageMapper;
import com.logistics.packagetracker.enumeration.PackageStatus;
import com.logistics.packagetracker.response.SuccessResponse;
import com.logistics.packagetracker.service.PackageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/package")
public class PackageController
{
    @Autowired
    PackageMapper packageMapper;
    
    @Autowired
    PackageService packageService;
    
    public PackageController(PackageService packageService)
    {
        this.packageService = packageService;
    }
    
    @Operation(summary = "Get all packages", description = "List of packages")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found all packages",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PackageDTO.class))}),
            @ApiResponse(responseCode = "404", description = "Packages not found",
                    content = @Content)})
    @GetMapping("/getPackages")
    public ResponseEntity<EntityModel<List<PackageDTO>>> getAllPackages()
    {
        ResponseEntity<List<PackageDTO>> responseEntity = ResponseEntity.ok(packageService.findAllPackages().stream().map(s -> packageMapper.convertToDto(s)).collect(Collectors.toList()));
        SuccessResponse sr = (SuccessResponse) responseEntity.getBody();
        List<PackageDTO> packageDTOs = (List<PackageDTO>) sr.getObject();
        EntityModel<List<PackageDTO>> resource = EntityModel.of(packageDTOs);
        resource.add(linkTo(methodOn(this.getClass()).getAllPackages()).withSelfRel());
        return new ResponseEntity<>(resource, HttpStatus.OK);
    }
    
    @GetMapping("/getPackage/{id}")
    public ResponseEntity<PackageDTO> getPackage(@PathVariable String id)
    {
        return ResponseEntity.ok(packageMapper.convertToDto(packageService.getPackageById(id)));
    }
    
    @PostMapping("/pickupPackage")
    public ResponseEntity pickUp(@RequestBody PackageDTO packageDTO)
    {
        packageService.pickUpPackage(packageMapper.convertToEntity(packageDTO));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    
    @PutMapping("/sendPackage/{id}")
    public ResponseEntity sendPackage(@RequestBody PackageDTO packageDTO)
    {
        packageService.sendPackage(packageMapper.convertToEntity(packageDTO));
        return ResponseEntity.ok().build();
    }
    
    @PutMapping("/storePackage/{id}")
    public ResponseEntity storePackage(@RequestBody PackageDTO packageDTO)
    {
        packageService.storePackage(packageMapper.convertToEntity(packageDTO));
        return ResponseEntity.ok().build();
    }
    
    @PutMapping("/deliverPackage/{id}")
    public ResponseEntity deliverPackage(@RequestBody PackageDTO packageDTO)
    {
        packageService.deliverPackage(packageMapper.convertToEntity(packageDTO));
        return ResponseEntity.ok().build();
    }
    
    @PutMapping("/cancel/{id}")
    public ResponseEntity cancelOrder(@PathVariable String id)
    {
        packageService.cancelOrderById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
    
    @GetMapping("/getTrackerByStatus/{status}")
    public ResponseEntity<PackageDTO> getPackage(@PathVariable PackageStatus status)
    {
        return ResponseEntity.ok(packageMapper.convertToDto(packageService.findByStatus(status)));
    }
    
}
