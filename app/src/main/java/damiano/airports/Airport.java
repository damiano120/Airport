package damiano.airports;

public class Airport {

    private String name;
    private int pathImage;

    public Airport(String name, int pathImage) {
        this.name = name;
        this.pathImage = pathImage;
    }

    public String getName() {
        return name;
    }

    public int getUrlImage() {
        return pathImage;
    }
}
