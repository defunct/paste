package com.goodworkalan.paste;

/**
 * A Guice created actor that acts upon a Guice created controller. Controllers
 * specify their actors using the {@link Actors} annotation.
 * 
 * @author Alan Gutierrez
 */
public interface Actor {
    /**
     * Act upon the given controller.
     * 
     * @param controller
     *            The controller.
     * @return An exception thrown during execution that might be reported using
     *         a renderer.
     */
    // FIXME Why not just throw the exception?
    public Throwable actUpon(Object controller);
}
