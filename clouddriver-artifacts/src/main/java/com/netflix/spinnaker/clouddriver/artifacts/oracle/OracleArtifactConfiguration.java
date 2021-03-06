/*
 * Copyright (c) 2017, 2018, Oracle America, Inc.
 *
 * The contents of this file are subject to the Apache License Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * If a copy of the Apache License Version 2.0 was not distributed with this file,
 * You can obtain one at https://www.apache.org/licenses/LICENSE-2.0.html
 */

package com.netflix.spinnaker.clouddriver.artifacts.oracle;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty("artifacts.oracle.enabled")
@EnableConfigurationProperties(OracleArtifactProviderProperties.class)
@RequiredArgsConstructor
@Slf4j
public class OracleArtifactConfiguration {
  private final OracleArtifactProviderProperties oracleArtifactProviderProperties;

  @Bean
  List<? extends OracleArtifactCredentials> oracleArtifactCredentials(
      String clouddriverUserAgentApplicationName) {
    return oracleArtifactProviderProperties.getAccounts().stream()
        .map(
            a -> {
              try {
                return new OracleArtifactCredentials(clouddriverUserAgentApplicationName, a);
              } catch (Exception e) {
                log.warn("Failure instantiating oracle artifact account {}: ", a, e);
                return null;
              }
            })
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
  }
}
