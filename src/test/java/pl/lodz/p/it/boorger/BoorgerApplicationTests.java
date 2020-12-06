package pl.lodz.p.it.boorger;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import pl.lodz.p.it.boorger.utils.DateFormatter;

import java.time.LocalDateTime;

@RunWith(SpringRunner.class)
public class BoorgerApplicationTests {

	@Test
	public void dateFormatterTest1() {
		LocalDateTime stringDate = DateFormatter.stringToDate("2021-01-01 20:00");
		LocalDateTime date = LocalDateTime.of(2021, 1, 1, 20, 0);
		Assert.assertEquals(stringDate, date);
	}

	@Test
	public void dateFormatterTest2() {
		String dateString = "2021-01-01 20:00";
		String date = DateFormatter.dateToString(LocalDateTime.of(2021,1,1,20,0));
		Assert.assertEquals(dateString, date);
	}
}
