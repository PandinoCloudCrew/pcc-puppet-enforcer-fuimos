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

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import pcc.puppet.enforcer.fuimos.network.ingress.adapters.repository.OperatorQueueRepository;
import pcc.puppet.enforcer.fuimos.network.ingress.command.MessageSendCommand;
import pcc.puppet.enforcer.fuimos.network.management.domain.DeliveryPriority;
import pcc.puppet.enforcer.fuimos.network.management.domain.Network;
import pcc.puppet.enforcer.fuimos.network.management.domain.OperatorQueue;
import pcc.puppet.enforcer.fuimos.network.management.domain.ServiceType;
import pcc.puppet.enforcer.fuimos.network.management.ports.mapper.OperatorQueueMapper;
import pcc.puppet.enforcer.fuimos.provider.management.domain.ServiceOperator;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultQueueManagementService implements QueueManagementService {
  private final OperatorQueueRepository operatorQueueRepository;
  private final OperatorQueueMapper operatorQueueMapper;

  public String queueName(
      Network network, ServiceOperator operator, ServiceType type, DeliveryPriority priority) {
    StringBuilder sb = new StringBuilder();
    return sb.append(network.getId())
        .append("-")
        .append(type)
        .append("-")
        .append(operator.getId())
        .append("-")
        .append(priority)
        .toString();
  }

  @Override
  public OperatorQueue create(
      String trackId,
      DeliveryPriority priority,
      ServiceOperator operator,
      Network network,
      MessageSendCommand message) {
    OperatorQueue queue = operatorQueueMapper.fromCommand(message);
    queue.setTrackId(trackId);
    queue.setOperator(operator);
    queue.setNetwork(network);
    queue.setPriority(priority);
    queue.setName(this.queueName(network, operator, message.getType(), priority));
    log.debug("Looking for queue {}", queue.getName());
    return operatorQueueRepository.save(queue);
  }

  @Cacheable(cacheNames = "pcc::fuimos::operator-queue-by-name")
  public Optional<OperatorQueue> findByName(String name) {
    return operatorQueueRepository.findByName(name);
  }
}
