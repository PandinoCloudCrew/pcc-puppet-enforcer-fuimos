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
package pcc.puppet.enforcer.realm.department.api;

import static pcc.puppet.enforcer.realm.configuration.HttpHeaders.ORGANIZATION;
import static pcc.puppet.enforcer.realm.configuration.HttpHeaders.REQUESTER;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Header;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.tracing.annotation.NewSpan;
import io.micronaut.tracing.annotation.SpanTag;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pcc.puppet.enforcer.realm.department.api.command.DepartmentCreateCommand;
import pcc.puppet.enforcer.realm.department.api.event.DepartmentCreateEvent;
import pcc.puppet.enforcer.realm.department.api.presenter.DepartmentPresenter;
import pcc.puppet.enforcer.realm.department.service.DepartmentService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Controller("/realm/department")
@Secured(SecurityRule.IS_ANONYMOUS)
@RequiredArgsConstructor
public class DepartmentController {
  private final DepartmentService departmentService;

  @NewSpan
  @Post(consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
  public Mono<DepartmentCreateEvent> departmentCreate(
      @SpanTag(REQUESTER) @NonNull @Header(REQUESTER) String requester,
      @SpanTag(ORGANIZATION) @NonNull @Header(ORGANIZATION) String organizationId,
      @NonNull @Valid @Body DepartmentCreateCommand createCommand) {
    return departmentService.create(requester, createCommand);
  }

  @NewSpan
  @Get(produces = MediaType.APPLICATION_JSON)
  public Mono<DepartmentPresenter> findDepartment(
      @SpanTag(REQUESTER) @NonNull @Header(REQUESTER) String requester,
      @SpanTag(ORGANIZATION) @NonNull @Header(ORGANIZATION) String organizationId,
      @SpanTag @NonNull @QueryValue("departmentId") String departmentId) {
    return departmentService.findById(requester, departmentId);
  }

  @NewSpan
  @Get(uri = "child", produces = MediaType.APPLICATION_JSON)
  public Flux<DepartmentPresenter> findChildDepartments(
      @SpanTag(REQUESTER) @NonNull @Header(REQUESTER) String requester,
      @SpanTag(ORGANIZATION) @NonNull @Header(ORGANIZATION) String organizationId,
      @SpanTag @NonNull @QueryValue("departmentId") String departmentId) {
    return departmentService.findByParentId(requester, departmentId);
  }
}
