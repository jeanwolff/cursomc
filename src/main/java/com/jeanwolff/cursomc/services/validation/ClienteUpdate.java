package com.jeanwolff.cursomc.services.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.jeanwolff.cursomc.ConstantsMessages;

@Constraint(validatedBy = ClienteUpdateValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ClienteUpdate {
	
	String message() default ConstantsMessages.ERRO_VALIDACAO;

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}