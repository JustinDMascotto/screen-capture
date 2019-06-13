package com.service;

import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.jnativehook.mouse.NativeMouseEvent;
import org.jnativehook.mouse.NativeMouseMotionListener;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
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
      implements NativeKeyListener, NativeMouseMotionListener
{
    private final Set<Integer> keysHeldDown = new HashSet<>();

    private Integer xpos = 0;

    private Integer ypos = 0;

    // Key code combos
    static final Set<List<Integer>> keyCodeCombos = new HashSet<>( Arrays.asList( Arrays.asList( 42, 30 ), // A
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
        keysHeldDown.add( e.getKeyCode() );
        if ( this.isHotKeyPressed( keysHeldDown ) )
        {
            waitForScreenToUpdate();
            if( xpos > 0 && ypos > 0 )
            {
                this.printScreen( getGraphicsDeviceList().get( 0 ) );
            }
            else if ( ypos < 0 )
            {
                this.printScreen( getGraphicsDeviceList().get( 2) );
            }
            else if ( xpos < 0 )
            {
                this.printScreen( getGraphicsDeviceList().get( 1 ) );
            }
            else {
                this.getGraphicsDeviceList()
                        .forEach(this::printScreen);
            }
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


    @Override
    public void nativeMouseMoved( NativeMouseEvent nativeMouseEvent )
    {
        xpos = nativeMouseEvent.getX();
        ypos = nativeMouseEvent.getY();
    }

    @Override
    public void nativeMouseDragged( NativeMouseEvent nativeMouseEvent)
    {
    }


    boolean isHotKeyPressed( final Set<Integer> keysHeldDown )
    {
        return keyCodeCombos.stream()
                            .anyMatch( hotKeyCombo -> hotKeyCombo.stream().allMatch( keysHeldDown::contains ) );
    }


    List<GraphicsDevice> getGraphicsDeviceList()
    {
        final GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        return Stream.of( graphicsEnvironment.getScreenDevices() )
                     .filter( device -> device.getType() == TYPE_RASTER_SCREEN )
                     .collect( Collectors.toList() );
    }


    void printScreen( final GraphicsDevice screen )
    {
        try
        {
            System.out.println( "Printing screen capture for screen " + screen.getIDstring() );
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


    File buildImageFile( final GraphicsDevice screen )
    {
        final String localPath = new File( "" ).getAbsolutePath();
        final String nowFileFriendlyFormat = LocalDateTime.now().toString().replaceAll(":","_");
        return new File( localPath + "/" + nowFileFriendlyFormat+ "_screen" + screen.getIDstring().substring(3) + ".jpg" );
    }


    private void waitForScreenToUpdate()
    {
        try
        {
            Thread.sleep( 1000 );
        }
        catch ( Exception ex )
        {
            System.out.println( "Unable to wait for screen to update." );
        }
    }
}
