package org.artek.app.main;

/**
 * Created by Sergey on 29.10.2016.
 */

public class News {

    String name;
    int price;
    int image;
    boolean box;


    News(String _describe, int _price, int _image, boolean _box) {
        name = _describe;
        price = _price;
        image = _image;
        box = _box;
    }
}
