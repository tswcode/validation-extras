package de.tswco.validation.constraints;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import de.tswco.validation.constraints.impl.CompareObjectsValidator;

import static java.lang.annotation.ElementType.TYPE;

@Documented
@Constraint(validatedBy = CompareObjectsValidator.class)
@Target({TYPE})
@Retention(RUNTIME)
public @interface CompareObjects {
	String[] propertyNames();
	Class<?> propertyClass() default String.class;
	String[] errorPropertyNames();
	ComparisonMode matchMode() default ComparisonMode.EQUAL;
	boolean allowNull() default false;
	boolean allowEmpty() default false;
	String message() default "";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
}
