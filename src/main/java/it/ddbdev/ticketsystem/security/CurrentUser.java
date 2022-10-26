package it.ddbdev.ticketsystem.security;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

@Target({ElementType.PARAMETER, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@AuthenticationPrincipal
/**
 * Annotation that is used to resolve {@link Authentication#getPrincipal()} to a method. Provides core user information.
 *
 * @return {@link UserPrincipal}
 * @see org.springframework.security.core.userdetails.UserDetails UserDetails
 *
 * @author Adelchi Valenti
 */
public @interface CurrentUser {
}

