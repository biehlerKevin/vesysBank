package soap;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import bank.InactiveException;
import bank.OverdrawException;

public class MyBank implements bank.Bank{

		private final Map<String, MyAccount> accounts = new HashMap<>();
		private int numberOfAccounts = 0;

		@Override
		public Set<String> getAccountNumbers() {
			return accounts.values().stream().filter(MyAccount::isActive).map(MyAccount::getNumber).collect(Collectors.toSet());
		}

		@Override
		public String createAccount(String owner) {
			String accountNumber = Integer.toString(numberOfAccounts);
			numberOfAccounts++;
			MyAccount account = new MyAccount(owner, accountNumber);
			accounts.put(accountNumber, account);
			return accountNumber;
		}

		@Override
		public boolean closeAccount(String number) {
			MyAccount acc = accounts.get(number);
			if(acc.getBalance() > 0 || !acc.isActive()) return false;

			acc.setActive(false);
			return true;
		}

		@Override
		public bank.Account getAccount(String number) {
			return accounts.get(number);
		}

		@Override
		public void transfer(bank.Account from, bank.Account to, double amount)
				throws IOException, InactiveException, OverdrawException, IllegalArgumentException {

			if(amount < 0) throw new IllegalArgumentException();
			if(amount > from.getBalance()) throw new OverdrawException();
			if(!(from.isActive() && to.isActive())) throw new InactiveException();
			from.withdraw(amount);
			to.deposit(amount);
		}


}
