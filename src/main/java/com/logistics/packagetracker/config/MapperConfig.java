package com.logistics.packagetracker.config;

import com.logistics.packagetracker.dto.PackageDTO;
import com.logistics.packagetracker.entity.Package;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class MapperConfig
{
    @Bean
    public ModelMapper modelMapper()
    {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                   .setFieldMatchingEnabled(true)
                   .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
                   .setMatchingStrategy(MatchingStrategies.LOOSE);
        
        modelMapper.typeMap(Package.class, PackageDTO.class).addMappings(mapper ->
                                                                         {
                                                                             mapper.map(Package::getId,
                                                                                        PackageDTO::setId);
                                                                             mapper.map(Package::getTrackingCode,
                                                                                        PackageDTO::setTrackingCode);
                                                                             mapper.map(Package::getCreatedDate,
                                                                                        PackageDTO::setCreatedDate);
                                                                             mapper.map(Package::getWeight,
                                                                                        PackageDTO::setWeight);
                                                                             mapper.map(Package::getCarrier,
                                                                                        PackageDTO::setCarrier);
                                                                             mapper.map(Package::getEstDeliveryDate,
                                                                                        PackageDTO::setEstDeliveryDate);
                                                                             mapper.map(src -> src.getCurrentTracker().getSource(),
                                                                                        PackageDTO::setCurrentSource);
                                                                             mapper.map(src -> src.getCurrentTracker().getCity(),
                                                                                        PackageDTO::setCurrentCity);
                                                                             mapper.map(src -> src.getCurrentTracker().getState(),
                                                                                        PackageDTO::setCurrentState);
                                                                             mapper.map(src -> src.getCurrentTracker().getCountry(),
                                                                                        PackageDTO::setCurrentCountry);
                                                                             mapper.map(src -> src.getCurrentTracker().getZip(),
                                                                                        PackageDTO::setCurrentZipcode);
            
                                                                         });
        modelMapper.validate();
        return modelMapper;
    }
    
    
}
