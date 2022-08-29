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
package pcc.puppet.enforcer;

import io.micronaut.context.ApplicationContextBuilder;
import io.micronaut.context.ApplicationContextConfigurer;
import io.micronaut.context.annotation.ContextConfigurer;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.runtime.Micronaut;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(info = @Info(title = "pcc-puppet-enforcer-realm", version = "0.0"))
public class Application {
  /**
   * Entry point for app
   *
   * @param args program arguments
   */
  public static void main(final String[] args) {
    Micronaut.run(Application.class, args);
  }

  @ContextConfigurer
  public static class DefaultEnvironmentConfigurer implements ApplicationContextConfigurer {
    @Override
    public void configure(@NonNull final ApplicationContextBuilder builder) {
      builder.defaultEnvironments("dev");
    }
  }
}
