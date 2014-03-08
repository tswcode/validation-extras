package de.tswco.validation.constraints.impl;

import de.tswco.validation.constraints.PersonalAusweis;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 *
 * @author watzke
 */
public class PersonalAusweisValidator implements ConstraintValidator<PersonalAusweis, String> {

    @Override
    public void initialize(PersonalAusweis constraintAnnotation) {}

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(value.length() != 10) {
            return false;
        }
        
        String idNr = value.toUpperCase();
        System.out.println(String.format("nr: %s", idNr));
        
        int   blockIndex = 0;
        int[] multiplicator = new int[]{ 7, 3, 1 };
        int   sum = 0;
        for(int i = 0; i < idNr.length()-1; i++) {
            sum += Character.getNumericValue(idNr.charAt(i)) * multiplicator[blockIndex];
            blockIndex = blockIndex == 2 ? 0 : blockIndex + 1;
        }
        
        if((sum % 10) == Character.getNumericValue(idNr.charAt(idNr.length()-1))) {
            return true;
        }
        
        return false;
    }

}
