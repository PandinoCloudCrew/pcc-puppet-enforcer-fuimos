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

package pcc.puppet.enforcer.fuimos.provider.ports.api;

import static pcc.puppet.enforcer.fuimos.common.PccHeaders.TRACK_ID;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pcc.puppet.enforcer.fuimos.common.error.NetworkNotFound;
import pcc.puppet.enforcer.fuimos.common.error.ServiceConsumerNotFound;
import pcc.puppet.enforcer.fuimos.common.error.ServiceOperatorNotFound;
import pcc.puppet.enforcer.fuimos.provider.command.ConsumerAuthenticateCommand;
import pcc.puppet.enforcer.fuimos.provider.event.ConsumerAuthenticationEvent;
import pcc.puppet.enforcer.fuimos.provider.service.OperatorAuthenticationService;

@Slf4j
@Validated
@RestController
@RequestMapping("/operator")
@RequiredArgsConstructor
public class OperatorIngressController {

  private final OperatorAuthenticationService authenticationSvc;

  @PostMapping("/authenticate")
  public ConsumerAuthenticationEvent authenticate(
      @NotNull @RequestHeader(TRACK_ID) String trackId, @Valid @RequestBody ConsumerAuthenticateCommand command)
      throws ServiceConsumerNotFound, ServiceOperatorNotFound, NetworkNotFound {
    log.info(
        "device {} authentication request for operator {}",
        command.getDeviceId(),
        command.getOperatorId());
    return authenticationSvc.authenticate(trackId, command);
  }
}
