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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pcc.puppet.enforcer.app.tools.Mask;
import pcc.puppet.enforcer.fuimos.common.error.DeviceNotFound;
import pcc.puppet.enforcer.fuimos.medium.domain.Device;
import pcc.puppet.enforcer.fuimos.medium.ports.repository.DeviceRepository;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultDeviceManagementService implements DeviceManagementService {
  private final DeviceRepository deviceRepository;

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
  public Mono<Device> findByAddressAndType(String trackId, String address, String type) {
    return deviceRepository
        .findByAddressAndType(address, type)
        .switchIfEmpty(Mono.error(new DeviceNotFound(trackId, address, type)));
  }
}
