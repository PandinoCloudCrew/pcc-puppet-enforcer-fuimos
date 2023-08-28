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

package pcc.puppet.enforcer.app.configuration;

import java.util.Collection;
import java.util.Map;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@Profile("!test")
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfiguration {
  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(
            authorizeExchangeSpec ->
                authorizeExchangeSpec
                    .requestMatchers("/hello/**")
                    .hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                    .anyRequest()
                    .authenticated())
        .oauth2ResourceServer(
            oauth2Configurer ->
                oauth2Configurer.jwt(
                    jwtConfigurer ->
                        jwtConfigurer.jwtAuthenticationConverter(
                            jwt -> {
                              Map<String, Collection<String>> realmAccess =
                                  jwt.getClaim("realm_access");
                              Collection<String> roles = realmAccess.get("roles");
                              var grantedAuthorities =
                                  roles.stream()
                                      .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                                      .toList();
                              return new JwtAuthenticationToken(jwt, grantedAuthorities);
                            })));
    return http.build();
  }
}
