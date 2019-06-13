package com;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyListener;
import org.jnativehook.mouse.NativeMouseMotionListener;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import com.service.ImageCaptureService;

import java.util.EventListener;


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

        final EventListener eventListener =  new ImageCaptureService();
        GlobalScreen.addNativeKeyListener( (NativeKeyListener) eventListener );
        GlobalScreen.addNativeMouseMotionListener( (NativeMouseMotionListener) eventListener );
    }
}
