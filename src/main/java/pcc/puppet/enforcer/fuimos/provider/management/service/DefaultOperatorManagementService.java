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

package pcc.puppet.enforcer.fuimos.provider.management.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pcc.puppet.enforcer.app.tools.Data;
import pcc.puppet.enforcer.fuimos.network.management.service.NetworkService;
import pcc.puppet.enforcer.fuimos.provider.domain.ServiceOperator;
import pcc.puppet.enforcer.fuimos.provider.management.command.ServiceOperatorCreateCommand;
import pcc.puppet.enforcer.fuimos.provider.management.event.ServiceOperatorCreatedEvent;
import pcc.puppet.enforcer.fuimos.provider.management.ports.mapper.ServiceOperatorMapper;
import pcc.puppet.enforcer.fuimos.provider.management.ports.repository.ServiceOperatorRepository;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultOperatorManagementService implements OperatorManagementService {
  private final ServiceOperatorRepository operatorRepository;
  private final ServiceOperatorMapper operatorMapper;
  private final NetworkService networkService;

  @Override
  public Mono<ServiceOperatorCreatedEvent> create(ServiceOperatorCreateCommand command) {
    return networkService
        .findById(command.getNetworkId())
        .flatMap(
            network -> {
              ServiceOperator operator =
                  ServiceOperator.builder()
                      .id(Data.id())
                      .name(command.getName())
                      .network(network)
                      .type(command.getType())
                      .build();
              return operatorRepository.save(operator).map(operatorMapper::toEvent);
            });
  }
}
