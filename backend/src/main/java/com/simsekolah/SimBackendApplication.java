package com.simsekolah;

import com.simsekolah.service.DatabaseSeederService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(exclude = {
    org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration.class,
    org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration.class
})
@EntityScan(basePackages = {"com.simsekolah.entity", "com.simsekolah.model"})
@org.springframework.context.annotation.ComponentScan(basePackages = {"com.simsekolah"})
public class SimBackendApplication {

    @Autowired
    private DatabaseSeederService databaseSeederService;

    public static void main(String[] args) {
        SpringApplication.run(SimBackendApplication.class, args);
    }

    // Database seeder runner
    @Bean
    public CommandLineRunner databaseSeederRunner() {
        return args -> {
            try {
                databaseSeederService.seedDatabase();
            } catch (Exception e) {
                org.slf4j.LoggerFactory.getLogger(SimBackendApplication.class)
                    .error("Error seeding database", e);
            }
        };
    }

    // Temporary startup runner for debugging bean registration
    @Bean
    public CommandLineRunner debugAuthBeans(ApplicationContext ctx) {
        return args -> {
            org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(SimBackendApplication.class);
            String[] beanNames = ctx.getBeanDefinitionNames();
            for (String name : beanNames) {
                try {
                    Object bean = ctx.getBean(name);
                    if (bean != null) {
                        Class<?> beanClass = bean.getClass();
                        // Check for RestController annotation or bean name containing 'auth'
                        boolean isRestController = beanClass.isAnnotationPresent(org.springframework.web.bind.annotation.RestController.class);
                        if (isRestController || name.toLowerCase().contains("auth")) {
                            logger.info("Startup bean: {} -> {} (isRestController={})", name, beanClass.getName(), isRestController);
                        }
                    }
                } catch (Exception e) {
                    logger.debug("Failed to inspect bean {}: {}", name, e.getMessage());
                }
            }

            // Additionally, try to resolve AuthController by type and log RequestMappingHandlerMapping info
            try {
                Class<?> authControllerClass = Class.forName("com.simsekolah.controller.AuthController");
                try {
                    Object authBean = ctx.getBean(authControllerClass);
                    logger.info("AuthController bean present: {} -> {}", authBean.getClass().getName(), authBean.getClass().getSuperclass().getName());
                } catch (Exception e) {
                    logger.warn("AuthController bean NOT found by type: {}", e.getMessage());
                }
            } catch (ClassNotFoundException cnfe) {
                logger.warn("AuthController class not found on classpath: {}", cnfe.getMessage());
            }

            // Log handler mappings for any mapping that includes '/api/auth' to confirm registration
            try {
                org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping mapping = ctx.getBean(org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping.class);
                mapping.getHandlerMethods().forEach((info, method) -> {
                    if (info != null && info.getPatternsCondition() != null) {
                        var patternsCondition = info.getPatternsCondition();
                        if (patternsCondition != null && patternsCondition.getPatterns() != null && !patternsCondition.getPatterns().isEmpty()) {
                            String patterns = patternsCondition.toString();
                            if (patterns.contains("/api/auth")) {
                                logger.info("Handler mapping: patterns={} -> method={}#{}", patterns, method.getBeanType().getName(), method.getMethod().getName());
                            }
                        }
                    }
                });
            } catch (Exception e) {
                logger.debug("Failed to read RequestMappingHandlerMapping: {}", e.getMessage());
            }
        };
    }
}
