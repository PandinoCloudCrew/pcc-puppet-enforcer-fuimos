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

import java.net.URI;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import pcc.puppet.enforcer.fuimos.common.error.RecordNotFound;

@Data
@EqualsAndHashCode(callSuper = true)
public class NotFoundResponse extends ProblemDetail {
  private static final HttpStatus status = HttpStatus.NOT_FOUND;
  private final RecordNotFound error;

  public NotFoundResponse(RecordNotFound error) {
    super(status.value());
    this.error = error;
    setInstance(
        URI.create(
            "https://error.pcc.fyi/%s/%s?trackId=%s"
                .formatted(error.getEntity(), error.getRecordId(), error.getTrackId())));
    setTitle("%s - %s".formatted(error.getEntity(), status.getReasonPhrase()));
    setDetail(error.getMessage());
  }
}
