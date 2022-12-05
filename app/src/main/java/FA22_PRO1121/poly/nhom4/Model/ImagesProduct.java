package FA22_PRO1121.poly.nhom4.Model;

public class ImagesProduct {

    private int id;
    private String image;
    private int id_product;

    public ImagesProduct(String image) {
        this.image = image;
    }

    public ImagesProduct(String image, int id_product) {
        this.image = image;
        this.id_product = id_product;
    }

    public ImagesProduct(int id, String image, int id_product) {
        this.id = id;
        this.image = image;
        this.id_product = id_product;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getId_product() {
        return id_product;
    }

    public void setId_product(int id_product) {
        this.id_product = id_product;
    }
}
