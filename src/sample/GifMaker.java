package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import shine.htetaung.giffer.Giffer;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class GifMaker {
    private ObservableList<File> image_list = FXCollections.observableArrayList();
    private int speedValue = 50;
    private int height = 500;
    private int width = 500;

    public void GifMaker(ObservableList<File> list) {
        image_list = list;
    }
    public int getSpeedValue() {return speedValue;}

    public void setSpeedValue(int newValue) {speedValue = newValue;}

    public int getHeight() {return height;}

    public void setHeight(int newValue) {height = newValue;}

    public int getWidth() {return width;}

    public void setWidth(int newValue) {width = newValue;}

    public ObservableList<File> getList() {
        return image_list;
    }

    public void deleteImage(File file) {
        image_list.remove(file);
    }

    public void addList(List<File> list) {
        image_list.addAll(list);
    }

    public void makeGif(String path) {
        BufferedImage[] images = new BufferedImage[image_list.size()];
        int i = 0;

        for (File file : image_list) {
            try {
                images[i] = resizeBufferedImage(ImageIO.read(file), width, height);
            } catch (IOException e) {
                e.printStackTrace();
            }
            i++;
        }

        try {
            Giffer.generateFromBI(images, path, speedValue, true);
            image_list.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private BufferedImage resizeBufferedImage(BufferedImage oldImage, int newWidth, int newHeight){
        BufferedImage scaledImage = new BufferedImage(
                newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = scaledImage.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2D.drawImage(oldImage, 0, 0, width, height, null);
        graphics2D.dispose();
        return scaledImage;
    }
}