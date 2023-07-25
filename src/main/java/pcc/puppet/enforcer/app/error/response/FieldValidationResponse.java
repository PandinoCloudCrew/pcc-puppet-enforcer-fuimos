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
import java.util.List;
import java.util.stream.Stream;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.FieldError;

@Data
@EqualsAndHashCode(callSuper = true)
public class FieldValidationResponse extends ProblemDetail {

  private Stream<String> errors;

  public FieldValidationResponse(HttpStatus httpStatus, String path, List<FieldError> errors) {
    super(httpStatus.value());
    this.setInstance(URI.create(path));
    this.errors =
        errors.stream()
            .map(
                fieldError ->
                    "Field error in object '%s' on field '%s': rejected value [%s]"
                        .formatted(
                            fieldError.getObjectName(),
                            fieldError.getField(),
                            ObjectUtils.nullSafeConciseToString(fieldError.getRejectedValue())));
  }
}
