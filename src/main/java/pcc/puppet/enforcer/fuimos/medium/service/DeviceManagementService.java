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

import pcc.puppet.enforcer.fuimos.common.error.DeviceNotFound;
import pcc.puppet.enforcer.fuimos.medium.domain.Device;
import pcc.puppet.enforcer.fuimos.medium.domain.DeviceType;
import pcc.puppet.enforcer.fuimos.network.ingress.domain.DeviceAuthentication;
import pcc.puppet.enforcer.fuimos.network.ingress.event.DeviceAuthenticationEvent;
import pcc.puppet.enforcer.fuimos.provider.event.ConsumerAuthenticationEvent;

public interface DeviceManagementService {
  Device create(String trackId, Device device);

  Device findByAddressAndType(String trackId, String address, DeviceType type)
      throws DeviceNotFound;

  DeviceAuthenticationEvent saveDeviceToken(
      Device device, ConsumerAuthenticationEvent registrationEvent);

  DeviceAuthentication findByToken(String trackId, String token);
}
