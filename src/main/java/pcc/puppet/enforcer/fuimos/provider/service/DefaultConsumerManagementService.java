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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pcc.puppet.enforcer.app.tools.Data;
import pcc.puppet.enforcer.fuimos.common.error.ServiceConsumerNotFound;
import pcc.puppet.enforcer.fuimos.provider.command.ServiceConsumerCreateCommand;
import pcc.puppet.enforcer.fuimos.provider.domain.ServiceConsumer;
import pcc.puppet.enforcer.fuimos.provider.event.ServiceConsumerCreationEvent;
import pcc.puppet.enforcer.fuimos.provider.ports.mapper.ServiceConsumerMapper;
import pcc.puppet.enforcer.fuimos.provider.ports.repository.ServiceConsumerRepository;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultConsumerManagementService implements ConsumerManagementService {
  private final ServiceConsumerRepository consumerRepository;
  private final ServiceConsumerMapper consumerMapper;

  @Override
  public Mono<ServiceConsumerCreationEvent> create(ServiceConsumerCreateCommand command) {
    ServiceConsumer serviceConsumer =
        ServiceConsumer.builder().id(Data.id()).name(command.getName()).build();
    return consumerRepository.save(serviceConsumer).map(consumerMapper::toEvent);
  }

  @Override
  public Mono<ServiceConsumer> findById(String trackId, String id) {
    return consumerRepository
        .findById(id)
        .switchIfEmpty(Mono.error(new ServiceConsumerNotFound(trackId, id)));
  }
}
