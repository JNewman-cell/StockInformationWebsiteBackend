package com.stockInformation.config;

import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Loads a local .env file (if present) and adds its entries as a PropertySource
 * so Spring's ${...} placeholders can resolve environment values defined there.
 *
 * Behavior:
 * - Looks for a file named `.env` in the current working directory (user.dir).
 * - Parses KEY=VALUE lines (ignores comments starting with # and blank lines).
 * - Adds the properties as highest-precedence property source so they override
 *   lower-priority sources when present.
 */
public class DotenvEnvironmentPostProcessor implements EnvironmentPostProcessor {

    private static final String DOTENV_FILENAME = ".env";
    private static final String PROPERTY_SOURCE_NAME = "dotenvPropertySource";

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, org.springframework.boot.SpringApplication application) {
        File dotenv = new File(System.getProperty("user.dir"), DOTENV_FILENAME);
        if (!dotenv.exists() || !dotenv.isFile()) {
            return; // nothing to do
        }

        Map<String, Object> map = new LinkedHashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(dotenv))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;
                int idx = line.indexOf('=');
                if (idx <= 0) continue;
                String key = line.substring(0, idx).trim();
                String value = line.substring(idx + 1).trim();
                // Remove surrounding quotes if present
                if ((value.startsWith("\"") && value.endsWith("\"")) || (value.startsWith("'") && value.endsWith("'"))) {
                    value = value.substring(1, value.length() - 1);
                }
                map.put(key, value);
            }
        } catch (IOException e) {
            // Silently ignore; dotenv loading is optional
            return;
        }

        if (!map.isEmpty()) {
            MutablePropertySources sources = environment.getPropertySources();
            MapPropertySource ps = new MapPropertySource(PROPERTY_SOURCE_NAME, map);
            sources.addFirst(ps);
        }
    }
}
