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
package pcc.puppet.enforcer.realm.organization.mapper;

import org.bson.types.ObjectId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pcc.puppet.enforcer.realm.organization.api.command.OrganizationCreateCommand;
import pcc.puppet.enforcer.realm.organization.api.event.OrganizationCreateEvent;
import pcc.puppet.enforcer.realm.organization.api.presenter.OrganizationPresenter;
import pcc.puppet.enforcer.realm.organization.domain.Organization;

@Mapper(imports = ObjectId.class)
public interface OrganizationMapper {

  @Mapping(target = "parentId", expression = "java(new ObjectId(command.getParentId()))")
  Organization commandToDomain(OrganizationCreateCommand command);

  @Mapping(target = "id", expression = "java(command.getId().toHexString())")
  @Mapping(target = "parentId", expression = "java(command.getParentId().toHexString())")
  @Mapping(source = "createdAt", target = "createdAt", dateFormat = "yyyy-MM-dd hh:mm:ss")
  OrganizationCreateEvent domainToEvent(Organization command);

  @Mapping(target = "id", expression = "java(command.getId().toHexString())")
  @Mapping(target = "parentId", expression = "java(command.getParentId().toHexString())")
  @Mapping(source = "createdAt", target = "createdAt", dateFormat = "yyyy-MM-dd hh:mm:ss")
  OrganizationPresenter domainToPresenter(Organization command);
}
