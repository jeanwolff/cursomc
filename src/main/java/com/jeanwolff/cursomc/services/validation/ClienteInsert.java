package com.jeanwolff.cursomc.services.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import com.jeanwolff.cursomc.util.constants.ConstantsMessages;

@Constraint(validatedBy = ClienteInsertValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ClienteInsert {
	
	String message() default ConstantsMessages.ERRO_VALIDACAO;

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};
}