package org.firstinspires.ftc.teamcode.internals.registration;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpModeManager;
import com.qualcomm.robotcore.eventloop.opmode.OpModeRegistrar;

import org.firstinspires.ftc.robotcore.internal.opmode.OpModeMeta;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * This registers {@link OperationMode}s. It should not be instantiated. As a rule of thumb, most classes referring to registration should not be instantiated as registration follows functional patterns. That's just the Qualcomm Way™.
 * <br>
 * <br>
 * <em>For advanced users:</em>
 * <br>
 * OpModes are a pain to create. Not because they're a lot of work or particulary hard, but because it's easy to forget to annotate them so the default registrar can find and register them. So, {@link OperationMode}s were created to solve this. They contain all the information needed to be registered without an annotation. To do this without annotations, we need to make our own registrar which finds OpModes differently. To do this, we create a static method annotated with {@link OpModeRegistrar} so the app can find the method. The method takes a manager, and that manager is what's used to actually register the OpModes. Via reflection, {@link OperationMode}s are found. They are then instantiated and their getters called which the values returned from are stored locally in the method. Finally, the metadata received from the {@link OperationMode}s and their respective getters is used to register each OpMode via {@link OpModeManager#register(OpModeMeta, Class)}, assuming the OpMode is not disabled.
 */
public class OperationModeRegistrar {

    private static final String KEY = "a7216e0b6a49850c6092991040467037d0fc899960bba2c08c4afafeb8b3bf1bfd748fe8050348f614c7e4421af2449a47db9d01de07b13a8c2fb060dac3e1ed5053643c4739479ff3fc665a9dba57e47d65803838d1617de4b1658a9e022e9bc0eaf71b";

    /**
     * Private constructor to prevent instantiation.
     */
    private OperationModeRegistrar() {}

    /**
     * The method which acts as a functional registrar. Do <strong>NOT</strong> call this yourself unless you are absolutely sure of what you're doing!
     * @param manager The manager to register OpModes with, passed by the app itself as the app is supposed to call this method, <em>not you (probably)</em>
     */
    @OpModeRegistrar
    public static void registerOperationModes(OpModeManager manager) {
        // log init and set up our store
        OperationModeRegistrationLogger.log("Initializing service...", KEY);
        OperationModeRegistrarStore.setManager(manager, KEY);
        register();
    }

    /**
     * Attempts to register an {@link OperationMode}. It will be registered assuming it's valid in all forms (basically, this finishes the 2-step sanitazion process begun by {@link OperationModeClassFilter}<code>#sanatize(Class)</code>.
     * @param clazz The class to register.
     */
    public static void attemptRegistration(Class<? extends OperationMode> clazz) {
        // check if the opmode is disabled, and skip this opmode if so
        if(clazz.isAnnotationPresent(Disabled.class)) {
            return;
        }
        // initialize registration data
        boolean isPsuedoDisabled = true;
        String name = "A";
        String nextName = null;
        // begin creation of opmode meta
        OpModeMeta.Builder opModeMetaBuilder = new OpModeMeta.Builder().setName(clazz.getName().replace(clazz.getPackage().getName() + ".", "")).setGroup(name).setSource(OpModeMeta.Source.EXTERNAL_LIBRARY);
        // check if auto or tele operation is implemented by opmode, and set it up accordingly
        if(AutonomousOperation.class.isAssignableFrom(clazz)) {
            // disable psudo disabling, which basically means the opmode isnt explicitly disabled but implements no ways of operation, so in essence is disabled anyway
            isPsuedoDisabled = false;
            // set to autonomous
            opModeMetaBuilder.setFlavor(OpModeMeta.Flavor.AUTONOMOUS);
            // we need to find the next opmode to queue after this, if it exists. To do this, find the method we need to run via reflection, invoke it, and cast the output to the necessary object assuming it's not null. If it's null or an exception is thrown, don't worry about this and move on
            try {
                Method method = clazz.getMethod("getNext", (Class<?>[]) null);
                Object next = method.invoke(clazz.newInstance(), (Object[]) null);
                if(next != null) {
                    Class<? extends OperationMode> nextClass = (Class<? extends OperationMode>) next;
                    // make sure this class is a valid opmode and will be or has already been instantiated at some point
                    if(!nextClass.isAnnotationPresent(Disabled.class) && TeleOperation.class.isAssignableFrom(nextClass)) {
                        // if so, set the name of the transition target to the name of the opmode which will be the name of the class
                        nextName = nextClass.getName().replace(nextClass.getPackage().getName() + ".", "");
                    }
                }
            }catch(NoSuchMethodException | SecurityException | IllegalAccessException | InstantiationException | InvocationTargetException | ClassCastException ignored) {}
            // set the next opmode to queue if one exists
            if(nextName != null) {
                opModeMetaBuilder.setTransitionTarget(nextName);
            }
        }else if(TeleOperation.class.isAssignableFrom(clazz)) {
            // disable psudo disabling, which basically means the opmode isnt explicitly disabled but implements no ways of operation, so in essence is disabled anyway
            isPsuedoDisabled = false;
            // set to teleop
            opModeMetaBuilder.setFlavor(OpModeMeta.Flavor.TELEOP);
        }
        // finish building the opmode's meta
        OpModeMeta opModeMeta = opModeMetaBuilder.build();
        if(!isPsuedoDisabled) {
            // finally, add the opmode to the queue, assuming the opmode has either auto control or teleop control
            OperationModeRegistrarStore.addFinalClass(new FullOperationMode(opModeMeta, clazz), KEY);
        }else{
            OperationModeRegistrationLogger.log("Registration failed, moving on...", KEY);
        }
    }

    /**
     * Registers an {@link OperationMode} if the {@link OperationModeClassFilter} has finished filtering and the {@link OpModeManager} for this operation exists.
     */
    public static synchronized void register() {
        // we need check if the manager and the classes are finished resolving bc they're on different threads
        if(OperationModeRegistrarStore.areClassesReady(KEY) && OperationModeRegistrarStore.isManagerReady(KEY)) {
            // then we grab the classes and manager and iterate through and register them
            ArrayList<FullOperationMode> classes = OperationModeRegistrarStore.getFinalClasses(KEY);
            OpModeManager manager = OperationModeRegistrarStore.getManager(KEY);
            for(FullOperationMode opmode : classes) {
                manager.register(opmode.getMeta(), opmode.getClazz());
                OperationModeRegistrationLogger.log("Operation Mode registered! Moving on...", KEY);
            }
        }
    }

}