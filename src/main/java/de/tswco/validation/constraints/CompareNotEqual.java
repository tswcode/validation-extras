package de.tswco.validation.constraints;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import de.tswco.validation.constraints.impl.CompareNotEqualValidator;

import static java.lang.annotation.ElementType.TYPE;

@Documented
@Constraint(validatedBy = CompareNotEqualValidator.class)
@Target({TYPE})
@Retention(RUNTIME)
public @interface CompareNotEqual {
	String[] attributes();
	Class<?> propertyClass() default String.class;
	String[] errorAttributes();
	ComparisonMode matchMode() default ComparisonMode.NOT_EQUAL;
	boolean allowNull() default false;
	boolean allowEmpty() default false;
	String message() default "";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
}
