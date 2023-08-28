/*
 * Copyright 2022 Pandino Cloud Crew (C)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package pcc.puppet.enforcer.app.configuration.http;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pcc.puppet.enforcer.fuimos.adapters.http.OperatorIngressClient;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class HttpClientFactory {

  private final HttpServiceConfiguration serviceConfiguration;


  @Bean
  public OperatorIngressClient operatorIngressClient(RestTemplateBuilder restTemplateBuilder) {
    String baseUrl = serviceConfiguration.getOperatorIngress().getUri();
    log.info("created OperatorIngressClient with host: " + baseUrl);
    return new OperatorIngressClient(baseUrl, restTemplateBuilder.build());
  }

}
