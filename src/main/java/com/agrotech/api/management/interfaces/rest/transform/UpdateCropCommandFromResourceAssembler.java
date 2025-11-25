package com.agrotech.api.management.interfaces.rest.transform;

import com.agrotech.api.management.domain.model.commands.UpdateCropCommand;
import com.agrotech.api.management.interfaces.rest.resources.UpdateCropResource;

public class UpdateCropCommandFromResourceAssembler {
    public static UpdateCropCommand toCommandFromResource(Long id, UpdateCropResource resource){
        return new UpdateCropCommand(
                id,
                resource.name(),
                resource.tankMaxVolume(),
                resource.tankHeight(),
                resource.temperatureMaxThreshold(),
                resource.humidityMinThreshold()
        );
    }
}
