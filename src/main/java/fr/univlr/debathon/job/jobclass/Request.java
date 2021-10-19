package fr.univlr.debathon.job.jobclass;

public class Request {

    private String requestName;
    private String request;

    public Request(String requestName) {
        this.requestName = requestName;
    }

    public Request() {
        this("default");
    }


    //Getter
    public String getRequestName() {
        return requestName;
    }

    public String getRequest() {
        return request;
    }


    //Setter
    public Request setRequest(String request) {
        this.request = request;
        return this;
    }


    @Override
    public String toString() {
        return requestName;
    }
}
