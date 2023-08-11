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

package pcc.puppet.enforcer.fuimos.medium;

import lombok.Data;
import pcc.puppet.enforcer.fuimos.network.domain.Network;
import pcc.puppet.enforcer.fuimos.provider.domain.ServiceOperator;

@Data
public class Device {

  private String id;
  private String address;
  private String status;
  private ServiceOperator provider;
  private Network network;
}
