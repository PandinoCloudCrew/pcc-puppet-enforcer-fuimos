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

package pcc.puppet.enforcer.fuimos.provider.management.ports.api;

import static pcc.puppet.enforcer.fuimos.common.PccHeaders.TRACK_ID;

import jakarta.validation.constraints.NotNull;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pcc.puppet.enforcer.fuimos.provider.domain.ServiceOperator;
import pcc.puppet.enforcer.fuimos.provider.management.command.ServiceOperatorCreateCommand;
import pcc.puppet.enforcer.fuimos.provider.management.event.ServiceOperatorCreatedEvent;
import pcc.puppet.enforcer.fuimos.provider.management.service.OperatorManagementService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Validated
@RestController
@RequestMapping("/operator/management")
@RequiredArgsConstructor
public class OperatorManagementController {
  private final OperatorManagementService managementService;

  @PostMapping
  public Mono<ServiceOperatorCreatedEvent> create(
      @NotNull @RequestHeader(TRACK_ID) String trackId,
      @Valid @RequestBody ServiceOperatorCreateCommand command) {
    return managementService.create(trackId, command);
  }

  @GetMapping
  public Flux<ServiceOperator> getAll(@NotNull @RequestHeader(TRACK_ID) String trackId) {
    return managementService.findAll(trackId);
  }
}
