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
package pcc.puppet.enforcer.fuimos.consumer;

import pcc.puppet.enforcer.fuimos.network.Device;
import pcc.puppet.enforcer.fuimos.network.Message;
import pcc.puppet.enforcer.fuimos.network.event.SendMessageEvent;
import reactor.core.publisher.Mono;

public interface PeopleInteractionOperator {

  Message compose(String channel, String content);

  Mono<SendMessageEvent> send(Message message);

  Mono<Device> search(String medium);

  AddContactEvent addContact(String medium);
}
