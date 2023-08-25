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

package pcc.puppet.enforcer.fuimos.network.management.ports.queue;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pcc.puppet.enforcer.fuimos.network.ingress.adapters.repository.OperatorQueueRepository;
import pcc.puppet.enforcer.fuimos.network.ingress.command.MessageSendCommand;
import pcc.puppet.enforcer.fuimos.network.management.domain.Network;
import pcc.puppet.enforcer.fuimos.network.management.domain.OperatorQueue;
import pcc.puppet.enforcer.fuimos.network.management.ports.mapper.OperatorQueueMapper;
import pcc.puppet.enforcer.fuimos.provider.domain.ServiceOperator;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultQueueManagementService implements QueueManagementService {
  private final OperatorQueueRepository operatorQueueRepository;
  private final OperatorQueueMapper operatorQueueMapper;

  @Override
  public Mono<OperatorQueue> create(
      ServiceOperator operator, Network network, MessageSendCommand message) {
    OperatorQueue queue = operatorQueueMapper.fromCommand(message);
    queue.setOperator(operator);
    queue.setNetwork(network);
    queue.setName(
        String.format(
            "%s-%s-%s-%s",
            network.getId(), queue.getType(), operator.getId(), queue.getPriority()));
    return operatorQueueRepository
        .findByName(queue.getName())
        .switchIfEmpty(operatorQueueRepository.save(queue));
  }
}
