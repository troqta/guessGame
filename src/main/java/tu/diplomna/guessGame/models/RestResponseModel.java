package tu.diplomna.guessGame.models;

public class RestResponseModel {

    private int statusCode;

    private Object body;

    public RestResponseModel() {
    }

    public RestResponseModel(int statusCode, Object body) {
        this.statusCode = statusCode;
        this.body = body;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }
}
