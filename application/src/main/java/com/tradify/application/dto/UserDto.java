package com.tradify.application.dto;

public record UserDto(
        String username,
        String name,
        String lastName,
        String companyName
) {
}
