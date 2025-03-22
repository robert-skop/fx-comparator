package com.robertskop.fxcomparator.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig {

    @Bean
    fun customOpenAPI() = OpenAPI()
        .info(
            Info()
                .title("Fx comparator")
                .version("1.0.1")
                .description(
                    """
        This documentation is for [GitHub repository](https://github.com/robert-skop/fx-comparator) and describes REST API 
        of FX Comparator between FX providers:
        
        * [CNB](https://www.cnb.cz/cs/financni_trhy/devizovy_trh/kurzy_devizoveho_trhu/denni_kurz.txt)
        * [Frankfurter](https://frankfurter.dev/)
        
        All endpoints are secured with Basic Auth and you can configure credentials at application startup.
    """.trimIndent()
                )
        )

}
