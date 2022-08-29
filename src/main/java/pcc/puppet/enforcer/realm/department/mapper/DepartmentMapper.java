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
package pcc.puppet.enforcer.realm.department.mapper;

import org.bson.types.ObjectId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pcc.puppet.enforcer.realm.department.api.command.DepartmentCreateCommand;
import pcc.puppet.enforcer.realm.department.api.event.DepartmentCreateEvent;
import pcc.puppet.enforcer.realm.department.api.presenter.DepartmentPresenter;
import pcc.puppet.enforcer.realm.department.domain.Department;

@Mapper(imports = ObjectId.class)
public interface DepartmentMapper {
  @Mapping(target = "parentId", expression = "java(new ObjectId(command.getParentId()))")
  @Mapping(
      target = "organizationId",
      expression = "java(new ObjectId(command.getOrganizationId()))")
  Department commandToDomain(DepartmentCreateCommand command);

  @Mapping(target = "id", expression = "java(command.getId().toHexString())")
  @Mapping(target = "parentId", expression = "java(command.getParentId().toHexString())")
  @Mapping(
      target = "organizationId",
      expression = "java(command.getOrganizationId().toHexString())")
  @Mapping(source = "createdAt", target = "createdAt", dateFormat = "yyyy-MM-dd hh:mm:ss")
  DepartmentCreateEvent domainToEvent(Department command);

  @Mapping(target = "id", expression = "java(command.getId().toHexString())")
  @Mapping(target = "parentId", expression = "java(command.getParentId().toHexString())")
  @Mapping(
      target = "organizationId",
      expression = "java(command.getOrganizationId().toHexString())")
  @Mapping(source = "createdAt", target = "createdAt", dateFormat = "yyyy-MM-dd hh:mm:ss")
  DepartmentPresenter domainToPresenter(Department command);
}
