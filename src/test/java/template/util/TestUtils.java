package template.util;

import org.mockito.verification.VerificationMode;

import static org.mockito.Mockito.times;

public class TestUtils {

    public static VerificationMode once() {
        return times(1);
    }

}
