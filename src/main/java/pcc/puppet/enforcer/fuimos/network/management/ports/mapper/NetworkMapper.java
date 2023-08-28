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

package pcc.puppet.enforcer.fuimos.network.management.ports.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import org.springframework.security.crypto.keygen.KeyGenerators;
import pcc.puppet.enforcer.app.tools.Data;
import pcc.puppet.enforcer.fuimos.network.management.command.NetworkCreateCommand;
import pcc.puppet.enforcer.fuimos.network.management.domain.Network;
import pcc.puppet.enforcer.fuimos.network.management.domain.NetworkStatus;
import pcc.puppet.enforcer.fuimos.network.management.event.NetworkCreatedEvent;

@Mapper(componentModel = ComponentModel.SPRING)
public interface NetworkMapper {
  default Network fromCommand(NetworkCreateCommand command) {
    return Network.builder()
        .id(Data.id())
        .status(NetworkStatus.ACTIVE)
        .name(command.getName())
        .sessionDuration(command.getSessionDuration())
        .salt(KeyGenerators.string().generateKey())
        .build();
  }

  NetworkCreatedEvent entityToEvent(Network network);
}
