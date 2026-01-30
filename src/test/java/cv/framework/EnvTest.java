package cv.framework;

import com.erling.jdz.load.RunEnv;
import org.junit.jupiter.api.Test;

public class EnvTest {

    @Test
    public void testEnv() {
        RunEnv.SET_ENV.run();
    }
}
