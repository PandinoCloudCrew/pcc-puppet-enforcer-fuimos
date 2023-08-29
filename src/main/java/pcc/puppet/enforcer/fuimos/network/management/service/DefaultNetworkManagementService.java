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

package pcc.puppet.enforcer.fuimos.network.management.service;

import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Service;
import pcc.puppet.enforcer.fuimos.common.error.NetworkNotFound;
import pcc.puppet.enforcer.fuimos.network.management.adapters.repository.NetworkRepository;
import pcc.puppet.enforcer.fuimos.network.management.command.NetworkCreateCommand;
import pcc.puppet.enforcer.fuimos.network.management.domain.Network;
import pcc.puppet.enforcer.fuimos.network.management.event.NetworkCreatedEvent;
import pcc.puppet.enforcer.fuimos.network.management.ports.mapper.NetworkMapper;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultNetworkManagementService implements NetworkManagementService {

  private final NetworkRepository networkRepository;
  private final NetworkMapper networkMapper;

  @Override
  public NetworkCreatedEvent create(String trackId, NetworkCreateCommand command) {
    Network network = networkMapper.fromCommand(command);
    TextEncryptor encryptor = Encryptors.text(network.getId(), network.getSalt());
    network.setFingerprint(encryptor.encrypt(command.getName()));
    Network entity = networkRepository.save(network);
    return networkMapper.entityToEvent(entity);
  }

  @Override
  public Network findById(String trackId, String id) throws NetworkNotFound {
    return networkRepository.findById(id).orElseThrow(() -> new NetworkNotFound(trackId, id));
  }

  @Override
  public Stream<NetworkCreatedEvent> getAllNetworks(String trackId) {
    return networkRepository.findAll().stream().map(networkMapper::entityToEvent);
  }
}
