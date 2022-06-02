package RMILabClient;//************************************************************
// Calculator.java                Interface for a Calculator

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Calculator extends Remote {
    // this method will be called from remote clients
    int add(int x, int y) throws RemoteException;
}

