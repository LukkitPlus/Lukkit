package online.pizzacrust.lukkitplus;

import javassist.CannotCompileException;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.NotFoundException;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.annotation.Annotation;

import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.reflections.Reflections;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import online.pizzacrust.lukkitplus.api.EventPoint;
import online.pizzacrust.lukkitplus.api.event.EventAttachment;

public class EventCallbackGenerator {

    public static boolean isClassGenerated() {
        try {
            Class.forName("online.pizzacrust.lukkitplus.EventCallback");
        } catch (ClassNotFoundException e) {
            return false;
        }
        return true;
    }

    public static void call(Event e) {
        for (EventAttachment attachment : EventPoint.ATTACHMENTS) {
            attachment.call(e);
        }
    }

    private static boolean containsStaticHandlerList(Class<?> clazz) {
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.getModifiers() == Modifier.PUBLIC + Modifier.STATIC) {
                if (method.getName().equals("getHandlerList")) {
                    return true;
                }
            }
        }
        return false;
    }

    public static Class<?> generateClass() throws NotFoundException, CannotCompileException {
        ClassPool classpath = ClassPool.getDefault();
        classpath.insertClassPath(new ClassClassPath(EventCallbackGenerator.class));
        CtClass eventClass = classpath.makeClass("online.pizzacrust.lukkitplus" +
                ".EventCallback");
        eventClass.addInterface(classpath.get(Listener.class.getName()));
        for (Class<? extends Event> event : LukkitPlus.BUKKIT_EVENTS) {
            if (containsStaticHandlerList(event)) {
                CtMethod eventMethod = CtNewMethod.make(CtClass.voidType, "on" + event.getSimpleName
                        (), new CtClass[]{classpath.get(event.getName())}, new CtClass[0], "online" +
                        ".pizzacrust.lukkitplus.EventCallbackGenerator.call($1);", eventClass);
                eventClass.addMethod(eventMethod);
                AnnotationsAttribute attribute = new AnnotationsAttribute(eventClass.getClassFile()
                        .getConstPool(), AnnotationsAttribute.visibleTag);
                Annotation eventHandlerAnnt = new Annotation(EventHandler.class.getName(), eventClass
                        .getClassFile().getConstPool());
                attribute.addAnnotation(eventHandlerAnnt);
                eventMethod.getMethodInfo().addAttribute(attribute);
            }
        }
        return eventClass.toClass(LukkitPlus.class.getClassLoader());
    }

    public static void main(String[] args) throws Exception {
        Reflections reflections = new Reflections("org.bukkit.event");
        LukkitPlus.BUKKIT_EVENTS = reflections.getSubTypesOf(Event.class);
        ClassPool classpath = ClassPool.getDefault();
        CtClass eventClass = classpath.makeClass("online.pizzacrust.lukkitplus" +
                ".EventCallback");
        for (Class<? extends Event> event : LukkitPlus.BUKKIT_EVENTS) {
            CtMethod eventMethod = CtNewMethod.make(CtClass.voidType, "on" + event.getSimpleName
                    (), new CtClass[] { classpath.get(event.getName()) }, new CtClass[0], "online" +
                    ".pizzacrust.lukkitplus.EventCallbackGenerator.call($1);", eventClass);
            eventClass.addMethod(eventMethod);
            AnnotationsAttribute attribute = new AnnotationsAttribute(eventClass.getClassFile()
                    .getConstPool(), AnnotationsAttribute.visibleTag);
            Annotation eventHandlerAnnt = new Annotation(EventHandler.class.getName(), eventClass
                    .getClassFile().getConstPool());
            attribute.addAnnotation(eventHandlerAnnt);
            eventMethod.getMethodInfo().addAttribute(attribute);
        }
        System.out.println("Done!");
        eventClass.writeFile();
    }

}
