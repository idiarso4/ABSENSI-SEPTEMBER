

b.servlet.DispatcherServlet        : Initializing Servlet 'dispatcherServlet'
2025-09-08T09:49:25.493+07:00  INFO 2712 --- [SIM Sekolah Management System] [http-nio-8080-exec-1] o.s.web.servlet.DispatcherServlet        : Completed initialization in 2 ms
2025-09-08T09:49:25.511+07:00 DEBUG 2712 --- [SIM Sekolah Management System] [http-nio-8080-exec-1] o.s.security.web.FilterChainProxy        : Securing POST /api/auth/login
2025-09-08T09:49:25.526+07:00 DEBUG 2712 --- [SIM Sekolah Management System] [http-nio-8080-exec-1] o.s.s.w.a.AnonymousAuthenticationFilter  : Set SecurityContextHolder to anonymous SecurityContext
2025-09-08T09:49:25.529+07:00  WARN 2712 --- [SIM Sekolah Management System] [http-nio-8080-exec-1] o.s.w.s.h.HandlerMappingIntrospector     : Cache miss for REQUEST dispatch to '/api/auth/login' (previous null). Performing MatchableHandlerMapping lookup. This is logged once only at WARN level, and every time at TRACE.
2025-09-08T09:49:25.543+07:00 DEBUG 2712 --- [SIM Sekolah Management System] [http-nio-8080-exec-1] o.s.security.web.FilterChainProxy        : Secured POST /api/auth/login
2025-09-08T09:49:25.559+07:00 DEBUG 2712 --- [SIM Sekolah Management System] [http-nio-8080-exec-1] o.s.security.web.FilterChainProxy        : Securing POST /error
2025-09-08T09:49:25.563+07:00 DEBUG 2712 --- [SIM Sekolah Management System] [http-nio-8080-exec-1] o.s.security.web.FilterChainProxy        : Secured POST /error
2025-09-08T09:49:25.627+07:00 DEBUG 2712 --- [SIM Sekolah Management System] [http-nio-8080-exec-1] o.s.s.w.a.AnonymousAuthenticationFilter  : Set SecurityContextHolder to anonymous SecurityContext
2025-09-08T09:52:53.753+07:00 DEBUG 2712 --- [SIM Sekolah Management System] [http-nio-8080-exec-3] o.s.security.web.FilterChainProxy        : Securing POST /api/auth/login
2025-09-08T09:52:53.891+07:00 DEBUG 2712 --- [SIM Sekolah Management System] [http-nio-8080-exec-3] o.s.s.w.a.AnonymousAuthenticationFilter  : Set SecurityContextHolder to anonymous SecurityContext
2025-09-08T09:52:54.017+07:00 DEBUG 2712 --- [SIM Sekolah Management System] [http-nio-8080-exec-3] o.s.security.web.FilterChainProxy        : Secured POST /api/auth/login
2025-09-08T09:52:54.295+07:00 DEBUG 2712 --- [SIM Sekolah Management System] [http-nio-8080-exec-3] o.s.security.web.FilterChainProxy        : Securing POST /error
2025-09-08T09:52:54.330+07:00 DEBUG 2712 --- [SIM Sekolah Management System] [http-nio-8080-exec-3] o.s.security.web.FilterChainProxy        : Secured POST /error
2025-09-08T09:52:54.545+07:00 DEBUG 2712 --- [SIM Sekolah Management System] [http-nio-8080-exec-3] o.s.s.w.a.AnonymousAuthenticationFilter  : Set SecurityContextHolder to anonymous SecurityContext
2025-09-08T09:56:06.365+07:00 DEBUG 2712 --- [SIM Sekolah Management System] [http-nio-8080-exec-2] o.s.security.web.FilterChainProxy        : Securing POST /api/auth/login
2025-09-08T09:56:06.369+07:00 DEBUG 2712 --- [SIM Sekolah Management System] [http-nio-8080-exec-2] o.s.s.w.a.AnonymousAuthenticationFilter  : Set SecurityCont2025-09-08T09:56:06.376+07:00 DEBUG 2712 --- [SIM Sekolah Management System] [http-nio-8080-exec-2] o.s.security.web.FilterChainProxy        : Secured POST /api/auth/login
2025-09-08T09:56:06.385+07:00 DEBUG 2712 --- [SIM Sekolah Management System] [http-nio-8080-exec-2] o.s.serror
2025-09-08T09:56:06.406+07:00 DEBUG 2712 --- [SIM Sekolah Management System] [http-nio-8080-exec-2] o.s.security.web.FilterChainProxy        : Secured POST /error
2025-09-08T09:56:06.412+07:00 DEBUG 2712 --- [SIM Sekolah Management System] [http-nio-8080-exec-2] o.s.s.w.a.AnonymousAuthenticationFilter  : Set SecurityContextHolder to anonymous SecurityContext
PS C:\Users\sija_003\Desktop\SIM_CLONE> powershell -NoProfile -Command "& 'C:\Windows\System32\curl.exe' -v -X POST 'http://localhost:8080/api/auth/login' -H 'Content-Type: application/json' -d '{\"identifier\":\"admin@simsekolah.com\",\"password\":\"admin123\"}'; 
Write-Host '--- SERVER LOG (last 120 lines) ---'; Get-Content -Path 'c:\Users\sija_003\Desktop\SIM_CLONE\backend\logs\dev-app.log' -Tail 120"
3\x5c"}'\x3b Write-Host '--- SERVER LOG (last 120 lines) ---'\x3b Get-Content -Path 'c:\x5cUsers\x5csija_003\x5cDesktop\x5cSIM_CLONE\x5cbackend\x5clogs\x5cdev-app.log' -Tail 120";7bd2fa6d-e085-4258-88c3-b8dfd7aa30f0Note: Unnecessary use of -X or --request, POST is 
Note: already inferred.
* Host localhost:8080 was resolved.
* IPv6: ::1
* IPv4: 127.0.0.1
*   Trying [::1]:8080...
* Connected to localhost (::1) port 8080
* using HTTP/1.x
> POST /api/auth/login HTTP/1.1
> Host: localhost:8080
> User-Agent: curl/8.13.0
> Accept: */*
> Content-Type: application/json
> Content-Length: 59
>
* upload completely sent off: 59 bytes
< HTTP/1.1 404
< Vary: Origin
< Vary: Access-Control-Request-Method
< Vary: Access-Control-Request-Headers
< X-Content-Type-Options: nosniff
< X-XSS-Protection: 0
< Cache-Control: no-cache, no-store, max-age=0, must-revalidate
< Pragma: no-cache
< Expires: 0
< Content-Type: application/json
< Transfer-Encoding: chunked
< Date: Mon, 08 Sep 2025 02:59:14 GMT
<
{"timestamp":"2025-09-08T02:59:14.983+00:00","status":404,"error":"Not Found","path":"/api/auth/login"}* Connection #0 to host localhost left intact
--- SERVER LOG (last 120 lines) ---
2025-09-08T09:26:58.858+07:00  INFO 10932 --- [SIM Sekolah Management System] [main] com.simsekolah.config.RedisCacheConfig   : Redis cache manager configured 
with 25 specific cache configurations
2025-09-08T09:27:00.555+07:00  WARN 10932 --- [SIM Sekolah Management System] [main] JpaBaseConfiguration$JpaWebConfiguration : spring.jpa.open-in-view is enabled by default. Therefore, database queries may be performed during view rendering. Explicitly configure spring.jpa.open-in-view to disable this warning       
2025-09-08T09:27:00.647+07:00 DEBUG 10932 --- [SIM Sekolah Management System] [main] o.s.s.a.h.RoleHierarchyImpl              : setHierarchy() - The following 
role hierarchy was set: ROLE_ADMIN > ROLE_TEACHER    
ROLE_TEACHER > ROLE_STUDENT
ROLE_STUDENT > ROLE_USER
2025-09-08T09:27:00.648+07:00 DEBUG 10932 --- [SIM Sekolah Management System] [main] o.s.s.a.h.RoleHierarchyImpl              : buildRolesReachableInOneStepMap() - From role ROLE_ADMIN one can reach role ROLE_TEACHER in one step.
2025-09-08T09:27:00.648+07:00 DEBUG 10932 --- [SIM Sekolah Management System] [main] o.s.s.a.h.RoleHierarchyImpl              : buildRolesReachableInOneStepMap() - From role ROLE_TEACHER one can reach role ROLE_STUDENT in one step.
2025-09-08T09:27:00.649+07:00 DEBUG 10932 --- [SIM Sekolah Management System] [main] o.s.s.a.h.RoleHierarchyImpl              : buildRolesReachableInOneStepMap() - From role ROLE_STUDENT one can reach role ROLE_USER in one step.
2025-09-08T09:27:00.650+07:00 DEBUG 10932 --- [SIM Sekolah Management System] [main] o.s.s.a.h.RoleHierarchyImpl              : buildRolesReachableInOneOrMoreStepsMap() - From role ROLE_STUDENT one can reach [ROLE_USER] in one or more steps.
2025-09-08T09:27:00.650+07:00 DEBUG 10932 --- [SIM Sekolah Management System] [main] o.s.s.a.h.RoleHierarchyImpl              : buildRolesReachableInOneOrMoreStepsMap() - From role ROLE_ADMIN one can reach [ROLE_STUDENT, ROLE_USER, ROLE_TEACHER] in one or more steps.
2025-09-08T09:27:00.651+07:00 DEBUG 10932 --- [SIM Sekolah Management System] [main] o.s.s.a.h.RoleHierarchyImpl              : buildRolesReachableInOneOrMoreStepsMap() - From role ROLE_TEACHER one can reach [ROLE_STUDENT, ROLE_USER] in one or more steps.
2025-09-08T09:27:01.100+07:00  INFO 10932 --- [SIM Sekolah Management System] [main] o.s.b.a.e.web.EndpointLinksResolver      : Exposing 1 endpoint(s) beneath 
base path '/actuator'
2025-09-08T09:27:01.170+07:00  INFO 10932 --- [SIM Sekolah Management System] [main] o.s.s.web.DefaultSecurityFilterChain     : Will secure any request with [org.springframework.security.web.session.DisableEncodeUrlFilter@1e64c9df, org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter@455113df, org.springframework.security.web.context.SecurityContextHolderFilter@2570f2fc, org.springframework.security.web.header.HeaderWriterFilter@6c87c418, org.springframework.web.filter.CorsFilter@3e8fed2, org.springframework.security.web.authentication.logout.LogoutFilter@57033983, com.simsekolah.security.JwtAuthenticationFilter@1e85cf67, org.springframework.security.web.savedrequest.RequestCacheAwareFilter@58d79a3, org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter@55faf965, org.springframework.security.web.authentication.AnonymousAuthenticationFilter@6b006c2a, org.springframework.security.web.session.SessionManagementFilter@6ebcf794, org.springframework.security.web.access.ExceptionTranslationFilter@70a3cf9d, org.springframework.security.web.access.intercept.AuthorizationFilter@43345549]
2025-09-08T09:27:02.193+07:00  INFO 10932 --- [SIM Sekolah Management System] [main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port 8080 (http) with context path ''
2025-09-08T09:27:02.204+07:00  INFO 10932 --- [SIM Sekolah Management System] [main] com.simsekolah.SimBackendApplication     : Started SimBackendApplication in 13.845 seconds (process running for 14.529)        
2025-09-08T09:27:02.281+07:00  INFO 10932 --- [SIM Sekolah Management System] [main] com.simsekolah.config.DataInitializer    : Starting data initialization...2025-09-08T09:27:02.282+07:00  INFO 10932 --- [SIM Sekolah Management System] [main] com.simsekolah.config.DataInitializer    : Checking system users...       
2025-09-08T09:27:02.367+07:00  INFO 10932 --- [SIM Sekolah Management System] [main] com.simsekolah.config.DataInitializer    : No users found. Creating default admin user...
2025-09-08T09:27:02.592+07:00  INFO 10932 --- [SIM Sekolah Management System] [main] com.simsekolah.config.DataInitializer    : Created default admin user: admin@simsekolah.com/admin123
2025-09-08T09:27:02.593+07:00  INFO 10932 --- [SIM Sekolah Management System] [main] com.simsekolah.config.DataInitializer    : Checking database status...    
2025-09-08T09:27:02.596+07:00  INFO 10932 --- [SIM Sekolah Management System] [main] com.simsekolah.config.DataInitializer    : Database status:
2025-09-08T09:27:02.596+07:00  INFO 10932 --- [SIM Sekolah Management System] [main] com.simsekolah.config.DataInitializer    : - Students: 0
2025-09-08T09:27:02.596+07:00  INFO 10932 --- [SIM Sekolah Management System] [main] com.simsekolah.config.DataInitializer    : No students found. Students can be added through the application interface.
2025-09-08T09:27:02.596+07:00  INFO 10932 --- [SIM Sekolah Management System] [main] com.simsekolah.config.DataInitializer    : Data initialization completed successfully
2025-09-08T09:27:52.757+07:00  INFO 10932 --- [SIM Sekolah Management System] [http-nio-8080-exec-1] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring DispatcherServlet 'dispatcherServlet'
2025-09-08T09:27:52.758+07:00  INFO 10932 --- [SIM Sekolah Management System] [http-nio-8080-exec-1] o.s.web.servlet.DispatcherServlet        : Initializing Servlet 'dispatcherServlet'
2025-09-08T09:27:52.760+07:00  INFO 10932 --- [SIM Sekolah Management System] [http-nio-8080-exec-1] o.s.web.servlet.DispatcherServlet        : Completed initialization in 2 ms
2025-09-08T09:27:52.778+07:00 DEBUG 10932 --- [SIM Sekolah Management System] [http-nio-8080-exec-1] o.s.security.web.FilterChainProxy        : Securing POST /api/auth/login
2025-09-08T09:27:52.794+07:00 DEBUG 10932 --- [SIM Sekolah Management System] [http-nio-8080-exec-1] o.s.s.w.a.AnonymousAuthenticationFilter  : Set SecurityContextHolder to anonymous SecurityContext
2025-09-08T09:27:52.798+07:00  WARN 10932 --- [SIM Sekolah Management System] [http-nio-8080-exec-1] o.s.w.s.h.HandlerMappingIntrospector     : Cache miss for 
REQUEST dispatch to '/api/auth/login' (previous null). Performing MatchableHandlerMapping lookup. This is 
logged once only at WARN level, and every time at TRACE.
2025-09-08T09:27:52.817+07:00 DEBUG 10932 --- [SIM Sekolah Management System] [http-nio-8080-exec-1] o.s.security.web.FilterChainProxy        : Secured POST /api/auth/login
2025-09-08T09:27:52.832+07:00 DEBUG 10932 --- [SIM Sekolah Management System] [http-nio-8080-exec-1] o.s.security.web.FilterChainProxy        : Securing POST /error
2025-09-08T09:27:52.838+07:00 DEBUG 10932 --- [SIM Sekolah Management System] [http-nio-8080-exec-1] o.s.security.web.FilterChainProxy        : Secured POST /error
2025-09-08T09:27:52.908+07:00 DEBUG 10932 --- [SIM Sekolah Management System] [http-nio-8080-exec-1] o.s.s.w.a.AnonymousAuthenticationFilter  : Set SecurityContextHolder to anonymous SecurityContext
2025-09-08T09:28:07.638+07:00 DEBUG 10932 --- [SIM Sekolah Management System] [http-nio-8080-exec-3] o.s.security.web.FilterChainProxy        : Securing POST /api/auth/login
2025-09-08T09:28:07.640+07:00 DEBUG 10932 --- [SIM Sekolah Management System] [http-nio-8080-exec-3] o.s.s.w.a.AnonymousAuthenticationFilter  : Set SecurityContextHolder to anonymous SecurityContext
2025-09-08T09:28:07.645+07:00 DEBUG 10932 --- [SIM Sekolah Management System] [http-nio-8080-exec-3] o.s.security.web.FilterChainProxy        : Secured POST /api/auth/login
2025-09-08T09:28:07.651+07:00 DEBUG 10932 --- [SIM Sekolah Management System] [http-nio-8080-exec-3] o.s.security.web.FilterChainProxy        : Securing POST /error
2025-09-08T09:28:07.655+07:00 DEBUG 10932 --- [SIM Sekolah Management System] [http-nio-8080-exec-3] o.s.security.web.FilterChainProxy        : Secured POST /error
2025-09-08T09:28:07.659+07:00 DEBUG 10932 --- [SIM Sekolah Management System] [http-nio-8080-exec-3] o.s.s.w.a.AnonymousAuthenticationFilter  : Set SecurityContextHolder to anonymous SecurityContext
2025-09-08T09:49:04.021+07:00  INFO 2712 --- [SIM Sekolah Management System] [main] com.simsekolah.SimBackendApplication     : Starting SimBackendApplication using Java 21.0.7 with PID 2712 (C:\Users\sija_003\Desktop\SIM_CLONE\backend\target\classes started by sija_003 in C:\Users\sija_003\Desktop\SIM_CLONE\backend) 
2025-09-08T09:49:04.024+07:00  INFO 2712 --- [SIM Sekolah Management System] [main] com.simsekolah.SimBackendApplication     : The following 1 profile is active: "dev"
2025-09-08T09:49:05.501+07:00  INFO 2712 --- [SIM Sekolah Management System] [main] .s.d.r.c.RepositoryConfigurationDelegate : Multiple Spring Data modules found, entering strict repository configuration mode    
2025-09-08T09:49:05.504+07:00  INFO 2712 --- [SIM Sekolah Management System] [main] .s.d.r.c.RepositoryConfigurationDelegate : Bootstrapping Spring Data JPA repositories in DEFAULT mode.
2025-09-08T09:49:05.801+07:00  INFO 2712 --- [SIM Sekolah Management System] [main] .s.d.r.c.RepositoryConfigurationDelegate : Finished Spring Data repository 
scanning in 286 ms. Found 17 JPA repository interfaces.
2025-09-08T09:49:06.806+07:00  INFO 2712 --- [SIM Sekolah Management System] [main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat initialized with port 8080 (http)
2025-09-08T09:49:06.837+07:00  INFO 2712 --- [SIM Sekolah Management System] [main] o.apache.catalina.core.StandardService   : Starting service [Tomcat]       
2025-09-08T09:49:06.838+07:00  INFO 2712 --- [SIM Sekolah Management System] [main] o.apache.catalina.core.StandardEngine    : Starting Servlet engine: [Apache Tomcat/10.1.16]
2025-09-08T09:49:06.939+07:00  INFO 2712 --- [SIM Sekolah Management System] [main] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring embedded WebApplicationContext
2025-09-08T09:49:06.940+07:00  INFO 2712 --- [SIM Sekolah Management System] [main] w.s.c.ServletWebServerApplicationContext : Root WebApplicationContext: initialization completed in 2861 ms
2025-09-08T09:49:07.093+07:00  INFO 2712 --- [SIM Sekolah Management System] [main] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Starting...      
2025-09-08T09:49:07.334+07:00  INFO 2712 --- [SIM Sekolah Management System] [main] com.zaxxer.hikari.pool.HikariPool        : HikariPool-1 - Added connection 
conn0: url=jdbc:h2:mem:simsekolahdb user=SA
2025-09-08T09:49:07.337+07:00  INFO 2712 --- [SIM Sekolah Management System] [main] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Start completed. 
2025-09-08T09:49:07.347+07:00  INFO 2712 --- [SIM Sekolah Management System] [main] o.s.b.a.h2.H2ConsoleAutoConfiguration    : H2 console available at '/h2-console'. Database available at 'jdbc:h2:mem:simsekolahdb'
2025-09-08T09:49:07.559+07:00  INFO 2712 --- [SIM Sekolah Management System] [main] o.hibernate.jpa.internal.util.LogHelper  : HHH000204: Processing PersistenceUnitInfo [name: default]
2025-09-08T09:49:07.643+07:00  INFO 2712 --- [SIM Sekolah Management System] [main] org.hibernate.Version 
                   : HHH000412: Hibernate ORM core version 6.3.1.Final
2025-09-08T09:49:07.690+07:00  INFO 2712 --- [SIM Sekolah Management System] [main] o.h.c.internal.RegionFactoryInitiator    : HHH000026: Second-level cache disabled
2025-09-08T09:49:07.967+07:00  INFO 2712 --- [SIM Sekolah Management System] [main] o.s.o.j.p.SpringPersistenceUnitInfo      : No LoadTimeWeaver setup: ignoring JPA class transformer
2025-09-08T09:49:08.028+07:00  WARN 2712 --- [SIM Sekolah Management System] [main] org.hibernate.orm.deprecation            : HHH90000025: H2Dialect does not 
need to be specified explicitly using 'hibernate.dialect' (remove the property setting and it will be selected by default)
2025-09-08T09:49:09.581+07:00  INFO 2712 --- [SIM Sekolah Management System] [main] o.h.e.t.j.p.i.JtaPlatformInitiator       : HHH000489: No JTA platform available (set 'hibernate.transaction.jta.platform' to enable JTA platform integration)
2025-09-08T09:49:09.828+07:00  INFO 2712 --- [SIM Sekolah Management System] [main] j.LocalContainerEntityManagerFactoryBean : Initialized JPA EntityManagerFactory for persistence unit 'default'
2025-09-08T09:49:10.279+07:00  INFO 2712 --- [SIM Sekolah Management System] [main] o.s.d.j.r.query.QueryEnhancerFactory     : Hibernate is in classpath; If applicable, HQL parser will be used.
2025-09-08T09:49:11.660+07:00  INFO 2712 --- [SIM Sekolah Management System] [main] com.simsekolah.config.RedisCacheConfig   : Configuring Redis connection to 
localhost:6379
2025-09-08T09:49:11.708+07:00  INFO 2712 --- [SIM Sekolah Management System] [main] com.simsekolah.config.RedisCacheConfig   : Redis connection factory configured successfully
2025-09-08T09:49:12.622+07:00  INFO 2712 --- [SIM Sekolah Management System] [main] com.simsekolah.config.RedisCacheConfig   : Configuring Redis template      
2025-09-08T09:49:12.632+07:00  INFO 2712 --- [SIM Sekolah Management System] [main] com.simsekolah.config.RedisCacheConfig   : Redis template configured successfully
2025-09-08T09:49:12.679+07:00 TRACE 2712 --- [SIM Sekolah Management System] [main] eGlobalAuthenticationAutowiredConfigurer : Eagerly initializing {securityConfig=com.simsekolah.config.SecurityConfig$$SpringCGLIB$$0@3a75a52a}
2025-09-08T09:49:13.070+07:00  INFO 2712 --- [SIM Sekolah Management System] [main] com.simsekolah.config.RedisCacheConfig   : Configuring Redis cache manager 
2025-09-08T09:49:13.131+07:00  INFO 2712 --- [SIM Sekolah Management System] [main] com.simsekolah.config.RedisCacheConfig   : Redis cache manager configured with 25 specific cache configurations
2025-09-08T09:49:14.976+07:00  WARN 2712 --- [SIM Sekolah Management System] [main] JpaBaseConfiguration$JpaWebConfiguration : spring.jpa.open-in-view is enabled by default. Therefore, database queries may be performed during view rendering. Explicitly configure spring.jpa.open-in-view to disable this warning        
2025-09-08T09:49:15.146+07:00 DEBUG 2712 --- [SIM Sekolah Management System] [main] o.s.s.a.h.RoleHierarchyImpl              : setHierarchy() - The following role hierarchy was set: ROLE_ADMIN > ROLE_TEACHER     
ROLE_TEACHER > ROLE_STUDENT
ROLE_STUDENT > ROLE_USER
2025-09-08T09:49:15.147+07:00 DEBUG 2712 --- [SIM Sekolah Management System] [main] o.s.s.a.h.RoleHierarchyImpl              : buildRolesReachableInOneStepMap() - From role ROLE_ADMIN one can reach role ROLE_TEACHER in one step.
2025-09-08T09:49:15.149+07:00 DEBUG 2712 --- [SIM Sekolah Management System] [main] o.s.s.a.h.RoleHierarchyImpl              : buildRolesReachableInOneStepMap() - From role ROLE_TEACHER one can reach role ROLE_STUDENT in one step.
2025-09-08T09:49:15.150+07:00 DEBUG 2712 --- [SIM Sekolah Management System] [main] o.s.s.a.h.RoleHierarchyImpl              : buildRolesReachableInOneStepMap() - From role ROLE_STUDENT one can reach role ROLE_USER in one step.
2025-09-08T09:49:15.151+07:00 DEBUG 2712 --- [SIM Sekolah Management System] [main] o.s.s.a.h.RoleHierarchyImpl              : buildRolesReachableInOneOrMoreStepsMap() - From role ROLE_STUDENT one can reach [ROLE_USER] in one or more steps.
2025-09-08T09:49:15.151+07:00 DEBUG 2712 --- [SIM Sekolah Management System] [main] o.s.s.a.h.RoleHierarchyImpl              : buildRolesReachableInOneOrMoreStepsMap() - From role ROLE_ADMIN one can reach [ROLE_STUDENT, ROLE_USER, ROLE_TEACHER] in one or more steps.
2025-09-08T09:49:15.153+07:00 DEBUG 2712 --- [SIM Sekolah Management System] [main] o.s.s.a.h.RoleHierarchyImpl              : buildRolesReachableInOneOrMoreStepsMap() - From role ROLE_TEACHER one can reach [ROLE_STUDENT, ROLE_USER] in one or more steps.
2025-09-08T09:49:15.631+07:00  INFO 2712 --- [SIM Sekolah Management System] [main] o.s.b.a.e.web.EndpointLinksResolver      : Exposing 1 endpoint(s) beneath base path '/actuator'
2025-09-08T09:49:15.701+07:00  INFO 2712 --- [SIM Sekolah Management System] [main] o.s.s.web.DefaultSecurityFilterChain     : Will secure any request with [org.springframework.security.web.session.DisableEncodeUrlFilter@385f12a7, org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter@329ffcfb, org.springframework.security.web.context.SecurityContextHolderFilter@7918e7fb, org.springframework.security.web.header.HeaderWriterFilter@60dae8f4, 
org.springframework.web.filter.CorsFilter@212f2d04, org.springframework.security.web.authentication.logout.LogoutFilter@376441d8, com.simsekolah.security.JwtAuthenticationFilter@665cfc03, org.springframework.security.web.savedrequest.RequestCacheAwareFilter@362552c5, org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter@7899948d, org.springframework.security.web.authentication.AnonymousAuthenticationFilter@4d85ee0e, org.springframework.security.web.session.SessionManagementFilter@610fcd51, org.springframework.security.web.access.ExceptionTranslationFilter@238db45, org.springframework.security.web.access.intercept.AuthorizationFilter@38d4039d]
2025-09-08T09:49:16.788+07:00  INFO 2712 --- [SIM Sekolah Management System] [main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port 8080 (http) with context path ''
2025-09-08T09:49:16.799+07:00  INFO 2712 --- [SIM Sekolah Management System] [main] com.simsekolah.SimBackendApplication     : Started SimBackendApplication in 13.309 seconds (process running for 13.863)
2025-09-08T09:49:16.896+07:00  INFO 2712 --- [SIM Sekolah Management System] [main] com.simsekolah.config.DataInitializer    : Starting data initialization... 
2025-09-08T09:49:16.897+07:00  INFO 2712 --- [SIM Sekolah Management System] [main] com.simsekolah.config.DataInitializer    : Checking system users...        
2025-09-08T09:49:17.008+07:00  INFO 2712 --- [SIM Sekolah Management System] [main] com.simsekolah.config.DataInitializer    : No users found. Creating default admin user...
2025-09-08T09:49:17.299+07:00  INFO 2712 --- [SIM Sekolah Management System] [main] com.simsekolah.config.DataInitializer    : Created default admin user: admin@simsekolah.com/admin123
2025-09-08T09:49:17.299+07:00  INFO 2712 --- [SIM Sekolah Management System] [main] com.simsekolah.config.DataInitializer    : Checking database status...     
2025-09-08T09:49:17.304+07:00  INFO 2712 --- [SIM Sekolah Management System] [main] com.simsekolah.config.DataInitializer    : Database status:
2025-09-08T09:49:17.304+07:00  INFO 2712 --- [SIM Sekolah Management System] [main] com.simsekolah.config.DataInitializer    : - Students: 0
2025-09-08T09:49:17.305+07:00  INFO 2712 --- [SIM Sekolah Management System] [main] com.simsekolah.config.DataInitializer    : No students found. Students can 
be added through the application interface.
2025-09-08T09:49:17.305+07:00  INFO 2712 --- [SIM Sekolah Management System] [main] com.simsekolah.config.DataInitializer    : Data initialization completed successfully
2025-09-08T09:49:25.489+07:00  INFO 2712 --- [SIM Sekolah Management System] [http-nio-8080-exec-1] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring DispatcherServlet 'dispatcherServlet'
2025-09-08T09:49:25.490+07:00  INFO 2712 --- [SIM Sekolah Management System] [http-nio-8080-exec-1] o.s.web.servlet.DispatcherServlet        : Initializing Servlet 'dispatcherServlet'
2025-09-08T09:49:25.493+07:00  INFO 2712 --- [SIM Sekolah Management System] [http-nio-8080-exec-1] o.s.web.servlet.DispatcherServlet        : Completed initialization in 2 ms
2025-09-08T09:49:25.511+07:00 DEBUG 2712 --- [SIM Sekolah Management System] [http-nio-8080-exec-1] o.s.security.web.FilterChainProxy        : Securing POST /api/auth/login
2025-09-08T09:49:25.526+07:00 DEBUG 2712 --- [SIM Sekolah Management System] [http-nio-8080-exec-1] o.s.s.w.a.AnonymousAuthenticationFilter  : Set SecurityContextHolder to anonymous SecurityContext
2025-09-08T09:49:25.529+07:00  WARN 2712 --- [SIM Sekolah Management System] [http-nio-8080-exec-1] o.s.w.s.h.HandlerMappingIntrospector     : Cache miss for REQUEST dispatch to '/api/auth/login' (previous null). Performing MatchableHandlerMapping lookup. This is logged once only at WARN level, and every time at TRACE.
2025-09-08T09:49:25.543+07:00 DEBUG 2712 --- [SIM Sekolah Management System] [http-nio-8080-exec-1] o.s.security.web.FilterChainProxy        : Secured POST /api/auth/login
2025-09-08T09:49:25.559+07:00 DEBUG 2712 --- [SIM Sekolah Management System] [http-nio-8080-exec-1] o.s.security.web.FilterChainProxy        : Securing POST /error
2025-09-08T09:49:25.563+07:00 DEBUG 2712 --- [SIM Sekolah Management System] [http-nio-8080-exec-1] o.s.security.web.FilterChainProxy        : Secured POST /error
2025-09-08T09:49:25.627+07:00 DEBUG 2712 --- [SIM Sekolah Management System] [http-nio-8080-exec-1] o.s.s.w.a.AnonymousAuthenticationFilter  : Set SecurityContextHolder to anonymous SecurityContext
2025-09-08T09:52:53.753+07:00 DEBUG 2712 --- [SIM Sekolah Management System] [http-nio-8080-exec-3] o.s.security.web.FilterChainProxy        : Securing POST /api/auth/login
2025-09-08T09:52:53.891+07:00 DEBUG 2712 --- [SIM Sekolah Management System] [http-nio-8080-exec-3] o.s.s.w.a.AnonymousAuthenticationFilter  : Set SecurityContextHolder to anonymous SecurityContext
2025-09-08T09:52:54.017+07:00 DEBUG 2712 --- [SIM Sekolah Management System] [http-nio-8080-exec-3] o.s.security.web.FilterChainProxy        : Secured POST /api/auth/login
2025-09-08T09:52:54.295+07:00 DEBUG 2712 --- [SIM Sekolah Management System] [http-nio-8080-exec-3] o.s.security.web.FilterChainProxy        : Securing POST /error
2025-09-08T09:52:54.330+07:00 DEBUG 2712 --- [SIM Sekolah Management System] [http-nio-8080-exec-3] o.s.security.web.FilterChainProxy        : Secured POST /error
2025-09-08T09:52:54.545+07:00 DEBUG 2712 --- [SIM Sekolah Management System] [http-nio-8080-exec-3] o.s.s.w.a.AnonymousAuthenticationFilter  : Set SecurityContextHolder to anonymous SecurityContext
2025-09-08T09:56:06.365+07:00 DEBUG 2712 --- [SIM Sekolah Management System] [http-nio-8080-exec-2] o.s.security.web.FilterChainProxy        : Securing POST /api/auth/login
2025-09-08T09:56:06.369+07:00 DEBUG 2712 --- [SIM Sekolah Management System] [http-nio-8080-exec-2] o.s.s.w.a.AnonymousAuthenticationFilter  : Set SecurityContextHolder to anonymous SecurityContext
2025-09-08T09:56:06.376+07:00 DEBUG 2712 --- [SIM Sekolah Management System] [http-nio-8080-exec-2] o.s.security.web.FilterChainProxy        : Secured POST /api/auth/login
2025-09-08T09:56:06.385+07:00 DEBUG 2712 --- [SIM Sekolah Management System] [http-nio-8080-exec-2] o.s.security.web.FilterChainProxy        : Securing POST /error
2025-09-08T09:56:06.406+07:00 DEBUG 2712 --- [SIM Sekolah Management System] [http-nio-8080-exec-2] o.s.security.web.FilterChainProxy        : Secured POST /error
2025-09-08T09:56:06.412+07:00 DEBUG 2712 --- [SIM Sekolah Management System] [http-nio-8080-exec-2] o.s.s.w.a.AnonymousAuthenticationFilter  : Set SecurityContextHolder to anonymous SecurityContext
2025-09-08T09:59:14.951+07:00 DEBUG 2712 --- [SIM Sekolah Management System] [http-nio-8080-exec-5] o.s.security.web.FilterChainProxy        : Securing POST /api/auth/login
2025-09-08T09:59:14.959+07:00 DEBUG 2712 --- [SIM Sekolah Management System] [http-nio-8080-exec-5] o.s.s.w.a.AnonymousAuthenticationFilter  : Set SecurityContextHolder to anonymous SecurityContext
2025-09-08T09:59:14.961+07:00 DEBUG 2712 --- [SIM Sekolah Management System] [http-nio-8080-exec-5] o.s.security.web.FilterChainProxy        : Secured POST /api/auth/login
2025-09-08T09:59:14.969+07:00 DEBUG 2712 --- [SIM Sekolah Management System] [http-nio-8080-exec-5] o.s.security.web.FilterChainProxy        : Securing POST /error
2025-09-08T09:59:14.981+07:00 DEBUG 2712 --- [SIM Sekrror
2025-09-08T09:59:14.981+07:00 DEBUG 2712 --- [SIM Sekolah Management System] [http-nio-8080-exec-5] o.s.security.web.FilterChainProxy        : Secured POST /error
2025-09-08T09:59:14.984+07:00 DEBUG 2712 --- [SIM Sekolah Management System] [http-nio-8080-exec-5] o.s.s.w.a.AnonymousAuthenticationFilter  : Set SecurityContextHolder to anonymous SecurityContext
PS C:\Users\sija_003\Desktop\SIM_CLONE> powershell -NoProfile -Command "& 'C:\Windows\System32\curl.exe' -v http://localhost:8080/actuator/health; Write-Host '---'; & 'C:\Windows\System32\curl.exe' -v http://localhost:8080/api/auth/status; Write-Host '--- SERVER LOG (tail 80) ---'; Get-Content -Path 'c:\Users\sija_003\Desktop\SIM_CLONE\backend\logs\dev-app.log' -Tail 80"
* Host localhost:8080 was resolved.
* IPv6: ::1
* IPv4: 127.0.0.1
*   Trying [::1]:8080...
* Connected to localhost (::1) port 8080
* using HTTP/1.x
> GET /actuator/health HTTP/1.1
> Host: localhost:8080
> User-Agent: curl/8.13.0
> Accept: */*
>
* Request completely sent off
< HTTP/1.1 503 
< Vary: Origin
< Vary: Access-Control-Request-Method
< Vary: Access-Control-Request-Headers
< X-Content-Type-Options: nosniff
< X-XSS-Protection: 0
< Cache-Control: no-cache, no-store, max-age=0, must-revalidate
< Pragma: no-cache
< Expires: 0
< Content-Type: application/vnd.spring-boot.actuator.v3+json
< Transfer-Encoding: chunked
< Date: Mon, 08 Sep 2025 03:03:24 GMT
< Connection: close
<
{"status":"DOWN"}* shutting down connection #0       
---
* Host localhost:8080 was resolved.
* IPv6: ::1
* IPv4: 127.0.0.1
*   Trying [::1]:8080...
* Connected to localhost (::1) port 8080
* using HTTP/1.x
> GET /api/auth/status HTTP/1.1
> Host: localhost:8080
> User-Agent: curl/8.13.0
> Accept: */*
>
* Request completely sent off
< HTTP/1.1 404 
< Vary: Origin
< Vary: Access-Control-Request-Method
< Vary: Access-Control-Request-Headers
< X-Content-Type-Options: nosniff
< X-XSS-Protection: 0
< Cache-Control: no-cache, no-store, max-age=0, must-revalidate
< Pragma: no-cache
< Expires: 0
< Content-Type: application/json
< Transfer-Encoding: chunked
< Date: Mon, 08 Sep 2025 03:03:24 GMT
<
{"timestamp":"2025-09-08T03:03:24.537+00:00","status":404,"error":"Not Found","path":"/api/auth/status"}* 
Connection #0 to host localhost left intact
--- SERVER LOG (tail 80) ---
        at org.springframework.security.web.ObservationFilterChainDecorator$ObservationFilter.wrapFilter(ObservationFilterChainDecorator.java:240) ~[spring-security-web-6.2.0.jar:6.2.0]
        at org.springframework.security.web.ObservationFilterChainDecorator$ObservationFilter.doFilter(ObservationFilterChainDecorator.java:227) ~[spring-security-web-6.2.0.jar:6.2.0]
        at org.springframework.security.web.ObservationFilterChainDecorator$VirtualFilterChain.doFilter(ObservationFilterChainDecorator.java:137) ~[spring-security-web-6.2.0.jar:6.2.0]
        at org.springframework.security.web.authentication.logout.LogoutFilter.doFilter(LogoutFilter.java:107) ~[spring-security-web-6.2.0.jar:6.2.0]
        at org.springframework.security.web.authentication.logout.LogoutFilter.doFilter(LogoutFilter.java:93) ~[spring-security-web-6.2.0.jar:6.2.0]
        at org.springframework.security.web.ObservationFilterChainDecorator$ObservationFilter.wrapFilter(ObservationFilterChainDecorator.java:240) ~[spring-security-web-6.2.0.jar:6.2.0]
        at org.springframework.security.web.ObservationFilterChainDecorator$ObservationFilter.doFilter(ObservationFilterChainDecorator.java:227) ~[spring-security-web-6.2.0.jar:6.2.0]
        at org.springframework.security.web.ObservationFilterChainDecorator$VirtualFilterChain.doFilter(ObservationFilterChainDecorator.java:137) ~[spring-security-web-6.2.0.jar:6.2.0]
        at org.springframework.web.filter.CorsFilter.doFilterInternal(CorsFilter.java:91) ~[spring-web-6.1.1.jar:6.1.1]
        at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:116) ~[spring-web-6.1.1.jar:6.1.1]
        at org.springframework.security.web.ObservationFilterChainDecorator$ObservationFilter.wrapFilter(ObservationFilterChainDecorator.java:240) ~[spring-security-web-6.2.0.jar:6.2.0]
        at org.springframework.security.web.ObservationFilterChainDecorator$ObservationFilter.doFilter(ObservationFilterChainDecorator.java:227) ~[spring-security-web-6.2.0.jar:6.2.0]
        at org.springframework.security.web.ObservationFilterChainDecorator$VirtualFilterChain.doFilter(ObservationFilterChainDecorator.java:137) ~[spring-security-web-6.2.0.jar:6.2.0]
        at org.springframework.security.web.header.HeaderWriterFilter.doHeadersAfter(HeaderWriterFilter.java:90) ~[spring-security-web-6.2.0.jar:6.2.0]        
        at org.springframework.security.web.header.HeaderWriterFilter.doFilterInternal(HeaderWriterFilter.java:75) ~[spring-security-web-6.2.0.jar:6.2.0]      
        at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:116) ~[spring-web-6.1.1.jar:6.1.1]
        at org.springframework.security.web.ObservationFilterChainDecorator$ObservationFilter.wrapFilter(ObservationFilterChainDecorator.java:240) ~[spring-security-web-6.2.0.jar:6.2.0]
        at org.springframework.security.web.ObservationFilterChainDecorator$ObservationFilter.doFilter(ObservationFilterChainDecorator.java:227) ~[spring-security-web-6.2.0.jar:6.2.0]
        at org.springframework.security.web.ObservationFilterChainDecorator$VirtualFilterChain.doFilter(ObservationFilterChainDecorator.java:137) ~[spring-security-web-6.2.0.jar:6.2.0]
        at org.springframework.security.web.context.SecurityContextHolderFilter.doFilter(SecurityContextHolderFilter.java:82) ~[spring-security-web-6.2.0.jar:6.2.0]
        at org.springframework.security.web.context.SecurityContextHolderFilter.doFilter(SecurityContextHolderFilter.java:69) ~[spring-security-web-6.2.0.jar:6.2.0]
        at org.springframework.security.web.ObservationFilterChainDecorator$ObservationFilter.wrapFilter(ObservationFilterChainDecorator.java:240) ~[spring-security-web-6.2.0.jar:6.2.0]
        at org.springframework.security.web.ObservationFilterChainDecorator$ObservationFilter.doFilter(ObservationFilterChainDecorator.java:227) ~[spring-security-web-6.2.0.jar:6.2.0]
        at org.springframework.security.web.ObservationFilterChainDecorator$VirtualFilterChain.doFilter(ObservationFilterChainDecorator.java:137) ~[spring-security-web-6.2.0.jar:6.2.0]
        at org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter.doFilterInternal(WebAsyncManagerIntegrationFilter.java:62) ~[spring-security-web-6.2.0.jar:6.2.0]
        at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:116) ~[spring-web-6.1.1.jar:6.1.1]
        at org.springframework.security.web.ObservationFilterChainDecorator$ObservationFilter.wrapFilter(ObservationFilterChainDecorator.java:240) ~[spring-security-web-6.2.0.jar:6.2.0]
        at org.springframework.security.web.ObservationFilterChainDecorator$ObservationFilter.doFilter(ObservationFilterChainDecorator.java:227) ~[spring-security-web-6.2.0.jar:6.2.0]
        at org.springframework.security.web.ObservationFilterChainDecorator$VirtualFilterChain.doFilter(ObservationFilterChainDecorator.java:137) ~[spring-security-web-6.2.0.jar:6.2.0]
        at org.springframework.security.web.session.DisableEncodeUrlFilter.doFilterInternal(DisableEncodeUrlFilter.java:42) ~[spring-security-web-6.2.0.jar:6.2.0]
        at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:116) ~[spring-web-6.1.1.jar:6.1.1]
        at org.springframework.security.web.ObservationFilterChainDecorator$ObservationFilter.wrapFilter(ObservationFilterChainDecorator.java:240) ~[spring-security-web-6.2.0.jar:6.2.0]
        at org.springframework.security.web.ObservationFilterChainDecorator$AroundFilterObservation$SimpleAroundFilterObservation.lambda$wrap$0(ObservationFilterChainDecorator.java:323) ~[spring-security-web-6.2.0.jar:6.2.0]
        at org.springframework.security.web.ObservationFilterChainDecorator$ObservationFilter.doFilter(ObservationFilterChainDecorator.java:224) ~[spring-security-web-6.2.0.jar:6.2.0]
        at org.springframework.security.web.ObservationFilterChainDecorator$VirtualFilterChain.doFilter(ObservationFilterChainDecorator.java:137) ~[spring-security-web-6.2.0.jar:6.2.0]
        at org.springframework.security.web.FilterChainProxy.doFilterInternal(FilterChainProxy.java:233) ~[spring-security-web-6.2.0.jar:6.2.0]
        at org.springframework.security.web.FilterChainProxy.doFilter(FilterChainProxy.java:191) ~[spring-security-web-6.2.0.jar:6.2.0]
        at org.springframework.web.filter.DelegatingFilterProxy.invokeDelegate(DelegatingFilterProxy.java:352) ~[spring-web-6.1.1.jar:6.1.1]
        at org.springframework.web.filter.DelegatingFilterProxy.doFilter(DelegatingFilterProxy.java:268) ~[spring-web-6.1.1.jar:6.1.1]
        at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:174) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
        at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:149) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
        at org.springframework.web.filter.RequestContextFilter.doFilterInternal(RequestContextFilter.java:100) ~[spring-web-6.1.1.jar:6.1.1]
        at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:116) ~[spring-web-6.1.1.jar:6.1.1]
        at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:174) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
        at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:149) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
        at org.springframework.web.filter.FormContentFilter.doFilterInternal(FormContentFilter.java:93) ~[spring-web-6.1.1.jar:6.1.1]
        at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:116) ~[spring-web-6.1.1.jar:6.1.1]
        at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:174) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
        at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:149) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
        at org.springframework.web.filter.ServerHttpObservationFilter.doFilterInternal(ServerHttpObservationFilter.java:109) ~[spring-web-6.1.1.jar:6.1.1]     
        at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:116) ~[spring-web-6.1.1.jar:6.1.1]
        at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:174) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
        at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:149) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
        at org.springframework.web.filter.CharacterEncodingFilter.doFilterInternal(CharacterEncodingFilter.java:201) ~[spring-web-6.1.1.jar:6.1.1]
        at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:116) ~[spring-web-6.1.1.jar:6.1.1]
        at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:174) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
        at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:149) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
        at org.apache.catalina.core.StandardWrapperValve.invoke(StandardWrapperValve.java:167) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
        at org.apache.catalina.core.StandardContextValve.invoke(StandardContextValve.java:90) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
        at org.apache.catalina.authenticator.AuthenticatorBase.invoke(AuthenticatorBase.java:482) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
        at org.apache.catalina.core.StandardHostValve.invoke(StandardHostValve.java:115) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
        at org.apache.catalina.valves.ErrorReportValve.invoke(ErrorReportValve.java:93) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
        at org.apache.catalina.core.StandardEngineValve.invoke(StandardEngineValve.java:74) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
        at org.apache.catalina.connector.CoyoteAdapter.service(CoyoteAdapter.java:340) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
        at org.apache.coyote.http11.Http11Processor.service(Http11Processor.java:391) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
        at org.apache.coyote.AbstractProcessorLight.process(AbstractProcessorLight.java:63) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
        at org.apache.coyote.AbstractProtocol$ConnectionHandler.process(AbstractProtocol.java:896) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
        at org.apache.tomcat.util.net.NioEndpoint$SocketProcessor.doRun(NioEndpoint.java:1744) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
        at org.apache.tomcat.util.net.SocketProcessorBase.run(SocketProcessorBase.java:52) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
        at org.apache.tomcat.util.threads.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1191) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
        at org.apache.tomcat.util.threads.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:659) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
        at org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:61) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
        at java.base/java.lang.Thread.run(Thread.java:1583) ~[na:na]

2025-09-08T10:03:24.504+07:00 DEBUG 2712 --- [SIM Sekolah Management System] [http-nio-8080-exec-7] o.s.security.web.FilterChainProxy        : Securing GET /api/auth/status
2025-09-08T10:03:24.506+07:00 DEBUG 2712 --- [SIM Sekolah Management System] [http-nio-8080-exec-7] o.s.s.w.a.AnonymousAuthenticationFilter  : Set SecurityContextHolder to anonymous SecurityContext
2025-09-08T10:03:24.517+07:00 DEBUG 2712 --- [SIM Sekcurity.web.FilterChainProxy        : Secured GET /api/auth/status
2025-09-08T10:03:24.532+07:00 DEBUG 2712 --- [SIM Sekolah Management System] [http-nio-8080-exec-7] o.s.security.web.FilterChainProxy        : Securing GET /error
2025-09-08T10:03:24.535+07:00 DEBUG 2712 --- [SIM Sekolah Management System] [http-nio-8080-exec-7] o.s.security.web.FilterChainProxy        : Secured GET /error
2025-09-08T10:03:24.539+07:00 DEBUG 2712 --- [SIM Sekw.a.AnonymousAuthenticationFilter  : Set SecurityContextHolder to anonymous SecurityContext
PS C:\Users\sija_003\Desktop\SIM_CLONE> powershell -NoProfile -Command "netstat -ano | Select-String ':8080' | ForEach-Object { $_.ToString() }"
+ netstat -ano | Select-String ':8080' | 
ForEach-Object { .ToString() }
+                                                    
                ~
An expression was expected after '('.
    + CategoryInfo          : ParserError: (:) [],   
   ParentContainsErrorRecordException
PS C:\Users\sija_003\Desktop\SIM_CLONE> netstat -ano 
| findstr ":8080"
  TCP    0.0.0.0:8080           0.0.0.0:0
  LISTENING       2712
  TCP    [::]:8080              [::]:0
  LISTENING       2712
PS C:\Users\sija_003\Desktop\SIM_CLONE> tasklist /FI 
"PID eq 2712"

Image Name                     PID Session Name      
  Session#    Mem Usage
========================= ======== ================ =========== ============
java.exe                      2712 Console
         1    197,116 K
PS C:\Users\sija_003\Desktop\SIM_CLONE> taskkill /PID 2712 /F
SUCCESS: The process with PID 2712 has been terminated.
PS C:\Users\sija_003\Desktop\SIM_CLONE> 
======
PS C:\Users\sija_003\Desktop\SIM_CLONE> cmd /c start 
"Start SIM Sekolah" "C:\Users\sija_003\Desktop\SIM_CLONE\start.bat"
PS C:\Users\sija_003\Desktop\SIM_CLONE> powershell -NoProfile -Command "Start-Sleep -Seconds 6; netstat -ano | findstr ":8080"; netstat -ano | findstr ":3000"; Write-Host '--- Actuator health'; & 'C:\Windows\System32\curl.exe' -sS http://localhost:8080/actuator/health; Write-Host '--- Frontend root status'; & 'C:\Windows\System32\curl.exe' -sS -I http://localhost:3000/"
  TCP    0.0.0.0:8080           0.0.0.0:0
  LISTENING       656
  TCP    [::]:8080              [::]:0
  LISTENING       656
  TCP    0.0.0.0:3000           0.0.0.0:0
  LISTENING       10272
  LISTENING       10272
--- Actuator health
{"status":"DOWN"}--- Frontend root status
HTTP/1.1 200 OK
X-Powered-By: Express
Content-Type: text/html; charset=utf-8
Content-Length: 1325
ETag: W/"52d-zM0NxC67WnfZi1lODZ7ochszwdQ"
Date: Mon, 08 Sep 2025 03:12:48 GMT
Connection: keep-alive
Keep-Alive: timeout=5

PS C:\Users\sija_003\Desktop\SIM_CLONE> & 'C:\Windows\System32\curl.exe' -v -X POST "http://localhost:8080/api/auth/login" -H "Content-Type: application/json" 
-d "{\"identifier\":\"admin@simsekolah.com\",\"password\":\"admin123\"}"
-0a0fd96eff1fNote: Unnecessary use of -X or --request, POST is 
Note: already inferred.
* Host localhost:8080 was resolved.
* IPv6: ::1
* IPv4: 127.0.0.1
*   Trying [::1]:8080...
* Connected to localhost (::1) port 8080
* using HTTP/1.x
> POST /api/auth/login HTTP/1.1
> Host: localhost:8080
> User-Agent: curl/8.13.0
> Accept: */*
> Content-Type: application/json
> Content-Length: 2
> 
* upload completely sent off: 2 bytes
< HTTP/1.1 404
< Vary: Origin
< Vary: Access-Control-Request-Method
< Vary: Access-Control-Request-Headers
< X-Content-Type-Options: nosniff
< X-XSS-Protection: 0
< Cache-Control: no-cache, no-store, max-age=0, must-revalidate
< Expires: 0
< Content-Type: application/json
< Transfer-Encoding: chunked
< Date: Mon, 08 Sep 2025 03:15:43 GMT
:404,"error":"Not Found","path":"/api/auth/login"}* Connection #0 to host localhost left intact
Note: Unnecessary use of -X or --request, POST is    
Note: already inferred.
* URL rejected: Port number was not a decimal number 
between 0 and 65535
* closing connection #-1
curl: (3) URL rejected: Port number was not a decimal number between 0 and 65535
PS C:\Users\sija_003\Desktop\SIM_CLONE> Start-Process -FilePath .\start.bat
PS C:\Users\sija_003\Desktop\SIM_CLONE> Start-Sleep -Seconds 6; netstat -ano | findstr ":8080"; netstat -ano | findstr ":3000"; & 'C:\Windows\System32\curl.exe' -sS http://localhost:8080/actuator/health; & 'C:\Windows\System32\curl.exe' -sS -I http://localhost:3000/
st:3000/;9da30aae-cc5f-4340-826f-0a0fd96eff1f  TCP    0.0.0.0:8080           0.0.0.0:0
  LISTENING       656
  TCP    [::]:8080              [::]:0
  LISTENING       656
  LISTENING       10272
  TCP    [::]:3000              [::]:0
  LISTENING       10272
{"status":"DOWN"}HTTP/1.1 200 OK
X-Powered-By: Express
Content-Type: text/html; charset=utf-8
Content-Length: 1325
ETag: W/"52d-zM0NxC67WnfZi1lODZ7ochszwdQ"
Date: Mon, 08 Sep 2025 03:37:21 GMT
Connection: keep-alive
Keep-Alive: timeout=5

PS C:\Users\sija_003\Desktop\SIM_CLONE> & 'C:\Windows\System32\curl.exe' -v -X POST "http://localhost:8080/api/auth/login" -H "Content-Type: application/json" 
-d "{\"identifier\":\"admin@simsekolah.com\",\"password\":\"admin123\"}"
-0a0fd96eff1fNote: Unnecessary use of -X or --request, POST is 
Note: already inferred.
* Host localhost:8080 was resolved.
* IPv6: ::1
* IPv4: 127.0.0.1
*   Trying [::1]:8080...
* Connected to localhost (::1) port 8080
* using HTTP/1.x
> POST /api/auth/login HTTP/1.1
> Host: localhost:8080
> User-Agent: curl/8.13.0
> Accept: */*
> Content-Type: application/json
> Content-Length: 2
>
* upload completely sent off: 2 bytes
< HTTP/1.1 404 
< Vary: Origin
< Vary: Access-Control-Request-Method
< Vary: Access-Control-Request-Headers
< X-Content-Type-Options: nosniff
< X-XSS-Protection: 0
< Cache-Control: no-cache, no-store, max-age=0, must-revalidate
< Pragma: no-cache
< Expires: 0
< Content-Type: application/json
< Transfer-Encoding: chunked
< Date: Mon, 08 Sep 2025 03:37:57 GMT
<
{"timestamp":"2025-09-08T03:37:57.062+00:00","status":404,"error":"Not Found","path":"/api/auth/login"}* Connection #0 to host localhost left intact
Note: Unnecessary use of -X or --request, POST is    
Note: already inferred.
Note: already inferred.
* URL rejected: Port number was not a decimal number 
between 0 and 65535
* closing connection #-1
curl: (3) URL rejected: Port number was not a decimal number between 0 and 65535
PS C:\Users\sija_003\Desktop\SIM_CLONE> $body = @{ identifier = 'admin@simsekolah.com'; password = 'admin123' } | ConvertTo-Json; try { $resp = Invoke-RestMethod -Uri 'http://localhost:8080/api/auth/login' -Method Post -ContentType 'application/json' -Body $body -ErrorAction Stop; Write-Host '---RESPONSE-BODY---'; $resp | ConvertTo-Json -Depth 5 } catch { Write-Host '---REQUEST-ERROR---'; if ($_.Exception.Response) { $_.Exception.Response | Format-List * -Force } else { $_.Exception.ToString() } } ; Write-Host '---LOG TAIL---'; Get-Content .\backend\logs\dev-app.log -Tail 160 
---REQUEST-ERROR---


IsMutuallyAuthenticated : False
Cookies                 : {}
Headers                 : {Vary,
                          X-Content-Type-Options,    
                          X-XSS-Protection,
                          Pragma...}
SupportsHeaders         : True
ContentLength           : -1
ContentEncoding         :
ContentType             : application/json
CharacterSet            :
Server                  :
LastModified            : 9/8/2025 10:38:58 AM       
StatusCode              : NotFound
StatusDescription       :
ProtocolVersion         : 1.1
ResponseUri             : http://localhost:8080/api/ 
                          auth/login
Method                  : POST
IsFromCache             : False



---LOG TAIL---
        at org.springframework.security.web.ObservationFilterChainDecorator$VirtualFilterChain.doFilter(ObservationFilterChainDecorator.java:137) ~[spring-security-web-6.2.0.jar:6.2.0]
        at org.springframework.security.web.savedrequest.RequestCacheAwareFilter.doFilter(RequestCacheAwareFilter.java:63) ~[spring-security-web-6.2.0.jar:6.2.0]
        at org.springframework.security.web.ObservationFilterChainDecorator$ObservationFilter.wrapFilter(ObservationFilterChainDecorator.java:240) ~[spring-security-web-6.2.0.jar:6.2.0]
        at org.springframework.security.web.ObservationFilterChainDecorator$ObservationFilter.doFilter(ObservationFilterChainDecorator.java:227) ~[spring-security-web-6.2.0.jar:6.2.0]
        at org.springframework.security.web.ObservationFilterChainDecorator$VirtualFilterChain.doFilter(ObservationFilterChainDecorator.java:137) ~[spring-security-web-6.2.0.jar:6.2.0]
        at com.simsekolah.security.JwtAuthenticationFilter.doFilterInternal(JwtAuthenticationFilter.java:73) ~[classes/:na]
        at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:116) ~[spring-web-6.1.1.jar:6.1.1]
        at org.springframework.security.web.ObservationFilterChainDecorator$ObservationFilter.wrapFilter(ObservationFilterChainDecorator.java:240) ~[spring-security-web-6.2.0.jar:6.2.0]
        at org.springframework.security.web.ObservationFilterChainDecorator$ObservationFilter.doFilter(ObservationFilterChainDecorator.java:227) ~[spring-security-web-6.2.0.jar:6.2.0]
        at org.springframework.security.web.ObservationFilterChainDecorator$VirtualFilterChain.doFilter(ObservationFilterChainDecorator.java:137) ~[spring-security-web-6.2.0.jar:6.2.0]
        at org.springframework.security.web.authentication.logout.LogoutFilter.doFilter(LogoutFilter.java:107) ~[spring-security-web-6.2.0.jar:6.2.0]
        at org.springframework.security.web.authentication.logout.LogoutFilter.doFilter(LogoutFilter.java:93) ~[spring-security-web-6.2.0.jar:6.2.0]
        at org.springframework.security.web.ObservationFilterChainDecorator$ObservationFilter.wrapFilter(ObservationFilterChainDecorator.java:240) ~[spring-security-web-6.2.0.jar:6.2.0]
        at org.springframework.security.web.ObservationFilterChainDecorator$ObservationFilter.doFilter(ObservationFilterChainDecorator.java:227) ~[spring-security-web-6.2.0.jar:6.2.0]
        at org.springframework.security.web.ObservationFilterChainDecorator$VirtualFilterChain.doFilter(ObservationFilterChainDecorator.java:137) ~[spring-security-web-6.2.0.jar:6.2.0]
        at org.springframework.web.filter.CorsFilter.doFilterInternal(CorsFilter.java:91) ~[spring-web-6.1.1.jar:6.1.1]
        at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:116) ~[spring-web-6.1.1.jar:6.1.1]
        at org.springframework.security.web.ObservationFilterChainDecorator$ObservationFilter.wrapFilter(ObservationFilterChainDecorator.java:240) ~[spring-security-web-6.2.0.jar:6.2.0]
        at org.springframework.security.web.ObservationFilterChainDecorator$ObservationFilter.doFilter(ObservationFilterChainDecorator.java:227) ~[spring-security-web-6.2.0.jar:6.2.0]
        at org.springframework.security.web.ObservationFilterChainDecorator$VirtualFilterChain.doFilter(ObservationFilterChainDecorator.java:137) ~[spring-security-web-6.2.0.jar:6.2.0]
        at org.springframework.security.web.header.HeaderWriterFilter.doHeadersAfter(HeaderWriterFilter.java:90) ~[spring-security-web-6.2.0.jar:6.2.0]        
        at org.springframework.security.web.header.HeaderWriterFilter.doFilterInternal(HeaderWriterFilter.java:75) ~[spring-security-web-6.2.0.jar:6.2.0]      
        at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:116) ~[spring-web-6.1.1.jar:6.1.1]
        at org.springframework.security.web.ObservationFilterChainDecorator$ObservationFilter.wrapFilter(ObservationFilterChainDecorator.java:240) ~[spring-security-web-6.2.0.jar:6.2.0]
        at org.springframework.security.web.ObservationFilterChainDecorator$ObservationFilter.doFilter(ObservationFilterChainDecorator.java:227) ~[spring-security-web-6.2.0.jar:6.2.0]
        at org.springframework.security.web.ObservationFilterChainDecorator$VirtualFilterChain.doFilter(ObservationFilterChainDecorator.java:137) ~[spring-security-web-6.2.0.jar:6.2.0]
        at org.springframework.security.web.context.SecurityContextHolderFilter.doFilter(SecurityContextHolderFilter.java:82) ~[spring-security-web-6.2.0.jar:6.2.0]
        at org.springframework.security.web.context.SecurityContextHolderFilter.doFilter(SecurityContextHolderFilter.java:69) ~[spring-security-web-6.2.0.jar:6.2.0]
        at org.springframework.security.web.ObservationFilterChainDecorator$ObservationFilter.wrapFilter(ObservationFilterChainDecorator.java:240) ~[spring-security-web-6.2.0.jar:6.2.0]
        at org.springframework.security.web.ObservationFilterChainDecorator$ObservationFilter.doFilter(ObservationFilterChainDecorator.java:227) ~[spring-security-web-6.2.0.jar:6.2.0]
        at org.springframework.security.web.ObservationFilterChainDecorator$VirtualFilterChain.doFilter(ObservationFilterChainDecorator.java:137) ~[spring-security-web-6.2.0.jar:6.2.0]
        at org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter.doFilterInternal(WebAsyncManagerIntegrationFilter.java:62) ~[spring-security-web-6.2.0.jar:6.2.0]
        at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:116) ~[spring-web-6.1.1.jar:6.1.1]
        at org.springframework.security.web.ObservationFilterChainDecorator$ObservationFilter.wrapFilter(ObservationFilterChainDecorator.java:240) ~[spring-security-web-6.2.0.jar:6.2.0]
        at org.springframework.security.web.ObservationFilterChainDecorator$ObservationFilter.doFilter(ObservationFilterChainDecorator.java:227) ~[spring-security-web-6.2.0.jar:6.2.0]
        at org.springframework.security.web.ObservationFilterChainDecorator$VirtualFilterChain.doFilter(ObservationFilterChainDecorator.java:137) ~[spring-security-web-6.2.0.jar:6.2.0]
        at org.springframework.security.web.session.DisableEncodeUrlFilter.doFilterInternal(DisableEncodeUrlFilter.java:42) ~[spring-security-web-6.2.0.jar:6.2.0]
        at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:116) ~[spring-web-6.1.1.jar:6.1.1]
        at org.springframework.security.web.ObservationFilterChainDecorator$ObservationFilter.wrapFilter(ObservationFilterChainDecorator.java:240) ~[spring-security-web-6.2.0.jar:6.2.0]
        at org.springframework.security.web.ObservationFilterChainDecorator$AroundFilterObservation$SimpleAroundFilterObservation.lambda$wrap$0(ObservationFilterChainDecorator.java:323) ~[spring-security-web-6.2.0.jar:6.2.0]
        at org.springframework.security.web.ObservationFilterChainDecorator$ObservationFilter.doFilter(ObservationFilterChainDecorator.java:224) ~[spring-security-web-6.2.0.jar:6.2.0]
        at org.springframework.security.web.ObservationFilterChainDecorator$VirtualFilterChain.doFilter(ObservationFilterChainDecorator.java:137) ~[spring-security-web-6.2.0.jar:6.2.0]
        at org.springframework.security.web.FilterChainProxy.doFilterInternal(FilterChainProxy.java:233) ~[spring-security-web-6.2.0.jar:6.2.0]
        at org.springframework.security.web.FilterChainProxy.doFilter(FilterChainProxy.java:191) ~[spring-security-web-6.2.0.jar:6.2.0]
        at org.springframework.web.filter.DelegatingFilterProxy.invokeDelegate(DelegatingFilterProxy.java:352) ~[spring-web-6.1.1.jar:6.1.1]
        at org.springframework.web.filter.DelegatingFilterProxy.doFilter(DelegatingFilterProxy.java:268) ~[spring-web-6.1.1.jar:6.1.1]
        at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:174) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
        at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:149) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
        at org.springframework.web.filter.RequestContextFilter.doFilterInternal(RequestContextFilter.java:100) ~[spring-web-6.1.1.jar:6.1.1]
        at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:116) ~[spring-web-6.1.1.jar:6.1.1]
        at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:174) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
        at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:149) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
        at org.springframework.web.filter.FormContentFilter.doFilterInternal(FormContentFilter.java:93) ~[spring-web-6.1.1.jar:6.1.1]
        at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:116) ~[spring-web-6.1.1.jar:6.1.1]
        at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:174) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
        at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:149) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
        at org.springframework.web.filter.ServerHttpObservationFilter.doFilterInternal(ServerHttpObservationFilter.java:109) ~[spring-web-6.1.1.jar:6.1.1]     
        at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:116) ~[spring-web-6.1.1.jar:6.1.1]
        at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:174) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
        at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:149) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
        at org.springframework.web.filter.CharacterEncodingFilter.doFilterInternal(CharacterEncodingFilter.java:201) ~[spring-web-6.1.1.jar:6.1.1]
        at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:116) ~[spring-web-6.1.1.jar:6.1.1]
        at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:174) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
        at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:149) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
        at org.apache.catalina.core.StandardWrapperValve.invoke(StandardWrapperValve.java:167) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
        at org.apache.catalina.core.StandardContextValve.invoke(StandardContextValve.java:90) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
        at org.apache.catalina.authenticator.AuthenticatorBase.invoke(AuthenticatorBase.java:482) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
        at org.apache.catalina.core.StandardHostValve.invoke(StandardHostValve.java:115) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
        at org.apache.catalina.valves.ErrorReportValve.invoke(ErrorReportValve.java:93) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
        at org.apache.catalina.core.StandardEngineValve.invoke(StandardEngineValve.java:74) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
        at org.apache.catalina.connector.CoyoteAdapter.service(CoyoteAdapter.java:340) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
        at org.apache.coyote.http11.Http11Processor.service(Http11Processor.java:391) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
        at org.apache.coyote.AbstractProcessorLight.process(AbstractProcessorLight.java:63) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
        at org.apache.coyote.AbstractProtocol$ConnectionHandler.process(AbstractProtocol.java:896) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
        at org.apache.tomcat.util.net.NioEndpoint$SocketProcessor.doRun(NioEndpoint.java:1744) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
        at org.apache.tomcat.util.net.SocketProcessorBase.run(SocketProcessorBase.java:52) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
        at org.apache.tomcat.util.threads.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1191) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
        at org.apache.tomcat.util.threads.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:659) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
        at org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:61) ~[tomcat-embed-core-10.1.16.jar:10.1.16]
        at java.base/java.lang.Thread.run(Thread.java:1583) ~[na:na]

2025-09-08T10:37:26.108+07:00  INFO 17492 --- [SIM Sekolah Management System] [main] com.simsekolah.SimBackendApplication     : Starting SimBackendApplication 
using Java 21.0.7 with PID 17492 (C:\Users\sija_003\Desktop\SIM_CLONE\backend\target\classes started by sija_003 in C:\Users\sija_003\Desktop\SIM_CLONE\backend)
2025-09-08T10:37:26.111+07:00  INFO 17492 --- [SIM Sekolah Management System] [main] com.simsekolah.SimBackendApplication     : The following 1 profile is active: "dev"
2025-09-08T10:37:27.860+07:00  INFO 17492 --- [SIM Sekolah Management System] [main] .s.d.r.c.RepositoryConfigurationDelegate : Multiple Spring Data modules found, entering strict repository configuration mode   
2025-09-08T10:37:27.863+07:00  INFO 17492 --- [SIM Sekolah Management System] [main] .s.d.r.c.RepositoryConfigurationDelegate : Bootstrapping Spring Data JPA repositories in DEFAULT mode.
2025-09-08T10:37:28.196+07:00  INFO 17492 --- [SIM Sekolah Management System] [main] .s.d.r.c.RepositoryConfigurationDelegate : Finished Spring Data repository scanning in 325 ms. Found 17 JPA repository interfaces.
2025-09-08T10:37:29.473+07:00  INFO 17492 --- [SIM Sekolah Management System] [main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat initialized with port 8080 (http)
2025-09-08T10:37:29.486+07:00  INFO 17492 --- [SIM Sekolah Management System] [main] o.apache.catalina.core.StandardService   : Starting service [Tomcat]      
2025-09-08T10:37:29.487+07:00  INFO 17492 --- [SIM Sekolah Management System] [main] o.apache.catalina.core.StandardEngine    : Starting Servlet engine: [Apache Tomcat/10.1.16]
2025-09-08T10:37:29.611+07:00  INFO 17492 --- [SIM Sekolah Management System] [main] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring embedded WebApplicationContext
2025-09-08T10:37:29.613+07:00  INFO 17492 --- [SIM Sekolah Management System] [main] w.s.c.ServletWebServerApplicationContext : Root WebApplicationContext: initialization completed in 3439 ms
2025-09-08T10:37:29.780+07:00  INFO 17492 --- [SIM Sekolah Management System] [main] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Starting...     
2025-09-08T10:37:30.040+07:00  INFO 17492 --- [SIM Sekolah Management System] [main] com.zaxxer.hikari.pool.HikariPool        : HikariPool-1 - Added connection conn0: url=jdbc:h2:mem:simsekolahdb user=SA
2025-09-08T10:37:30.042+07:00  INFO 17492 --- [SIM Sekolah Management System] [main] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Start completed.2025-09-08T10:37:30.057+07:00  INFO 17492 --- [SIM Sekolah Management System] [main] o.s.b.a.h2.H2ConsoleAutoConfiguration    : H2 console available at '/h2-console'. Database available at 'jdbc:h2:mem:simsekolahdb'
2025-09-08T10:37:30.302+07:00  INFO 17492 --- [SIM Sekolah Management System] [main] o.hibernate.jpa.internal.util.LogHelper  : HHH000204: Processing PersistenceUnitInfo [name: default]
2025-09-08T10:37:30.389+07:00  INFO 17492 --- [SIM Sekolah Management System] [main] org.hibernate.Version                    : HHH000412: Hibernate ORM core version 6.3.1.Final
2025-09-08T10:37:30.438+07:00  INFO 17492 --- [SIM Sekolah Management System] [main] o.h.c.internal.RegionFactoryInitiator    : HHH000026: Second-level cache disabled
2025-09-08T10:37:30.792+07:00  INFO 17492 --- [SIM Sekolah Management System] [main] o.s.o.j.p.SpringPersistenceUnitInfo      : No LoadTimeWeaver setup: ignoring JPA class transformer
2025-09-08T10:37:30.877+07:00  WARN 17492 --- [SIM Sekolah Management System] [main] org.hibernate.orm.deprecation            : HHH90000025: H2Dialect does not need to be specified explicitly using 'hibernate.dialect' (remove the property setting and it will be selected by default)
2025-09-08T10:37:32.679+07:00  INFO 17492 --- [SIM Sekolah Management System] [main] o.h.e.t.j.p.i.JtaPlatformInitiator       : HHH000489: No JTA platform available (set 'hibernate.transaction.jta.platform' to enable JTA platform integration)
2025-09-08T10:37:32.970+07:00  INFO 17492 --- [SIM Sekolah Management System] [main] j.LocalContainerEntityManagerFactoryBean : Initialized JPA EntityManagerFactory for persistence unit 'default'
2025-09-08T10:37:33.411+07:00  INFO 17492 --- [SIM Sekolah Management System] [main] o.s.d.j.r.query.QueryEnhancerFactory     : Hibernate is in classpath; If applicable, HQL parser will be used.
2025-09-08T10:37:35.069+07:00  INFO 17492 --- [SIM Sekolah Management System] [main] com.simsekolah.config.RedisCacheConfig   : Configuring Redis connection to localhost:6379
2025-09-08T10:37:35.112+07:00  INFO 17492 --- [SIM Sekolah Management System] [main] com.simsekolah.config.RedisCacheConfig   : Redis connection factory configured successfully
2025-09-08T10:37:35.589+07:00 DEBUG 17492 --- [SIM Sekolah Management System] [main] c.s.security.JwtAuthenticationFilter     : Filter 'jwtAuthenticationFilter' configured for use
2025-09-08T10:37:36.113+07:00  INFO 17492 --- [SIM Sekolah Management System] [main] com.simsekolah.config.RedisCacheConfig   : Configuring Redis template     
2025-09-08T10:37:36.127+07:00  INFO 17492 --- [SIM Sekolah Management System] [main] com.simsekolah.config.RedisCacheConfig   : Redis template configured successfully
2025-09-08T10:37:36.196+07:00 TRACE 17492 --- [SIM Sekolah Management System] [main] eGlobalAuthenticationAutowiredConfigurer : Eagerly initializing {securityConfig=com.simsekolah.config.SecurityConfig$$SpringCGLIB$$0@3a524163}
2025-09-08T10:37:37.009+07:00  INFO 17492 --- [SIM Sekolah Management System] [main] com.simsekolah.config.RedisCacheConfig   : Configuring Redis cache manager2025-09-08T10:37:37.035+07:00  INFO 17492 --- [SIM Sekolah Management System] [main] com.simsekolah.config.RedisCacheConfig   : Redis cache manager configured 
with 25 specific cache configurations
2025-09-08T10:37:39.140+07:00  WARN 17492 --- [SIM Sekolah Management System] [main] JpaBaseConfiguration$JpaWebConfiguration : spring.jpa.open-in-view is enabled by default. Therefore, database queries may be performed during view rendering. Explicitly configure spring.jpa.open-in-view to disable this warning       
2025-09-08T10:37:39.294+07:00 DEBUG 17492 --- [SIM Sekolah Management System] [main] o.s.s.a.h.RoleHierarchyImpl              : setHierarchy() - The following 
role hierarchy was set: ROLE_ADMIN > ROLE_TEACHER    
ROLE_TEACHER > ROLE_STUDENT
ROLE_STUDENT > ROLE_USER
2025-09-08T10:37:39.295+07:00 DEBUG 17492 --- [SIM Sekolah Management System] [main] o.s.s.a.h.RoleHierarchyImpl              : buildRolesReachableInOneStepMap() - From role ROLE_ADMIN one can reach role ROLE_TEACHER in one step.
2025-09-08T10:37:39.299+07:00 DEBUG 17492 --- [SIM Sekolah Management System] [main] o.s.s.a.h.RoleHierarchyImpl              : buildRolesReachableInOneStepMap() - From role ROLE_TEACHER one can reach role ROLE_STUDENT in one step.
2025-09-08T10:37:39.300+07:00 DEBUG 17492 --- [SIM Sekolah Management System] [main] o.s.s.a.h.RoleHierarchyImpl              : buildRolesReachableInOneStepMap() - From role ROLE_STUDENT one can reach role ROLE_USER in one step.
2025-09-08T10:37:39.301+07:00 DEBUG 17492 --- [SIM Sekolah Management System] [main] o.s.s.a.h.RoleHierarchyImpl              : buildRolesReachableInOneOrMoreStepsMap() - From role ROLE_STUDENT one can reach [ROLE_USER] in one or more steps.
2025-09-08T10:37:39.301+07:00 DEBUG 17492 --- [SIM Sekolah Management System] [main] o.s.s.a.h.RoleHierarchyImpl              : buildRolesReachableInOneOrMoreStepsMap() - From role ROLE_ADMIN one can reach [ROLE_STUDENT, ROLE_USER, ROLE_TEACHER] in one or more steps.
2025-09-08T10:37:39.302+07:00 DEBUG 17492 --- [SIM Sekolah Management System] [main] o.s.s.a.h.RoleHierarchyImpl              : buildRolesReachableInOneOrMoreStepsMap() - From role ROLE_TEACHER one can reach [ROLE_STUDENT, ROLE_USER] in one or more steps.
2025-09-08T10:37:39.814+07:00  INFO 17492 --- [SIM Sekolah Management System] [main] o.s.b.a.e.web.EndpointLinksResolver      : Exposing 1 endpoint(s) beneath 
base path '/actuator'
2025-09-08T10:37:39.901+07:00  INFO 17492 --- [SIM Sekolah Management System] [main] o.s.s.web.DefaultSecurityFilterChain     : Will secure any request with [org.springframework.security.web.session.DisableEncodeUrlFilter@61b69169, org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter@382556ac, org.springframework.security.web.context.SecurityContextHolderFilter@5ae74f78, org.springframework.security.web.header.HeaderWriterFilter@38d4039d, org.springframework.web.filter.CorsFilter@512f3e71, 
org.springframework.security.web.authentication.logout.LogoutFilter@2666f09, com.simsekolah.security.JwtAuthenticationFilter@73b0b1d5, org.springframework.security.web.savedrequest.RequestCacheAwareFilter@70a3cf9d, org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter@7fb43d4a, org.springframework.security.web.authentication.AnonymousAuthenticationFilter@55d7f8dd, org.springframework.security.web.session.SessionManagementFilter@4ebb7a4b, org.springframework.security.web.access.ExceptionTranslationFilter@371473b0, org.springframework.security.web.access.intercept.AuthorizationFilter@310482a5]        
2025-09-08T10:37:41.273+07:00  WARN 17492 --- [SIM Sekolah Management System] [main] ConfigServletWebServerApplicationContext : Exception encountered during context initialization - cancelling refresh attempt: org.springframework.context.ApplicationContextException: Failed to start bean 'webServerStartStop'
2025-09-08T10:37:41.279+07:00  INFO 17492 --- [SIM Sekolah Management System] [main] j.LocalContainerEntityManagerFactoryBean : Closing JPA EntityManagerFactory for persistence unit 'default'
2025-09-08T10:37:41.310+07:00  INFO 17492 --- [SIM Sekolah Management System] [main] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Shutdown initiated...
2025-09-08T10:37:41.312+07:00  INFO 17492 --- [SIM Sekolah Management System] [main] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Shutdown completed.
2025-09-08T10:37:41.334+07:00  INFO 17492 --- [SIM Sekolah Management System] [main] .s.b.a.l.ConditionEvaluationReportLogger :

Error starting ApplicationContext. To display the condition evaluation report re-run your application with 'debug' enabled.
2025-09-08T10:37:41.357+07:00 ERROR 17492 --- [SIM Sekolah Management System] [main] o.s.b.d.LoggingFailureAnalysisReporter   :

***************************
APPLICATION FAILED TO START
***************************

Description:

Web server failed to start. Port 8080 was already in 
use.

Action:

Identify and stop the process that's listening on port 8080 or configure this application to listen on another port.

2025-09-08T10:37:56.695+07:00 DEBUG 656 --- [SIM Sekolah Management System] [http-nio-8080-exec-2] o.s.security.web.FilterChainProxy        : Securing POST /api/auth/login
2025-09-08T10:37:56.702+07:00 DEBUG 656 --- [SIM Sekolah Management System] [http-nio-8080-exec-2] c.s.security.JwtAuthenticationFilter     : JwtAuthFilter - incoming request: POST /api/auth/login
2025-09-08T10:37:56.703+07:00 DEBUG 656 --- [SIM Sekolah Management System] [http-nio-8080-exec-2] c.s.security.JwtAuthenticationFilter     : JwtAuthFilter - Authorization header: null
2025-09-08T10:37:56.705+07:00 DEBUG 656 --- [SIM Sekolah Management System] [http-nio-8080-exec-2] o.s.s.w.a.AnonymousAuthenticationFilter  : Set SecurityContextHolder to anonymous SecurityContext
2025-09-08T10:37:56.738+07:00 DEBUG 656 --- [SIM Sekolah Management System] [http-nio-8080-exec-2] o.s.security.web.FilterChainProxy        : Secured POST /api/auth/login
2025-09-08T10:37:57.035+07:00 DEBUG 656 --- [SIM Sekolah Management System] [http-nio-8080-exec-2] o.s.security.web.FilterChainProxy        : Securing POST /error
2025-09-08T10:37:57.052+07:00 DEBUG 656 --- [SIM Sekolah Management System] [http-nio-8080-exec-2] o.s.security.web.FilterChainProxy        : Secured POST /error
2025-09-08T10:37:57.084+07:00 DEBUG 656 --- [SIM Sekolah Management System] [http-nio-8080-exec-2] o.s.s.w.a.AnonymousAuthenticationFilter  : Set SecurityContextHolder to anonymous SecurityContext
2025-09-08T10:38:58.227+07:00 DEBUG 656 --- [SIM Sekolah Management System] [http-nio-8080-exec-8] o.s.security.web.FilterChainProxy        : Securing POST /api/auth/login
2025-09-08T10:38:58.230+07:00 DEBUG 656 --- [SIM Sekolah Management System] [http-nio-8080-exec-8] c.s.security.JwtAuthenticationFilter     : JwtAuthFilter - incoming request: POST /api/auth/login
2025-09-08T10:38:58.231+07:00 DEBUG 656 --- [SIM Sekolah Management System] [http-nio-8080-exec-8] c.s.security.JwtAuthenticationFilter     : JwtAuthFilter - Authorization header: null
2025-09-08T10:38:58.231+07:00 DEBUG 656 --- [SIM Sekolah Management System] [http-nio-8080-exec-8] o.s.s.w.a.AnonymousAuthenticationFilter  : Set SecurityContextHolder to anonymous SecurityContext
2025-09-08T10:38:58.238+07:00 DEBUG 656 --- [SIM Sekolah Management System] [http-nio-8080-exec-8] o.s.security.web.FilterChainProxy        : Secured POST /api/auth/login
2025-09-08T10:38:58.246+07:00 DEBUG 656 --- [SIM Sekoror
2025-09-08T10:38:58.249+07:00 DEBUG 656 --- [SIM Sekolah Management System] [http-nio-8080-exec-8] o.s.security.web.FilterChainProxy        : Secured POST /error
2025-09-08T10:38:58.253+07:00 DEBUG 656 --- [SIM Sekolah Management System] [http-nio-8080-exec-8] o.s.s.w.a.AnonymousAuthenticationFilter  : Set SecurityContextHolder to anonymous SecurityContext
PS C:\Users\sija_003\Desktop\SIM_CLONE> Set-Content -Path .\tmp_login.json -Value '{"identifier":"admin@simsekolah.com","password":"admin123"}' -NoNewline; & 'C:\Windows\System32\curl.exe' -v -X POST "http://localhost:8080/api/auth/login" -H "Content-Type: application/json" -d @.\tmp_login.json; Write-Host '---LOG TAlhost:8080/api/auth/login" -H "Content-Type: application/json" -d @.\tmp_login.json; Write-Host '---LOG TAIL---'; Get-Content .\backend\logs\dev-app.log -Tail 
240
At line:1 char:239
+ ... 8080/api/auth/login" -H "Content-Type:         
application/json" -d @.\tmp_lo ...
+                                                    
              ~
Unrecognized token in source text.
    + CategoryInfo          : ParserError: (:) [],   
   ParentContainsErrorRecordException
    + FullyQualifiedErrorId : UnrecognizedToken      
 
PS C:\Users\sija_003\Desktop\SIM_CLONE> curl.exe -v -X POST http://localhost:8080/api/auth/login -H "Content-Type: application/json" --data-binary @- <<EOF    
At line:1 char:108
+ ... auth/login -H "Content-Type: 
application/json" --data-binary @- <<EOF
+                                                    
               ~
Unrecognized token in source text.
At line:1 char:112
+ ... auth/login -H "Content-Type: 
application/json" --data-binary @- <<EOF
+                                                    
                   ~
Missing file specification after redirection         
operator.
+ ... auth/login -H "Content-Type: 
application/json" --data-binary @- <<EOF
+                                                    
                  ~
The '<' operator is reserved for future use.
At line:1 char:112
+ ... auth/login -H "Content-Type: 
application/json" --data-binary @- <<EOF
+                                                    
                   ~
The '<' operator is reserved for future use.
    + CategoryInfo          : ParserError: (:) [],   
   ParentContainsErrorRecordException
    + FullyQualifiedErrorId : UnrecognizedToken      
 
:"admin@simsekolah.com","password":"admin123"}       
At line:1 char:14
+ {"identifier":"admin@simsekolah.com","password":"a 
dmin123"}
+              ~~~~~~~~~~~~~~~~~~~~~~~
Unexpected token ':"admin@simsekolah.com"' in        
expression or statement.
At line:1 char:37
+ {"identifier":"admin@simsekolah.com","password":"a 
dmin123"}
+                                     ~
Missing argument in parameter list.
    + CategoryInfo          : ParserError: (:) [],   
   ParentContainsErrorRecordException
    + FullyQualifiedErrorId : UnexpectedToken        
 
PS C:\Users\sija_003\Desktop\SIM_CLONE> EOF
EOF : The term 'EOF' is not recognized as the name 
of a cmdlet, function, script file, or operable      
program. Check the spelling of the name, or if a     
path was included, verify that the path is correct   
path was included, verify that the path is correct   
and try again.
At line:1 char:1
+ EOF
+ ~~~
    + CategoryInfo          : ObjectNotFound: (EOF:  
   String) [], CommandNotFoundException
    + FullyQualifiedErrorId : CommandNotFoundExcept  
   ion
 
PS C:\Users\sija_003\Desktop\SIM_CLONE> $body='{"identifier":"admin@simsekolah.com","password":"admin123"}'; $tmp=[System.IO.Path]::GetTempFileName(); Set-Content -Path $tmp -Value $body -Encoding UTF8; curl.exe 
-v -X POST "http://localhost:8080/api/auth/login" -H 
"Content-Type: application/json" --data-binary "@$tmp"; Remove-Item $tmp
Note: Unnecessary use of -X or --request, POST is 
Note: already inferred.
* Host localhost:8080 was resolved.
* IPv6: ::1
* IPv4: 127.0.0.1
*   Trying [::1]:8080...
* Connected to localhost (::1) port 8080
* using HTTP/1.x
> POST /api/auth/login HTTP/1.1
> Host: localhost:8080
> User-Agent: curl/8.13.0
> Accept: */*
> Content-Type: application/json
> Content-Length: 64
>
* upload completely sent off: 64 bytes
< HTTP/1.1 404 
< Vary: Access-Control-Request-Method
< Vary: Access-Control-Request-Headers
< X-Content-Type-Options: nosniff
< X-XSS-Protection: 0
< Cache-Control: no-cache, no-store, max-age=0, must-revalidate
< Pragma: no-cache
< Expires: 0
< Content-Type: application/json
< Transfer-Encoding: chunked
< Date: Mon, 08 Sep 2025 04:01:11 GMT
<
{"timestamp":"2025-09-08T04:01:10.676+00:00","status":404,"error":"Not Found","path":"/api/auth/login"}* Connection #0 to host localhost left intact
PS C:\Users\sija_003\Desktop\SIM_CLONE> curl.exe -v http://localhost:8080/api/auth/status
* Host localhost:8080 was resolved.
* IPv6: ::1
* IPv4: 127.0.0.1
*   Trying [::1]:8080...
* Connected to localhost (::1) port 8080
* using HTTP/1.x
> GET /api/auth/status HTTP/1.1
> Host: localhost:8080
> User-Agent: curl/8.13.0
> Accept: */*
>
* Request completely sent off
< HTTP/1.1 404 
< Vary: Access-Control-Request-Method
< Vary: Access-Control-Request-Headers
< X-Content-Type-Options: nosniff
< X-XSS-Protection: 0
< Cache-Control: no-cache, no-store, max-age=0, must-revalidate
< Pragma: no-cache
< Expires: 0
< Content-Type: application/json
< Transfer-Encoding: chunked
< Date: Mon, 08 Sep 2025 05:27:26 GMT
<
{"timestamp":"2025-09-08T05:27:26.749+00:00","status":404,"error":"Not Found","path":"/api/auth/status"}* 
Connection #0 to host localhost left intact
PS C:\Users\sija_003\Desktop\SIM_CLONE> curl.exe -v http://localhost:8080/actuator
* Host localhost:8080 was resolved.
* IPv6: ::1
* IPv4: 127.0.0.1
*   Trying [::1]:8080...
* Connected to localhost (::1) port 8080
* using HTTP/1.x
> GET /actuator HTTP/1.1
> Host: localhost:8080
> User-Agent: curl/8.13.0
> Accept: */*
>
* Request completely sent off
< HTTP/1.1 200 
< Vary: Origin
< Vary: Access-Control-Request-Method
< Vary: Access-Control-Request-Headers
< X-Content-Type-Options: nosniff
< Cache-Control: no-cache, no-store, max-age=0, must-revalidate
< Pragma: no-cache
< Expires: 0
< Content-Type: application/vnd.spring-boot.actuator.v3+json
< Transfer-Encoding: chunked
< Date: Mon, 08 Sep 2025 05:34:00 GMT
<
{"_links":{"self":{"href":"http://localhost:8080/actuator","templated":false},"health":{"href":"http://localhost:8080/actuator/health","templated":false},"health-path":{"href":"http://localhost:8080/actuator/health/{*path}","templated":true}}}* Connection #0 to host localhost left intact
PS C:\Users\sija_003\Desktop\SIM_CLONE> curl.exe -v http://localhost:8080/api/v1/auth/status
* Host localhost:8080 was resolved.
* IPv6: ::1
* IPv4: 127.0.0.1
*   Trying [::1]:8080...
* Connected to localhost (::1) port 8080
* using HTTP/1.x
> GET /api/v1/auth/status HTTP/1.1
> Host: localhost:8080
> User-Agent: curl/8.13.0
> Accept: */*
> 
* Request completely sent off
< HTTP/1.1 404 
< Vary: Origin
< Vary: Access-Control-Request-Method
< Vary: Access-Control-Request-Headers
< X-Content-Type-Options: nosniff
< X-XSS-Protection: 0
< Cache-Control: no-cache, no-store, max-age=0, must-revalidate
< Pragma: no-cache
< Expires: 0
< Content-Type: application/json
< Transfer-Encoding: chunked
< Date: Mon, 08 Sep 2025 05:39:18 GMT
<
{"timestamp":"2025-09-08T05:39:18.083+00:00","status":404,"error":"Not Found","path":"/api/v1/auth/status"}* Connection #0 to host localhost left intact       
PS C:\Users\sija_003\Desktop\SIM_CLONE>

========================
2025-09-08T13:43:33.801+07:00  INFO 15660 --- [SIM Sekolah Management System] [nio-8080-exec-1] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring DispatcherServlet 'dispatcherServlet'                                            2025-09-08T13:43:33.802+07:00  INFO 15660 --- [SIM Sekolah Management System] [nio-8080-exec-1] o.s.web.servlet.DispatcherServlet        : Initializing Servlet 'dispatcherServlet'                                                             2025-09-08T13:43:33.805+07:00  INFO 15660 --- [SIM Sekolah Management System] [nio-8080-exec-1] o.s.web.servlet.DispatcherServlet        : Completed initialization in 2 ms                                                                     2025-09-08T13:43:33.823+07:00 DEBUG 15660 --- [SIM Sekolah Management System] [nio-8080-exec-1] o.s.security.web.FilterChainProxy        : Securing POST /api/auth/login                                                                        2025-09-08T13:43:33.836+07:00 DEBUG 15660 --- [SIM Sekolah Management System] [nio-8080-exec-1] c.s.security.JwtAuthenticationFilter     : JwtAuthFilter - incoming request: POST /api/auth/login                                               2025-09-08T13:43:33.836+07:00 DEBUG 15660 --- [SIM Sekolah Management System] [nio-8080-exec-1] c.s.security.JwtAuthenticationFilter     : JwtAuthFilter - Authorization header: null                                                           2025-09-08T13:43:33.840+07:00 DEBUG 15660 --- [SIM Sekolah Management System] [nio-8080-exec-1] o.s.s.w.a.AnonymousAuthenticationFilter  : Set SecurityContextHolder to anonymous SecurityContext                                               2025-09-08T13:43:33.844+07:00  WARN 15660 --- [SIM Sekolah Management System] [nio-8080-exec-1] o.s.w.s.h.HandlerMappingIntrospector     : Cache miss for REQUEST dispatch to '/api/auth/login' (previous null). Performing MatchableHandlerMapping lookup. This is logged once only at WARN level, and every time at TRACE.                                            2025-09-08T13:43:33.858+07:00 DEBUG 15660 --- [SIM Sekolah Management System] [nio-8080-exec-1] o.s.security.web.FilterChainProxy        : Secured POST /api/auth/login                                                                         2025-09-08T13:43:33.875+07:00 DEBUG 15660 --- [SIM Sekolah Management System] [nio-8080-exec-1] o.s.security.web.FilterChainProxy        : Securing POST /error                                                                                 2025-09-08T13:43:33.881+07:00 DEBUG 15660 --- [SIM Sekolah Management System] [nio-8080-exec-1] o.s.security.web.FilterChainProxy        : Secured POST /error                                                                                  2025-09-08T13:43:33.945+07:00 DEBUG 15660 --- [SIM Sekolah Management System] [nio-8080-exec-1] o.s.s.w.a.AnonymousAuthenticationFilter  : Set SecurityContextHolder to anonymous SecurityContext 


===============

olah Management System] [main] o.s.s.a.h.RoleHierarchyImpl              : buildRolesReachableInOneOrMoreStepsMap() - From role ROLE_ADMIN one can reach [ROLE_STUDENT, ROLE_USER, ROLE_TEACHER] in one or more steps.
2025-09-08T12:42:06.861+07:00 DEBUG 5052 --- [SIM Sekolah Management System] [main] o.s.s.a.h.RoleHierarchyImpl              : buildRolesReachableInOneOrMoreStepsMap() - From role ROLE_TEACHER one can reach [ROLE_STUDENT, ROLE_USER] in one or more steps.
2025-09-08T12:42:07.474+07:00  INFO 5052 --- [SIM Sekolah Management System] [main] o.s.b.a.e.web.EndpointLinksResolver      : Exposing 3 endpoint(s) beneath base path '/actuator'
2025-09-08T12:42:07.555+07:00  INFO 5052 --- [SIM Sekolah Management System] [main] o.s.s.web.DefaultSecurityFilterChain     : Will secure any request with [org.springframework.security.web.session.DisableEncodeUrlFilter@673e6fc9, org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter@6b3a527, org.springframework.security.web.context.SecurityContextHolderFilter@792af176, org.springframework.security.web.header.HeaderWriterFilter@84a95e5, org.springframework.web.filter.CorsFilter@578e5975, org.springframework.security.web.authentication.logout.LogoutFilter@1e64c9df, com.simsekolah.security.JwtAuthenticationFilter@413c710e, org.springframework.security.web.savedrequest.RequestCacheAwareFilter@512f3e71, org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter@50b2de4e, org.springframework.security.web.authentication.AnonymousAuthenticationFilter@15f49be8, org.springframework.security.web.session.SessionManagementFilter@58a27867, org.springframework.security.web.access.ExceptionTranslationFilter@46b947c, org.springframework.security.web.access.intercept.AuthorizationFilter@71792a02]
2025-09-08T12:42:08.747+07:00  INFO 5052 --- [SIM Sekolah Management System] [main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port 8080 (http) with context path ''
2025-09-08T12:42:08.760+07:00  INFO 5052 --- [SIM Sekolah Management System] [main] com.simsekolah.SimBackendApplication     : Started SimBackendApplication in 13.982 seconds (process running for 14.524)
2025-09-08T12:42:08.871+07:00  INFO 5052 --- [SIM Sekolah Management System] [main] com.simsekolah.config.DataInitializer    : Starting data initialization... 
2025-09-08T12:42:08.871+07:00  INFO 5052 --- [SIM Sekolah Management System] [main] com.simsekolah.config.DataInitializer    : Checking system users...        
2025-09-08T12:42:09.000+07:00  INFO 5052 --- [SIM Sekolah Management System] [main] com.simsekolah.config.DataInitializer    : No users found. Creating default admin user...
2025-09-08T12:42:09.272+07:00  INFO 5052 --- [SIM Sekolah Management System] [main] com.simsekolah.config.DataInitializer    : Created default admin user: admin@simsekolah.com/admin123
2025-09-08T12:42:09.272+07:00  INFO 5052 --- [SIM Sekolah Management System] [main] com.simsekolah.config.DataInitializer    : Checking database status...     
2025-09-08T12:42:09.277+07:00  INFO 5052 --- [SIM Sekolah Management System] [main] com.simsekolah.config.DataInitializer    : Database status:
2025-09-08T12:42:09.278+07:00  INFO 5052 --- [SIM Sekolah Management System] [main] com.simsekolah.config.DataInitializer    : - Students: 0
2025-09-08T12:42:09.279+07:00  INFO 5052 --- [SIM Sekolah Management System] [main] com.simsekolah.config.DataInitializer    : No students found. Students can 
be added through the application interface.
2025-09-08T12:42:09.280+07:00  INFO 5052 --- [SIM Sekolah Management System] [main] com.simsekolah.config.DataInitializer    : Data initialization completed successfully
2025-09-08T12:42:13.498+07:00  INFO 5052 --- [SIM Sekolah Management System] [http-nio-8080-exec-1] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring DispatcherServlet 'dispatcherServlet'
2025-09-08T12:42:13.499+07:00  INFO 5052 --- [SIM Sekolah Management System] [http-nio-8080-exec-1] o.s.web.servlet.DispatcherServlet        : Initializing Servlet 'dispatcherServlet'
2025-09-08T12:42:13.501+07:00  INFO 5052 --- [SIM Sekolah Management System] [http-nio-8080-exec-1] o.s.web.servlet.DispatcherServlet        : Completed initialization in 2 ms
2025-09-08T12:42:13.519+07:00 DEBUG 5052 --- [SIM Sekolah Management System] [http-nio-8080-exec-1] o.s.security.web.FilterChainProxy        : Securing GET /actuator/mappings
2025-09-08T12:42:13.532+07:00 DEBUG 5052 --- [SIM Sekolah Management System] [http-nio-8080-exec-1] c.s.security.JwtAuthenticationFilter     : JwtAuthFilter - 
incoming request: GET /actuator/mappings
2025-09-08T12:42:13.533+07:00 DEBUG 5052 --- [SIM Sekolah Management System] [http-nio-8080-exec-1] c.s.security.JwtAuthenticationFilter     : JwtAuthFilter - 
Authorization header: null
2025-09-08T12:42:13.535+07:00 DEBUG 5052 --- [SIM Sekolah Management System] [http-nio-8080-exec-1] o.s.s.w.a.AnonymousAuthenticationFilter  : Set SecurityContextHolder to anonymous SecurityContext
2025-09-08T12:42:13.539+07:00  WARN 5052 --- [SIM Sekolah Management System] [http-nio-8080-exec-1] o.s.w.s.h.HandlerMappingIntrospector     : Cache miss for REQUEST dispatch to '/actuator/mappings' (previous null). Performing MatchableHandlerMapping lookup. This is logged once only at WARN level, and every time at TRACE.
2025-09-08T12:42:13.550+07:00 DEBUG 5052 --- [SIM Sekolah Management System] [http-nio-8080-exec-1] o.s.security.web.FilterChainProxy        : Secured GET /actuator/mappings
2025-09-08T12:43:28.743+07:00 DEBUG 5052 --- [SIM Sekolah Management System] [http-nio-8080-exec-3] o.s.security.web.FilterChainProxy        : Securing GET /actuator/mappings
2025-09-08T12:43:28.745+07:00 DEBUG 5052 --- [SIM Sekolah Management System] [http-nio-8080-exec-3] c.s.security.JwtAuthenticationFilter     : JwtAuthFilter - 
incoming request: GET /actuator/mappings
2025-09-08T12:43:28.745+07:00 DEBUG 5052 --- [SIM Sekolah Management System] [http-nio-8080-exec-3] c.s.security.JwtAuthenticationFilter     : JwtAuthFilter - 
Authorization header: null
2025-09-08T12:43:28.746+07:00 DEBUG 5052 --- [SIM Sekolah Management System] [http-nio-8080-exec-3] o.s.s.w.a.AnonymousAuthenticationFilter  : Set SecurityContextHolder to anonymous SecurityContext
2025-09-08T12:43:28.751+07:00 DEBUG 5052 --- [SIM Sekolah Management System] [http-nio-8080-exec-3] o.s.security.web.FilterChainProxy        : Secured GET /actuator/mappings
2025-09-08T12:43:52.857+07:00 DEBUG 5052 --- [SIM Sekolah Management System] [http-nio-8080-exec-7] o.s.security.web.FilterChainProxy        : Securing GET /actuator/mappings
curity.JwtAuthenticationFilter     : JwtAuthFilter - 
incoming request: GET /actuator/mappings
2025-09-08T12:43:52.860+07:00 DEBUG 5052 --- [SIM Sekolah Management System] [http-nio-8080-exec-7] c.s.security.JwtAuthenticationFilter     : JwtAuthFilter - 
Authorization header: null
2025-09-08T12:43:52.860+07:00 DEBUG 5052 --- [SIM Sekolah Management System] [http-nio-8080-exec-7] o.s.s.w.a.AnonymousAuthenticationFilter  : Set SecurityContextHolder to anonymous SecurityContext
2025-09-08T12:43:52.865+07:00 DEBUG 5052 --- [SIM Sekolah Management System] [http-nio-8080-exec-7] o.s.security.web.FilterChainProxy        : Secured GET /actuator/mappings
PS C:\Users\sija_003\Desktop\SIM_CLONE> Start-Sleep -Seconds 8; Get-Content backend\logs\dev-app.log -Tail 200
2025-09-08T10:40:31.271+07:00  INFO 17092 --- [SIM Sekolah Management System] [main] com.simsekolah.config.DataInitializer    : No students found. Students can be added through the application interface.
2025-09-08T10:40:31.271+07:00  INFO 17092 --- [SIM Sekolah Management System] [main] com.simsekolah.config.DataInitializer    : Data initialization completed successfully
2025-09-08T11:01:09.428+07:00  INFO 17092 --- [SIM Sekolah Management System] [http-nio-8080-exec-1] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring DispatcherServlet 'dispatcherServlet'
2025-09-08T11:01:09.477+07:00  INFO 17092 --- [SIM Sekolah Management System] [http-nio-8080-exec-1] o.s.web.servlet.DispatcherServlet        : Initializing Servlet 'dispatcherServlet'
2025-09-08T11:01:09.768+07:00  INFO 17092 --- [SIM Sekolah Management System] [http-nio-8080-exec-1] o.s.web.servlet.DispatcherServlet        : Completed initialization in 289 ms
2025-09-08T11:01:09.991+07:00 DEBUG 17092 --- [SIM Sekolah Management System] [http-nio-8080-exec-1] o.s.security.web.FilterChainProxy        : Securing POST /api/auth/login
2025-09-08T11:01:10.112+07:00 DEBUG 17092 --- [SIM Sekolah Management System] [http-nio-8080-exec-1] c.s.security.JwtAuthenticationFilter     : JwtAuthFilter - incoming request: POST /api/auth/login
2025-09-08T11:01:10.113+07:00 DEBUG 17092 --- [SIM Sekolah Management System] [http-nio-8080-exec-1] c.s.security.JwtAuthenticationFilter     : JwtAuthFilter - Authorization header: null
2025-09-08T11:01:10.140+07:00 DEBUG 17092 --- [SIM Sekolah Management System] [http-nio-8080-exec-1] o.s.s.w.a.AnonymousAuthenticationFilter  : Set SecurityContextHolder to anonymous SecurityContext
2025-09-08T11:01:10.163+07:00  WARN 17092 --- [SIM Sekolah Management System] [http-nio-8080-exec-1] o.s.w.s.h.HandlerMappingIntrospector     : Cache miss for 
REQUEST dispatch to '/api/auth/login' (previous null). Performing MatchableHandlerMapping lookup. This is 
logged once only at WARN level, and every time at TRACE.
2025-09-08T11:01:10.297+07:00 DEBUG 17092 --- [SIM Sekolah Management System] [http-nio-8080-exec-1] o.s.security.web.FilterChainProxy        : Secured POST /api/auth/login
2025-09-08T11:01:10.530+07:00 DEBUG 17092 --- [SIM Sekolah Management System] [http-nio-8080-exec-1] o.s.security.web.FilterChainProxy        : Securing POST /error
2025-09-08T11:01:10.563+07:00 DEBUG 17092 --- [SIM Sekolah Management System] [http-nio-8080-exec-1] o.s.security.web.FilterChainProxy        : Secured POST /error
2025-09-08T11:01:11.053+07:00 DEBUG 17092 --- [SIM Sekolah Management System] [http-nio-8080-exec-1] o.s.s.w.a.AnonymousAuthenticationFilter  : Set SecurityContextHolder to anonymous SecurityContext
2025-09-08T12:27:26.679+07:00 DEBUG 17092 --- [SIM Sekolah Management System] [http-nio-8080-exec-3] o.s.security.web.FilterChainProxy        : Securing GET /api/auth/status
2025-09-08T12:27:26.693+07:00 DEBUG 17092 --- [SIM Sekolah Management System] [http-nio-8080-exec-3] c.s.security.JwtAuthenticationFilter     : JwtAuthFilter - incoming request: GET /api/auth/status
2025-09-08T12:27:26.693+07:00 DEBUG 17092 --- [SIM Sekolah Management System] [http-nio-8080-exec-3] c.s.security.JwtAuthenticationFilter     : JwtAuthFilter - Authorization header: null
2025-09-08T12:27:26.694+07:00 DEBUG 17092 --- [SIM Sekolah Management System] [http-nio-8080-exec-3] o.s.s.w.a.AnonymousAuthenticationFilter  : Set SecurityContextHolder to anonymous SecurityContext
2025-09-08T12:27:26.712+07:00 DEBUG 17092 --- [SIM Sekolah Management System] [http-nio-8080-exec-3] o.s.security.web.FilterChainProxy        : Secured GET /api/auth/status
2025-09-08T12:27:26.742+07:00 DEBUG 17092 --- [SIM Sekolah Management System] [http-nio-8080-exec-3] o.s.security.web.FilterChainProxy        : Securing GET /error
2025-09-08T12:27:26.747+07:00 DEBUG 17092 --- [SIM Sekolah Management System] [http-nio-8080-exec-3] o.s.security.web.FilterChainProxy        : Secured GET /error
2025-09-08T12:27:26.767+07:00 DEBUG 17092 --- [SIM Sekolah Management System] [http-nio-8080-exec-3] o.s.s.w.a.AnonymousAuthenticationFilter  : Set SecurityContextHolder to anonymous SecurityContext
2025-09-08T12:34:00.047+07:00 DEBUG 17092 --- [SIM Sekolah Management System] [http-nio-8080-exec-5] o.s.security.web.FilterChainProxy        : Securing GET /actuator
2025-09-08T12:34:00.052+07:00 DEBUG 17092 --- [SIM Sekolah Management System] [http-nio-8080-exec-5] c.s.security.JwtAuthenticationFilter     : JwtAuthFilter - incoming request: GET /actuator
2025-09-08T12:34:00.052+07:00 DEBUG 17092 --- [SIM Sekolah Management System] [http-nio-8080-exec-5] c.s.security.JwtAuthenticationFilter     : JwtAuthFilter - Authorization header: null
2025-09-08T12:34:00.054+07:00 DEBUG 17092 --- [SIM Sekolah Management System] [http-nio-8080-exec-5] o.s.s.w.a.AnonymousAuthenticationFilter  : Set SecurityContextHolder to anonymous SecurityContext
2025-09-08T12:34:00.077+07:00 DEBUG 17092 --- [SIM Sekolah Management System] [http-nio-8080-exec-5] o.s.security.web.FilterChainProxy        : Secured GET /actuator
2025-09-08T12:39:18.038+07:00 DEBUG 17092 --- [SIM Sekolah Management System] [http-nio-8080-exec-7] o.s.security.web.FilterChainProxy        : Securing GET /api/v1/auth/status
2025-09-08T12:39:18.041+07:00 DEBUG 17092 --- [SIM Sekolah Management System] [http-nio-8080-exec-7] c.s.security.JwtAuthenticationFilter     : JwtAuthFilter - incoming request: GET /api/v1/auth/status
2025-09-08T12:39:18.044+07:00 DEBUG 17092 --- [SIM Sekolah Management System] [http-nio-8080-exec-7] c.s.security.JwtAuthenticationFilter     : JwtAuthFilter - Authorization header: null
2025-09-08T12:39:18.046+07:00 DEBUG 17092 --- [SIM Sekolah Management System] [http-nio-8080-exec-7] o.s.s.w.a.AnonymousAuthenticationFilter  : Set SecurityContextHolder to anonymous SecurityContext
2025-09-08T12:39:18.056+07:00 DEBUG 17092 --- [SIM Sekolah Management System] [http-nio-8080-exec-7] o.s.security.web.FilterChainProxy        : Secured GET /api/v1/auth/status
2025-09-08T12:39:18.079+07:00 DEBUG 17092 --- [SIM Sekolah Management System] [http-nio-8080-exec-7] o.s.security.web.FilterChainProxy        : Securing GET /error
2025-09-08T12:39:18.082+07:00 DEBUG 17092 --- [SIM Sekolah Management System] [http-nio-8080-exec-7] o.s.security.web.FilterChainProxy        : Secured GET /error
2025-09-08T12:39:18.087+07:00 DEBUG 17092 --- [SIM Sekolah Management System] [http-nio-8080-exec-7] o.s.s.w.a.AnonymousAuthenticationFilter  : Set SecurityContextHolder to anonymous SecurityContext
2025-09-08T12:41:16.112+07:00  INFO 16532 --- [SIM Sekolah Management System] [main] com.simsekolah.SimBackendApplication     : Starting SimBackendApplication 
using Java 21.0.7 with PID 16532 (C:\Users\sija_003\Desktop\SIM_CLONE\backend\target\classes started by sija_003 in C:\Users\sija_003\Desktop\SIM_CLONE\backend)
2025-09-08T12:41:16.115+07:00  INFO 16532 --- [SIM Sekolah Management System] [main] com.simsekolah.SimBackendApplication     : The following 1 profile is active: "dev"
2025-09-08T12:41:18.004+07:00  INFO 16532 --- [SIM Sekolah Management System] [main] .s.d.r.c.RepositoryConfigurationDelegate : Multiple Spring Data modules found, entering strict repository configuration mode   
2025-09-08T12:41:18.007+07:00  INFO 16532 --- [SIM Sekolah Management System] [main] .s.d.r.c.RepositoryConfigurationDelegate : Bootstrapping Spring Data JPA repositories in DEFAULT mode.
2025-09-08T12:41:18.353+07:00  INFO 16532 --- [SIM Sekolah Management System] [main] .s.d.r.c.RepositoryConfigurationDelegate : Finished Spring Data repository scanning in 334 ms. Found 17 JPA repository interfaces.
2025-09-08T12:41:19.555+07:00  INFO 16532 --- [SIM Sekolah Management System] [main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat initialized with port 8080 (http)
2025-09-08T12:41:19.588+07:00  INFO 16532 --- [SIM Sekolah Management System] [main] o.apache.catalina.core.StandardService   : Starting service [Tomcat]      
2025-09-08T12:41:19.589+07:00  INFO 16532 --- [SIM Sekolah Management System] [main] o.apache.catalina.core.StandardEngine    : Starting Servlet engine: [Apache Tomcat/10.1.16]
2025-09-08T12:41:19.703+07:00  INFO 16532 --- [SIM Sekolah Management System] [main] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring embedded WebApplicationContext
2025-09-08T12:41:19.704+07:00  INFO 16532 --- [SIM Sekolah Management System] [main] w.s.c.ServletWebServerApplicationContext : Root WebApplicationContext: initialization completed in 3510 ms
2025-09-08T12:41:19.873+07:00  INFO 16532 --- [SIM Sekolah Management System] [main] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Starting...     
2025-09-08T12:41:20.157+07:00  INFO 16532 --- [SIM Sekolah Management System] [main] com.zaxxer.hikari.pool.HikariPool        : HikariPool-1 - Added connection conn0: url=jdbc:h2:mem:simsekolahdb user=SA
2025-09-08T12:41:20.159+07:00  INFO 16532 --- [SIM Sekolah Management System] [main] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Start completed.2025-09-08T12:41:20.174+07:00  INFO 16532 --- [SIM Sekolah Management System] [main] o.s.b.a.h2.H2ConsoleAutoConfiguration    : H2 console available at '/h2-console'. Database available at 'jdbc:h2:mem:simsekolahdb'
2025-09-08T12:41:20.416+07:00  INFO 16532 --- [SIM Sekolah Management System] [main] o.hibernate.jpa.internal.util.LogHelper  : HHH000204: Processing PersistenceUnitInfo [name: default]
2025-09-08T12:41:20.507+07:00  INFO 16532 --- [SIM Sekolah Management System] [main] org.hibernate.Version                    : HHH000412: Hibernate ORM core version 6.3.1.Final
2025-09-08T12:41:20.562+07:00  INFO 16532 --- [SIM Sekolah Management System] [main] o.h.c.internal.RegionFactoryInitiator    : HHH000026: Second-level cache disabled
2025-09-08T12:41:20.882+07:00  INFO 16532 --- [SIM Sekolah Management System] [main] o.s.o.j.p.SpringPersistenceUnitInfo      : No LoadTimeWeaver setup: ignoring JPA class transformer
2025-09-08T12:41:20.946+07:00  WARN 16532 --- [SIM Sekolah Management System] [main] org.hibernate.orm.deprecation            : HHH90000025: H2Dialect does not need to be specified explicitly using 'hibernate.dialect' (remove the property setting and it will be selected by default)
2025-09-08T12:41:22.671+07:00  INFO 16532 --- [SIM Sekolah Management System] [main] o.h.e.t.j.p.i.JtaPlatformInitiator       : HHH000489: No JTA platform available (set 'hibernate.transaction.jta.platform' to enable JTA platform integration)
2025-09-08T12:41:22.960+07:00  INFO 16532 --- [SIM Sekolah Management System] [main] j.LocalContainerEntityManagerFactoryBean : Initialized JPA EntityManagerFactory for persistence unit 'default'
2025-09-08T12:41:23.455+07:00  INFO 16532 --- [SIM Sekolah Management System] [main] o.s.d.j.r.query.QueryEnhancerFactory     : Hibernate is in classpath; If applicable, HQL parser will be used.
2025-09-08T12:41:24.933+07:00  INFO 16532 --- [SIM Sekolah Management System] [main] com.simsekolah.config.RedisCacheConfig   : Configuring Redis connection to localhost:6379
2025-09-08T12:41:24.981+07:00  INFO 16532 --- [SIM Sekolah Management System] [main] com.simsekolah.config.RedisCacheConfig   : Redis connection factory configured successfully
2025-09-08T12:41:25.435+07:00 DEBUG 16532 --- [SIM Sekolah Management System] [main] c.s.security.JwtAuthenticationFilter     : Filter 'jwtAuthenticationFilter' configured for use
2025-09-08T12:41:25.873+07:00  INFO 16532 --- [SIM Sekolah Management System] [main] com.simsekolah.config.RedisCacheConfig   : Configuring Redis template     
2025-09-08T12:41:25.887+07:00  INFO 16532 --- [SIM Sekolah Management System] [main] com.simsekolah.config.RedisCacheConfig   : Redis template configured successfully
2025-09-08T12:41:25.944+07:00 TRACE 16532 --- [SIM Sekolah Management System] [main] eGlobalAuthenticationAutowiredConfigurer : Eagerly initializing {securityConfig=com.simsekolah.config.SecurityConfig$$SpringCGLIB$$0@162fc7cd}
2025-09-08T12:41:26.573+07:00  INFO 16532 --- [SIM Sekolah Management System] [main] com.simsekolah.config.RedisCacheConfig   : Configuring Redis cache manager2025-09-08T12:41:26.621+07:00  INFO 16532 --- [SIM Sekolah Management System] [main] com.simsekolah.config.RedisCacheConfig   : Redis cache manager configured 
with 25 specific cache configurations
2025-09-08T12:41:28.803+07:00  WARN 16532 --- [SIM Sekolah Management System] [main] JpaBaseConfiguration$JpaWebConfiguration : spring.jpa.open-in-view is enabled by default. Therefore, database queries may be performed during view rendering. Explicitly configure spring.jpa.open-in-view to disable this warning       
2025-09-08T12:41:28.956+07:00 DEBUG 16532 --- [SIM Sekolah Management System] [main] o.s.s.a.h.RoleHierarchyImpl              : setHierarchy() - The following 
role hierarchy was set: ROLE_ADMIN > ROLE_TEACHER    
ROLE_TEACHER > ROLE_STUDENT
ROLE_STUDENT > ROLE_USER
2025-09-08T12:41:28.957+07:00 DEBUG 16532 --- [SIM Sekolah Management System] [main] o.s.s.a.h.RoleHierarchyImpl              : buildRolesReachableInOneStepMap() - From role ROLE_ADMIN one can reach role ROLE_TEACHER in one step.
2025-09-08T12:41:28.959+07:00 DEBUG 16532 --- [SIM Sekolah Management System] [main] o.s.s.a.h.RoleHierarchyImpl              : buildRolesReachableInOneStepMap() - From role ROLE_TEACHER one can reach role ROLE_STUDENT in one step.
2025-09-08T12:41:28.960+07:00 DEBUG 16532 --- [SIM Sekolah Management System] [main] o.s.s.a.h.RoleHierarchyImpl              : buildRolesReachableInOneStepMap() - From role ROLE_STUDENT one can reach role ROLE_USER in one step.
2025-09-08T12:41:28.961+07:00 DEBUG 16532 --- [SIM Sekolah Management System] [main] o.s.s.a.h.RoleHierarchyImpl              : buildRolesReachableInOneOrMoreStepsMap() - From role ROLE_STUDENT one can reach [ROLE_USER] in one or more steps.
2025-09-08T12:41:28.961+07:00 DEBUG 16532 --- [SIM Sekolah Management System] [main] o.s.s.a.h.RoleHierarchyImpl              : buildRolesReachableInOneOrMoreStepsMap() - From role ROLE_ADMIN one can reach [ROLE_STUDENT, ROLE_USER, ROLE_TEACHER] in one or more steps.
2025-09-08T12:41:28.962+07:00 DEBUG 16532 --- [SIM Sekolah Management System] [main] o.s.s.a.h.RoleHierarchyImpl              : buildRolesReachableInOneOrMoreStepsMap() - From role ROLE_TEACHER one can reach [ROLE_STUDENT, ROLE_USER] in one or more steps.
2025-09-08T12:41:55.324+07:00  INFO 5052 --- [SIM Sekolah Management System] [main] com.simsekolah.SimBackendApplication     : Starting SimBackendApplication using Java 21.0.7 with PID 5052 (C:\Users\sija_003\Desktop\SIM_CLONE\backend\target\classes started by sija_003 in C:\Users\sija_003\Desktop\SIM_CLONE\backend) 
2025-09-08T12:41:55.328+07:00  INFO 5052 --- [SIM Sekolah Management System] [main] com.simsekolah.SimBackendApplication     : The following 1 profile is active: "dev"
2025-09-08T12:41:56.951+07:00  INFO 5052 --- [SIM Sekolah Management System] [main] .s.d.r.c.RepositoryConfigurationDelegate : Multiple Spring Data modules found, entering strict repository configuration mode    
2025-09-08T12:41:56.954+07:00  INFO 5052 --- [SIM Sekolah Management System] [main] .s.d.r.c.RepositoryConfigurationDelegate : Bootstrapping Spring Data JPA repositories in DEFAULT mode.
2025-09-08T12:41:57.277+07:00  INFO 5052 --- [SIM Sekolah Management System] [main] .s.d.r.c.RepositoryConfigurationDelegate : Finished Spring Data repository 
scanning in 315 ms. Found 17 JPA repository interfaces.
2025-09-08T12:41:58.625+07:00  INFO 5052 --- [SIM Sekolah Management System] [main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat initialized with port 8080 (http)
2025-09-08T12:41:58.651+07:00  INFO 5052 --- [SIM Sekolah Management System] [main] o.apache.catalina.core.StandardService   : Starting service [Tomcat]       
2025-09-08T12:41:58.652+07:00  INFO 5052 --- [SIM Sekolah Management System] [main] o.apache.catalina.core.StandardEngine    : Starting Servlet engine: [Apache Tomcat/10.1.16]
2025-09-08T12:41:58.751+07:00  INFO 5052 --- [SIM Sekolah Management System] [main] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring embedded WebApplicationContext
2025-09-08T12:41:58.752+07:00  INFO 5052 --- [SIM Sekolah Management System] [main] w.s.c.ServletWebServerApplicationContext : Root WebApplicationContext: initialization completed in 3367 ms
2025-09-08T12:41:58.944+07:00  INFO 5052 --- [SIM Sekolah Management System] [main] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Starting...      
2025-09-08T12:41:59.151+07:00  INFO 5052 --- [SIM Sekolah Management System] [main] com.zaxxer.hikari.pool.HikariPool        : HikariPool-1 - Added connection 
conn0: url=jdbc:h2:mem:simsekolahdb user=SA
2025-09-08T12:41:59.156+07:00  INFO 5052 --- [SIM Sekolah Management System] [main] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Start completed. 
2025-09-08T12:41:59.166+07:00  INFO 5052 --- [SIM Sekolah Management System] [main] o.s.b.a.h2.H2ConsoleAutoConfiguration    : H2 console available at '/h2-console'. Database available at 'jdbc:h2:mem:simsekolahdb'
2025-09-08T12:41:59.397+07:00  INFO 5052 --- [SIM Sekolah Management System] [main] o.hibernate.jpa.internal.util.LogHelper  : HHH000204: Processing PersistenceUnitInfo [name: default]
2025-09-08T12:41:59.462+07:00  INFO 5052 --- [SIM Sekolah Management System] [main] org.hibernate.Version 
                   : HHH000412: Hibernate ORM core version 6.3.1.Final
2025-09-08T12:41:59.500+07:00  INFO 5052 --- [SIM Sekolah Management System] [main] o.h.c.internal.RegionFactoryInitiator    : HHH000026: Second-level cache disabled
2025-09-08T12:41:59.750+07:00  INFO 5052 --- [SIM Sekolah Management System] [main] o.s.o.j.p.SpringPersistenceUnitInfo      : No LoadTimeWeaver setup: ignoring JPA class transformer
2025-09-08T12:41:59.800+07:00  WARN 5052 --- [SIM Sekolah Management System] [main] org.hibernate.orm.deprecation            : HHH90000025: H2Dialect does not 
need to be specified explicitly using 'hibernate.dialect' (remove the property setting and it will be selected by default)
2025-09-08T12:42:01.378+07:00  INFO 5052 --- [SIM Sekolah Management System] [main] o.h.e.t.j.p.i.JtaPlatformInitiator       : HHH000489: No JTA platform available (set 'hibernate.transaction.jta.platform' to enable JTA platform integration)
2025-09-08T12:42:01.548+07:00  INFO 5052 --- [SIM Sekolah Management System] [main] j.LocalContainerEntityManagerFactoryBean : Initialized JPA EntityManagerFactory for persistence unit 'default'
2025-09-08T12:42:01.896+07:00  INFO 5052 --- [SIM Sekolah Management System] [main] o.s.d.j.r.query.QueryEnhancerFactory     : Hibernate is in classpath; If applicable, HQL parser will be used.
2025-09-08T12:42:03.268+07:00  INFO 5052 --- [SIM Sekolah Management System] [main] com.simsekolah.config.RedisCacheConfig   : Configuring Redis connection to 
localhost:6379
2025-09-08T12:42:03.297+07:00  INFO 5052 --- [SIM Sekolah Management System] [main] com.simsekolah.config.RedisCacheConfig   : Redis connection factory configured successfully
2025-09-08T12:42:03.751+07:00 DEBUG 5052 --- [SIM Sekolah Management System] [main] c.s.security.JwtAuthenticationFilter     : Filter 'jwtAuthenticationFilter' configured for use
2025-09-08T12:42:04.167+07:00  INFO 5052 --- [SIM Sekolah Management System] [main] com.simsekolah.config.RedisCacheConfig   : Configuring Redis template      
2025-09-08T12:42:04.179+07:00  INFO 5052 --- [SIM Sekolah Management System] [main] com.simsekolah.config.RedisCacheConfig   : Redis template configured successfully
2025-09-08T12:42:04.236+07:00 TRACE 5052 --- [SIM Sekolah Management System] [main] eGlobalAuthenticationAutowiredConfigurer : Eagerly initializing {securityConfig=com.simsekolah.config.SecurityConfig$$SpringCGLIB$$0@38e3f098}
2025-09-08T12:42:04.754+07:00  INFO 5052 --- [SIM Sekolah Management System] [main] com.simsekolah.config.RedisCacheConfig   : Configuring Redis cache manager 
2025-09-08T12:42:04.768+07:00  INFO 5052 --- [SIM Sekolah Management System] [main] com.simsekolah.config.RedisCacheConfig   : Redis cache manager configured with 25 specific cache configurations
2025-09-08T12:42:06.747+07:00  WARN 5052 --- [SIM Sekolah Management System] [main] JpaBaseConfiguration$JpaWebConfiguration : spring.jpa.open-in-view is enabled by default. Therefore, database queries may be performed during view rendering. Explicitly configure spring.jpa.open-in-view to disable this warning        
2025-09-08T12:42:06.858+07:00 DEBUG 5052 --- [SIM Sekolah Management System] [main] o.s.s.a.h.RoleHierarchyImpl              : setHierarchy() - The following role hierarchy was set: ROLE_ADMIN > ROLE_TEACHER     
ROLE_TEACHER > ROLE_STUDENT
ROLE_STUDENT > ROLE_USER
2025-09-08T12:42:06.859+07:00 DEBUG 5052 --- [SIM Sekolah Management System] [main] o.s.s.a.h.RoleHierarchyImpl              : buildRolesReachableInOneStepMap() - From role ROLE_ADMIN one can reach role ROLE_TEACHER in one step.
2025-09-08T12:42:06.859+07:00 DEBUG 5052 --- [SIM Sekolah Management System] [main] o.s.s.a.h.RoleHierarchyImpl              : buildRolesReachableInOneStepMap() - From role ROLE_TEACHER one can reach role ROLE_STUDENT in one step.
2025-09-08T12:42:06.859+07:00 DEBUG 5052 --- [SIM Sekolah Management System] [main] o.s.s.a.h.RoleHierarchyImpl              : buildRolesReachableInOneStepMap() - From role ROLE_STUDENT one can reach role ROLE_USER in one step.
2025-09-08T12:42:06.860+07:00 DEBUG 5052 --- [SIM Sekolah Management System] [main] o.s.s.a.h.RoleHierarchyImpl              : buildRolesReachableInOneOrMoreStepsMap() - From role ROLE_STUDENT one can reach [ROLE_USER] in one or more steps.
2025-09-08T12:42:06.861+07:00 DEBUG 5052 --- [SIM Sekolah Management System] [main] o.s.s.a.h.RoleHierarchyImpl              : buildRolesReachableInOneOrMoreStepsMap() - From role ROLE_ADMIN one can reach [ROLE_STUDENT, ROLE_USER, ROLE_TEACHER] in one or more steps.
2025-09-08T12:42:06.861+07:00 DEBUG 5052 --- [SIM Sekolah Management System] [main] o.s.s.a.h.RoleHierarchyImpl              : buildRolesReachableInOneOrMoreStepsMap() - From role ROLE_TEACHER one can reach [ROLE_STUDENT, ROLE_USER] in one or more steps.
2025-09-08T12:42:07.474+07:00  INFO 5052 --- [SIM Sekolah Management System] [main] o.s.b.a.e.web.EndpointLinksResolver      : Exposing 3 endpoint(s) beneath base path '/actuator'
2025-09-08T12:42:07.555+07:00  INFO 5052 --- [SIM Sekolah Management System] [main] o.s.s.web.DefaultSecurityFilterChain     : Will secure any request with [org.springframework.security.web.session.DisableEncodeUrlFilter@673e6fc9, org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter@6b3a527, org.springframework.security.web.context.SecurityContextHolderFilter@792af176, org.springframework.security.web.header.HeaderWriterFilter@84a95e5, org.springframework.web.filter.CorsFilter@578e5975, org.springframework.security.web.authentication.logout.LogoutFilter@1e64c9df, com.simsekolah.security.JwtAuthenticationFilter@413c710e, org.springframework.security.web.savedrequest.RequestCacheAwareFilter@512f3e71, org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter@50b2de4e, org.springframework.security.web.authentication.AnonymousAuthenticationFilter@15f49be8, org.springframework.security.web.session.SessionManagementFilter@58a27867, org.springframework.security.web.access.ExceptionTranslationFilter@46b947c, org.springframework.security.web.access.intercept.AuthorizationFilter@71792a02]
2025-09-08T12:42:08.747+07:00  INFO 5052 --- [SIM Sekolah Management System] [main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port 8080 (http) with context path ''
2025-09-08T12:42:08.760+07:00  INFO 5052 --- [SIM Sekolah Management System] [main] com.simsekolah.SimBackendApplication     : Started SimBackendApplication in 13.982 seconds (process running for 14.524)
2025-09-08T12:42:08.871+07:00  INFO 5052 --- [SIM Sekolah Management System] [main] com.simsekolah.config.DataInitializer    : Starting data initialization... 
2025-09-08T12:42:08.871+07:00  INFO 5052 --- [SIM Sekolah Management System] [main] com.simsekolah.config.DataInitializer    : Checking system users...        
2025-09-08T12:42:09.000+07:00  INFO 5052 --- [SIM Sekolah Management System] [main] com.simsekolah.config.DataInitializer    : No users found. Creating default admin user...
2025-09-08T12:42:09.272+07:00  INFO 5052 --- [SIM Sekolah Management System] [main] com.simsekolah.config.DataInitializer    : Created default admin user: admin@simsekolah.com/admin123
2025-09-08T12:42:09.272+07:00  INFO 5052 --- [SIM Sekolah Management System] [main] com.simsekolah.config.DataInitializer    : Checking database status...     
2025-09-08T12:42:09.277+07:00  INFO 5052 --- [SIM Sekolah Management System] [main] com.simsekolah.config.DataInitializer    : Database status:
2025-09-08T12:42:09.278+07:00  INFO 5052 --- [SIM Sekolah Management System] [main] com.simsekolah.config.DataInitializer    : - Students: 0
2025-09-08T12:42:09.279+07:00  INFO 5052 --- [SIM Sekolah Management System] [main] com.simsekolah.config.DataInitializer    : No students found. Students can 
be added through the application interface.
2025-09-08T12:42:09.280+07:00  INFO 5052 --- [SIM Sekolah Management System] [main] com.simsekolah.config.DataInitializer    : Data initialization completed successfully
2025-09-08T12:42:13.498+07:00  INFO 5052 --- [SIM Sekolah Management System] [http-nio-8080-exec-1] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring DispatcherServlet 'dispatcherServlet'
2025-09-08T12:42:13.499+07:00  INFO 5052 --- [SIM Sekolah Management System] [http-nio-8080-exec-1] o.s.web.servlet.DispatcherServlet        : Initializing Servlet 'dispatcherServlet'
2025-09-08T12:42:13.501+07:00  INFO 5052 --- [SIM Sekolah Management System] [http-nio-8080-exec-1] o.s.web.servlet.DispatcherServlet        : Completed initialization in 2 ms
2025-09-08T12:42:13.519+07:00 DEBUG 5052 --- [SIM Sekolah Management System] [http-nio-8080-exec-1] o.s.security.web.FilterChainProxy        : Securing GET /actuator/mappings
2025-09-08T12:42:13.532+07:00 DEBUG 5052 --- [SIM Sekolah Management System] [http-nio-8080-exec-1] c.s.security.JwtAuthenticationFilter     : JwtAuthFilter - 
incoming request: GET /actuator/mappings
2025-09-08T12:42:13.533+07:00 DEBUG 5052 --- [SIM Sekolah Management System] [http-nio-8080-exec-1] c.s.security.JwtAuthenticationFilter     : JwtAuthFilter - 
Authorization header: null
2025-09-08T12:42:13.535+07:00 DEBUG 5052 --- [SIM Sekolah Management System] [http-nio-8080-exec-1] o.s.s.w.a.AnonymousAuthenticationFilter  : Set SecurityContextHolder to anonymous SecurityContext
2025-09-08T12:42:13.539+07:00  WARN 5052 --- [SIM Sekolah Management System] [http-nio-8080-exec-1] o.s.w.s.h.HandlerMappingIntrospector     : Cache miss for REQUEST dispatch to '/actuator/mappings' (previous null). Performing MatchableHandlerMapping lookup. This is logged once only at WARN level, and every time at TRACE.
2025-09-08T12:42:13.550+07:00 DEBUG 5052 --- [SIM Sekolah Management System] [http-nio-8080-exec-1] o.s.security.web.FilterChainProxy        : Secured GET /actuator/mappings
2025-09-08T12:43:28.743+07:00 DEBUG 5052 --- [SIM Sekolah Management System] [http-nio-8080-exec-3] o.s.security.web.FilterChainProxy        : Securing GET /actuator/mappings
2025-09-08T12:43:28.745+07:00 DEBUG 5052 --- [SIM Sekolah Management System] [http-nio-8080-exec-3] c.s.security.JwtAuthenticationFilter     : JwtAuthFilter - 
incoming request: GET /actuator/mappings
2025-09-08T12:43:28.745+07:00 DEBUG 5052 --- [SIM Sekolah Management System] [http-nio-8080-exec-3] c.s.security.JwtAuthenticationFilter     : JwtAuthFilter - 
Authorization header: null
2025-09-08T12:43:28.746+07:00 DEBUG 5052 --- [SIM Sekolah Management System] [http-nio-8080-exec-3] o.s.s.w.a.AnonymousAuthenticationFilter  : Set SecurityContextHolder to anonymous SecurityContext
2025-09-08T12:43:28.751+07:00 DEBUG 5052 --- [SIM Sekolah Management System] [http-nio-8080-exec-3] o.s.security.web.FilterChainProxy        : Secured GET /actuator/mappings
2025-09-08T12:43:52.857+07:00 DEBUG 5052 --- [SIM Sekolah Management System] [http-nio-8080-exec-7] o.s.security.web.FilterChainProxy        : Securing GET /actuator/mappings
2025-09-08T12:43:52.859+07:00 DEBUG 5052 --- [SIM Sekolah Management System] [http-nio-8080-exec-7] c.s.security.JwtAuthenticationFilter     : JwtAuthFilter - 
incoming request: GET /actuator/mappings
2025-09-08T12:43:52.860+07:00 DEBUG 5052 --- [SIM Sekolah Management System] [http-nio-8080-exec-7] c.s.security.JwtAuthenticationFilter     : JwtAuthFilter - 
Authorization header: null
2025-09-08T12:43:52.860+07:00 DEBUG 5052 --- [SIM Sekolah Management System] [http-nio-8080-exec-7] o.s.s.w.a.AnonymousAuthenticationFilter  : Set SecurityContextHolder to anonymous SecurityContext
2025-09-08T12:43:52.865+07:00 DEBUG 5052 --- [SIM Sekolah Management System] [http-nio-8080-exec-7] o.s.security.web.FilterChainProxy        : Secured GET /actuator/mappings
2025-09-08T12:47:57.033+07:00  INFO 12324 --- [SIM Sekolah Management System] [main] com.simsekolah.SimBackendApplication     : Starting SimBackendApplication 
using Java 21.0.7 with PID 12324 (C:\Users\sija_003\Desktop\SIM_CLONE\backend\target\classes started by sija_003 in C:\Users\sija_003\Desktop\SIM_CLONE\backend)
2025-09-08T12:47:57.035+07:00  INFO 12324 --- [SIM Sekolah Management System] [main] com.simsekolah.SimBackendApplication     : The following 1 profile is active: "dev"
2025-09-08T12:47:58.791+07:00  INFO 12324 --- [SIM Sekolah Management System] [main] .s.d.r.c.RepositoryConfigurationDelegate : Multiple Spring Data modules found, entering strict repository configuration mode   
2025-09-08T12:47:58.795+07:00  INFO 12324 --- [SIM Sekolah Management System] [main] .s.d.r.c.RepositoryConfigurationDelegate : Bootstrapping Spring Data JPA repositories in DEFAULT mode.
2025-09-08T12:47:59.160+07:00  INFO 12324 --- [SIM Sekolah Management System] [main] .s.d.r.c.RepositoryConfigurationDelegate : Finished Spring Data repository scanning in 346 ms. Found 17 JPA repository interfaces.
2025-09-08T12:48:00.362+07:00  INFO 12324 --- [SIM Sekolah Management System] [main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat initialized with port 8080 (http)
2025-09-08T12:48:00.391+07:00  INFO 12324 --- [SIM Sekolah Management System] [main] o.apache.catalina.core.StandardService   : Starting service [Tomcat]      
2025-09-08T12:48:00.392+07:00  INFO 12324 --- [SIM Sekolah Management System] [main] o.apache.catalina.core.StandardEngine    : Starting Servlet engine: [Apache Tomcat/10.1.16]
2025-09-08T12:48:00.494+07:00  INFO 12324 --- [SIM Sekolah Management System] [main] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring embedded WebApplicationContext
2025-09-08T12:48:00.495+07:00  INFO 12324 --- [SIM Sekolah Management System] [main] w.s.c.ServletWebServerApplicationContext : Root WebApplicationContext: initialization completed in 3368 ms
2025-09-08T12:48:00.665+07:00  INFO 12324 --- [SIM Sekolah Management System] [main] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Starting...     
2025-09-08T12:48:00.959+07:00  INFO 12324 --- [SIM Sekolah Management System] [main] com.zaxxer.hikari.pool.HikariPool        : HikariPool-1 - Added connection conn0: url=jdbc:h2:mem:simsekolahdb user=SA
2025-09-08T12:48:00.961+07:00  INFO 12324 --- [SIM Sekolah Management System] [main] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Start completed.2025-09-08T12:48:00.973+07:00  INFO 12324 --- [SIM Sekolah Management System] [main] o.s.b.a.h2.H2ConsoleAutoConfiguration    : H2 console available at '/h2-console'. Database available at 'jdbc:h2:mem:simsekolahdb'
2025-09-08T12:48:01.204+07:00  INFO 12324 --- [SIM Sekolah Management System] [main] o.hibernate.jpa.internal.util.LogHelper  : HHH000204: Processing PersistenceUnitInfo [name: default]
2025-09-08T12:48:01.289+07:00  INFO 12324 --- [SIM Sekolah Management System] [main] org.hibernate.Version                    : HHH000412: Hibernate ORM core version 6.3.1.Final
2025-09-08T12:48:01.374+07:00  INFO 12324 --- [SIM Sekolah Management System] [main] o.h.c.internal.RegionFactoryInitiator    : HHH000026: Second-level cache disabled
2025-09-08T12:48:01.688+07:00  INFO 12324 --- [SIM Sekolah Management System] [main] o.s.o.j.p.SpringPersistenceUnitInfo      : No LoadTimeWeaver setup: ignoring JPA class transformer
2025-09-08T12:48:01.743+07:00  WARN 12324 --- [SIM Sekolah Management System] [main] org.hibernate.orm.deprecation            : HHH90000025: H2Dialect does not need to be specified explicitly using 'hibernate.dialect' (remove the property setting and it will be selected by default)
2025-09-08T12:48:03.533+07:00  INFO 12324 --- [SIM Sekolah Management System] [main] o.h.e.t.j.p.i.JtaPlatformInitiator       : HHH000489: No JTA platform available (set 'hibernate.transaction.jta.platform' to enable JTA platform integration)
2025-09-08T12:48:03.703+07:00  INFO 12324 --- [SIM Sekolah Management System] [main] j.LocalContainerEntityManagerFactoryBean : Initialized JPA EntityManagerFactory for persistence unit 'default'
2025-09-08T12:48:04.095+07:00  INFO 12324 --- [SIM Sekolah Management System] [main] o.s.d.j.r.query.QueryEnhancerFactory     : Hibernate is in classpath; If applicable, HQL parser will be used.
2025-09-08T12:48:05.636+07:00  INFO 12324 --- [SIM Sekolah Management System] [main] com.simsekolah.config.RedisCacheConfig   : Configuring Redis connection to localhost:6379
2025-09-08T12:48:05.665+07:00  INFO 12324 --- [SIM Sekolah Management System] [main] com.simsekolah.config.RedisCacheConfig   : Redis connection factory configured successfully
2025-09-08T12:48:06.047+07:00 DEBUG 12324 --- [SIM Sekolah Management System] [main] c.s.security.JwtAuthenticationFilter     : Filter 'jwtAuthenticationFilter' configured for use
2025-09-08T12:48:06.488+07:00  INFO 12324 --- [SIM Sekolah Management System] [main] com.simsekolah.config.RedisCacheConfig   : Configuring Redis template     
2025-09-08T12:48:06.497+07:00  INFO 12324 --- [SIM Sekolah Management System] [main] com.simsekolah.config.RedisCacheConfig   : Redis template configured successfully
2025-09-08T12:48:06.550+07:00 TRACE 12324 --- [SIM Sekolah Management System] [main] eGlobalAuthenticationAutowiredConfigurer : Eagerly initializing {securityConfig=com.simsekolah.config.SecurityConfig$$SpringCGLIB$$0@2ca18d8a}
2025-09-08T12:48:07.126+07:00  INFO 12324 --- [SIM Sekolah Management System] [main] com.simsekolah.config.RedisCacheConfig   : Configuring Redis cache manager2025-09-08T12:48:07.143+07:00  INFO 12324 --- [SIM Sekolah Management System] [main] com.simsekolah.config.RedisCacheConfig   : Redis cache manager configured 
with 25 specific cache configurations
2025-09-08T12:48:08.917+07:00  WARN 12324 --- [SIM Sekolah Management System] [main] JpaBaseConfiguration$JpaWebConfiguration : spring.jpa.open-in-view is enabled by default. Therefore, database queries may be performed during view rendering. Explicitly configure spring.jpa.open-in-view to disable this warning       
2025-09-08T12:48:09.022+07:00 DEBUG 12324 --- [SIM Sekolah Management System] [main] o.s.s.a.h.RoleHierarchyImpl              : setHierarchy() - The following 
role hierarchy was set: ROLE_ADMIN > ROLE_TEACHER    
ROLE_TEACHER > ROLE_STUDENT
ROLE_STUDENT > ROLE_USER
2025-09-08T12:48:09.023+07:00 DEBUG 12324 --- [SIM Sekolah Management System] [main] o.s.s.a.h.RoleHierarchyImpl              : buildRolesReachableInOneStepMap() - From role ROLE_ADMIN one can reach role ROLE_TEACHER in one step.
2025-09-08T12:48:09.024+07:00 DEBUG 12324 --- [SIM Sekolah Management System] [main] o.s.s.a.h.RoleHierarchyImpl              : buildRolesReachableInOneStepMap() - From role ROLE_TEACHER one can reach role ROLE_STUDENT in one step.
2025-09-08T12:48:09.024+07:00 DEBUG 12324 --- [SIM Sekolah Management System] [main] o.s.s.a.h.RoleHierarchyImpl              : buildRolesReachableInOneStepMap() - From role ROLE_STUDENT one can reach role ROLE_USER in one step.
2025-09-08T12:48:09.025+07:00 DEBUG 12324 --- [SIM Sekolah Management System] [main] o.s.s.a.h.RoleHierarchyImpl              : buildRolesReachableInOneOrMoreStepsMap() - From role ROLE_STUDENT one can reach [ROLE_USER] in one or more steps.
2025-09-08T12:48:09.025+07:00 DEBUG 12324 --- [SIM Sekolah Management System] [main] o.s.s.a.h.RoleHierarchyImpl              : buildRolesReachableInOneOrMoreStepsMap() - From role ROLE_ADMIN one can reach [ROLE_STUDENT, ROLE_USER, ROLE_TEACHER] in one or more steps.
2025-09-08T12:48:09.025+07:00 DEBUG 12324 --- [SIM Sekolah Management System] [main] o.s.s.a.h.RoleHierarchyImpl              : buildRolesReachableInOneOrMoreStepsMap() - From role ROLE_TEACHER one can reach [ROLE_STUDENT, ROLE_USER] in one or more steps.
2025-09-08T12:48:09.556+07:00  INFO 12324 --- [SIM Sekolah Management System] [main] o.s.b.a.e.web.EndpointLinksResolver      : Exposing 3 endpoint(s) beneath 
base path '/actuator'
2025-09-08T12:48:09.634+07:00  INFO 12324 --- [SIM Sekolah Management System] [main] o.s.s.web.DefaultSecurityFilterChain     : Will secure any request with [org.springframework.security.web.session.DisableEncodeUrlFilter@4aff20e2, org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter@67c42f9d, org.springframework.security.web.context.SecurityContextHolderFilter@7599639, org.springframework.security.web.header.HeaderWriterFilter@68bc8942, 
org.springframework.web.filter.CorsFilter@43e8334a, org.springframework.security.web.authentication.logout.LogoutFilter@6257903, com.simsekolah.security.JwtAuthenticationFilter@40a772db, org.springframework.security.web.savedrequest.RequestCacheAwareFilter@60916780, org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestFilter@62f15824, org.springframework.security.web.authentication.AnonymousAuthenticationFilter@a7f78eb, org.springframework.security.web.session.SessionManagementFilter@72c46a75, org.springframework.security.web.access.ExceptionTranslationFilter@4d59fb29, org.springframework.security.web.access.intercept.AuthorizationFilter@7ba46cd1]
2025-09-08T12:48:10.827+07:00  INFO 12324 --- [SIM Sekolah Management System] [main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port 8080 (http) with context path ''
2025-09-08T12:48:10.839+07:00  INFO 12324 --- [SIM Sekolah Management System] [main] com.simsekolah.SimBackendApplication     : Started SimBackendApplication in 14.389 seconds (process running for 15.037)        
2025-09-08T12:48:10.950+07:00  INFO 12324 --- [SIM Sekolah Management System] [main] com.simsekolah.config.DataInitializer    : Starting data initialization...2025-09-08T12:48:10.950+07:00  INFO 12324 --- [SIM Sekolah Management System] [main] com.simsekolah.config.DataInitializer    : Checking system users...       
2025-09-08T12:48:11.098+07:00  INFO 12324 --- [SIM Sekolah Management System] [main] com.simsekolah.config.DataInitializer    : No users found. Creating default admin user...
2025-09-08T12:48:11.376+07:00  INFO 12324 --- [SIM Sekolah Management System] [main] com.simsekolah.config.DataInitializer    : Created default admin user: admin@simsekolah.com/admin123
2025-09-08T12:48:11.376+07:00  INFO 12324 --- [SIM Sekolah Management System] [main] com.simsekolah.config.DataInitializer    : Checking database status...    
kolah Management System] [main] com.simsekolah.config.DataInitializer    : Database status:
2025-09-08T12:48:11.380+07:00  INFO 12324 --- [SIM Se.DataInitializer    : - Students: 0
2025-09-08T12:48:11.381+07:00  INFO 12324 --- [SIM Sekolah Management System] [main] com.simsekolah.config be added through the application interface.
.DataInitializer    : Data initialization completed successfully  
PS C:\Users\sija_003\Desktop\SIM_CLONE> Get-Content backend\logs\dev-app.log -Tail 400 | Select-String -Pattern "AuthController class loaded|Bean: .*auth|Bean: .*Auth|AuthController|Bean:" -AllMatches
PS C:\Users\sija_003\Desktop\SIM_CLONE> Get-Content backend\logs\dev-app.log -Tail 400 | Select-String -Pattern "AuthController class loaded|Bean: .*auth|Bean: .*Auth|AuthController" -AllMatches -CaseSensitive
PS C:\Users\sija_003\Desktop\SIM_CLONE> Get-Content backend\logs\dev-app.log -Tail 800 | Select-String -Pattern "AuthController class loaded|Bean: .*auth|Bean: .*Auth|AuthController" -AllMatches -CaseSensitive
PS C:\Users\sija_003\Desktop\SIM_CLONE> cd c:\Users\sija_003\Desktop\SIM_CLONE\backend; mvn -DskipTests package -q
PS C:\Users\sija_003\Desktop\SIM_CLONE\backend>