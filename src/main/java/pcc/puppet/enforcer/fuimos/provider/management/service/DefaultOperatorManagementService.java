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
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.stereotype.Service;
import pcc.puppet.enforcer.app.tools.Data;
import pcc.puppet.enforcer.fuimos.common.error.ServiceOperatorNotFound;
import pcc.puppet.enforcer.fuimos.network.management.domain.NetworkOperator;
import pcc.puppet.enforcer.fuimos.network.management.ports.repository.NetworkOperatorRepository;
import pcc.puppet.enforcer.fuimos.network.management.service.NetworkManagementService;
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
  private final NetworkManagementService networkManagementService;
  private final NetworkOperatorRepository networkOperatorRepository;

  @Override
  public Mono<ServiceOperatorCreatedEvent> create(ServiceOperatorCreateCommand command) {
    return networkManagementService
        .findById(command.getTrackId(), command.getNetworkId())
        .flatMap(
            network -> {
              ServiceOperator operator =
                  ServiceOperator.builder()
                      .id(Data.id())
                      .salt(KeyGenerators.string().generateKey())
                      .name(command.getName())
                      .network(network)
                      .type(command.getType())
                      .build();
              return operatorRepository
                  .save(operator)
                  .flatMap(
                      entity -> {
                        NetworkOperator networkOperator =
                            NetworkOperator.builder()
                                .id(Data.id())
                                .network(network)
                                .operator(entity)
                                .build();
                        return networkOperatorRepository
                            .save(networkOperator)
                            .map(NetworkOperator::getOperator);
                      })
                  .map(operatorMapper::toEvent);
            });
  }

  @Override
  public Mono<ServiceOperator> findById(String trackId, String id) {
    return operatorRepository
        .findById(id)
        .switchIfEmpty(Mono.error(new ServiceOperatorNotFound(trackId, id)));
  }
}
