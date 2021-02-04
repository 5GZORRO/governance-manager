package eu._5gzorro.governancemanager.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DistributionListValidator implements ConstraintValidator<DistributionListConstraint, Collection<String>> {

    private final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    @Override
    public void initialize(DistributionListConstraint distributionList) {
    }

    @Override
    public boolean isValid(Collection<String> distributionListField,
                           ConstraintValidatorContext cxt) {

        return distributionListField.size() > 0 && distributionListField.stream().allMatch(email -> {
            Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
            return matcher.find();
        });


    }
}
