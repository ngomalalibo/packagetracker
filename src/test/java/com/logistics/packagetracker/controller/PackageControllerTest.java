package com.logistics.packagetracker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.logistics.packagetracker.entity.Package;
import com.logistics.packagetracker.entity.TrackingDetail;
import com.logistics.packagetracker.enumeration.PackageStatus;
import com.logistics.packagetracker.service.PackageService;
import com.logistics.packagetracker.util.DateConverter;
import com.logistics.packagetracker.util.GenerateTrackingCode;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.LinkedHashMap;

@Slf4j
@AutoConfigureMockMvc
@SpringBootTest
class PackageControllerTest
{
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Autowired
    PackageService packageService;
    
    @BeforeEach
    public void setup()
    {
    }
    
    @Test
    void getAllPackages() throws Exception
    {
        String path = "/api/package/getAll";
        
        mockMvc.perform(MockMvcRequestBuilders.get(path)
                                              .contentType(MediaType.APPLICATION_JSON_VALUE)
                                              .param("key", PackageController.API_KEY))
               .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(MockMvcResultMatchers.status().isOk());
    }
    
    @Test
    void getPackage() throws Exception
    {
        String path = "/api/package/tracker/{id}";
        String exists = "60a7dbb7c497eb75fd0c148d";
        String doesNot = "60a7e70e13cd056c5c843ba8";
        
        
        // pass
        mockMvc.perform(MockMvcRequestBuilders.get(path, exists)
                                              .contentType("application/json")
                                              .param("key", PackageController.API_KEY))
               .andExpect(MockMvcResultMatchers.status().isOk());
        
        // fail
        mockMvc.perform(MockMvcRequestBuilders.get(path, doesNot)
                                              .contentType("application/json")
                                              .param("key", PackageController.API_KEY))
               .andExpect(MockMvcResultMatchers.status().is5xxServerError());
    }
    
    @Test
    void updatePackage() throws Exception
    {
        String id = "60a6d03bbd41d20bcbd60d28";
        long dateTime = System.currentTimeMillis();
        TrackingDetail expected = new TrackingDetail(PackageStatus.WAREHOUSE, dateTime, "Fedex",
                                                     "Ikeja", "Lagos", "Nigeria", "100001");
        TrackingDetail actual = packageService.getCurrentTracker(id);
        
        
        String path = "/api/package/tracker/{id}";
        mockMvc.perform(MockMvcRequestBuilders.put(path, id)
                                              .contentType(MediaType.APPLICATION_JSON_VALUE)
                                              .param("key", PackageController.API_KEY)
                                              .content(objectMapper.writeValueAsString(expected)))
               .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(MockMvcResultMatchers.status().isOk());
    }
    
    @Test
    void testGetPackage() throws Exception
    {
        String path = "/api/package/status/{status}";
        mockMvc.perform(MockMvcRequestBuilders.get(path, "PICKED_UP")
                                              .contentType(MediaType.APPLICATION_JSON_VALUE)
                                              .param("key", PackageController.API_KEY))
               .andExpect(MockMvcResultMatchers.status().isOk());
    }
    
    @Test
    void createPackage() throws Exception
    {
        // Arrange // Act // Assert
        TrackingDetail td = new TrackingDetail(PackageStatus.PICKED_UP, System.currentTimeMillis(), "Raven", "Westeros", "Kings Landing", "The Wall", "102332");
        Package pack = new Package(GenerateTrackingCode.generateTrackingCode(), PackageStatus.PICKED_UP, ZonedDateTime.now().format(DateConverter.formatter), 20.4,
                                   ZonedDateTime.now().plusDays(3).format(DateConverter.formatter), Collections.singletonList(td));
        
        String path = "/api/package/create";
        mockMvc.perform(MockMvcRequestBuilders.post(path)
                                              .contentType(MediaType.APPLICATION_JSON_VALUE)
                                              .param("key", PackageController.API_KEY)
                                              .content(objectMapper.writeValueAsString(pack)))
               .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(MockMvcResultMatchers.status().isOk());
        
    }
    
    @Test
    void getPackageTrackingHistory() throws Exception
    {
        String id = "60a7d2293c55cd377f8f0254";
        String path = "/api/package/history/{id}";
        mockMvc.perform(MockMvcRequestBuilders.get(path, id)
                                              .contentType(MediaType.APPLICATION_JSON_VALUE)
                                              .param("key", PackageController.API_KEY))
               .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(MockMvcResultMatchers.status().isOk());
    }
    
}