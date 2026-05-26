package br.com.project.hydroflow.security.annotation;

import br.com.project.hydroflow.security.Permissions;
import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// Acesso para ADMIN ou permissão específica
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasAuthority('" + Permissions.ADMIN + "') or hasAuthority('" + Permissions.EDIT_FAMILY + "')")
public @interface AdminOrEditFamily {}
