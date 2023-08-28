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

package pcc.puppet.enforcer.fuimos.network.ingress.ports.queue;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;
import pcc.puppet.enforcer.app.tools.Data;
import pcc.puppet.enforcer.fuimos.medium.service.DeviceManagementService;
import pcc.puppet.enforcer.fuimos.network.ingress.command.MessageSendCommand;
import pcc.puppet.enforcer.fuimos.network.ingress.domain.DeviceAuthentication;
import pcc.puppet.enforcer.fuimos.network.ingress.event.MessageSentEvent;
import pcc.puppet.enforcer.fuimos.network.management.domain.OperatorQueue;
import pcc.puppet.enforcer.fuimos.network.management.ports.queue.QueueManagementService;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultMessagePendingQueue implements MessagePendingQueue {

  private final StreamBridge streamBridge;
  private final QueueManagementService queueManagementService;
  private final DeviceManagementService deviceManagementService;

  @Override
  public MessageSentEvent accept(String trackId, MessageSendCommand message) {
    DeviceAuthentication deviceAuthentication =
        deviceManagementService.findByToken(trackId, message.getSenderToken());
    OperatorQueue operatorQueue =
        queueManagementService.create(
            trackId,
            deviceAuthentication.getDevice().getOperator(),
            deviceAuthentication.getDevice().getNetwork(),
            message);
    streamBridge.send(operatorQueue.getName(), message);
    return MessageSentEvent.builder()
        .id(Data.id())
        .queue(operatorQueue)
        .createDate(Data.now())
        .trackId(trackId)
        .build();
  }
}
