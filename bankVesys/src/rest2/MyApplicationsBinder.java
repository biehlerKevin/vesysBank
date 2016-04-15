package rest2;

import bank.BankDriver;
import bank.local.Driver;
import org.glassfish.hk2.utilities.binding.AbstractBinder;

/**
 * Created by mfrey on 15/04/2016.
 *
 * binds a single driver for all requests
 */
public class MyApplicationsBinder extends AbstractBinder {
    @Override
    protected void configure() {
        Driver driver = new Driver();
        driver.connect(null);
        bind(driver).to(BankDriver.class);
    }
}
