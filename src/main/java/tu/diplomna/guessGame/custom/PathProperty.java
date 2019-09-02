package tu.diplomna.guessGame.custom;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 *I made this so i can change the upload directory from application properties.
 * That way if someone wants to deploy the application they can go in the war file and set their directory in application.properties.
 */
@ConfigurationProperties("app.upload")
public class PathProperty {

    private String path = "file:////C:/Users/aleksandar.simeonov/Desktop/Diplomna/myFaceApp/guessGame/upload-dir/";

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
