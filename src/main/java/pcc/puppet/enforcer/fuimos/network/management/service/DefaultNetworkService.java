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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.stereotype.Service;
import pcc.puppet.enforcer.app.tools.Data;
import pcc.puppet.enforcer.fuimos.network.management.command.NetworkCreateCommand;
import pcc.puppet.enforcer.fuimos.network.management.domain.Network;
import pcc.puppet.enforcer.fuimos.network.management.domain.NetworkStatus;
import pcc.puppet.enforcer.fuimos.network.management.event.NetworkCreatedEvent;
import pcc.puppet.enforcer.fuimos.network.management.ports.NetworkMapper;
import pcc.puppet.enforcer.fuimos.network.management.ports.repository.NetworkRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultNetworkService implements NetworkService {

  private final NetworkRepository networkRepository;
  private final NetworkMapper networkMapper;

  @Override
  public Mono<NetworkCreatedEvent> create(NetworkCreateCommand command) {
    Network network =
        Network.builder()
            .id(Data.id())
            .status(NetworkStatus.ACTIVE)
            .name(command.getName())
            .sessionDuration(command.getSessionDuration())
            .salt(KeyGenerators.string().generateKey())
            .build();
    TextEncryptor encryptor = Encryptors.text(network.getId(), network.getSalt());
    network.setFingerprint(encryptor.encrypt(command.getName()));
    return networkRepository.save(network).map(networkMapper::toEvent);
  }

  @Override
  public Mono<Network> findById(String id) {
    return networkRepository.findById(id);
  }

  @Override
  public Flux<NetworkCreatedEvent> getAllNetworks() {
    return networkRepository.findAll().map(networkMapper::toEvent);
  }
}
