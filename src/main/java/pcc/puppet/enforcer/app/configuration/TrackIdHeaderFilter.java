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

import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import pcc.puppet.enforcer.app.tools.Data;
import reactor.core.publisher.Mono;

public class TrackIdHeaderFilter implements WebFilter {

  @NotNull
  @Override
  public Mono<Void> filter(@NotNull ServerWebExchange exchange, @NotNull WebFilterChain chain) {
    var trackId = List.of(Data.id());
    exchange.getRequest().getHeaders().putIfAbsent("track-id", trackId);
    exchange.getResponse().getHeaders().putIfAbsent("track-id", trackId);
    return chain.filter(exchange);
  }
}
