package com.logistics.packagetracker.controller;

import com.google.common.base.Strings;
import com.logistics.packagetracker.mapper.TrackerMapper;
import com.logistics.packagetracker.entity.Package;
import com.logistics.packagetracker.entity.PackageDTO;
import com.logistics.packagetracker.entity.TrackingDetail;
import com.logistics.packagetracker.enumeration.PackageStatus;
import com.logistics.packagetracker.exception.EntityNotFoundException;
import com.logistics.packagetracker.response.ApiResponse;
import com.logistics.packagetracker.service.PackageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/package")
public class PackageController
{
    @Autowired
    private TrackerMapper trackerMapper;
    
    @Autowired
    private PackageService packageService;
    
    public static final String API_KEY = System.getenv().get("PACKAGETRACKER_KEY"); // temporary security key for the end points kept in an environment variable
    
    public PackageController(PackageService packageService)
    {
        this.packageService = packageService;
    }
    // Spring Open API Documenation for endpoint
    @Operation(summary = "Get all packages", description = "List of packages")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Found all packages",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = PackageDTO.class))}),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Packages not found",
                    content = @Content)})
    @GetMapping("/getAllPackages")
    public ResponseEntity<List<PackageDTO>> getAllPackages(@RequestParam("key") String key) throws AccessDeniedException
    {
        // validates the temporary security setup to secure endpoints.
        if (!key.equals(API_KEY))
        {
            throw new AccessDeniedException("Access denied. Provide a valid Key.");
        }
        List<PackageDTO> result = packageService.findAllPackages().stream().map(s -> trackerMapper.convertToDto(s)).collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }
    
    @GetMapping("/tracker/{id}")
    public ResponseEntity<Object> getPackage(@PathVariable String id, @RequestParam("key") String key) throws AccessDeniedException
    {
        if (!key.equals(API_KEY))
        {
            throw new AccessDeniedException("Access denied. Provide a valid Key.");
        }
        PackageDTO packageDTO = trackerMapper.convertToDto(packageService.getPackageById(id));
        if (packageDTO != null)
        {
            ApiResponse apiResponse = new ApiResponse(HttpStatus.OK, "Package tracking complete.", packageDTO);
            return buildResponseEntity(apiResponse, HttpStatus.OK);
        }
        throw new EntityNotFoundException("Package not found");
    }
    
    @PutMapping("/tracker/{id}")
    public ResponseEntity<Object> updatePackage(@RequestBody TrackingDetail tracker, @PathVariable String id, @RequestParam("key") String key) throws AccessDeniedException
    {
        if (!key.equals(API_KEY))
        {
            throw new AccessDeniedException("Access denied. Provide a valid Key.");
        }
        String updId = packageService.trackPackage(tracker, id);
        if (!Strings.isNullOrEmpty(updId))
        {
            ApiResponse apiResponse = new ApiResponse(HttpStatus.OK, "Tracker updated successfully", updId);
            return new ResponseEntity<>(apiResponse, apiResponse.getStatus());
        }
        throw new EntityNotFoundException("Tracker not updated");
    }
    
    @GetMapping("/status/{status}")
    public ResponseEntity<List<PackageDTO>> getPackageByStatus(@PathVariable PackageStatus status, @RequestParam("key") String key) throws AccessDeniedException
    {
        if (!key.equals(API_KEY))
        {
            throw new AccessDeniedException("Access denied. Provide a valid Key.");
        }
        return ResponseEntity.ok(packageService.findByStatus(status).stream().map(d -> trackerMapper.convertToDto(d)).collect(Collectors.toList()));
    }
    
    @PostMapping(value = "/createPackage")
    public ResponseEntity<PackageDTO> createPackage(@RequestBody Package pack, @RequestParam("key") String key) throws AccessDeniedException
    {
        if (!key.equals(API_KEY))
        {
            throw new AccessDeniedException("Access denied. Provide a valid Key.");
        }
        Package created = packageService.createPackage(pack);
        PackageDTO dto = trackerMapper.convertToDto(created);
        return ResponseEntity.ok(dto);
    }
    
    private ResponseEntity<Object> buildResponseEntity(ApiResponse apiResponse, HttpStatus status)
    {
        return new ResponseEntity<Object>(apiResponse, new HttpHeaders(), status);
    }
}
