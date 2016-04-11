package soap;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import bank.Account;
import bank.Bank;
import bank.BankDriver;
import bank.InactiveException;
import bank.OverdrawException;

public class SoapDriver implements BankDriver {

    SoapBank bank;

    @Override
    public void connect(String[] args) throws IOException {

        URL url = new URL("http://localhost:9876/soap?wsdl");

        QName qname = new QName("http://soap/", "BankWebserviceImplService");

        Service service = Service.create(url, qname);

        BankWebservice web = service.getPort(BankWebservice.class);

        this.bank = new SoapBank(web);

        System.out.println("connected...");

    }

    @Override
    public void disconnect() throws IOException {
    }

    @Override
    public Bank getBank() {
        return this.bank;
    }

    static class SoapBank implements bank.Bank {
        public BankWebservice service;

        public SoapBank(BankWebservice web) {
            this.service = web;
        }

        @Override
        public Set<String> getAccountNumbers() throws IOException {
            Set<String> activeAccounts = new HashSet<String>(Arrays.asList(service.getAccountNumbers()));
            return activeAccounts;
        }

        @Override
        public String createAccount(String owner) throws IOException {
            return service.createAccount(owner);
        }

        @Override
        public boolean closeAccount(String number) throws IOException {
            return service.closeAccount(number);
        }

        @Override
        public bank.Account getAccount(String number) throws IOException {
            bank.Account res;
            if (service.getAccount(number).equals("")) {
                System.out.println("Account not existing");
                res = null;
            } else
                res = new SoapAccount(number, service);
            return res;
        }

        @Override
        public void transfer(bank.Account from, bank.Account to, double amount)
                throws IOException, InactiveException, OverdrawException, IllegalArgumentException {
            try {
                service.transfer(from.getNumber(), to.getNumber(), amount);
            } catch (MyIllegalArgumentException e) {
                throw new IllegalArgumentException();
            }

        }

        static class SoapAccount implements bank.Account {

            private String number;
            BankWebservice service;

            SoapAccount(String number, BankWebservice svc) {
                this.number = number;
                this.service = svc;

            }

            @Override
            public double getBalance() throws IOException {
                try {
                    return service.getBalance(this.number);
                } catch (MyIllegalArgumentException e) {
                    throw new IllegalArgumentException();
                }
            }

            @Override
            public String getOwner() throws IOException {
                return service.getOwner(this.number);
            }

            @Override
            public String getNumber() {
                return this.number;
            }

            @Override
            public boolean isActive() throws IOException {
                try {
                    return service.isActive(this.number);
                } catch (MyIllegalArgumentException e) {
                    throw new IllegalArgumentException();
                }
            }

            @Override
            public void deposit(double amount) throws InactiveException, IOException, IllegalArgumentException {
                try {
                    service.deposit(this.number, amount);
                } catch (MyIllegalArgumentException e) {
                    throw new IllegalArgumentException();
                }
            }

            @Override
            public void withdraw(double amount)
                    throws InactiveException, OverdrawException, IOException, IllegalArgumentException {
                try {
                    service.withdraw(this.number, amount);
                } catch (MyIllegalArgumentException e) {
                    throw new IllegalArgumentException();
                }
            }

        }
    }
}
