package com.carlib.api;

import java.util.Set;

public interface CarIntegrationProvider {
    String providerId();
    Set<String> supportedFeatures();
    Object invoke(String featureKey, Object input);
}
