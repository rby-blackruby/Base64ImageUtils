package hu.blackruby.base64imageutils;

import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

public class Base64ImageUtilsTest {

    // VALID TEST IMAGES ENCODED IN BASE64
    // 5x5 px filled with rgb(163,69,83) = 0xFFA34553 (In case of the image being ARGB the A value is always FF) png
    // created and manually encoded.
    final String base64Image = "iVBORw0KGgoAAAANSUhEUgAAAAUAAAAFCAIAAAACDbGyAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAADsMAAA7DAcdvqGQAAAAYdEVYdFNvZnR3YXJlAFBhaW50Lk5FVCA1LjEuOWxu2j4AAAC2ZVhJZklJKgAIAAAABQAaAQUAAQAAAEoAAAAbAQUAAQAAAFIAAAAoAQMAAQAAAAIAAAAxAQIAEAAAAFoAAABphwQAAQAAAGoAAAAAAAAAYAAAAAEAAABgAAAAAQAAAFBhaW50Lk5FVCA1LjEuOQADAACQBwAEAAAAMDIzMAGgAwABAAAAAQAAAAWgBAABAAAAlAAAAAAAAAACAAEAAgAEAAAAUjk4AAIABwAEAAAAMDEwMAAAAABMz8BIJY/XoAAAABNJREFUGFdjXOwazIAEmJA5ZPABYioBRW7yFFUAAAAASUVORK5CYII=";
    // 2x 1x1px white and black images encoded manually to base64 strings.
    final String white1pxImageInBase64 = "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAIAAACQd1PeAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAADsMAAA7DAcdvqGQAAAAYdEVYdFNvZnR3YXJlAFBhaW50Lk5FVCA1LjEuOWxu2j4AAAC2ZVhJZklJKgAIAAAABQAaAQUAAQAAAEoAAAAbAQUAAQAAAFIAAAAoAQMAAQAAAAIAAAAxAQIAEAAAAFoAAABphwQAAQAAAGoAAAAAAAAAYAAAAAEAAABgAAAAAQAAAFBhaW50Lk5FVCA1LjEuOQADAACQBwAEAAAAMDIzMAGgAwABAAAAAQAAAAWgBAABAAAAlAAAAAAAAAACAAEAAgAEAAAAUjk4AAIABwAEAAAAMDEwMAAAAABMz8BIJY/XoAAAAAxJREFUGFdj+P//PwAF/gL+pzWBhAAAAABJRU5ErkJggg==";
    final String black1pxImageInBase64 = "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAIAAACQd1PeAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAAJcEhZcwAADsMAAA7DAcdvqGQAAAAYdEVYdFNvZnR3YXJlAFBhaW50Lk5FVCA1LjEuOWxu2j4AAAC2ZVhJZklJKgAIAAAABQAaAQUAAQAAAEoAAAAbAQUAAQAAAFIAAAAoAQMAAQAAAAIAAAAxAQIAEAAAAFoAAABphwQAAQAAAGoAAAAAAAAAYAAAAAEAAABgAAAAAQAAAFBhaW50Lk5FVCA1LjEuOQADAACQBwAEAAAAMDIzMAGgAwABAAAAAQAAAAWgBAABAAAAlAAAAAAAAAACAAEAAgAEAAAAUjk4AAIABwAEAAAAMDEwMAAAAABMz8BIJY/XoAAAAAxJREFUGFdjYGBgAAAABAABXM3/aQAAAABJRU5ErkJggg==";

    @Test
    void convertToBufferedImage_validBase64Text() throws IOException {
        // 5x5 px filled with rgb(163,69,83) = 0xFFA34553 (In case of the image being ARGB the A value is always FF) png
        // created and manually encoded.
        byte[] decodedBase64Image = Base64.getDecoder().decode(base64Image);

        BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(decodedBase64Image));
        int bufferedImageColor = bufferedImage.getRGB(0, 0);

        assertNotNull(bufferedImage, "The test base64 encoded image is valid and can be converted to BufferedImage.");
        assertEquals(5, bufferedImage.getWidth(), "The test image provided was 5px wide.");
        assertEquals(5, bufferedImage.getHeight(), "The test image's height was set to be 5px.");
        assertEquals(0xFFA34553, bufferedImageColor, "The test image was filled with the color of hex code 0xA34553");
    }

    @Test
    void convertToBufferedImage_invalidBase64Text() throws IOException {
        String invalidBase64Text = "aaa";

        assertThrows(IllegalArgumentException.class, () -> {
            BufferedImage bufferedImage = Base64ImageUtils.convertToBufferedImage(invalidBase64Text);
        });
    }

    @Test
    void merge_validImages_HorizontalDefault() throws IOException {
        // Should be a 2px wide and 1px tall image, left pixel white, right pixel black.
        BufferedImage mergedImage = Base64ImageUtils.merge(white1pxImageInBase64, black1pxImageInBase64);

        assertNotNull(mergedImage, "Valid base64 string representation was provided, merged image should not return null.");
        assertEquals(0xFFFFFFFF, mergedImage.getRGB(0,0), "The left pixel should be white.");
        assertEquals(0xFF000000, mergedImage.getRGB(1,0), "The right pixel should be black.");
    }

    @Test
    void merge_validImages_Vertical() throws IOException {
        BufferedImage mergedImage = Base64ImageUtils.merge(white1pxImageInBase64, black1pxImageInBase64, MergeDirection.VERTICAL);

        assertNotNull(mergedImage, "Valid base64 string representation was provided, merged image should not return null.");
        assertEquals(0xFFFFFFFF, mergedImage.getRGB(0,0), "The top pixel should be white.");
        assertEquals(0xFF000000, mergedImage.getRGB(0,1), "The bottom pixel should be black.");
    }

}
