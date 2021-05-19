package com.logistics.packagetracker.controller;

import com.google.common.base.Strings;
import com.logistics.packagetracker.dto.PackageMapper;
import com.logistics.packagetracker.entity.Package;
import com.logistics.packagetracker.entity.PackageDTO;
import com.logistics.packagetracker.entity.TrackingDetails;
import com.logistics.packagetracker.enumeration.PackageStatus;
import com.logistics.packagetracker.exception.EntityNotFoundException;
import com.logistics.packagetracker.response.ApiResponse;
import com.logistics.packagetracker.service.PackageService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpHeaders;
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
    
    public ResponseEntity<?> getAllPackages()
    {
        ResponseEntity<List<PackageDTO>> responseEntity = ResponseEntity.ok(packageService.findAllPackages().stream().map(s -> packageMapper.convertToDto(s)).collect(Collectors.toList()));
        List<PackageDTO> packageDTOs = responseEntity.getBody();
        if (packageDTOs != null)
        {
            EntityModel<List<PackageDTO>> resource = EntityModel.of(packageDTOs);
            resource.add(linkTo(methodOn(this.getClass()).getAllPackages()).withSelfRel());
            
            ApiResponse apiResponse = new ApiResponse(HttpStatus.OK, "Packages retrieved successfully", HttpStatus.OK.getReasonPhrase(), resource);
            return ResponseEntity.ok().body(apiResponse);
        }
        ApiResponse response = new ApiResponse(HttpStatus.NO_CONTENT, "No Package found", HttpStatus.NO_CONTENT.getReasonPhrase(), null);
        return buildResponseEntity(response);
    }
    
    @GetMapping("/tracker/{id}")
    public ResponseEntity<Object> getPackage(@PathVariable String id)
    {
        PackageDTO packageDTO = packageMapper.convertToDto(packageService.getPackageById(id));
        if (packageDTO != null)
        {
            ApiResponse apiResponse = new ApiResponse(HttpStatus.OK, "Transaction completed successfully", HttpStatus.OK.getReasonPhrase(), packageDTO);
            return buildResponseEntity(EntityModel.of(apiResponse, List.of(linkTo(methodOn(this.getClass()).getPackage(id)).withSelfRel())));
        }
        ApiResponse apiResponse = new ApiResponse(HttpStatus.BAD_REQUEST, "Error retrieving package", HttpStatus.BAD_REQUEST.getReasonPhrase(), null);
        return buildResponseEntity(apiResponse);
    }
    
    @PostMapping("/tracker/{id}")
    public ResponseEntity<Object> updatePackage(@RequestBody TrackingDetails tracker, @PathVariable String id)
    {
        String updId = packageService.trackPackage(tracker, id);
        if (Strings.isNullOrEmpty(updId))
        {
            throw new EntityNotFoundException("Tracker not updated");
        }
        ApiResponse apiResponse = new ApiResponse(HttpStatus.OK, "Tracker updated successfully", HttpStatus.OK.getReasonPhrase(), updId);
        return new ResponseEntity<>(apiResponse, apiResponse.getStatus());
    }
    
    @GetMapping("/tracker/status/{status}")
    public ResponseEntity<List<PackageDTO>> getPackage(@PathVariable PackageStatus status)
    {
        return ResponseEntity.ok(packageService.findByStatus(status).stream().map(d -> packageMapper.convertToDto(d)).collect(Collectors.toList()));
    }
    
    @PostMapping("/tracker/createPackage")
    public ResponseEntity<PackageDTO> createPackage(@RequestBody Package pack)
    {
        Package created = packageService.createPackage(pack);
        PackageDTO dto = packageMapper.convertToDto(created);
        return ResponseEntity.ok(dto);
    }
    
    private ResponseEntity<Object> buildResponseEntity(Object object)
    {
        if (object instanceof ApiResponse)
        {
            return new ResponseEntity<Object>(object, new HttpHeaders(), ((ApiResponse) object).getStatus());
        }
        return new ResponseEntity<Object>(object, new HttpHeaders(), HttpStatus.ALREADY_REPORTED);
    }
}
