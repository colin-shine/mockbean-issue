package issues.spring.shine.colin;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MyTest {

    @MockBean
    private BeanB mockBeanB;

    @Test
    public void testStuff() {
        System.out.println(mockBeanB);
    }
}