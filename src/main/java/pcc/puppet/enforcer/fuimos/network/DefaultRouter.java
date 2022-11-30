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
import pcc.puppet.enforcer.fuimos.network.event.AcknowledgeMessageEvent;
import pcc.puppet.enforcer.fuimos.network.event.RelayMessageEvent;
import pcc.puppet.enforcer.fuimos.network.event.SendMessageEvent;
import pcc.puppet.enforcer.fuimos.provider.CarrierGatewayProvider;
import pcc.puppet.enforcer.fuimos.provider.event.AuthenticateDeviceEvent;
import reactor.core.publisher.Mono;

@Slf4j
@Singleton
@RequiredArgsConstructor
public class DefaultRouter implements Router {

  private final CarrierGatewayProvider carrierGatewayProvider;
  private final DeviceGateway deviceGateway;

  @Override
  public Mono<AuthenticateDeviceEvent> authenticate(Device device) {
    return carrierGatewayProvider
        .search(device.getCarrier())
        .flatMap(carrierNetworkNegotiator -> carrierNetworkNegotiator.authenticate(device));
  }

  @Override
  public Mono<RelayMessageEvent> relay(Message message) {
    return carrierGatewayProvider
        .search(chooseDevice(message).getCarrier())
        .flatMap(carrierGateway -> carrierGateway.relay(message));
  }

  @Override
  public Mono<AcknowledgeMessageEvent> acknowledge(Message message) {
    return deviceGateway.acknowledge(chooseDevice(message));
  }

  @Override
  public Mono<SendMessageEvent> deliver(ChannelGateway gateway, Message message) {
    return gateway.send(message);
  }

  private Device chooseDevice(Message message) {
    return MessageDirection.IN.equals(message.getDirection())
        ? message.getSource()
        : message.getDestination();
  }
}
