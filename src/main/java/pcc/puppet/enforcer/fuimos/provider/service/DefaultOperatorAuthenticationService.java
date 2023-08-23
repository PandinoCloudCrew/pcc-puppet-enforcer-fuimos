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

package pcc.puppet.enforcer.fuimos.provider.service;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.stereotype.Service;
import pcc.puppet.enforcer.fuimos.network.management.domain.Network;
import pcc.puppet.enforcer.fuimos.network.management.service.NetworkManagementService;
import pcc.puppet.enforcer.fuimos.provider.command.ConsumerAuthenticateCommand;
import pcc.puppet.enforcer.fuimos.provider.domain.ServiceConsumer;
import pcc.puppet.enforcer.fuimos.provider.domain.ServiceOperator;
import pcc.puppet.enforcer.fuimos.provider.event.ConsumerAuthenticationEvent;
import pcc.puppet.enforcer.fuimos.provider.management.service.OperatorManagementService;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultOperatorAuthenticationService implements OperatorAuthenticationService {
  private final OperatorManagementService operatorManagementService;
  private final ConsumerManagementService consumerManagementService;
  private final NetworkManagementService networkManagementService;
  private final Map<String, TextEncryptor> encryptors = new HashMap<>();

  @Override
  public Mono<ConsumerAuthenticationEvent> authenticate(
      String trackId, ConsumerAuthenticateCommand command) {
    return consumerManagementService
        .findById(trackId, command.getConsumerId())
        .zipWith(operatorManagementService.findById(trackId, command.getOperatorId()))
        .zipWith(networkManagementService.findById(trackId, command.getNetworkId()))
        .map(
            objects -> {
              ServiceConsumer consumer = objects.getT1().getT1();
              ServiceOperator operator = objects.getT1().getT2();
              Network network = objects.getT2();
              TextEncryptor encryptor = obtainEncryptor(operator, network);
              return ConsumerAuthenticationEvent.builder()
                  .deviceId(command.getDeviceId())
                  .expirationDate(Instant.now().plus(Duration.ofDays(7)))
                  .token(encryptor.encrypt(generateToken(consumer).toString()))
                  .build();
            });
  }

  private ServiceToken generateToken(ServiceConsumer consumer) {
    return ServiceToken.builder()
        .id(consumer.getId())
        .naturalId(consumer.getNaturalId())
        .randomKey(KeyGenerators.string().generateKey())
        .build();
  }

  private TextEncryptor obtainEncryptor(ServiceOperator operator, Network network) {
    String id = String.format("%s::%s", operator.getId(), network.getId());
    if (encryptors.containsKey(id)) return encryptors.get(id);
    TextEncryptor encryptor = Encryptors.text(operator.getSalt(), network.getSalt());
    return encryptors.put(id, encryptor);
  }

  @Data
  @Builder
  public static class ServiceToken {
    private String id;
    private String naturalId;
    private String randomKey;

    @Override
    public String toString() {
      return String.format("%s::%s::%s", id, naturalId, randomKey);
    }
  }
}
