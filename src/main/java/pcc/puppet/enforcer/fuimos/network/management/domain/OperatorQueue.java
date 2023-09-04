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

package pcc.puppet.enforcer.fuimos.network.management.domain;

import java.time.Instant;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import pcc.puppet.enforcer.fuimos.provider.management.domain.ServiceOperator;

@Data
@Builder
@Document
@Jacksonized
public class OperatorQueue {

  @NotNull @Id private String id;
  @NotNull private String trackId;

  @NotNull
  @Indexed(unique = true, background = true)
  private String name;

  private ServiceType type;
  private DeliveryPriority priority;
  @NotNull @DocumentReference private ServiceOperator operator;
  @NotNull @DocumentReference private Network network;
  private Instant createDate;
}
