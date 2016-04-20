package issues.spring.shine.colin;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.core.Ordered;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class MockitoRefreshScopePostProcessor implements BeanFactoryPostProcessor, Ordered {

    private static final String BEAN_NAME = MockitoRefreshScopePostProcessor.class.getName();

    private final Set<Class> mockedTypes;

    public MockitoRefreshScopePostProcessor(Set<Class> mockedTypes) {
        this.mockedTypes = mockedTypes;
    }

    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        BeanDefinitionRegistry bdr = (BeanDefinitionRegistry) beanFactory;

        for (String mockedBean : getMockedBeans(beanFactory)) {
            if (isRefreshScope(mockedBean, bdr)) {
                bdr.removeBeanDefinition(mockedBean);
            }
        }
    }

    private List<String> getMockedBeans(ConfigurableListableBeanFactory beanFactory) {
        List<String> result = new ArrayList<>();

        for (Class mockedType : mockedTypes) {
            String[] mockedBeans = beanFactory.getBeanNamesForType(mockedType);
            result.addAll(Arrays.asList(mockedBeans));
        }

        return result;
    }

    private static boolean isRefreshScope(String mockedBean, BeanDefinitionRegistry bdr) {
        BeanDefinition bd = bdr.getBeanDefinition(mockedBean);
        return bd.getScope().equals("refresh");
    }

    public static void register(BeanDefinitionRegistry registry, Set<Class> mockedTypes) {
        BeanDefinition definition = getOrAddBeanDefinition(registry);

        if (mockedTypes != null) {
            getConstructorArgs(definition).addAll(mockedTypes);
        }
    }

    @SuppressWarnings("unchecked")
    private static Set<Class> getConstructorArgs(BeanDefinition definition) {
        ConstructorArgumentValues.ValueHolder constructorArg = definition.getConstructorArgumentValues()
                .getIndexedArgumentValue(0, Set.class);
        return (Set<Class>) constructorArg.getValue();
    }

    private static BeanDefinition getOrAddBeanDefinition(BeanDefinitionRegistry registry) {
        if (!registry.containsBeanDefinition(BEAN_NAME)) {
            addBeanDefinition(registry);
        }

        return registry.getBeanDefinition(BEAN_NAME);
    }

    private static void addBeanDefinition(BeanDefinitionRegistry registry) {
        RootBeanDefinition def = new RootBeanDefinition(MockitoRefreshScopePostProcessor.class);
        def.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
        ConstructorArgumentValues constructorArguments = def.getConstructorArgumentValues();
        constructorArguments.addIndexedArgumentValue(0, new LinkedHashSet<Class>());
        registry.registerBeanDefinition(BEAN_NAME, def);
    }

    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}