package com.tradify.application.dto;

import java.util.Set;

public record AddUsersToCompanyDto(
        long companyId,
        Set<String> usernames
) {
}
