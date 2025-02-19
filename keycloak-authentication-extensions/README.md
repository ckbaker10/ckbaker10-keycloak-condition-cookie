# Keycloak extension: conditional authenticator to check if the user has SSO configured
This is a sample keycloak extension that provides the condition in the authentication flow to check whether the user has single sign-on (linked identity provider) configured. See the [blog post](https://karelics.fi/building-a-custom-conditional-authenticator-for-keycloak) for a detailed description.

# Usage
Set the correct keycloak version in the `pom.xml` file.
```
<keycloak.version>20.0.5</keycloak.version>
```
To build the package, execute `mvn package` command, which creates `target/deploy/authentication-extensions-module-0.1.jar` package file.

To deploy the package to keycloak, place it in the `/opt/keycloak/providers` directory of the Keycloak and execute `build` or `start` command of Keycloak.

After deployment, the condition "Condition - SSO configured" becomes available in the conditions of the authentication flows in the Keycloak administration panel.
