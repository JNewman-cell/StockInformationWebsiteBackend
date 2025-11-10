package com.stockInformation.search.utils;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class UtilsTest {

    @Test
    void testSanitizeQueryForSql_withAlphanumeric() {
        // Given
        String input = "apple inc.";

        // When
        String result = utils.sanitizeQueryForSql(input);

        // Then
        assertThat(result).isEqualTo("apple%inc%");
    }

    @Test
    void testSanitizeQueryForSql_withMultipleSpecialChars() {
        // Given
        String input = "apple & co.";

        // When
        String result = utils.sanitizeQueryForSql(input);

        // Then
        assertThat(result).isEqualTo("apple%co%");
    }

    @Test
    void testSanitizeQueryForSql_withNoAlphanumeric() {
        // Given
        String input = "!!!";

        // When
        String result = utils.sanitizeQueryForSql(input);

        // Then
        assertThat(result).isEqualTo("!!!");
    }

    @Test
    void testSanitizeQueryForSql_withNull() {
        // Given
        String input = null;

        // When
        String result = utils.sanitizeQueryForSql(input);

        // Then
        assertThat(result).isNull();
    }

    @Test
    void testSanitizeQueryForSql_withEmptyString() {
        // Given
        String input = "";

        // When
        String result = utils.sanitizeQueryForSql(input);

        // Then
        assertThat(result).isEqualTo("");
    }

    @Test
    void testSanitizeQueryForSql_withOnlyAlphanumeric() {
        // Given
        String input = "apple";

        // When
        String result = utils.sanitizeQueryForSql(input);

        // Then
        assertThat(result).isEqualTo("apple");
    }

    @Test
    void testSanitizeQueryForSql_withConsecutiveSpecialChars() {
        // Given
        String input = "apple   inc";

        // When
        String result = utils.sanitizeQueryForSql(input);

        // Then
        assertThat(result).isEqualTo("apple%inc");
    }
}