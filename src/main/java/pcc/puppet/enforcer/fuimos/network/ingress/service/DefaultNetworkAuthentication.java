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

package pcc.puppet.enforcer.fuimos.network.ingress.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pcc.puppet.enforcer.fuimos.common.error.NetworkNotFound;
import pcc.puppet.enforcer.fuimos.common.error.ServiceConsumerNotFound;
import pcc.puppet.enforcer.fuimos.common.error.ServiceOperatorNotFound;
import pcc.puppet.enforcer.fuimos.medium.domain.Device;
import pcc.puppet.enforcer.fuimos.medium.ports.mapper.DeviceMapper;
import pcc.puppet.enforcer.fuimos.medium.service.DeviceManagementService;
import pcc.puppet.enforcer.fuimos.network.ingress.command.DeviceAuthenticateCommand;
import pcc.puppet.enforcer.fuimos.network.ingress.command.DeviceRegisterCommand;
import pcc.puppet.enforcer.fuimos.network.ingress.event.DeviceAuthenticationEvent;
import pcc.puppet.enforcer.fuimos.network.ingress.event.DeviceRegistrationEvent;
import pcc.puppet.enforcer.fuimos.network.management.domain.Network;
import pcc.puppet.enforcer.fuimos.network.management.service.NetworkManagementService;
import pcc.puppet.enforcer.fuimos.provider.domain.ServiceConsumer;
import pcc.puppet.enforcer.fuimos.provider.domain.ServiceOperator;
import pcc.puppet.enforcer.fuimos.provider.management.service.OperatorManagementService;
import pcc.puppet.enforcer.fuimos.provider.service.ConsumerManagementService;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultNetworkAuthentication implements NetworkAuthentication {

  private final DeviceMapper deviceMapper;
  private final DeviceManagementService deviceManagementService;
  private final NetworkManagementService networkManagementService;
  private final OperatorManagementService operatorManagementService;
  private final ConsumerManagementService consumerManagementService;

  @Override
  public DeviceAuthenticationEvent createOrGet(DeviceAuthenticateCommand registerCommand) {
    return null;
  }

  @Override
  public DeviceRegistrationEvent register(String trackId, DeviceRegisterCommand command)
      throws ServiceConsumerNotFound, ServiceOperatorNotFound, NetworkNotFound {
    Device device = deviceMapper.fromCommand(command);
    ServiceConsumer consumer = consumerManagementService.findById(trackId, command.getConsumerId());
    device.setConsumer(consumer);
    ServiceOperator operator = operatorManagementService.findById(trackId, command.getOperatorId());
    device.setOperator(operator);
    Network network = networkManagementService.findById(trackId, command.getNetworkId());
    device.setNetwork(network);
    Device entity = deviceManagementService.create(trackId, device);
    return deviceMapper.fromEntity(entity);
  }
}
