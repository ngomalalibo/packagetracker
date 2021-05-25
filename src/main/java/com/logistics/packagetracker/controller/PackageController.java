package com.logistics.packagetracker.controller;

import com.google.common.base.Strings;
import com.logistics.packagetracker.entity.Package;
import com.logistics.packagetracker.entity.TrackingDetail;
import com.logistics.packagetracker.entity.TrackingDetailDTO;
import com.logistics.packagetracker.exception.EntityNotFoundException;
import com.logistics.packagetracker.mapper.TrackerMapper;
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

@RestController
@RequestMapping("/api/package")
public class PackageController
{
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
                            schema = @Schema(implementation = TrackingDetailDTO.class))}),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Packages not found",
                    content = @Content)})
    @GetMapping("/getAll")
    public ResponseEntity<Object> getAllPackages(@RequestParam("key") String key) throws AccessDeniedException
    {
        // validates the temporary security setup to secure endpoints.
        if (!key.equals(API_KEY))
        {
            throw new AccessDeniedException("Access denied. Provide a valid Key.");
        }
        List<Package> result = packageService.findAllPackages();
        ApiResponse apiResponse = new ApiResponse(HttpStatus.OK, "Packages retrieved.", result);
        return buildResponseEntity(apiResponse, HttpStatus.OK);
    }
    
    @GetMapping("/tracker/{id}")
    public ResponseEntity<Object> getPackage(@PathVariable String id, @RequestParam("key") String key) throws AccessDeniedException
    {
        if (!key.equals(API_KEY))
        {
            throw new AccessDeniedException("Access denied. Provide a valid Key.");
        }
        Package pack = packageService.getPackageById(id);
        if (pack != null)
        {
            ApiResponse apiResponse = new ApiResponse(HttpStatus.OK, "Package retrieved.", pack);
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
            ApiResponse apiResponse = new ApiResponse(HttpStatus.OK, "Tracker updated.", updId);
            return buildResponseEntity(apiResponse, apiResponse.getStatus());
        }
        throw new EntityNotFoundException("Tracker not updated");
    }
    
    @GetMapping("/status/{status}")
    public ResponseEntity<Object> getPackageByStatus(@PathVariable String status, @RequestParam("key") String key) throws AccessDeniedException
    {
        if (!key.equals(API_KEY))
        {
            throw new AccessDeniedException("Access denied. Provide a valid Key.");
        }
        List<Package> pack = packageService.findByStatus(status);
        ApiResponse apiResponse = new ApiResponse(HttpStatus.OK, "Tracker updated.", pack);
        return buildResponseEntity(apiResponse, HttpStatus.OK);
    }
    
    @PostMapping(value = "/create")
    public ResponseEntity<Package> createPackage(@RequestBody Package pack, @RequestParam("key") String key) throws AccessDeniedException
    {
        if (!key.equals(API_KEY))
        {
            throw new AccessDeniedException("Access denied. Provide a valid Key.");
        }
        Package created = packageService.createPackage(pack);
        return ResponseEntity.ok(created);
    }
    
    @GetMapping("/history/{id}")
    public ResponseEntity<Object> getPackageTrackingHistory(@PathVariable String id, @RequestParam("key") String key) throws AccessDeniedException
    {
        if (!key.equals(API_KEY))
        {
            throw new AccessDeniedException("Access denied. Provide a valid Key.");
        }
        List<TrackingDetailDTO> pth = packageService.getPackageTrackingHistory(id);
        if (pth != null)
        {
            ApiResponse apiResponse = new ApiResponse(HttpStatus.OK, "Package tracking history retrieved.", pth);
            return buildResponseEntity(apiResponse, HttpStatus.OK);
        }
        throw new EntityNotFoundException("Package not found");
    }
    
    
    private ResponseEntity<Object> buildResponseEntity(ApiResponse apiResponse, HttpStatus status)
    {
        return new ResponseEntity<Object>(apiResponse, new HttpHeaders(), status);
    }
}
