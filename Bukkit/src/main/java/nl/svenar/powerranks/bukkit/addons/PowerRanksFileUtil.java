package nl.svenar.powerranks.bukkit.addons;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

public class PowerRanksFileUtil {

    public static List<Class<?>> getClasses(File file, Class<PowerRanksAddon> class1) {
        List<Class<?>> list = new ArrayList<>();

        try {
            URL jar = file.toURI().toURL();
            
            try (URLClassLoader cl = new URLClassLoader(new URL[]{jar}, class1.getClassLoader()); JarInputStream jis = new JarInputStream(jar.openStream())) {
                while (true) {
                    JarEntry j = jis.getNextJarEntry();
                    if (j == null) {
                      break;
                    }
                    
                    String name = j.getName();
                    if (name == null || name.isEmpty()) {
                      continue;
                    }
                    
                    if (name.endsWith(".class")) {
                        name = name.replace("/", ".");
                        String cname = name.substring(0, name.lastIndexOf(".class"));

                        Class<?> c = cl.loadClass(cname);
                        if (class1.isAssignableFrom(c)) {
                          list.add(c);
                        }
                      }
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return list;
    }

}
