package de.tswco.validation.groups;

import javax.validation.GroupSequence;
import javax.validation.groups.Default;

@GroupSequence({ Default.class, 
				 FirstCheck.class, 
				 SecondCheck.class,
				 ThirdCheck.class,
				 FourthCheck.class 
			   })
public interface OrderedCheck {

}
