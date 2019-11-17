package mainactivity.filmlib;


import javafx.scene.image.ImageView;

public class FilmInfo {
    private String filmNo;
    private String filmName;
    private String releaseTime;
    private double runtime;
    private ImageView imageView;

    public FilmInfo() {
    }

    public String getFilmNo() {
        return filmNo;
    }

    public void setFilmNo(String filmNo) {
        this.filmNo = filmNo;
    }

    public String getFilmName() {
        return filmName;
    }

    public void setFilmName(String filmName) {
        this.filmName = filmName;
    }

    public String getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(String releaseTime) {
        this.releaseTime = releaseTime;
    }

    public double getRuntime() {
        return runtime;
    }

    public void setRuntime(double runtime) {
        this.runtime = runtime;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }
}
