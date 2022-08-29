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
package pcc.puppet.enforcer.realm.member.api;

import static pcc.puppet.enforcer.realm.configuration.HttpHeaders.DEPARTMENT;
import static pcc.puppet.enforcer.realm.configuration.HttpHeaders.ORGANIZATION;
import static pcc.puppet.enforcer.realm.configuration.HttpHeaders.REQUESTER;

import com.agorapulse.permissions.RequiresPermission;
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
import pcc.puppet.enforcer.realm.member.api.command.MemberCreateCommand;
import pcc.puppet.enforcer.realm.member.api.event.MemberCreateEvent;
import pcc.puppet.enforcer.realm.member.api.presenter.MemberPresenter;
import pcc.puppet.enforcer.realm.member.service.MemberService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Controller("/realm/member")
@Secured(SecurityRule.IS_ANONYMOUS)
@RequiredArgsConstructor
public class MemberController {
  private final MemberService memberService;

  @NewSpan
  @Post(consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
  @RequiresPermission("member-controller::member-create")
  public Mono<MemberCreateEvent> memberCreate(
      @SpanTag(REQUESTER) @NonNull @Header(REQUESTER) String requester,
      @SpanTag(ORGANIZATION) @NonNull @Header(ORGANIZATION) String organizationId,
      @SpanTag(DEPARTMENT) @NonNull @Header(DEPARTMENT) String departmentId,
      @NonNull @Valid @Body MemberCreateCommand createCommand) {
    return memberService.create(requester, createCommand);
  }

  @NewSpan
  @Get(produces = MediaType.APPLICATION_JSON)
  @RequiresPermission("member-controller::find-member")
  public Mono<MemberPresenter> findMember(
      @SpanTag(REQUESTER) @NonNull @Header(REQUESTER) String requester,
      @SpanTag(ORGANIZATION) @NonNull @Header(ORGANIZATION) String organizationId,
      @SpanTag(DEPARTMENT) @NonNull @Header(DEPARTMENT) String departmentId,
      @SpanTag @NonNull @QueryValue("memberId") String memberId) {
    return memberService.findById(requester, memberId);
  }

  @NewSpan
  @Get(uri = "child/organization", produces = MediaType.APPLICATION_JSON)
  @RequiresPermission("member-controller::find-child-organization-members")
  public Flux<MemberPresenter> findChildOrganizationMembers(
      @SpanTag(REQUESTER) @NonNull @Header(REQUESTER) String requester,
      @SpanTag(ORGANIZATION) @NonNull @Header(ORGANIZATION) String organizationId,
      @SpanTag(DEPARTMENT) @NonNull @Header(DEPARTMENT) String departmentId,
      @SpanTag @NonNull @QueryValue("id") String id) {
    return memberService.findByOrganizationId(requester, id);
  }

  @NewSpan
  @Get(uri = "child/department", produces = MediaType.APPLICATION_JSON)
  @RequiresPermission("member-controller::find-child-department-members")
  public Flux<MemberPresenter> findChildDepartmentMembers(
      @SpanTag(REQUESTER) @NonNull @Header(REQUESTER) String requester,
      @SpanTag(ORGANIZATION) @NonNull @Header(ORGANIZATION) String organizationId,
      @SpanTag(DEPARTMENT) @NonNull @Header(DEPARTMENT) String departmentId,
      @SpanTag @NonNull @QueryValue("id") String id) {
    return memberService.findByDepartmentId(requester, id);
  }
}
