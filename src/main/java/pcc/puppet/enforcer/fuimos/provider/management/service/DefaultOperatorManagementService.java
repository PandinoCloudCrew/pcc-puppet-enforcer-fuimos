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

import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.stereotype.Service;
import pcc.puppet.enforcer.app.tools.Data;
import pcc.puppet.enforcer.fuimos.common.error.NetworkNotFound;
import pcc.puppet.enforcer.fuimos.common.error.ServiceOperatorNotFound;
import pcc.puppet.enforcer.fuimos.network.management.adapters.repository.NetworkOperatorRepository;
import pcc.puppet.enforcer.fuimos.network.management.domain.Network;
import pcc.puppet.enforcer.fuimos.network.management.domain.NetworkOperator;
import pcc.puppet.enforcer.fuimos.network.management.service.NetworkManagementService;
import pcc.puppet.enforcer.fuimos.provider.management.adapters.repository.ServiceOperatorRepository;
import pcc.puppet.enforcer.fuimos.provider.management.command.ServiceOperatorCreateCommand;
import pcc.puppet.enforcer.fuimos.provider.management.domain.ServiceOperator;
import pcc.puppet.enforcer.fuimos.provider.management.event.ServiceOperatorCreatedEvent;
import pcc.puppet.enforcer.fuimos.provider.management.ports.mapper.ServiceOperatorMapper;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultOperatorManagementService implements OperatorManagementService {
  private final ServiceOperatorRepository operatorRepository;
  private final ServiceOperatorMapper operatorMapper;
  private final NetworkManagementService networkMgmtSvc;
  private final NetworkOperatorRepository networkOperatorRepository;

  @Override
  public ServiceOperatorCreatedEvent create(String trackId, ServiceOperatorCreateCommand command)
      throws NetworkNotFound {
    Network network = networkMgmtSvc.findById(trackId, command.getNetworkId());
    ServiceOperator operator =
        operatorRepository.save(
            ServiceOperator.builder()
                .id(Data.id())
                .salt(KeyGenerators.string().generateKey())
                .name(command.getName())
                .network(network)
                .type(command.getType())
                .build());
    networkOperatorRepository.save(
        NetworkOperator.builder().id(Data.id()).network(network).operator(operator).build());
    return operatorMapper.toEvent(operator);
  }

  @Override
  public ServiceOperator findById(String trackId, String id) throws ServiceOperatorNotFound {
    return operatorRepository
        .findById(id)
        .orElseThrow(() -> new ServiceOperatorNotFound(trackId, id));
  }

  @Override
  public Stream<ServiceOperator> findAll(String trackId) {
    return operatorRepository.findAll().stream();
  }
}
