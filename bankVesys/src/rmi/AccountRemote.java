package rmi;

import bank.Account;

import java.rmi.Remote;

/**
 * Created by mfrey on 10/07/2016.
 */
public interface AccountRemote extends Account,Remote {
}
