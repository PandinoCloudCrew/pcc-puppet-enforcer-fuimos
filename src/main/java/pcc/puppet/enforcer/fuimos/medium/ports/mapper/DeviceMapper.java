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

package pcc.puppet.enforcer.fuimos.medium.ports.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import pcc.puppet.enforcer.app.tools.Data;
import pcc.puppet.enforcer.fuimos.medium.domain.Device;
import pcc.puppet.enforcer.fuimos.network.ingress.command.DeviceRegisterCommand;
import pcc.puppet.enforcer.fuimos.network.ingress.event.DeviceRegistrationEvent;
import pcc.puppet.enforcer.fuimos.network.management.domain.Network;
import pcc.puppet.enforcer.fuimos.provider.domain.ServiceConsumer;
import pcc.puppet.enforcer.fuimos.provider.domain.ServiceOperator;
import reactor.util.function.Tuple2;

@Mapper(componentModel = ComponentModel.SPRING)
public interface DeviceMapper {

  default void addValues(
      Tuple2<Tuple2<ServiceConsumer, ServiceOperator>, Network> tuple, Device device) {
    device.setConsumer(tuple.getT1().getT1());
    device.setOperator(tuple.getT1().getT2());
    device.setNetwork(tuple.getT2());
  }

  default Device fromCommand(DeviceRegisterCommand command) {
    return Device.builder()
        .id(Data.id())
        .address(command.getAddress())
        .type(command.getType())
        .build();
  }

  default DeviceRegistrationEvent fromEntity(Device device) {
    return DeviceRegistrationEvent.builder()
        .id(device.getId())
        .networkId(device.getNetwork().getId())
        .networkName(device.getNetwork().getName())
        .operatorId(device.getOperator().getId())
        .operatorName(device.getOperator().getName())
        .consumerId(device.getConsumer().getId())
        .build();
  }
}
