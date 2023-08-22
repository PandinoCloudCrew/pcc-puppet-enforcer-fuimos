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
import pcc.puppet.enforcer.fuimos.provider.command.ServiceConsumerCreateCommand;
import pcc.puppet.enforcer.fuimos.provider.event.ServiceConsumerCreationEvent;
import pcc.puppet.enforcer.fuimos.provider.service.ConsumerManagementService;
import reactor.core.publisher.Mono;

@Slf4j
@Validated
@RestController
@RequestMapping("/consumer")
@RequiredArgsConstructor
public class ServiceConsumerController {

  private final ConsumerManagementService consumerManagementService;

  @PostMapping
  public Mono<ServiceConsumerCreationEvent> create(
      @NotNull @RequestHeader("track-id") String trackId,
      @Valid @RequestBody ServiceConsumerCreateCommand command) {
    log.info("creating service consumer");
    return consumerManagementService.create(trackId, command);
  }
}
