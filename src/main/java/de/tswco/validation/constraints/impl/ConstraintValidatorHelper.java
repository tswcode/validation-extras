package de.tswco.validation.constraints.impl;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import de.tswco.validation.constraints.ComparisonMode;

public abstract class ConstraintValidatorHelper {
	
	private final static String ARG_EXCEPTION_MSG = "Invalid argument: %s must not be null!";
	private final static String PROP_EXCEPTION_MSG = "Property %s of %s is not %s!";
	
	@SuppressWarnings("unchecked")
	public static <T> T getPropertyValue(Class<?> requiredType, String propertyName, Object instance) {
		if(requiredType == null) {
			throw new IllegalArgumentException(String.format(ARG_EXCEPTION_MSG, "requiredType"));
		}
		if(propertyName == null) {
			throw new IllegalArgumentException(String.format(ARG_EXCEPTION_MSG, "propertyName"));
		}
		if(instance == null) {
			throw new IllegalArgumentException(String.format(ARG_EXCEPTION_MSG, "Object instance"));
		}
		if(!(Comparable.class.isAssignableFrom(requiredType))) {
			throw new IllegalArgumentException(String.format("Object %s is not an instance of Comparable!", instance.getClass().getName()));
		}
		
		T propertyValue = null;
		
		try {
			PropertyDescriptor descriptor = new PropertyDescriptor(propertyName, instance.getClass());
			Method readMethod = descriptor.getReadMethod();
			if(readMethod == null) {
				throw new IllegalStateException(
						String.format(PROP_EXCEPTION_MSG, propertyName, instance.getClass().getName(), "readable"));
			}
			if(requiredType.isAssignableFrom(readMethod.getReturnType())) {
				try {
					Object propertyValueObject = readMethod.invoke(instance);
					propertyValue = (T) propertyValueObject;
				} catch(Exception e) {
					e.printStackTrace(); // unable to invoke readMethod
				}
			}
		} catch(IntrospectionException e) {
			throw new IllegalArgumentException(
					String.format(PROP_EXCEPTION_MSG, propertyName, instance.getClass().getName(), "defined"), 
					e);
		}
		
		return propertyValue;
	}
	
	public static boolean isValid(Collection<Object> propertyValues, ComparisonMode comparisonMode) {
		boolean ignoreCase = false;
		boolean isValid = true;
		List<Object> values = new ArrayList<Object> (propertyValues.size());
		
		switch(comparisonMode) {
			case EQUAL_IGNORE_CASE:
			case NOT_EQUAL_IGNORE_CASE:
				ignoreCase = true;
				break;
			default:
		}
		
		for(Object propertyValue: propertyValues) {
			if(ignoreCase) {
				if(String.class.isInstance(propertyValue)) {
					values.add((Object) ((String) propertyValue).toLowerCase());
				}
			} else {
				values.add(propertyValue);
			}
		}
	
		switch(comparisonMode) {
			case EQUAL:
			case EQUAL_IGNORE_CASE:
				Set<Object> uniqueValues = new HashSet<Object>(values);
				isValid = uniqueValues.size() == 1;
				break;
			case NOT_EQUAL:
			case NOT_EQUAL_IGNORE_CASE:
				Set<Object> allValues = new HashSet<Object>(values);
				isValid = allValues.size() == values.size();
				break;
		}
		
		return isValid;
	}
	
	public static String resolveMessage(Collection<String> propertyNames, ComparisonMode comparisonMode) {
		StringBuffer buffer = concatPropertyNames(propertyNames);
		buffer.append(" must ");
		
		switch(comparisonMode) {
			case EQUAL:
			case EQUAL_IGNORE_CASE:
				buffer.append("be equal");
				break;
			case NOT_EQUAL:
			case NOT_EQUAL_IGNORE_CASE:
				buffer.append("not be equal");
				break;
		}
		
		buffer.append('.');
		return buffer.toString();
	}
	
	private static StringBuffer concatPropertyNames(Collection<String> propertyNames) {
		StringBuffer buffer = new StringBuffer();
		buffer.append('[');
			
		for ( Iterator<String> stringIter = propertyNames.iterator(); stringIter.hasNext(); ) {
			buffer.append(stringIter.next());
			if(stringIter.hasNext()) {
				buffer.append(", ");
			}
		}
		
		buffer.append(']');
		return buffer;
	}
}
