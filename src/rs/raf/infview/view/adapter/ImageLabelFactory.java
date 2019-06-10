package rs.raf.infview.view.adapter;

import rs.raf.infview.core.App;
import rs.raf.infview.core.Res;
import rs.raf.infview.util.log.Log;
import rs.raf.infview.view.component.ImageLabel;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ImageLabelFactory {

    public ImageLabel getImageLabel(String path) {
        byte[] bytes;

        try {
            InputStream is = new FileInputStream(path);
            bytes = new byte[is.available()];
            new DataInputStream(is).readFully(bytes);
        }catch(IOException e) {
            bytes = new byte[0];
        }

        return getImageLabel(bytes, true);
    }

    public ImageLabel getImageLabel(byte[] bytes) {
        return getImageLabel(bytes, true);
    }

    private ImageLabel getImageLabel(byte[] bytes, boolean firstCall) {
        ImageLabel image;

        try {
            image = App.UI.getImageLabel(bytes);
        }catch(IOException e) {
            Log.e(e.getMessage());

            if(firstCall) {
                image = getImageLabel(Res.ICONS.IMAGE_DEFAULT, false);
            }else {
                Log.e(Res.STRINGS.ERROR_NO_DEFAULT_IMAGE);

                image = getImageLabel(null, false);
            }
        }

        return image;
    }
}