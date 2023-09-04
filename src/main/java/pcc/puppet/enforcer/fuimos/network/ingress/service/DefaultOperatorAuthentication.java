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
import pcc.puppet.enforcer.fuimos.common.error.DeviceNotFound;
import pcc.puppet.enforcer.fuimos.medium.domain.Device;
import pcc.puppet.enforcer.fuimos.medium.ports.mapper.DeviceMapper;
import pcc.puppet.enforcer.fuimos.medium.service.DeviceManagementService;
import pcc.puppet.enforcer.fuimos.network.ingress.command.DeviceAuthenticateCommand;
import pcc.puppet.enforcer.fuimos.network.ingress.event.DeviceAuthenticationEvent;
import pcc.puppet.enforcer.fuimos.provider.ingress.adapters.http.OperatorIngressClient;
import pcc.puppet.enforcer.fuimos.provider.ingress.event.ConsumerAuthenticationEvent;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultOperatorAuthentication implements OperatorAuthentication {
  private final DeviceManagementService deviceMgmtSvc;
  private final OperatorIngressClient ingressClient;
  private final DeviceMapper deviceMapper;

  @Override
  public DeviceAuthenticationEvent authenticate(String trackId, DeviceAuthenticateCommand command)
      throws DeviceNotFound {
    Device device =
        deviceMgmtSvc.findByAddressAndType(trackId, command.getAddress(), command.getType());
    ConsumerAuthenticationEvent authenticationEvent =
        ingressClient.authenticate(trackId, deviceMapper.toAuthenticateCommand(device));
    return deviceMgmtSvc.saveDeviceToken(device, authenticationEvent);
  }
}
