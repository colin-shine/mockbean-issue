package issues.spring.shine.colin;

import org.springframework.test.context.ContextConfigurationAttributes;
import org.springframework.test.context.ContextCustomizer;
import org.springframework.test.context.ContextCustomizerFactory;

import java.util.List;

public class MockitoRefreshScopeContextCustomizerFactory implements ContextCustomizerFactory {

    public ContextCustomizer createContextCustomizer(Class<?> testClass,
            List<ContextConfigurationAttributes> configAttributes) {
        return new MockitoRefreshScopeContextCustomizer();
    }
}
