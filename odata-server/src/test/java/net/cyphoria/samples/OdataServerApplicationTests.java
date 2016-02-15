package net.cyphoria.samples;

import net.cyphoria.samples.domain.Order;
import net.cyphoria.samples.repository.OrderRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ODataServerApplication.class)
@WebAppConfiguration
public class OdataServerApplicationTests {

	@Autowired
    private OrderRepository repository;

    @Before
    public void setup() {
        final Order o = new Order();
        repository.save(o);
    }


	@Test
	public void contextLoads() {

	}

    @Test
    public void testDataExists() {
        assertThat(repository.count(), is(1L));
    }

}
