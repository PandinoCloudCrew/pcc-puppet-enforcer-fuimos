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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.Builder;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import pcc.puppet.enforcer.fuimos.adapters.http.OperatorIngressClient;
import reactor.netty.http.client.HttpClient;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class HttpClientFactory {

  private final HttpServiceConfiguration serviceConfiguration;

  private static HttpClient httpClient(String baseUrl) {
    return HttpClient.create().baseUrl(baseUrl).followRedirect(true).compress(true).wiretap(true);
  }

  @Bean
  public OperatorIngressClient operatorIngressClient(Builder builder) {
    String baseUrl = serviceConfiguration.getOperatorIngress().getUri();
    log.info("created OperatorIngressClient with host: " + baseUrl);
    HttpServiceProxyFactory factory = getFactory(builder, baseUrl);
    return factory.createClient(OperatorIngressClient.class);
  }

  private static HttpServiceProxyFactory getFactory(Builder builder, String url) {
    WebClient webClient =
        builder
            .baseUrl(url)
            .clientConnector(new ReactorClientHttpConnector(httpClient(url)))
            .build();
    return HttpServiceProxyFactory.builder()
        .clientAdapter(WebClientAdapter.forClient(webClient))
        .build();
  }
}
