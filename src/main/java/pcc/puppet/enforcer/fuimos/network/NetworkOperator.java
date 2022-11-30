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
package pcc.puppet.enforcer.fuimos.network;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pcc.puppet.enforcer.fuimos.network.channel.ChannelGateway;
import pcc.puppet.enforcer.fuimos.network.channel.ChannelGatewayProvider;
import pcc.puppet.enforcer.fuimos.network.event.AcknowledgeMessageEvent;
import pcc.puppet.enforcer.fuimos.network.event.SendMessageEvent;
import reactor.core.publisher.Mono;

@Slf4j
@Singleton
@RequiredArgsConstructor
public class NetworkOperator {

  private final Router router;
  private final ChannelGatewayProvider channelGatewayProvider;

  Mono<AcknowledgeMessageEvent> acceptMessage(Message message) {
    return router
        .authenticate(message.getSource())
        .flatMap(
            authenticateDeviceEvent ->
                router.relay(message).flatMap(relayMessageEvent -> router.acknowledge(message)));
  }

  Mono<SendMessageEvent> sendMessage(Message message) {
    ChannelGateway channelGateway =
        channelGatewayProvider.search(message.getDestination().getCarrier());
    return router.deliver(channelGateway, message);
  }
}
