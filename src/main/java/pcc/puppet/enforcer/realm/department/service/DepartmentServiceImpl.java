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
package pcc.puppet.enforcer.realm.department.service;

import io.micronaut.tracing.annotation.NewSpan;
import io.micronaut.tracing.annotation.SpanTag;
import jakarta.inject.Singleton;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import pcc.puppet.enforcer.realm.department.api.command.DepartmentCreateCommand;
import pcc.puppet.enforcer.realm.department.api.event.DepartmentCreateEvent;
import pcc.puppet.enforcer.realm.department.api.presenter.DepartmentPresenter;
import pcc.puppet.enforcer.realm.department.domain.Department;
import pcc.puppet.enforcer.realm.department.mapper.DepartmentMapper;
import pcc.puppet.enforcer.realm.department.repository.DepartmentRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Singleton
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {
  private final DepartmentMapper mapper;
  private final DepartmentRepository repository;

  @NewSpan
  @Override
  public Mono<DepartmentCreateEvent> create(
      @SpanTag String requester, @SpanTag DepartmentCreateCommand createCommand) {
    Department department = mapper.commandToDomain(createCommand);
    department.setCreatedBy(requester);
    department.setCreatedAt(Instant.now());
    return repository.save(department).map(mapper::domainToEvent);
  }

  @NewSpan
  @Override
  public Mono<DepartmentPresenter> findById(
      @SpanTag String requester, @SpanTag String departmentId) {
    return repository.findById(new ObjectId(departmentId)).map(mapper::domainToPresenter);
  }

  @NewSpan
  @Override
  public Flux<DepartmentPresenter> findByParentId(
      @SpanTag String requester, @SpanTag String departmentId) {
    return repository.findByParentId(new ObjectId(departmentId)).map(mapper::domainToPresenter);
  }
}
