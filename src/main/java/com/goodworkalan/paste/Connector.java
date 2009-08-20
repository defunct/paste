package com.goodworkalan.paste;

/**
 * Used by {@link Router} instances to define path to controller mappings and
 * controller to renderer mappings.
 * 
 * @author Alan Gutierrez
 */
public interface Connector
{
    /**
     * Create a new group of connections that will map paths to controller
     * classes. When processing a request, each group of connections is
     * evaluated in the order in which they were defined. Each group of
     * connections has the opportunity to match the path and apply a controller
     * to the request. If the controller intercepts the request, either by
     * explicitly calling intercept, throwing an exception, or writing output,
     * connection group evaluation processing, no further connection groups are
     * evaluated, and view evaluation begins.
     * 
     * @return A domain-specific language element used to define a group
     */
    public ConnectStatement connect();
    
    /**
     * Return an element in the domain-specific language that will map
     * controllers and exceptions to renderers, based on their type and on
     * additional request properties.
     * 
     * @return A domain-specific language used to define the renderer.
     */
    // FIXME Rename render.
    public RenderStatement view();
}
