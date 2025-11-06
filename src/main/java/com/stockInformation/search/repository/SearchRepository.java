package com.stockInformation.search.repository;

import com.stockInformation.search.dto.AutocompleteResult;

import java.util.List;

public interface SearchRepository {

    List<AutocompleteResult> searchByInputIgnoreCase(String query);
}