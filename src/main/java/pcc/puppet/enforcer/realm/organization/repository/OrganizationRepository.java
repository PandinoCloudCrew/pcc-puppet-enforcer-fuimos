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
package pcc.puppet.enforcer.realm.organization.repository;

import io.micronaut.data.mongodb.annotation.MongoRepository;
import io.micronaut.data.repository.reactive.ReactorPageableRepository;
import org.bson.types.ObjectId;
import pcc.puppet.enforcer.realm.organization.domain.Organization;
import reactor.core.publisher.Flux;

@MongoRepository
public interface OrganizationRepository extends ReactorPageableRepository<Organization, ObjectId> {
  Flux<Organization> findByParentId(ObjectId parentId);
}
