package dev.entze.sge.engine.loader;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.jar.JarFile;

public class JarLoader {

  protected final File file;
  protected final JarFile jarFile;

  public JarLoader(File file) throws IOException {
    this.file = file;
    this.jarFile = new JarFile(file);
  }

  /**
   * Loads specified file into classpath. Taken from https://stackoverflow.com/a/27187663.
   *
   * @throws NoSuchMethodException -
   * @throws InvocationTargetException -
   * @throws MalformedURLException -
   * @throws IllegalAccessException -
   */
  public synchronized void loadJar()
      throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, MalformedURLException {
    /*We are using reflection here to circumvent encapsulation; addURL is not public*/
    java.net.URLClassLoader loader = (java.net.URLClassLoader) ClassLoader.getSystemClassLoader();
    java.net.URL url = file.toURI().toURL();
    /*Disallow if already loaded*/
    for (java.net.URL it : java.util.Arrays.asList(loader.getURLs())) {
      if (it.equals(url)) {
        return;
      }
    }

    java.lang.reflect.Method method = java.net.URLClassLoader.class
        .getDeclaredMethod("addURL", java.net.URL.class);
    method.setAccessible(true); /*promote the method to public access*/
    method.invoke(loader, url);

  }

}

