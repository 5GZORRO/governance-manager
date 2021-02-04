package eu._5gzorro.governancemanager.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;


@Documented
@Constraint(validatedBy = DistributionListValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface DistributionListConstraint {
    String message() default "Invalid distribution list. At least one valid email is required";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}