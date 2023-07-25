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

package pcc.puppet.enforcer.app.error.response;

import java.util.Objects;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.ProblemDetail;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@Data
@EqualsAndHashCode(callSuper = true)
public class ClientErrorResponse extends ProblemDetail {

  public ClientErrorResponse(WebClientResponseException error) {
    super(error.getStatusCode().value());
    if (Objects.nonNull(error.getRequest())) {
      setInstance(error.getRequest().getURI());
    }
    setTitle(error.getMessage());
    setDetail(error.getResponseBodyAsString());
  }
}
