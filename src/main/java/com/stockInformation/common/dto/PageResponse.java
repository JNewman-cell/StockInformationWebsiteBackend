package com.stockInformation.common.dto;

import org.springframework.data.domain.Sort;
import java.util.List;

/**
 * Compact page response to avoid duplicative pagination fields produced by Spring's Page serialization.
 */
public record PageResponse<T>(
        List<T> content,
        int pageNumber,
        int pageSize,
        long totalElements,
        int totalPages,
        int numberOfElements,
        Sort sort
) {
}
