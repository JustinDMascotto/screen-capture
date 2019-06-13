package com;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import com.service.ImageCaptureService;


@SpringBootApplication
public class Application
{
    public static void main( String[] args )
    {
        initailize();
        SpringApplicationBuilder builder = new SpringApplicationBuilder( Application.class);
        builder.headless(false);
        ConfigurableApplicationContext context = builder.run( args );
    }


    private static void initailize()
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

        GlobalScreen.addNativeKeyListener( new ImageCaptureService() );
    }
}
