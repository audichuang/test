package com.example.mapstructdemo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * OpenAPI (Swagger) 配置類
 * 設置API文檔的基本信息和服務器
 */
@Configuration
public class OpenApiConfig {

        @Bean
        public OpenAPI myOpenAPI() {
                Server devServer = new Server();
                devServer.setUrl("http://localhost:8082");
                devServer.setDescription("開發環境 API 伺服器");

                Contact contact = new Contact();
                contact.setEmail("info@example.com");
                contact.setName("MapStruct 示例");
                contact.setUrl("https://www.example.com");

                License mitLicense = new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT");

                Info info = new Info()
                                .title("MapStruct 示例 API")
                                .version("1.0")
                                .contact(contact)
                                .description("這個API展示了使用MapStruct進行不同類型映射的示例，包含實體-DTO轉換和嵌套Map結構處理。")
                                .license(mitLicense);

                return new OpenAPI()
                                .info(info)
                                .servers(List.of(devServer));
        }
}