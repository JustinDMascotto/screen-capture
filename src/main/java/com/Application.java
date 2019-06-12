package com;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import javax.imageio.ImageIO;
import java.awt.AWTException;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;


public class Application
      implements NativeKeyListener
{
    public static void main( String[] args )
    {
        try
        {
            GlobalScreen.registerNativeHook();
        }
        catch ( NativeHookException ex )
        {
            System.err.println( "There was a problem registering the native hook." );
            System.err.println( ex.getMessage() );

            System.exit( 1 );
        }

        GlobalScreen.addNativeKeyListener( new Application() );
    }


    @Override
    public void nativeKeyPressed( NativeKeyEvent e )
    {
        System.out.println( "Native Key Pressed: " + e.getKeyChar() );
    }


    @Override
    public void nativeKeyTyped( NativeKeyEvent nke )
    {
        System.out.println( "Native Key Typed: " + nke.getKeyChar() );
    }


    @Override
    public void nativeKeyReleased( NativeKeyEvent nke )
    {
        System.out.println( "Native Key Released: " + nke.getKeyChar() );
    }

    private void printScreens()
    {
        final GraphicsEnvironment graphicsEnvironment = this.getGraphicsEnvironment();
        final List<GraphicsDevice> graphicsDeviceList = Arrays.asList( graphicsEnvironment.getScreenDevices() );

    }

    private void printScreen( final GraphicsDevice screen )
          throws AWTException
    {
        Rectangle rec = new Rectangle(
              Toolkit.getDefaultToolkit().getScreenSize() );
        Robot robot = new Robot();
        BufferedImage img = robot.createScreenCapture( rec );

        ImageIO.write( img, "jpg", setupFileNamePath() );
    }

    private GraphicsEnvironment getGraphicsEnvironment()
    {
        return GraphicsEnvironment.getLocalGraphicsEnvironment();
    }
}
