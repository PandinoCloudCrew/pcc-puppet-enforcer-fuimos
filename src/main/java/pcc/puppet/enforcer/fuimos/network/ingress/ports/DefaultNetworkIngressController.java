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

package pcc.puppet.enforcer.fuimos.network.ingress.ports;

import jakarta.validation.constraints.NotNull;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pcc.puppet.enforcer.fuimos.network.ingress.command.DeviceAuthenticateCommand;
import pcc.puppet.enforcer.fuimos.network.ingress.command.DeviceRegisterCommand;
import pcc.puppet.enforcer.fuimos.network.ingress.command.MessageSendCommand;
import pcc.puppet.enforcer.fuimos.network.ingress.event.DeviceAuthenticationEvent;
import pcc.puppet.enforcer.fuimos.network.ingress.event.DeviceRegistrationEvent;
import pcc.puppet.enforcer.fuimos.network.ingress.event.MessageSentEvent;
import pcc.puppet.enforcer.fuimos.network.ingress.service.NetworkAuthentication;
import pcc.puppet.enforcer.fuimos.network.ingress.service.OperatorAuthentication;
import reactor.core.publisher.Mono;

@Slf4j
@Validated
@RestController
@RequestMapping("network")
@RequiredArgsConstructor
public class DefaultNetworkIngressController {

  private final OperatorAuthentication operatorAuthentication;
  private final NetworkAuthentication networkAuthentication;
  private final MessagePendingQueue messagePendingQueue;

  @PostMapping("send")
  public Mono<MessageSentEvent> send(@Valid @RequestBody MessageSendCommand message) {
    return messagePendingQueue.accept(message);
  }

  @PostMapping("authenticate")
  public Mono<DeviceAuthenticationEvent> authenticate(
      @Valid @RequestBody DeviceAuthenticateCommand command) {
    return operatorAuthentication.authenticate(command);
  }

  @PostMapping("join")
  public Mono<DeviceRegistrationEvent> join(
      @NotNull @RequestHeader("track-id") String trackId,
      @Valid @RequestBody DeviceRegisterCommand command) {
    log.info("create device {}", command);
    return networkAuthentication.register(trackId, command);
  }
}
