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

package pcc.puppet.enforcer.fuimos.network.management.ports.api;

import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pcc.puppet.enforcer.fuimos.network.management.command.NetworkCreateCommand;
import pcc.puppet.enforcer.fuimos.network.management.event.NetworkCreatedEvent;
import pcc.puppet.enforcer.fuimos.network.management.service.NetworkService;
import reactor.core.publisher.Mono;

@Validated
@RestController
@RequestMapping("network")
@RequiredArgsConstructor
public class NetworkController {
  private final NetworkService networkService;

  @PostMapping
  public Mono<NetworkCreatedEvent> create(@Valid @RequestBody NetworkCreateCommand command) {
    return networkService.create(command);
  }
}