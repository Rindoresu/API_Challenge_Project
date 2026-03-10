package ar.rindoresu.apinotificationchallenge.api.dto;

import java.util.List;

public record ValidationErrorResponse(
        String error,
        List<String> details
) {}