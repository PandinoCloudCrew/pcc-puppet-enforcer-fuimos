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

package pcc.puppet.enforcer.fuimos.network.ingress.ports;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pcc.puppet.enforcer.fuimos.network.ingress.command.DeviceAuthenticateCommand;
import pcc.puppet.enforcer.fuimos.network.ingress.command.DeviceRegisterCommand;
import pcc.puppet.enforcer.fuimos.network.ingress.command.MessageSendCommand;
import pcc.puppet.enforcer.fuimos.network.ingress.event.DeviceAuthenticationEvent;
import pcc.puppet.enforcer.fuimos.network.ingress.event.DeviceRegistrationEvent;
import pcc.puppet.enforcer.fuimos.network.ingress.event.MessageSentEvent;
import pcc.puppet.enforcer.fuimos.network.ingress.service.NetworkAuthentication;
import pcc.puppet.enforcer.fuimos.network.ingress.service.OperatorAuthentication;

@Service
@RequiredArgsConstructor
public class DefaultNetworkIngress implements NetworkIngress {

  private final OperatorAuthentication operatorAuthentication;
  private final NetworkAuthentication networkAuthentication;
  private final MessagePendingQueue messagePendingQueue;

  @Override
  public MessageSentEvent send(MessageSendCommand message) {
    return messagePendingQueue.accept(message);
  }

  @Override
  public DeviceRegistrationEvent join(DeviceRegisterCommand device) {
    DeviceAuthenticateCommand authenticateCommand = networkAuthentication.request(device);
    DeviceAuthenticationEvent authenticationEvent =
        operatorAuthentication.authenticate(authenticateCommand);
    return networkAuthentication.register(authenticationEvent);
  }
}
