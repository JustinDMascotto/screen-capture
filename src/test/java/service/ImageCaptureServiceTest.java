package service;

import org.junit.Ignore;
import org.junit.Test;
import sun.awt.X11GraphicsDevice;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static junit.framework.TestCase.assertTrue;


public class ImageCaptureServiceTest
{
    private final ImageCaptureService application = new ImageCaptureService();

    private final List<X11GraphicsDevice> graphicsDevices = application.getGraphicsDeviceList();

    @Test
    public void testBuildImageFile()
    {
        final File file = application.buildImageFile( graphicsDevices.get( 1 ) );

        System.out.println( file.getAbsoluteFile() );
    }

    @Ignore
    @Test
    public void testPrintScreen()
    {
        application.printScreen( graphicsDevices.get( 0 ) );
    }


    @Test
    public void testIsHotkeyPressed()
    {
        final Set<Integer> keysHeldDown = new HashSet<>( Arrays.asList( 42, 30 ) );

        assertTrue( application.isHotKeyPressed( keysHeldDown ) );
    }

    @Test
    public void testIsHotkeyPressedShouldNotReturnTrue1()
    {
        final Set<Integer> keysHeldDown = new HashSet<>( Arrays.asList( 30 ) );

        assertTrue( !application.isHotKeyPressed( keysHeldDown ) );
    }

    @Test
    public void testIsHotkeyPressedShouldNotReturnTrue2()
    {
        final Set<Integer> keysHeldDown = new HashSet<>( Arrays.asList( 42 ) );

        assertTrue( !application.isHotKeyPressed( keysHeldDown ) );
    }
}
