package de.tswco.validation.constraints.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder;
import javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderDefinedContext;

import de.tswco.validation.constraints.Compare;
import de.tswco.validation.constraints.ComparisonMode;

public class CompareValidator implements ConstraintValidator<Compare, Object> {

	private Collection<String> attributes;
	private Collection<String> errorAttributes;
	private Class<?> propertyClass;
	private ComparisonMode comparisonMode;
	private boolean allowNull;
	private boolean allowEmpty;
	
	@Override
	public void initialize(Compare constraintAnnotation) {
		this.attributes  = Arrays.asList(constraintAnnotation.attributes());
		this.errorAttributes  = Arrays.asList(constraintAnnotation.errorAttributes());
		this.propertyClass  = constraintAnnotation.propertyClass();
		this.comparisonMode = constraintAnnotation.matchMode();
		this.allowNull      = constraintAnnotation.allowNull();
		this.allowEmpty	    = constraintAnnotation.allowEmpty();
	}

	@Override
	public boolean isValid(Object target, ConstraintValidatorContext context) {
		boolean isValid = true;
		
		List<Object> propertyValues = new ArrayList<>(attributes.size());
		for(String propertyName: attributes) {
			Object propertyValue = ConstraintValidatorHelper.getPropertyValue(propertyClass, propertyName, target);
			
			if((propertyValue == null) && (!allowNull)) {
				isValid = false;
				break;
			} else if(String.class.isInstance(propertyValue) && (!allowEmpty) && ((String) propertyValue).isEmpty()) {
				isValid = false;
				break;
			} else {
				propertyValues.add(propertyValue);
			}
		}
		
		if(isValid) {
			isValid = ConstraintValidatorHelper.isValid(propertyValues, comparisonMode);
		} 
		
		if(!isValid) {
			/*
			 * if custom message was provided, don't touch it, 
			 * otherwise build the default message
			 */
			String message = context.getDefaultConstraintMessageTemplate();
			String resourceMessageFormat = "{%s.%s.%s}";
					
			context.disableDefaultConstraintViolation();
			String targetClassName = target.getClass().getSimpleName();
			targetClassName = targetClassName.substring(0, 1).toLowerCase() + targetClassName.substring(1); 
			
			for(String propertyName: errorAttributes) {
				if(message.isEmpty()) {
					message = String.format(resourceMessageFormat, 
							Compare.class.getSimpleName(),
							targetClassName,
							propertyName);
				}
				ConstraintViolationBuilder violationBuilder = context.buildConstraintViolationWithTemplate(message);
				NodeBuilderDefinedContext nbdc = violationBuilder.addNode(propertyName);
				nbdc.addConstraintViolation();
			}
		}
		
		return isValid;
	}

}
