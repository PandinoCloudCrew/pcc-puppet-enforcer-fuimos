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

package pcc.puppet.enforcer.fuimos.provider.ingress.adapters.http;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static pcc.puppet.enforcer.fuimos.common.PccHeaders.TRACK_ID;

import io.micrometer.observation.annotation.Observed;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import pcc.puppet.enforcer.app.Project;
import pcc.puppet.enforcer.fuimos.common.util.JwtTool;
import pcc.puppet.enforcer.fuimos.provider.ingress.command.ConsumerAuthenticateCommand;
import pcc.puppet.enforcer.fuimos.provider.ingress.event.ConsumerAuthenticationEvent;

@Slf4j
@RequiredArgsConstructor
public class OperatorIngressClient {
  private static final String USER_AGENT =
      "OperatorIngressClient/" + Project.VERSION + " (" + Project.NAME + ")";
  private final String operatorAuthenticateUrl;
  private final RestTemplate restTemplate;

  @Observed
  public ConsumerAuthenticationEvent authenticate(
      @NotNull String trackId, @NotNull ConsumerAuthenticateCommand command) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.add(HttpHeaders.USER_AGENT, USER_AGENT);
    headers.add(TRACK_ID, trackId);
    headers.add(AUTHORIZATION, JwtTool.bearerToken());
    HttpEntity<ConsumerAuthenticateCommand> request = new HttpEntity<>(command, headers);
    return restTemplate
        .postForEntity(
            operatorAuthenticateUrl.concat("/authenticate"),
            request,
            ConsumerAuthenticationEvent.class)
        .getBody();
  }
}
