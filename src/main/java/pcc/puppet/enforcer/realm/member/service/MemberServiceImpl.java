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
package pcc.puppet.enforcer.realm.member.service;

import io.micronaut.tracing.annotation.NewSpan;
import io.micronaut.tracing.annotation.SpanTag;
import jakarta.inject.Singleton;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import pcc.puppet.enforcer.realm.member.api.command.MemberCreateCommand;
import pcc.puppet.enforcer.realm.member.api.event.MemberCreateEvent;
import pcc.puppet.enforcer.realm.member.api.presenter.MemberPresenter;
import pcc.puppet.enforcer.realm.member.domain.Member;
import pcc.puppet.enforcer.realm.member.mapper.MemberMapper;
import pcc.puppet.enforcer.realm.member.repository.MemberRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Singleton
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

  private final MemberRepository repository;
  private final MemberMapper mapper;

  @NewSpan
  @Override
  public Mono<MemberCreateEvent> create(
      @SpanTag String requester, MemberCreateCommand createCommand) {
    Member member = mapper.commandToDomain(createCommand);
    member.setCreatedBy(requester);
    member.setCreatedAt(Instant.now());
    return repository.save(member).map(mapper::domainToEvent);
  }

  @NewSpan
  @Override
  public Mono<MemberPresenter> findById(@SpanTag String requester, @SpanTag String memberId) {
    return repository.findById(new ObjectId(memberId)).map(mapper::domainToPresenter);
  }

  @NewSpan
  @Override
  public Flux<MemberPresenter> findByOrganizationId(
      @SpanTag String requester, @SpanTag String organizationId) {
    return repository
        .findByOrganizationId(new ObjectId(organizationId))
        .map(mapper::domainToPresenter);
  }

  @NewSpan
  @Override
  public Flux<MemberPresenter> findByDepartmentId(
      @SpanTag String requester, @SpanTag String departmentId) {
    return repository.findByDepartmentId(new ObjectId(departmentId)).map(mapper::domainToPresenter);
  }
}
