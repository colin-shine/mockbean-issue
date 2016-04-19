package issues.spring.shine.colin;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextCustomizer;
import org.springframework.test.context.MergedContextConfiguration;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

public class MockitoRefreshScopeContextCustomizer implements ContextCustomizer {

    public void customizeContext(ConfigurableApplicationContext context, MergedContextConfiguration mergedConfig) {
        Set<Class> mockedTypes = findMockedTypes(mergedConfig.getTestClass());
        MockitoRefreshScopePostProcessor.register((BeanDefinitionRegistry) context, mockedTypes);
    }

    private static Set<Class> findMockedTypes(Class<?> testClass) {
        final Set<Class> mockTypes = new HashSet<Class>();

        ReflectionUtils.doWithFields(testClass, new ReflectionUtils.FieldCallback() {
            public void doWith(Field field) {
                if (field.isAnnotationPresent(MockBean.class)) {
                    mockTypes.add(field.getType());
                }
            }
        });

        return mockTypes;
    }
}