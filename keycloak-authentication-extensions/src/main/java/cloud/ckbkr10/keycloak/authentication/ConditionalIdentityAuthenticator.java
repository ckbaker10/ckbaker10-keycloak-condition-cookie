package cloud.ckbkr10.keycloak.authentication;

import org.jboss.logging.Logger;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.AuthenticationFlowError;
import org.keycloak.authentication.AuthenticationFlowException;
import org.keycloak.authentication.authenticators.conditional.ConditionalAuthenticator;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;

// https://www.keycloak.org/docs-api/26.0.1/javadocs/index.html
// https://github.com/keycloak/keycloak/blob/26.1.2/services/src/main/java/org/keycloak/authentication/authenticators/browser/CookieAuthenticator.java
// https://github.com/keycloak/keycloak/discussions/32584

// https://github.com/keycloak/keycloak/pull/7462/commits/091579ab357c00d9ed1a7becc2ae1aeb89f1ac69#diff-74d505a6cdcb86aebd2aadda9e583e39280ebe8b0601a71df7fdac8c4446430a
// Alte Cookie Methode
// https://github.com/keycloak/keycloak/blob/22.0.9/services/src/main/java/org/keycloak/services/util/CookieHelper.java

import java.util.Map;

public class ConditionalIdentityAuthenticator implements ConditionalAuthenticator{

    static final ConditionalIdentityAuthenticator SINGLETON = new ConditionalIdentityAuthenticator();

    private static final Logger logger = Logger.getLogger(ConditionalIdentityAuthenticator.class);

    @Override
    public boolean matchCondition(AuthenticationFlowContext context) {
        if (context == null){
            throw new AuthenticationFlowException("context is null", AuthenticationFlowError.INTERNAL_ERROR);
        }
        boolean negateOutput = false;

        var authenticatorConfig = context.getAuthenticatorConfig();
        if (authenticatorConfig != null){
            Map<String, String> config = authenticatorConfig.getConfig();
            if (config != null) {
                negateOutput = Boolean.parseBoolean(config.get(ConditionalIdentityAuthenticatorFactory.CONF_NOT));
            }
        }

        var result = getConditionValue(context);

        var fullResult = negateOutput != result;

        logger.debug("ConditionalIdentityAuthenticator result: " + fullResult);
        return fullResult;
    }

    private boolean getConditionValue(AuthenticationFlowContext context){
        var currentSession = context.getSession();
        // https://github.com/keycloak/keycloak/blob/22.0.9/services/src/main/java/org/keycloak/services/util/CookieHelper.java#L106
        var cookieName = "ConditionalCookie";
        var httpHeaders = currentSession.getContext().getHttpRequest().getHttpHeaders();
        var cookie = httpHeaders.getCookies().get(cookieName);
        var conditionalResult = false;
        if (cookie != null) {
            conditionalResult = true;
        }
        return conditionalResult;
    }

    @Override
    public void action(AuthenticationFlowContext authenticationFlowContext) {
        //Not used
    }

    @Override
    public boolean requiresUser() {
        return true;
    }

    @Override
    public void setRequiredActions(KeycloakSession keycloakSession, RealmModel realmModel, UserModel userModel) {
        //Not used
    }

    @Override
    public void close() {
        //Does nothing
    }
}
