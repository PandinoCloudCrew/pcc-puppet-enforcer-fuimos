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
package pcc.puppet.enforcer.realm.member.api.command;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.serde.annotation.Serdeable;
import lombok.Builder;
import lombok.Data;
import pcc.puppet.enforcer.realm.common.ContactInformation;

@Data
@Builder
@Serdeable
public class MemberCreateCommand {
  @NonNull private String organizationId;
  @NonNull private String departmentId;
  @NonNull private String username;
  @NonNull private String password;
  @NonNull private String location;
  @NonNull private ContactInformation contact;
}
