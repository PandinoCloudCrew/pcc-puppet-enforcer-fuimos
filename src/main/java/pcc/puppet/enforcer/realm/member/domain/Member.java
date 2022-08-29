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
package pcc.puppet.enforcer.realm.member.domain;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.data.annotation.AutoPopulated;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.data.annotation.Version;
import io.micronaut.serde.annotation.Serdeable;
import java.time.Instant;
import lombok.Builder;
import lombok.Data;
import org.bson.types.ObjectId;
import pcc.puppet.enforcer.realm.common.ContactInformation;

@Data
@Builder
@Serdeable
@MappedEntity(value = "member")
public class Member {
  @Id private ObjectId id;
  @NonNull private ObjectId organizationId;
  @NonNull private ObjectId departmentId;
  @NonNull private String username;
  @NonNull private String password;
  @NonNull private String location;
  @NonNull private ContactInformation contact;
  @NonNull private String createdBy;
  @NonNull private Instant createdAt;
  @Nullable private String updatedBy;
  @Nullable private Instant updatedAt;
  @Version @AutoPopulated private Integer version;
}
