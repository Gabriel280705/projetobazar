package br.com.fatec.bazar.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "API do Bazar",
                version = "1.0",
                description = "API REST desenvolvida com Spring Boot para controle de estoque e vendas de um bazar. " +
                        "Resolve o problema de vender itens que já acabaram e de perder o controle do que sobrou: " +
                        "permite gerenciar categorias e produtos e registrar vendas, com baixa automática " +
                        "do estoque, cálculo automático do valor total e bloqueio de venda sem estoque suficiente. " +
                        "Ordem de uso: 1) cadastrar categoria, 2) cadastrar produto, 3) registrar venda.",
                contact = @Contact(
                        name = "Gabriel Freitas, Luan Massuia e Tâmily Porto - Fatec Jales",
                        email = "devfatecjales@cps.sp.gov.br"
                )
        )
)
public class SwaggerConfig {

}
