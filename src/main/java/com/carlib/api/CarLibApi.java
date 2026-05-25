package com.carlib.api;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public final class CarLibApi {
    private static final Map<String, CarIntegrationProvider> PROVIDERS = new ConcurrentHashMap<>();

    private CarLibApi() {}

    public static void registerProvider(CarIntegrationProvider provider) {
        PROVIDERS.put(provider.providerId(), provider);
    }

    public static Optional<CarIntegrationProvider> getProvider(String providerId) {
        return Optional.ofNullable(PROVIDERS.get(providerId));
    }

    public static Collection<CarIntegrationProvider> providers() {
        return Collections.unmodifiableCollection(PROVIDERS.values());
    }
}
