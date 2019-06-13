package service;

import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.springframework.stereotype.Service;
import sun.awt.X11GraphicsDevice;

import javax.imageio.ImageIO;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.File;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.awt.GraphicsDevice.TYPE_RASTER_SCREEN;


@Service
public class ImageCaptureService
      implements NativeKeyListener
{
    private final Set<Integer> keysHeldDown = new HashSet<>();

    static final Set<List<Integer>> hotKeyCombos = new HashSet<>( Arrays.asList( Arrays.asList( 42, 30 ), // A
                                                                                 Arrays.asList( 42, 44 ), // Z
                                                                                 Arrays.asList( 42, 16 ), // Q
                                                                                 Arrays.asList( 42, 2 ), // 1
                                                                                 Arrays.asList( 42, 3 ), // 2
                                                                                 Arrays.asList( 42, 4 ), // 3
                                                                                 Arrays.asList( 42, 5 ), // 4
                                                                                 Arrays.asList( 42, 6 ), // 5
                                                                                 Arrays.asList( 42, 7 ), // 6
                                                                                 Arrays.asList( 42, 19 ), // R
                                                                                 Arrays.asList( 42, 33 ), // F
                                                                                 Arrays.asList( 42, 47 ) ) ); // V



    @Override
    public void nativeKeyPressed( NativeKeyEvent e )
    {
        System.out.println( "Native Key Pressed: " + e.getKeyCode() );
        keysHeldDown.add( e.getKeyCode() );
        if ( this.isHotKeyPressed( keysHeldDown ) )
        {
            this.getGraphicsDeviceList()
                .forEach( this::printScreen );
        }
    }


    @Override
    public void nativeKeyTyped( NativeKeyEvent nke )
    {
    }


    @Override
    public void nativeKeyReleased( NativeKeyEvent nke )
    {
        keysHeldDown.remove( nke.getKeyCode() );
    }


    boolean isHotKeyPressed( final Set<Integer> keysHeldDown )
    {
        return hotKeyCombos.stream()
                           .anyMatch( hotKeyCombo -> hotKeyCombo.stream().allMatch( keysHeldDown::contains ) );
    }


    List<X11GraphicsDevice> getGraphicsDeviceList()
    {
        final GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        return Stream.of( graphicsEnvironment.getScreenDevices() )
                     .filter( device -> device.getType() == TYPE_RASTER_SCREEN )
                     .map( device -> (X11GraphicsDevice) device )
                     .collect( Collectors.toList() );
    }


    void printScreen( final X11GraphicsDevice screen )
    {
        try
        {
            Rectangle rec = new Rectangle( screen.getDefaultConfiguration().getBounds() );
            Robot robot = new Robot( screen );
            BufferedImage img = robot.createScreenCapture( rec );

            ImageIO.write( img, "jpg", buildImageFile( screen ) );
        }
        catch ( Exception ex )
        {
            System.out.println( ex.getMessage() );
        }
    }


    File buildImageFile( final X11GraphicsDevice screen )
    {
        final String localPath = new File( "" ).getAbsolutePath();
        return new File( localPath + "/" + LocalDateTime.now() + "_screen" + screen.getScreen() + ".jpg" );
    }
}
