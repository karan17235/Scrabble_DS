package server;

public class UserNameVoteBooleanTuple {

    public String userName;
    public boolean voteBool1;
    public boolean voteBool2;
    public boolean success;

    UserNameVoteBooleanTuple(String userName, boolean voteBool1, boolean voteBool2, boolean success) {
        this.userName = userName;
        this.voteBool1 = voteBool1;
        this.voteBool2 = voteBool2;
        this.success = success;

    }

    UserNameVoteBooleanTuple(String userName, boolean voteBool1, boolean success) {
        this.userName = userName;
        this.voteBool1 = voteBool1;
        //this.voteBool2 = voteBool2;
        this.success = success;
    }

}
