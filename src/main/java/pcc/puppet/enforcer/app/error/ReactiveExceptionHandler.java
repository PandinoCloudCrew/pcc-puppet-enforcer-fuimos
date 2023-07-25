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

package pcc.puppet.enforcer.app.error;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import pcc.puppet.enforcer.app.error.response.ClientErrorResponse;
import pcc.puppet.enforcer.app.error.response.FieldValidationResponse;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ReactiveExceptionHandler extends AbstractErrorWebExceptionHandler {
  private final HttpStatus defaultStatus;

  public ReactiveExceptionHandler(
      ErrorAttributes errorAttributes,
      WebProperties webProperties,
      ApplicationContext applicationContext,
      ServerCodecConfigurer configurer,
      HttpStatus defaultStatus) {
    super(errorAttributes, webProperties.getResources(), applicationContext);
    this.setMessageWriters(configurer.getWriters());
    this.setMessageReaders(configurer.getReaders());
    this.defaultStatus = defaultStatus;
  }

  @Override
  protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
    return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
  }

  private Mono<ServerResponse> renderErrorResponse(ServerRequest request) {

    Throwable error = getError(request);
    log.error("An error has been occurred {}", error.getClass());
    HttpStatus httpStatus;
    if (error instanceof WebExchangeBindException ex) {
      httpStatus = HttpStatus.resolve(ex.getStatusCode().value());
      List<FieldError> fieldErrors = ex.getFieldErrors();
      FieldValidationResponse response =
          new FieldValidationResponse(httpStatus, request.path(), fieldErrors);
      return getServerResponse(httpStatus, response);
    } else if (error instanceof WebClientRequestException ex) {
      httpStatus = HttpStatus.FAILED_DEPENDENCY;
      return getServerResponse(httpStatus, ex.getMessage());
    } else if (error instanceof WebClientResponseException ex) {
      httpStatus = HttpStatus.resolve(ex.getStatusCode().value());
      return getServerResponse(httpStatus, new ClientErrorResponse(ex));
    } else if (error instanceof IllegalStateException ex) {
      httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
      return getServerResponse(error, httpStatus, ex.getMessage());
    } else if (error instanceof Exception) {
      httpStatus = defaultStatus;
      log.error("failed to handle error", error);
    } else {
      httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    return getServerResponse(error, httpStatus, error.getMessage());
  }

  private static Mono<ServerResponse> getServerResponse(
      Throwable error, HttpStatus httpStatus, String message) {
    return ServerResponse.status(httpStatus)
        .contentType(MediaType.APPLICATION_PROBLEM_JSON)
        .body(BodyInserters.fromValue(ErrorResponse.builder(error, httpStatus, message).build()));
  }

  private static Mono<ServerResponse> getServerResponse(HttpStatus httpStatus, Object response) {
    return ServerResponse.status(httpStatus)
        .contentType(MediaType.APPLICATION_PROBLEM_JSON)
        .body(BodyInserters.fromValue(response));
  }
}
