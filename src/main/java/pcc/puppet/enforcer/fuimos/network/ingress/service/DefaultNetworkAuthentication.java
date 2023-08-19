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
import pcc.puppet.enforcer.app.tools.Mask;
import pcc.puppet.enforcer.fuimos.medium.domain.Device;
import pcc.puppet.enforcer.fuimos.medium.ports.mapper.DeviceMapper;
import pcc.puppet.enforcer.fuimos.medium.ports.repository.DeviceRepository;
import pcc.puppet.enforcer.fuimos.network.ingress.command.DeviceAuthenticateCommand;
import pcc.puppet.enforcer.fuimos.network.ingress.command.DeviceRegisterCommand;
import pcc.puppet.enforcer.fuimos.network.ingress.event.DeviceAuthenticationEvent;
import pcc.puppet.enforcer.fuimos.network.ingress.event.DeviceRegistrationEvent;
import pcc.puppet.enforcer.fuimos.network.management.service.NetworkService;
import pcc.puppet.enforcer.fuimos.provider.management.service.OperatorManagementService;
import pcc.puppet.enforcer.fuimos.provider.service.ConsumerManagementService;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultNetworkAuthentication implements NetworkAuthentication {

  private final DeviceMapper deviceMapper;
  private final DeviceRepository deviceRepository;
  private final NetworkService networkService;
  private final OperatorManagementService operatorManagementService;
  private final ConsumerManagementService consumerManagementService;

  @Override
  public DeviceAuthenticationEvent createOrGet(DeviceAuthenticateCommand registerCommand) {
    return null;
  }

  @Override
  public Mono<DeviceRegistrationEvent> register(String trackId, DeviceRegisterCommand command) {
    Device device = deviceMapper.fromCommand(command);
    return consumerManagementService
        .findById(trackId, command.getConsumerId())
        .zipWith(operatorManagementService.findById(trackId, command.getOperatorId()))
        .zipWith(networkService.findById(trackId, command.getNetworkId()))
        .flatMap(
            tupleResult -> {
              deviceMapper.addValues(tupleResult, device);
              log.info(
                  "created device for consumer {} of type {} with address {}",
                  device.getConsumer().getId(),
                  device.getType(),
                  Mask.lastThree(device.getAddress()));
              return deviceRepository.save(device).map(deviceMapper::fromEntity);
            });
  }
}
