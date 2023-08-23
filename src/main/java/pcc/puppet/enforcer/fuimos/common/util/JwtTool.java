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

package pcc.puppet.enforcer.fuimos.common.util;

import lombok.experimental.UtilityClass;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.jwt.Jwt;
import pcc.puppet.enforcer.keycloak.domain.BearerTokenResponse;
import reactor.core.publisher.Mono;

@UtilityClass
public class JwtTool {

  private static final String DEFAULT_VALUE =
      "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaW"
          + "F0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";

  public String toBearer(Jwt authentication) {
    return "Bearer " + authentication.getTokenValue();
  }

  public String toBearer(BearerTokenResponse authentication) {
    return "Bearer " + authentication.getAccessToken();
  }

  public static Mono<Jwt> authentication() {
    return ReactiveSecurityContextHolder.getContext()
        .map(SecurityContext::getAuthentication)
        .filter(Authentication::isAuthenticated)
        .map(Authentication::getPrincipal)
        .map(Jwt.class::cast)
        .defaultIfEmpty(getJwt());
  }

  private static Jwt getJwt() {
    return Jwt.withTokenValue(DEFAULT_VALUE)
        .header("typ", "JWT")
        .header("alg", "HS256")
        .claim("preferred_username", "default-not-valid")
        .build();
  }
}
