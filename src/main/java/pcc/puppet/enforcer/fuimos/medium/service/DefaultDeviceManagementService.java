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
import org.springframework.stereotype.Service;
import pcc.puppet.enforcer.app.tools.Mask;
import pcc.puppet.enforcer.fuimos.common.error.DeviceAuthenticationDenied;
import pcc.puppet.enforcer.fuimos.common.error.DeviceNotFound;
import pcc.puppet.enforcer.fuimos.medium.domain.Device;
import pcc.puppet.enforcer.fuimos.medium.domain.DeviceType;
import pcc.puppet.enforcer.fuimos.medium.ports.repository.DeviceRepository;
import pcc.puppet.enforcer.fuimos.network.ingress.domain.DeviceAuthentication;
import pcc.puppet.enforcer.fuimos.network.ingress.event.DeviceAuthenticationEvent;
import pcc.puppet.enforcer.fuimos.network.ingress.ports.repository.DeviceAuthenticationRepository;
import pcc.puppet.enforcer.fuimos.provider.event.ConsumerAuthenticationEvent;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultDeviceManagementService implements DeviceManagementService {
  private final DeviceRepository deviceRepository;
  private final DeviceAuthenticationRepository deviceAuthenticationRepository;

  @Override
  public Mono<Device> create(String trackId, Device device) {
    log.info(
        "creating device for consumer {} of type {} with address {}",
        device.getConsumer().getId(),
        device.getType(),
        Mask.lastThree(device.getAddress()));
    return deviceRepository.save(device);
  }

  @Override
  public Mono<Device> findByAddressAndType(String trackId, String address, DeviceType type) {
    return deviceRepository
        .findByAddressAndType(address, type)
        .switchIfEmpty(Mono.error(new DeviceNotFound(trackId, address, type)));
  }

  @Override
  public Mono<DeviceAuthenticationEvent> saveDeviceToken(
      Device device, ConsumerAuthenticationEvent registrationEvent) {
    DeviceAuthentication deviceAuthentication =
        DeviceAuthentication.builder()
            .token(registrationEvent.getToken())
            .expirationDate(registrationEvent.getExpirationDate())
            .device(device)
            .build();
    return deviceAuthenticationRepository
        .save(deviceAuthentication)
        .map(
            entity ->
                DeviceAuthenticationEvent.builder()
                    .authenticationDate(Instant.now())
                    .expirationDate(entity.getExpirationDate())
                    .token(entity.getToken())
                    .build());
  }

  @Override
  public Mono<DeviceAuthentication> findByToken(String trackId, String token) {
    return deviceAuthenticationRepository
        .findById(token)
        .switchIfEmpty(Mono.error(new DeviceAuthenticationDenied(trackId, token)));
  }
}
