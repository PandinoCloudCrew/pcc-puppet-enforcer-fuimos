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

package pcc.puppet.enforcer.fuimos.adapters.http;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import jakarta.validation.constraints.NotNull;
import javax.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;
import pcc.puppet.enforcer.app.Project;
import pcc.puppet.enforcer.fuimos.common.util.JwtTool;
import pcc.puppet.enforcer.fuimos.provider.command.ConsumerAuthenticateCommand;
import pcc.puppet.enforcer.fuimos.provider.event.ConsumerAuthenticationEvent;
import reactor.core.publisher.Mono;

@HttpExchange
public interface OperatorIngressClient {
  String USER_AGENT = "OperatorIngressClient/" + Project.VERSION + " (" + Project.NAME + ")";

  @PostExchange
  Mono<ConsumerAuthenticationEvent> authenticate(
      @NotNull @RequestHeader(HttpHeaders.USER_AGENT) String userAgent,
      @NotNull @RequestHeader(AUTHORIZATION) String authorization,
      @NotNull @Valid @RequestBody ConsumerAuthenticateCommand command);

  default Mono<ConsumerAuthenticationEvent> authenticate(
      @NotNull @Valid ConsumerAuthenticateCommand command) {
    return JwtTool.authentication()
        .flatMap(token -> authenticate(USER_AGENT, JwtTool.toBearer(token), command));
  }
}
