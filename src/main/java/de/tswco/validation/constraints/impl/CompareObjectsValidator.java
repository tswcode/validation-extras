package de.tswco.validation.constraints.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder;
import javax.validation.ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderDefinedContext;

import de.tswco.validation.constraints.CompareObjects;
import de.tswco.validation.constraints.ComparisonMode;

public class CompareObjectsValidator implements ConstraintValidator<CompareObjects, Object> {

	private Collection<String> propertyNames;
	private Collection<String> errorPropertyNames;
	private Class<?> propertyClass;
	private ComparisonMode comparisonMode;
	private boolean allowNull;
	private boolean allowEmpty;
	
	@Override
	public void initialize(CompareObjects constraintAnnotation) {
		this.propertyNames  = Arrays.asList(constraintAnnotation.propertyNames());
		this.errorPropertyNames  = Arrays.asList(constraintAnnotation.errorPropertyNames());
		this.propertyClass  = constraintAnnotation.propertyClass();
		this.comparisonMode = constraintAnnotation.matchMode();
		this.allowNull      = constraintAnnotation.allowNull();
		this.allowEmpty	    = constraintAnnotation.allowEmpty();
	}

	@Override
	public boolean isValid(Object target, ConstraintValidatorContext context) {
		boolean isValid = true;
		
		List<Object> propertyValues = new ArrayList<>(propertyNames.size());
		for(String propertyName: propertyNames) {
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
			
			for(String propertyName: errorPropertyNames) {
				if(message.isEmpty()) {
					message = String.format(resourceMessageFormat, 
							CompareObjects.class.getSimpleName(),
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
