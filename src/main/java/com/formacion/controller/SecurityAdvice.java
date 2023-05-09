package com.formacion.controller;

import com.formacion.model.CustomerView;
import com.formacion.model.Role;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.AbstractMappingJacksonResponseBodyAdvice;

import java.util.Collection;

@RestControllerAdvice
//Para gestionar la respuesta que vamos a devolver de manera global
//Solo para el customer, pero se puede configurar para otras clases y para otros DTOS
public class SecurityAdvice  extends AbstractMappingJacksonResponseBodyAdvice {

    @Override
    protected void beforeBodyWriteInternal(
            MappingJacksonValue bodyContainer,
            MediaType contentType,
            MethodParameter returnType,
            ServerHttpRequest request,
            ServerHttpResponse response) {

        //sino está logado no hace nada
        if (SecurityContextHolder.getContext().getAuthentication() == null) return;
        //sino tienen Authorities los usuarios tampoco puedes comprobar nada
        if (SecurityContextHolder.getContext().getAuthentication().getAuthorities() == null) return;
        //Aquí ya hemos comprobado que está autenticado y hay authorities (basicamente rol) cargadas

        //es una forma generics, collección de objetos que extienden de GrantedAuthority
        Collection<? extends GrantedAuthority> authorities =
                SecurityContextHolder.getContext()
                        .getAuthentication().getAuthorities();


        Class jsonView = authorities.stream()
                .map(GrantedAuthority::getAuthority)//esto me devuelve el string que hemos definido en roles del User
                .map(Role::valueOf)//A partir del string lo trasformo en mi enum Role (el ROLE_ se pone automaticamente en spring)
                .map(CustomerView.ROLE_VIEWS::get)//Para el Role le saco la View que tiene ese role configurada
                .findFirst().orElseThrow(); //Me saca la unica view que tiene que tener o excepción sino tiene rol valido o vista configurada para ese rol


        bodyContainer.setSerializationView(jsonView);
    }
}
