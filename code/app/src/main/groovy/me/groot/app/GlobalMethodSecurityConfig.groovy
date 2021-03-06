package me.groot.app

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.security.access.PermissionEvaluator
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration

@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
class GlobalMethodSecurityConfig extends GlobalMethodSecurityConfiguration {

    @Autowired
    PermissionEvaluator permissionEvaluator
    /**
     * Provide a {@link MethodSecurityExpressionHandler} that is registered with the
     * {@link ExpressionBasedPreInvocationAdvice}. The default is
     * {@link DefaultMethodSecurityExpressionHandler} which optionally will Autowire an
     * {@link AuthenticationTrustResolver}.
     *
     * <p>
     * Subclasses may override this method to provide a custom
     * {@link MethodSecurityExpressionHandler}
     * </p>
     *
     * @return
     */
    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {

        DefaultMethodSecurityExpressionHandler handler = new DefaultMethodSecurityExpressionHandler()
        handler.permissionEvaluator = permissionEvaluator
        return handler
    }
}
