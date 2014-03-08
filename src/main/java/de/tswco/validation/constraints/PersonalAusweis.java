package de.tswco.validation.constraints;

import de.tswco.validation.constraints.impl.PersonalAusweisValidator;
import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

/**
 *
 * @author watzke
 */
@Target( { METHOD, FIELD, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = PersonalAusweisValidator.class)
@Documented
public @interface PersonalAusweis {
    String message() default "Personalausweis-Nummer ist ungültig";
    
    public Class<?>[] groups() default {};

    public Class<? extends Payload>[] payload() default {};
}
