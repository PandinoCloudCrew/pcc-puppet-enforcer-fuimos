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
package pcc.puppet.enforcer.fuimos.provider;

import io.micronaut.core.annotation.Introspected;
import java.time.Instant;
import lombok.Builder;
import lombok.Data;
import pcc.puppet.enforcer.fuimos.network.channel.ChannelType;

@Data
@Builder
@Introspected
public class Carrier {

  private String id;
  private String name;
  private ChannelType channel;
  private String createdBy;
  private Instant createdAt;
  private String modifiedBy;
  private Instant modifiedAt;
}
