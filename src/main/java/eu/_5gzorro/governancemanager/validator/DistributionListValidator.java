package eu._5gzorro.governancemanager.validator;

import org.apache.logging.log4j.util.Strings;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class DistributionListValidator implements ConstraintValidator<DistributionListConstraint, String> {

    private final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    @Override
    public void initialize(DistributionListConstraint distributionList) {
    }

    @Override
    public boolean isValid(String distributionListField,
                           ConstraintValidatorContext cxt) {

        if(distributionListField == null)
            return false;

        List<String> recipients = Arrays.stream(distributionListField.split(",")).collect(Collectors.toList());

        return recipients.size() > 0 && recipients.stream().allMatch(email -> {
            Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
            return matcher.find();
        });


    }
}
