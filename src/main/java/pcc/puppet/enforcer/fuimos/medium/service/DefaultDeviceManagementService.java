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

package pcc.puppet.enforcer.fuimos.medium.service;

import java.time.Instant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import pcc.puppet.enforcer.app.tools.Mask;
import pcc.puppet.enforcer.fuimos.common.error.DeviceAuthenticationDenied;
import pcc.puppet.enforcer.fuimos.common.error.DeviceNotFound;
import pcc.puppet.enforcer.fuimos.medium.adapters.repository.DeviceRepository;
import pcc.puppet.enforcer.fuimos.medium.domain.Device;
import pcc.puppet.enforcer.fuimos.medium.domain.DeviceType;
import pcc.puppet.enforcer.fuimos.network.ingress.adapters.repository.DeviceAuthenticationRepository;
import pcc.puppet.enforcer.fuimos.network.ingress.domain.DeviceAuthentication;
import pcc.puppet.enforcer.fuimos.network.ingress.event.DeviceAuthenticationEvent;
import pcc.puppet.enforcer.fuimos.provider.ingress.event.ConsumerAuthenticationEvent;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultDeviceManagementService implements DeviceManagementService {
  private final DeviceRepository deviceRepository;
  private final DeviceAuthenticationRepository deviceAuthenticationRepository;

  @Override
  public Device create(String trackId, Device device) {
    log.info(
        "creating device for consumer {} of type {} with address {}",
        device.getConsumer().getId(),
        device.getType(),
        Mask.lastThree(device.getAddress()));
    return deviceRepository.save(device);
  }

  @Override
  public Device findByAddressAndType(String trackId, String address, DeviceType type)
      throws DeviceNotFound {
    return deviceRepository
        .findByAddressAndType(address, type)
        .orElseThrow(() -> new DeviceNotFound(trackId, address, type));
  }

  @Override
  public DeviceAuthenticationEvent saveDeviceToken(
      Device device, ConsumerAuthenticationEvent registrationEvent) {
    DeviceAuthentication deviceAuthentication =
        DeviceAuthentication.builder()
            .token(registrationEvent.getToken())
            .expirationDate(registrationEvent.getExpirationDate())
            .device(device)
            .build();
    DeviceAuthentication entity = deviceAuthenticationRepository.save(deviceAuthentication);
    return DeviceAuthenticationEvent.builder()
        .authenticationDate(Instant.now())
        .expirationDate(entity.getExpirationDate())
        .token(entity.getToken())
        .build();
  }

  @Override
  @Cacheable(cacheNames = "pcc::fuimos::device-by-token", key = "#token")
  public DeviceAuthentication findByToken(String trackId, String token) {
    return deviceAuthenticationRepository
        .findById(token)
        .orElseThrow(() -> new DeviceAuthenticationDenied(trackId, token));
  }
}
