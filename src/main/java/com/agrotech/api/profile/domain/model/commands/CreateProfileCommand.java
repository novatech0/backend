package com.agrotech.api.profile.domain.model.commands;

import java.time.LocalDate;

public record CreateProfileCommand(Long userId,
                                   String firstName,
                                   String lastName,
                                   String city,
                                   String country,
                                   LocalDate birthDate,
                                   String description,
                                   String photo,
                                   String occupation,
                                   Integer experience){}
